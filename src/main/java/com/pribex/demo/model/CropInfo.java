package com.pribex.demo.model;

import lombok.Data;

@Data
public class CropInfo {

    private String aspectRatio; // "9:16"
    private Float x;
    private Float y;
    private Float width;
    private Float height;
}

