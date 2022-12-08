package com.watch.shopwatchonline.security.Request;

import java.util.Date;
import java.util.Set;

import javax.validation.constraints.*;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {
  @NotNull
  @Size(min = 3, max = 20)
  private String username;
  @NotNull
  private String name;
  @NotNull
  @Size(max = 50)
  @Email
  private String email;

  @NotNull
  private Short gender;
  private MultipartFile imageFile;
  @NotNull
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birtday;
  @NotNull
  private String phone;

  private Set<String> role;

  @Size(min = 6, max = 40)
  private String password;
}
