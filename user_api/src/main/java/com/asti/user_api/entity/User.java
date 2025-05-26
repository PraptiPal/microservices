package com.asti.user_api.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.Getter;

@Data
@Table(name="USER", schema="public")
@Entity
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long userId;
	
	@Size(min = 4, message = "First name should be atleast 4 characters")
	@Column(name = "first_name", length=50)
	private String firstName;
	
	@Column(name = "last_name", length = 50)
	private String lastName;
	
	//@Size(min = 6, message = "User name should be atleast 6 characters")
	@Pattern(regexp = "^[a-zA-Z0-9]{6,12}$", message = "Username must be 6-12 characters with no special characters")
	@NotBlank(message = "Username is missing")
	@NotEmpty(message = "Username should not be empty")
	@NotNull(message = "Username should not be null")
	@Column(name="user_name")
	private String userName;
	
	@Size(min = 8, message = "Password should be atleast 8 characters")
	@NotBlank(message = "Password is missing")
	@NotEmpty(message = "Password should not be empty")
	@NotNull(message = "Password should not be null")
	@Column(name="password")
	private String password;
	
	@Column(name= "enabled")
	private Boolean enabled;
	
	@Size(min = 4, message = "Role should be atleast 4 characters")
	@NotBlank(message = "Role is missing")
	@NotEmpty(message = "Role should not be empty")
	@NotNull(message = "Role should not be null")
	@Column(name="role")
	private String role;
	
	@Column(name="adress")
	private String adress;
	
	@Email(message = "Email should be valid")
	@NotBlank(message = "Email is missing")
	@NotEmpty(message = "Email should not be empty")
	@NotNull(message = "Email should not be null")
	@Column(name="email")
	private String email;
}
