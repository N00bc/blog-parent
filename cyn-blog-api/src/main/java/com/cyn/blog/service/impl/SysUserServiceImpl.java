package com.cyn.blog.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.LoginUserVo;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.gloableEnum.ErrorCode;
import com.cyn.blog.mapper.SysUserMapper;
import com.cyn.blog.service.SysUserService;
import com.cyn.blog.utils.JwtUtils;
import com.cyn.blog.utils.SystemConstants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 22:45
 */
@Service
public class SysUserServiceImpl implements SysUserService {
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private StringRedisTemplate redisTemplate;

    /**
     * 根据作者id查询作者对象信息
     *
     * @param id:作者id
     * @return com.cyn.blog.entity.pojo.SysUser
     * @author G0dc
     * @date 2022/7/25 22:49
     */
    @Override
    public SysUser findUserById(Long id) {
        SysUser sysUser = sysUserMapper.selectById(id);
        if (sysUser == null) {
            sysUser = new SysUser();
            sysUser.setNickname("站长");
        }
        return sysUser;
    }

    /**
     * 根据用户名密码查询用户
     *
     * @param account:  用户名
     * @param password: 密码(加密后)
     * @return com.cyn.blog.entity.pojo.SysUser
     * @author G0dc
     * @date 2022/7/27 14:50
     */
    @Override
    public SysUser findUser(String account, String password) {
        LambdaQueryWrapper<SysUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper
                .eq(SysUser::getAccount, account)
                .eq(!StringUtils.isBlank(password), SysUser::getPassword, password);
        return sysUserMapper.selectOne(queryWrapper);
    }


    /**
     * 根据请求头信息获取当前用户信息
     *
     * @param token:
     * @return com.cyn.blog.entity.vo.Result
     * @author G0dc
     * @date 2022/7/27 14:51
     */
    @Override
    public Result getCurrentUser(String token) {
        Map<String, Object> stringObjectMap = JwtUtils.checkToken(token);
        // 1.判断token是否为空
        if (stringObjectMap == null) {
            // 无登录信息
            return Result.fail(ErrorCode.NO_LOGIN.getCode(), ErrorCode.NO_LOGIN.getMsg());
        }
        String userInfo = redisTemplate.opsForValue().get(SystemConstants.REDIS_TOKEN_KEY + token);
        // 2.查看redis中是否有当前对象信息
        if (StringUtils.isBlank(userInfo)) {
            // 会话超时
            return Result.fail(ErrorCode.SESSION_TIME_OUT.getCode(), ErrorCode.SESSION_TIME_OUT.getMsg());
        }
        // 3.解析json
        LoginUserVo currentUser = JSON.parseObject(userInfo, LoginUserVo.class);
        return Result.success(currentUser);
    }

    /**
     * 向MySQL中插入一条用户数据
     *
     * @param user:
     * @return void
     * @author G0dc
     * @date 2022/7/28 19:44
     */
    @Override
    public void saveUser(SysUser user) {
        sysUserMapper.insert(user);
    }

    /**
     * 校验请求头中的token
     *
     * @param token:
     * @return com.cyn.blog.entity.pojo.SysUser
     * @author G0dc
     * @date 2022/7/28 19:45
     */
    @Override
    public SysUser checkToken(String token) {
        // 1.从redis中根据key 获取 value，若value == null 说明已经过期，需要重新登录
        String userInfo = redisTemplate.opsForValue().get(SystemConstants.REDIS_TOKEN_KEY + token);
        return JSON.parseObject(userInfo, SysUser.class);
    }
}
