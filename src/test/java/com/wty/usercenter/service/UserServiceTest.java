package com.wty.usercenter.service;
import java.util.Date;

import com.wty.usercenter.model.domain.User;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest {
    @Resource
    UserService service;

    @Test
    public void testAdd(){
        User user = new User();
        user.setUsername("李四");
        user.setUserAccount("1111");
        user.setAvatarUrl("aaaa");
        user.setGender(0);
        user.setUserPassword("1111");
        user.setPhone("13555555555");
        user.setEmail("111@qq.com");
        user.setUserStatus(0);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setIsDelete(0);
        boolean save = service.save(user);
        Assert.assertEquals(true,save);

    }

    /*@Test
    public void userRegister() {
        String userAccount = "tomcat";
        String userPassword = "12345678";
        String checkPassword = "12345678";
        long result = service.userRegister(UserRegisterRequest);
        Assertions.assertEquals(-1,result);

    }*/
}