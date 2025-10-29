package com.example.web_petvibe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Entity
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    @Enumerated(EnumType.ORDINAL)
    @Column(nullable = false, length = 20)
    Role role;

    // kh trả về và kh bắt user nhập thông tin
    boolean deleted = false; //false = not deleted - Đổi tên từ isDeleted thành deleted

//    @NotBlank
    String fullName;

//    @Email(message = "Invalid Email!")
    @Column(unique = true)
    String email;

    @Pattern(regexp = "(84|0[3|5|7|8|9])+(\\d{8})", message = "Invalid phone!")
    @Column(unique = true)
    String phone;

    @Size(min = 6, message = "Password must be at least 6 character!")
    String password;

    // Thêm quan hệ One-to-Many
    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore  // Tránh vòng lặp vô hạn khi serialize JSON
    private List<PetProfile> petProfiles = new ArrayList<>();

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if(this.role != null) authorities.add(new SimpleGrantedAuthority(this.role.toString()));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.phone;
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

    // Override getPassword() method từ UserDetails interface
    @Override
    public String getPassword() {
        return this.password;
    }


}
