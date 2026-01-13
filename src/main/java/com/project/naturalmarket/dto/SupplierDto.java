package com.project.naturalmarket.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;


@Data
public class SupplierDto implements Serializable {

    private Long id;

    @NotEmpty
    @Size(min = 2,message = "ingrese minimo 2 caracteres para el nombre")
    private String name;

    @NotEmpty
    private String address;

    @NotEmpty
    private String phone;

    @NotEmpty
    @Email
    private String email;

    private List<ProductDto> product;
}