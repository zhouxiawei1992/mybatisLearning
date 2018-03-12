package test;
import io.thekraken.grok.api.Match;
import mybatis.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.jdbc.Null;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.log4j.Logger;
import org.junit.*;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhouxw on 17/11/22.
 */
public class MybatisFirst {
    String resource = null;
    InputStream inputStream = null;
    SqlSessionFactory sqlSessionFactory = null;
    SqlSession sqlSession = null;
    @Before
    public void init() throws Exception{
         resource = "config/mybatis-config.xml";
         inputStream = Resources.getResourceAsStream(resource);
         sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
         sqlSession = sqlSessionFactory.openSession();
    }


    @Test
    public void findUserByIdTest() throws Exception{
        User user = sqlSession.selectOne("test.findUserById",1);
        System.out.println(user);
        //release resource
        sqlSession.close();
    }
    @Test
    public void findUserByUserName() throws Exception {
        List<User> list = sqlSession.selectList("test.findUserByName","wangxiaojun%");
        System.out.println(list);
        sqlSession.close();
    }
    @Test
    public void insertUserTest() {
        User user = new User();
       // user.setId(101);
        user.setSex("1");
        user.setAddress(null);
        user.setUsername("wangxiaojun2");
        user.setBirthday(null);
        sqlSession.insert("test.insertUser",user);
        sqlSession.commit();
        sqlSession.close();
    }

    @Test
    public void testLog4J() {
         Logger logger = Logger.getLogger(Test.class);
        logger.debug("This is debug message.");
        // 记录info级别的信息
        logger.info("This is info message.");
        // 记录error级别的信息
        logger.error("This is error message.");


    }
    @Test
    public void testPP() {

        char result = '\t';
        String patternString = "\\|{2,5}";

        if (result == '.' || result == '|' || result == '$' || result == '￥' || result == '?' || result == '+' || result == '-'
                || result == '*' || result == '\t' || result == ' ') {
            patternString = patternString.replace("|",result + "");
        }else {
            patternString = result + "{2,5}";
        }

        Pattern pattern  = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher("1;;;;;11111\t\t\t|2\t3|");
        while (matcher.find()) {
            System.out.println(matcher.group() + matcher.start() + " " + matcher.end());
        }

    }
    @Test
    public void testPP2() {
        Pattern pattern  = Pattern.compile("\t");
        Matcher matcher = pattern.matcher("\t1|1|");
        while (matcher.find()) {
            System.out.println(matcher.group() + matcher.start() + " " + matcher.end());
        }
        String[] strings = "1|\t\t\t1|1".split("\\t\\t\\t");
        String s = "\\t\\t\\t".replaceAll("\\t","|");
        System.out.println(new ArrayList(Arrays.asList(strings)));
        System.out.println(new ArrayList(Arrays.asList("a   b  c  d".split("  "))));
    }
}
