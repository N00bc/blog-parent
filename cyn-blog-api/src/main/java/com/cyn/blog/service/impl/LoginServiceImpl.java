package com.cyn.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.LoginParam;
import com.cyn.blog.entity.vo.LoginUserVo;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.gloableEnum.ErrorCode;
import com.cyn.blog.service.LoginService;
import com.cyn.blog.service.SysUserService;
import com.cyn.blog.utils.JwtUtils;
import com.cyn.blog.utils.SystemConstants;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/26 17:12
 */
@Service
public class LoginServiceImpl implements LoginService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    private static final String salt = "cyn0v%";

    /**
     * 用户登录
     * 1.判断参数合法性，通过后校验用户。
     * 2.将用户信息存储到redis
     * 存储用户信息到token
     *
     * @param loginParam:
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/27 15:27
     */
    @Override
    public Result login(LoginParam loginParam) {
        String password = loginParam.getPassword();
        String account = loginParam.getAccount();
        // 1.判断参数合法性 非法则返回错误信息
        if (StringUtils.isBlank(account) || StringUtils.isBlank(password))
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        // Append 对密码进行加密处理
        password = DigestUtils.md5Hex(password + salt);
        // 2.判断用户是否存在 不存在则返回错误信息
        SysUser user = sysUserService.findUser(account, password);
        if (user == null) {
            return Result.fail(ErrorCode.ACCOUNT_PWD_NOT_EXIST.getCode(), ErrorCode.ACCOUNT_PWD_NOT_EXIST.getMsg());
        }
        // 3.根据用户id生成token
        String token = JwtUtils.createToken(user.getId());
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user, loginUserVo);
        String tokenBody = JSON.toJSONString(loginUserVo);
        System.out.println(tokenBody);
        // 4.将token存入redis
        redisTemplate.opsForValue().set(SystemConstants.REDIS_TOKEN_KEY + token, tokenBody, 1, TimeUnit.DAYS);
        return Result.success(token);
    }

    /**
     * 退出登录
     *
     * @param token:
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/27 15:27
     */
    @Override
    public Result logout(String token) {
        redisTemplate.delete(SystemConstants.REDIS_TOKEN_KEY + token);
        return Result.success(null);
    }

    /**
     * 注册业务
     * 1、参数是否合法
     * 2、account是否已注册
     * 3、将注册信息写入数据库
     * 4、将登录信息记录到redis
     * @param loginParam:
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/28 13:25
     */
    @Override
    public Result register(LoginParam loginParam) {
        // 1.参数合法性判断
        if (StringUtils.isBlank(loginParam.getAccount())
                || StringUtils.isBlank(loginParam.getNickname())
                || StringUtils.isBlank(loginParam.getPassword()))
            return Result.fail(ErrorCode.PARAMS_ERROR.getCode(), ErrorCode.PARAMS_ERROR.getMsg());
        // 2.用户名不能重复
        SysUser user = sysUserService.findUser(loginParam.getAccount(),"");
        if (user != null) {
            return Result.fail(ErrorCode.ACCOUNT_EXIST.getCode(), ErrorCode.ACCOUNT_EXIST.getMsg());
        }
        // 3.将用户信息存入数据库
        BeanUtils.copyProperties(loginParam,user = new SysUser());
        user.setPassword(DigestUtils.md5Hex(loginParam.getPassword() + salt));
        user.setCreateDate(System.currentTimeMillis());
        user.setLastLogin(System.currentTimeMillis());
        sysUserService.saveUser(user);
        // 4.返回token作为redis的key
        String token = JwtUtils.createToken(user.getId());
        LoginUserVo loginUserVo = new LoginUserVo();
        BeanUtils.copyProperties(user,loginUserVo);
        redisTemplate.opsForValue().set(token, JSON.toJSONString(loginUserVo),1,TimeUnit.DAYS);
        return Result.success(token);
    }
    //  -------------------private methods---------------------

}
