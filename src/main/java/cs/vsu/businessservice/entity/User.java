package cs.vsu.businessservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="users")
@Getter
@Setter
@NoArgsConstructor
public class User implements Serializable, UserDetails {
    @Builder
    public User(String email,
                String password,
                String username,
                Role role
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public User(String email,
                String password,
                String username
    ) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false)
    private Long id;

    @Column(name = "user_name", unique = true, nullable = false)
    private String username;

    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "Email is not valid")
    private String email;

    @Column(name = "password", nullable = false)
    @Size(min = 8, max = 22, message = "Password length should be between 8 an 22 characters")
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name="role", nullable = false)
    private Role role;

    @JsonIgnore
    @OneToMany(mappedBy = "user", orphanRemoval = true)
    private Set<Project> projects = new LinkedHashSet<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
