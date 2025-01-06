package com.chicu.cakeshop.model;


import com.chicu.cakeshop.enums.Authority;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private int id;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role")
    private Set<Role> authorities;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String phone;

    private String firstname;

    private String surname;

    private Boolean subscribeToDiscounts;




    @Override
    public String getUsername() {
        return getEmail();
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

    public boolean hasAuthority(Authority authority) {
        return getAuthorities().stream().map(Role::getAuthority).anyMatch(el -> el.equals(authority.toString()));
    }

    public String getAuthoritiesText() {
        StringBuilder result = new StringBuilder();
        for (Role role : authorities) {
            result.append(Authority.valueOf(role.getAuthority()).getReadableName()).append(", ");
        }

        return result.substring(0, result.length() - 2);
    }

    public List<Long> getRoleIds() {
        return getAuthorities().stream().map(Role::getId).collect(Collectors.toList());
    }

    public String getOrderDetails() {
        return getFirstname() + " " + getSurname();
    }
}
