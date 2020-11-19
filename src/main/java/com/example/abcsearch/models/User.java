package com.example.abcsearch.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.TreeMap;

@Data
@NoArgsConstructor
@Entity
@Table(name="usr")
public class User  implements UserDetails {
    private String firstName;
    private String lastName;
    @Id
    private String username; //email
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;


    @Override
    public boolean equals(Object obj) {
        return this.username.equals(((User) obj).getUsername());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getRoles();
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

//add validation
//add "My search history"      search query + date ( mb put in Map) String/Date
//