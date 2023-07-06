package bts.delation.service;

import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackFlowService {

    private final DiscordNotificationService discordNotificationService;
    private final FeedbackService feedbackService;
    private final HistoryService historyService;


    public void manageStatusFlow(Long feedbackId, Status newStatus, String moder) {

        Feedback feedback = feedbackService.getById(feedbackId);
        Status currentStatus = feedback.getStatus();

        if (currentStatus.equals(newStatus)) return;


        switch (newStatus) {
            case NEW -> {
            }
            case IN_PROGRESS -> {
                moveInProgress(feedbackId);
                historyService.changeStatus(feedback, moder, currentStatus, newStatus);
            }
            case VALIDATION -> {
                if (currentStatus.equals(Status.IN_PROGRESS)) {
                    moveToValidation(feedbackId);
                    historyService.changeStatus(feedback, moder, currentStatus, newStatus);
                }
            }
            case DONE -> {
                if (currentStatus.equals(Status.VALIDATION)) {
                    if (isReadyToDone(feedback)) {
                        moveToDone(feedbackId);
                        historyService.changeStatus(feedback, moder, currentStatus, newStatus);
                    }
                }
            }
            case CANCELED -> {
                moveInCanceled(feedbackId);
                historyService.changeStatus(feedback, moder, currentStatus, newStatus);
            }
        }
    }

    private boolean isReadyToDone(Feedback feedback) {
        return feedback.isCommentAdded()
                && feedback.isModerAssigned();
    }

    private void moveInCanceled(Long taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.CANCELED);

        feedback.setStatus(Status.CANCELED);

        feedbackService.saveUpdate(feedback);
    }

    private void moveInProgress(Long taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.IN_PROGRESS);

        feedback.setStatus(Status.IN_PROGRESS);

        feedbackService.saveUpdate(feedback);
    }


    private void moveToDone(Long taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.DONE);

        feedback.setStatus(Status.DONE);

        feedbackService.saveUpdate(feedback);
    }

    private void moveToValidation(Long taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.VALIDATION);

        feedback.setStatus(Status.VALIDATION);

        feedbackService.saveUpdate(feedback);
    }
}
