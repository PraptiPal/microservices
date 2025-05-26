package com.asti.myservlets.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Hello {

	private String name;
	
	private Integer age;
	
	public String greet() {
		return "Welcome to Spring Boot";
	}
}
