package com.pribex.demo.dto;

import com.pribex.demo.enums.PrivacyType;
import lombok.Data;

import java.util.List;

@Data
public class StoryPrivacy {
    private PrivacyType type;
    private List<Long> allowedUserIds;
    private List<Long> blockedUserIds;
}

