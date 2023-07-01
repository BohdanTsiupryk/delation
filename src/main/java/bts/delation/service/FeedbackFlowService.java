package bts.delation.service;

import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FeedbackFlowService {

    private final DiscordNotificationService discordNotificationService;
    private final FeedbackService feedbackService;
    private final HistoryService historyService;


    public void manageStatusFlow(String feedbackId, Status newStatus, String moder) {

        Feedback feedback = feedbackService.getById(feedbackId);
        Status currentStatus = feedback.getStatus();

        if (currentStatus.equals(newStatus)) return;


        switch (newStatus) {
            case NEW -> {
            }
            case IN_PROGRESS -> {
                if (currentStatus.equals(Status.NEW) || currentStatus.equals(Status.VALIDATION)) {
                    moveInProgress(feedbackId);
                    historyService.changeStatus(feedback, moder, currentStatus, newStatus);
                }
            }
            case VALIDATION -> {
                if (currentStatus.equals(Status.IN_PROGRESS)) {
                    moveToValidation(feedbackId);
                    historyService.changeStatus(feedback, moder, currentStatus, newStatus);
                }
            }
            case DONE -> {
                if (currentStatus.equals(Status.VALIDATION)) {
                    moveToDone(feedbackId);
                    historyService.changeStatus(feedback, moder, currentStatus, newStatus);
                }
            }
            case CANCELED -> {
                moveInCanceled(feedbackId);
                historyService.changeStatus(feedback, moder, currentStatus, newStatus);
            }
        }
    }

    private void moveInCanceled(String taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.CANCELED);

        feedback.setStatus(Status.CANCELED);

        feedbackService.save(feedback);
    }

    private void moveInProgress(String taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.IN_PROGRESS);

        feedback.setStatus(Status.IN_PROGRESS);

        feedbackService.save(feedback);
    }


    private void moveToDone(String taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.DONE);

        feedback.setStatus(Status.DONE);

        feedbackService.save(feedback);
    }

    private void moveToValidation(String taskId) {
        Feedback feedback = feedbackService.getById(taskId);

        discordNotificationService.notifyTaskStatusChanged(taskId, Status.VALIDATION);

        feedback.setStatus(Status.VALIDATION);

        feedbackService.save(feedback);
    }
}
