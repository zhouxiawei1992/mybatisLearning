package mybatis.po;

import java.util.Date;

/**
 * Created by zhouxw on 17/11/22.
 */
public class User {
    //属性名和数据库表的字段对应
    private int id;
    private String username;// 用户姓名
    private String sex;// 性别
    private Date birthday;// 生日
    private String address;// 地址

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday=" + birthday +
                ", address='" + address + '\'' +
                '}';
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getId() {
        return id;
    }

    public String getSex() {
        return sex;
    }

    public String getUsername() {
        return username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getAddress() {
        return address;
    }
}
