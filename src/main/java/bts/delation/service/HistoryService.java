package bts.delation.service;

import bts.delation.model.*;
import bts.delation.repo.HistoryRecordRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryRecordRepo repository;

    public List<HistoryRecord> feedbackHistory(Feedback feedback) {

        return repository.getAllByFeedback(feedback);
    }

    public void taskCreated(Feedback feedback, String author) {
        HistoryRecord record = HistoryRecord.builder()
                .type(HistoryRecord.HistoryType.CREATED)
                .feedback(feedback)
                .author(author)
                .build();

        repository.save(record);
    }

    public void assignedModer(Feedback feedback, String author, String before, String after) {
        HistoryRecord record = HistoryRecord.builder()
                .type(HistoryRecord.HistoryType.ASSIGNED_USER)
                .author(author)
                .before(before)
                .after(after)
                .feedback(feedback)
                .time(LocalDateTime.now())
                .build();

        repository.save(record);
    }

    public void changeStatus(Feedback feedback, String author, Status before, Status after) {
        HistoryRecord record = HistoryRecord.builder()
                .type(HistoryRecord.HistoryType.CHANGE_STATUS)
                .author(author)
                .before(before.name())
                .after(after.name())
                .feedback(feedback)
                .time(LocalDateTime.now())
                .build();

        repository.save(record);
    }

    public void addComment(Feedback feedback, String author, String comment) {
        HistoryRecord record = HistoryRecord.builder()
                .type(HistoryRecord.HistoryType.COMMENT_ADDED)
                .feedback(feedback)
                .author(author)
                .comment(comment)
                .time(LocalDateTime.now())
                .build();

        repository.save(record);
    }
}
