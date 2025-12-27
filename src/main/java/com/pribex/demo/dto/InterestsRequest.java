package com.pribex.demo.dto;

import java.util.List;

public class InterestsRequest {
    private List<Long> interestIds;

    public List<Long> getInterestIds() {
        return interestIds;
    }

    public void setInterestIds(List<Long> interestIds) {
        this.interestIds = interestIds;
    }
}
