package test;

import mybatis.Vo.CustomUser;
import mybatis.Vo.UserQueryVo;
import mybatis.dao.UserMapper;
import mybatis.po.User;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.List;

/**
 * Created by zhouxw on 17/11/23.
 */
public class MapperUserTest {
    String resource = null;
    InputStream inputStream = null;
    SqlSessionFactory sqlSessionFactory = null;
    SqlSession sqlSession = null;
    @Before
    public void setUp() throws Exception{
        resource = "config/mybatis-config.xml";
        inputStream = Resources.getResourceAsStream(resource);
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        sqlSession = sqlSessionFactory.openSession();
    }
    @Test
    public void findUserByIdTest() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);

        User user = userMapper.findUserById(1);
        System.out.println(user);
        sqlSession.close();
    }
    @Test
    public void findUserByNameTest() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        List<User> users = userMapper.findUserByName("%xiaoming%");
        System.out.println(users);
        sqlSession.close();
    }
    @Test
    public void findUserList() throws Exception {
        SqlSession sqlSession = sqlSessionFactory.openSession();
        UserMapper userMapper = sqlSession.getMapper(UserMapper.class);
        CustomUser customUser = new CustomUser();
        customUser.setUsername("zhangsanfeng");
        customUser.setId(24);
        UserQueryVo userQueryVo1 = new UserQueryVo(customUser);
        CustomUser customUser1 = userMapper.findUserList(userQueryVo1);
        System.out.println(customUser1);

    }
}
