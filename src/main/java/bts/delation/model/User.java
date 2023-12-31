package bts.delation.model;

import bts.delation.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usr")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"feedbacks"})
public class User implements UserDetails {

    @Id
    private String id;

    private String email;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "moder")
    private List<Feedback> feedbacks = new ArrayList<>();

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "discord_id", referencedColumnName = "id")
    private DiscordUser discordUser;

    public User(String id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(UserRole.values());
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
