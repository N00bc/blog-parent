package com.cyn.blog;

import com.alibaba.fastjson.JSON;
import com.cyn.blog.entity.pojo.SysUser;
import com.cyn.blog.entity.vo.ArticleVo;
import com.cyn.blog.entity.vo.LoginParam;
import com.cyn.blog.entity.vo.PageParams;
import com.cyn.blog.entity.vo.Result;
import com.cyn.blog.service.ArticleService;
import com.cyn.blog.service.SysUserService;
import com.cyn.blog.service.TagService;
import com.cyn.blog.service.impl.LoginServiceImpl;
import com.cyn.blog.utils.JwtUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;
import java.util.Map;

/**
 * @author G0dc
 * @description:
 * @date 2022/7/25 16:37
 */
@SpringBootTest
public class AppTest {
    public static final String salt = "cyn0v%";
    @Autowired
    private ArticleService articleService;
    @Autowired
    private TagService tagService;

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Test
    void testArticleService() {
        PageParams pageParams = new PageParams();
        pageParams.setPage(1);
        pageParams.setPageSize(10);
        Result result = articleService.listArticles(pageParams);
        List<ArticleVo> data = (List<ArticleVo>) result.getData();
        System.out.println("------------------------------------------");
        System.out.println(data.size());
        System.out.println(result);
    }

    @Test
    void testTagService() {
        Result hotTags = tagService.getHotTags(2);
        System.out.println(hotTags);
    }


    @Test
    void testRedis() {
        redisTemplate.opsForValue().set("a","hello");
        String a = redisTemplate.opsForValue().get("a");
        System.out.println("a = " + a);
    }

    @Test
    void testMd5() {
        for (int i = 0; i < 4; i++) {
            String s = DigestUtils.md5Hex("test" + salt);
            System.out.println("s = " + s);
        }
    }

    @Test
    void testJwtUtils() {
        String token = JwtUtils.createToken(1l);
        System.out.println("token = " + token);
        Map<String, Object> stringObjectMap = JwtUtils.checkToken(token);
        System.out.println("stringObjectMap = " + stringObjectMap);
    }

    @Autowired
    private LoginServiceImpl loginService;

    @Test
    void testLoginService() {
        LoginParam loginParam = new LoginParam();
        loginParam.setAccount("admin");
        loginParam.setPassword("test");
        Result login = loginService.login(loginParam);
        System.out.println("login = " + login);
    }

    @Autowired
    private SysUserService sysUserService;

    @Test
    void testSysUserService() {
        SysUser user = sysUserService.findUser("123", "1230");
        System.out.println(user);
        LoginParam loginParam = new LoginParam();
        loginParam.setPassword("123");
        loginParam.setNickname("zhangsan");
        loginParam.setAccount("123");
        BeanUtils.copyProperties(loginParam,user = new SysUser());
        System.out.println(user);
        System.out.println(user.hashCode());
    }

    @Test
    void testStringUtils() {
        boolean blank = StringUtils.isBlank("");
        System.out.println("blank = " + blank);
    }

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Test
    void testStringRedisTemplate() {
        String adf = stringRedisTemplate.opsForValue().get("adf");
        System.out.println(JSON.parseObject(adf, SysUser.class));
    }
}
