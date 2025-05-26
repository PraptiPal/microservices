package com.asti.user_api.exception;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomErrorDetails {
	
	private Date timestamp;
	
	private String message;
	
	private String errorDetails;

}
