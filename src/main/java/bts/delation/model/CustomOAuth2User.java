package bts.delation.model;

import bts.delation.model.enums.UserRole;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;

@ToString
public class CustomOAuth2User implements OidcUser {
 
    private OidcUser oidcUser;

    private UserRole role;

    public CustomOAuth2User(OidcUser oidcUser, UserRole role) {
        this.oidcUser = oidcUser;
        this.role = role;
    }

    @Override
    public Map<String, Object> getClaims() {
        return oidcUser.getClaims();
    }

    @Override
    public OidcUserInfo getUserInfo() {
        return oidcUser.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return oidcUser.getIdToken();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oidcUser.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(role);
    }

    @Override
    public String getName() {
        return oidcUser.getName();
    }

    public UserRole getRole() {
        return role;
    }

    public boolean isModer() {
        return role.equals(UserRole.MODER);
    }
    public boolean isAdmin() {
        return role.equals(UserRole.ADMIN);
    }
}