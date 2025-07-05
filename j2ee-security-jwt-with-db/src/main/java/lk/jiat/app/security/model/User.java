package lk.jiat.app.security.model;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;  // <-- Missing import

@Entity
@Table(name = "users")
public class User {

    @Id

    private String username;
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_roles" , joinColumns = @JoinColumn(name = "username"))
    @Column(name = "role")
    private Set<String> roles = new HashSet<>();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Set<String> getRoles() {
        return roles;
    }

    public void setRoles(Set<String> roles) {
        this.roles = roles;
    }
}
