package com.example.managementsystem.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    @JsonProperty("id")
    private Long id;
    @JsonProperty("firstName")
    @NotBlank(message = "required first name")
    @Size(min = 3, max = 100)
    private String firstName;
    @JsonProperty("lastName")
    @NotBlank(message = "required last name")
    @Size(min = 2, max = 100)
    private String lastName;
    @JsonProperty("email")
    @NotBlank(message = "required email")
    @Email
    private String email;
}
