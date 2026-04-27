package com.HackOverflow.backend.dto;

public class AiEligibilityResponse {
    private String summary;
    private String skillMatch;
    private String eligibility;

    public AiEligibilityResponse() {
    }

    public AiEligibilityResponse(String summary, String skillMatch, String eligibility) {
        this.summary = summary;
        this.skillMatch = skillMatch;
        this.eligibility = eligibility;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getSkillMatch() {
        return skillMatch;
    }

    public void setSkillMatch(String skillMatch) {
        this.skillMatch = skillMatch;
    }

    public String getEligibility() {
        return eligibility;
    }

    public void setEligibility(String eligibility) {
        this.eligibility = eligibility;
    }
}
