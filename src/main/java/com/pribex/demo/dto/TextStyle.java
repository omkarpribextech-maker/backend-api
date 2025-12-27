package com.pribex.demo.dto;

import com.pribex.demo.enums.TextAlignment;
import lombok.Data;

@Data
public class TextStyle {
    private String fontName;
    private Float fontSize;
    private String textColor;
    private TextBackground background;
    private TextAlignment alignment;
}
