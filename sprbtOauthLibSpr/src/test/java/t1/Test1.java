package t1;

import g1.ClassInTopPackage;
import g1.libcmn.domain.*;
import g1.libcmn.util.*;
import g1.libspr.service.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Date;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class Test1 {

    static final Logger logger             = LoggerFactory.getLogger(Test1.class);



    @Autowired
    BbsPostService postService;
    @Autowired
    UserInfoService userInfoService;

    @SpringBootApplication( scanBasePackageClasses ={Test1.class, ClassInTopPackage.class} )
    @EnableAutoConfiguration(exclude={MongoAutoConfiguration.class})
    @Configuration
    static public class Config1 {

    }


    @Test
    public void test1() {
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter");










    }
    @Test
    public void TestPost(){
        logger.info(""+ Util1.getCurrentClassName()+"."+Util1.getCurrentMethodName()+" enter");
        Date dtNow = new Date();
        BbsPost post = new BbsPost();
        long userId = dtNow.getTime();
        String title1 = "title"+dtNow.getTime();
        post.setUserId(userId);
        post.setTitle(title1);
        post.setContent("content"+dtNow.getTime());
        postService.insert(post);

        assertTrue(post.getPostId()>0);
        long postId = post.getPostId();

        BbsPost post10 = postService.getById(postId);
        assertTrue(post10!=null);
        assertTrue(title1.equals(post10.getTitle()));

        postService.delete(postId, 0);
        BbsPost post20 = postService.getById(postId);
        assertTrue(post20==null);


    }


    @Test
    public void CreateUser(){
        String mobile = "111";
        String name = "n"+mobile;
        String pwd = "aaa";
        UserReg ur = new UserReg();
        ur.setNickName(name);
        ur.setMobile(mobile);
        ur.setPassword(pwd);
        ur.setPwd2(pwd);
        userInfoService.registerUser(ur);

    }








}
