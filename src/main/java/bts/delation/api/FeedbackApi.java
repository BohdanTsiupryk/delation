package bts.delation.api;

import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
public class FeedbackApi {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;

    @PostMapping
    public ResponseEntity<FeedbackDto> save(@RequestBody FeedbackDto feedback) {

        DiscordUser discordUser = discordUserService.getById(feedback.author());

        Feedback save = feedbackService.save(feedback.toEntity(discordUser));
        return ResponseEntity.ok(new FeedbackDto(save));
    }

    @GetMapping
    public ResponseEntity<List<FeedbackDto>> getAll(@RequestParam(value = "ds_user_id", required = false) String id) {

        List<Feedback> feedbacks = Objects.nonNull(id) ?
                feedbackService.getByAuthor(id) :
                feedbackService.getAll();

        List<FeedbackDto> list = feedbacks
                .stream()
                .map(FeedbackDto::new)
                .toList();
        return ResponseEntity.ok(list);
    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
record FeedbackDto(
        String id,
        String author,
        Collection<String> mentions,
        String text,
        String attUrl,
        String status,
        FeedbackType type,
        LocalDateTime date,
        String guildId
) {
    public FeedbackDto(Feedback feedback) {
        this(
                feedback.getId().toString(),
                feedback.getAuthor().getDiscordUsername(),
                feedback.getMentions(),
                feedback.getText(),
                feedback.getAttachmentUrl(),
                feedback.getStatus().name(),
                feedback.getType(),
                feedback.getCreatedAt(),
                feedback.getGuildId()
        );
    }

    public Feedback toEntity(DiscordUser discordUser) {
        return new Feedback(
                discordUser,
                new HashSet<>(this.mentions),
                this.text,
                Status.valueOf(this.status),
                this.attUrl,
                this.type,
                this.date,
                this.guildId
        );
    }
}
