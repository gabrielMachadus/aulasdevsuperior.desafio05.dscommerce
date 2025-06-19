package br.com.devsuperior.dscommerce.entities;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.util.*;

@SuppressWarnings("serial")
@Entity
@Table(name = "tb_user")
@EqualsAndHashCode
public class User implements UserDetails {

    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    @Getter
    private String name;
    @Setter
    @Getter
    @Column(unique = true)
    private String email;
    @Setter
    @Getter
    private String phone;
    @Setter
    @Getter
    private LocalDate birthDate;
    @Setter
    @Getter
    private String password;

    @ManyToMany
    @JoinTable(name = "tb_user_role",
            joinColumns = @JoinColumn (name = "user_id"),
            inverseJoinColumns =  @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    @Getter
    @OneToMany(mappedBy = "client")
    private List<Order> orders = new ArrayList();

    public User() {
    }

    public void addRole(Role role){
        roles.add(role);
    }

    public boolean hasRole(String roleName){
        for (Role role: roles){
           if(role.getAuthority().equals(roleName)){
               return true;
            }
        }
        return false;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return UserDetails.super.isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return UserDetails.super.isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return UserDetails.super.isCredentialsNonExpired();
    }

    @Override
    public boolean isEnabled() {
        return UserDetails.super.isEnabled();
    }

    public User(Long id, String name, String email, String phone, LocalDate birthDate, String password ) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.birthDate = birthDate;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", roles='NOT IMPLEMENTED YET" +  + '\'' +
                '}';
    }

}
