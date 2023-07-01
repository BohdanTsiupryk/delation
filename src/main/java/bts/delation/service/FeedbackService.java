package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.User;
import bts.delation.model.dto.FeedbackPage;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.model.enums.UserRole;
import bts.delation.repo.FeedbackRepo;
import bts.delation.repo.FeedbackSearchService;
import bts.delation.repo.dto.FeedbackSearchQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepo repo;
    private final FeedbackSearchService feedbackSearchService;
    private final DiscordUserService discordUserService;
    private final UserService userService;
    private final HistoryService historyService;


    public List<Feedback> getAll() {

        return repo.findAll();
    }

    public FeedbackPage getAll(UserRole role, FeedbackSearchQuery query) {


        List<Feedback> feedbacks = feedbackSearchService.searchByQuery(query);
        long total = feedbackSearchService.countByQuery(query);

        List<Feedback> result = switch (role) {
            case ADMIN -> feedbacks;
            case MODER -> feedbacks.stream().filter(feedback -> !feedback.getType().equals(FeedbackType.APPEAL_MODER)).toList();
            case CLIENT -> Collections.emptyList();
        };

        return new FeedbackPage(result, query.page(), query.size(), total);
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

    public void assignModer(String feedbackId, String moderId, String email) {
        Feedback feedback = getById(feedbackId);
        Optional<User> moder = Optional.ofNullable(feedback.getModer());

        if (isSameModer(moderId, moder) || isVitya(moderId, moder)) return;

        if (moderId.equals("none")) {
            feedback.setModer(null);
        } else {
            User user = userService.getById(moderId);

            feedback.setModer(user);
        }

        historyService.assignedModer(feedback, email, Objects.isNull(feedback.getModer()) ? "none" : feedback.getModer().getEmail(), moderId);

        repo.save(feedback);
    }

    private static boolean isVitya(String moderId, Optional<User> moder) {
        return moder.isEmpty() && (moderId.equals("none"));
    }

    private static boolean isSameModer(String moderId, Optional<User> moder) {
        return moder.isPresent() && moder.get().getId().equals(moderId);
    }

    public Feedback getById(String taskId) {
        return repo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Feedback not found"));
    }
}
