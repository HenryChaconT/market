package com.project.naturalmarket.dto;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NonNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.sql.Blob;

@Data
public class ProductDto implements Serializable {

    private Long id;

    @NotEmpty
    @Size(min = 2,message = "el nombre del producto debe tener minimo 2 caracteres")
    private String name;

    @NotEmpty
    @Size(min = 5,message = "el descripcion del producto debe tener minimo 5 caracteres")
    private String description;

    @NotNull
    @Digits(integer = 7, fraction = 2)
    private double price;

    private int stock;

    @NotNull
    private long categoryId;

    @NotNull
    private long supplierId;


    private String imagenBase64;
}