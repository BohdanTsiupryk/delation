package bts.delation.service;

import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackFlowService {

    private final FeedbackService feedbackService;
    private final HistoryService historyService;

    public void manageStatusFlow(Long feedbackId, Status newStatus, String moder) {

        Feedback feedback = feedbackService.getById(feedbackId);
        Status currentStatus = feedback.getStatus();

        if (currentStatus.equals(newStatus)) return;


        switch (newStatus) {
            case NEW -> {}
            case IN_PROGRESS -> handleInProgress(newStatus, moder, feedback, currentStatus);
            case VALIDATION -> handleValidation(newStatus, moder, currentStatus, feedback);
            case DONE -> handleDone(newStatus, moder, currentStatus, feedback);
            case CANCELED -> handleCanceled(newStatus, moder, feedback, currentStatus);
        }
    }

    private void handleCanceled(Status newStatus, String moder, Feedback feedback, Status currentStatus) {
        feedback.setStatus(Status.CANCELED);

        feedbackService.saveUpdate(feedback);
        historyService.changeStatus(feedback, moder, currentStatus, newStatus);
    }

    private void handleDone(Status newStatus, String moder, Status currentStatus, Feedback feedback) {
        if (currentStatus.equals(Status.VALIDATION)) {
            if (isReadyToDone(feedback)) {
                feedback.setStatus(Status.DONE);

                feedbackService.saveUpdate(feedback);
                historyService.changeStatus(feedback, moder, currentStatus, newStatus);
            }
        }
    }

    private void handleInProgress(Status newStatus, String moder, Feedback feedback, Status currentStatus) {
        feedback.setStatus(Status.IN_PROGRESS);

        feedbackService.saveUpdate(feedback);
        historyService.changeStatus(feedback, moder, currentStatus, newStatus);
    }

    private void handleValidation(Status newStatus, String moder, Status currentStatus, Feedback feedback) {
        if (currentStatus.equals(Status.IN_PROGRESS)) {
            feedback.setStatus(Status.VALIDATION);

            feedbackService.saveUpdate(feedback);

            historyService.changeStatus(feedback, moder, currentStatus, newStatus);
        }
    }

    private boolean isReadyToDone(Feedback feedback) {
        return feedback.isCommentAdded()
                && feedback.isModerAssigned();
    }
}
