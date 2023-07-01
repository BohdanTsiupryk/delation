package bts.delation.model.dto;

import bts.delation.model.Feedback;

import java.util.List;

public record FeedbackPage(
        List<Feedback> feedbacks,
        int page,
        int size,
        long total
) {
}
