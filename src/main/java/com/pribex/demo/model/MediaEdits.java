package com.pribex.demo.model;

import lombok.Data;

@Data
public class MediaEdits {
    private CropInfo crop;
    private FilterAdjustments filters;
    private Float rotation;
    private Float scale;
}
