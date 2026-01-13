package com.project.naturalmarket.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements Serializable {


    private Long id;
    @NotEmpty
    @Size(min = 2,message = "ingrese minimo 2 caracteres para el nombre")
    private String name;

    @NotEmpty
    @Size(min = 2,message = "ingrese minimo 5 caracteres para la descripcion")
    private String description;
}