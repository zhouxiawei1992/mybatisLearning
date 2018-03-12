package mybatis.dao;

import mybatis.Vo.CustomUser;
import mybatis.Vo.UserQueryVo;
import mybatis.po.User;

import java.util.List;

/**
 * Created by zhouxw on 17/11/23.
 */
public interface UserMapper {
    public User findUserById(int id) throws Exception;
    public List<User> findUserByName(String name) throws Exception;
    public CustomUser findUserList(UserQueryVo userQueryVo) throws Exception;
}
