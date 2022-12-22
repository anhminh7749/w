package com.watch.shopwatchonline.Model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
    @UniqueConstraint(columnNames = "username"),
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "phone")
})
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  
  @Column(columnDefinition = "nvarchar(50)")
  private String name;

  
  @Size(max = 20)
  private String username;

  
  @Size(max = 50)
  @Email
  private String email;

  
  @Size(max = 120)
  private String password;

  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birtday;

  private String avatar;

  
  private Short gender;

  
  private String phone;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<Raiting> raitings;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<wishlist> wishlists;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<ChatBox> chatBox;

  @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
  private Set<Address> address;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

}
