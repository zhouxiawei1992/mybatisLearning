package mybatis.Vo;

/**
 * Created by zhouxw on 17/11/23.
 */
public class UserQueryVo {
    public UserQueryVo(CustomUser customUser) {
        this.customUser = customUser;
    }

    public CustomUser getCustomUser() {
        return customUser;
    }

    public void setCustomUser(CustomUser customUser) {
        this.customUser = customUser;
    }

    private CustomUser customUser = null;
}
