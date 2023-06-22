package bts.delation.model.enums;

import org.springframework.security.core.GrantedAuthority;

public enum UserRole implements GrantedAuthority {
    CLIENT, MODER, ADMIN;

    @Override
    public String getAuthority() {
        return this.name();
    }
}