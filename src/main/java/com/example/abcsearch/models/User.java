package com.example.abcsearch.models;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.lucene.document.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="usr")
public class User  implements UserDetails {

    @NotEmpty( message = "First Name can't be empty")
    private String firstName;
    @NotEmpty(message = "Last Name can't be empty")
    private String lastName;
    @NotEmpty(message = "Email can't be empty")
   //@Email(message = "Email should be valid")
    @Id
    private String username;
    @NotEmpty(message = "Password can't be empty")
    private String password;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
    @ElementCollection(fetch = FetchType.EAGER)
    Map<Date, String> history;


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