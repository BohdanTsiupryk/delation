package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.User;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.UserRole;
import bts.delation.repo.FeedbackRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;
    private final DiscordUserService discordUserService;
    private final UserService userService;
    private final HistoryService historyService;


    public List<Feedback> getAll() {

        return repo.findAll();
    }

    public List<Feedback> getAll(UserRole role) {
        return switch (role) {
            case ADMIN -> repo.findAll();
            case MODER -> repo.findAllByTypeNotIn(List.of(FeedbackType.APPEAL_MODER));
            case CLIENT -> Collections.emptyList();
        };
    }

    public Feedback addComment(String id, String comment, String email) {

        Feedback feedback = getById(id);
        feedback.setReviewComment(comment);

        historyService.addComment(feedback, email, comment);

        return repo.save(feedback);
    }

    public List<Feedback> getByAuthor(String id) {

        DiscordUser user = discordUserService.getById(id);

        return repo.findByAuthor(user);
    }

    public Feedback save(Feedback feedback) {

        return repo.save(feedback);
    }

    public void assignModer(String id, String moder, String email) {
        Feedback feedback = getById(id);

        historyService.assignedModer(feedback, email, Objects.isNull(feedback.getModer()) ? "none" : feedback.getModer().getEmail(), moder);

        if (moder.equals("none")) {
            feedback.setModer(null);
        } else {
            User user = userService.getById(moder);

            feedback.setModer(user);
        }

        repo.save(feedback);
    }

    public Feedback getById(String taskId) {
        return repo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }
}
