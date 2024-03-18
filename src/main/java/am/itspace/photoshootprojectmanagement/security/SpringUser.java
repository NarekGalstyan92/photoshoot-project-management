package am.itspace.photoshootprojectmanagement.security;

import am.itspace.photoshootprojectmanagement.entity.User;
import lombok.Getter;
import org.springframework.security.core.authority.AuthorityUtils;

@Getter
public class SpringUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public SpringUser(User user) {
        super(user.getEmail(), user.getPassword(),
                user.isActive(), true, true, true,
                AuthorityUtils.createAuthorityList(user.getRole().name()));
        this.user = user;
    }
}
