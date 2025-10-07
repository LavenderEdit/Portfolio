package com.portfolio.lavender.dto.simple;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 *
 * @author Joan Lavender
 */
@Data
public class ContactForm {

    @NotBlank
    @Size(max = 120)
    private String name;
    @NotBlank
    @Email
    @Size(max = 160)
    private String email;
    @NotBlank
    @Size(max = 5000)
    private String message;
}
