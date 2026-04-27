package com.HackOverflow.backend.service;

import com.HackOverflow.backend.dto.AiEligibilityResponse;
import com.HackOverflow.backend.model.Event;
import com.HackOverflow.backend.model.VolunteerProfile;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AiEligibilityService {

    private final ChatClient chatClient;

    public AiEligibilityService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public AiEligibilityResponse analyze(Event event, VolunteerProfile volunteer) {

        String prompt = """
                You are an assistant helping match volunteers to events.

                EVENT DETAILS:
                Name: %s
                Description: %s
                Category: %s
                Location: %s

                VOLUNTEER DETAILS:
                Skills: %s
                Location: %s
                Availability: %s to %s

                TASK:
                1. Summarize the event in 2-3 lines.
                2. Check if volunteer skills match the event.
                3. Decide if the volunteer is eligible.

                FORMAT RESPONSE STRICTLY:
                Summary: ...
                Skill Match: ...
                Eligibility: ...
                """.formatted(
                event.getName(),
                event.getDescription(),
                event.getCategory(),
                event.getLocation(),
                volunteer.getSkills(),
                volunteer.getLocation(),
                volunteer.getAvailabilityStart(),
                volunteer.getAvailabilityEnd()
        );

        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return parseResponse(response);
    }

    private AiEligibilityResponse parseResponse(String response) {

        String summary = extract(response, "Summary:");
        String skillMatch = extract(response, "Skill Match:");
        String eligibility = extract(response, "Eligibility:");

        return new AiEligibilityResponse(summary, skillMatch, eligibility);
    }

    private String extract(String text, String key) {
        int start = text.indexOf(key);
        if (start == -1) return "";

        int end = text.indexOf("\n", start);
        if (end == -1) end = text.length();

        return text.substring(start + key.length(), end).trim();
    }
}