package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.Status;
import bts.delation.model.User;
import bts.delation.repo.FeedbackRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;
    private final DiscordUserService discordUserService;
    private final UserService userService;


    public List<Feedback> getAll() {

        return repo.findAll();
    }

    public Feedback getOne(String id) {
        return getById(id);
    }

    public List<Feedback> getByAuthor(String username) {

        DiscordUser user = discordUserService.getByUsername(username);

        return repo.findByAuthor(user);
    }

    public Feedback save(Feedback feedback) {

        return repo.save(feedback);
    }

    public void moveInProgress(String taskId) {
        Feedback feedback = getById(taskId);

        feedback.setStatus(Status.IN_PROGRESS);

        save(feedback);
    }


    public void moveToDone(String taskId) {
        Feedback feedback = getById(taskId);

        feedback.setStatus(Status.DONE);

        save(feedback);
    }

    public void assignModer(String id, String moder) {
        Feedback feedback = getById(id);

        if (moder.equals("none")) {
            feedback.setModer(null);
        } else {
            User user = userService.getById(moder);

            feedback.setModer(user);
        }

        repo.save(feedback);
    }

    private Feedback getById(String taskId) {
        return repo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }
}
