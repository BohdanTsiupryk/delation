package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.Status;
import bts.delation.repo.FeedbackRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;
    private final DiscordUserService discordUserService;


    public List<Feedback> getAll() {

        return repo.findAll();
    }

    public List<Feedback> getByAuthor(String username) {

        DiscordUser user = discordUserService.getByUsername(username);

        return repo.findByAuthor(user);
    }

    public Feedback save(Feedback feedback) {

        return repo.save(feedback);
    }

    public void moveInProgress(String taskId) {
        Feedback feedback = getAppealById(taskId);

        feedback.setStatus(Status.IN_PROGRESS);

        save(feedback);
    }


    public void moveToDone(String taskId) {
        Feedback feedback = getAppealById(taskId);

        feedback.setStatus(Status.DONE);

        save(feedback);
    }

    private Feedback getAppealById(String taskId) {
        return repo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Appeal not found"));
    }
}
