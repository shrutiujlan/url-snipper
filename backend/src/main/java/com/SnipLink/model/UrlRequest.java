package com.SnipLink.model;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;


@Data
public class UrlRequest {

    @NotBlank
    private String url;

}
