package bts.delation.model;

import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "feedback")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Feedback {

    @Id
    private String id;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private DiscordUser author;

    @ManyToOne
    @JoinColumn(name = "moder_id")
    private User moder;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "mentions", joinColumns = @JoinColumn(name = "feedback_id"))
    @Column(name = "mention", nullable = false)
    private Set<String> mentions;

    @Column(length = 3000)
    private String text;

    private String attachmentUrl;

    @Column(length = 3000)
    private String reviewComment;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;

    private String guildId;

    public Feedback(String id,
                    DiscordUser author,
                    Set<String> mentions,
                    String text,
                    Status status,
                    String attachmentUrl,
                    FeedbackType type,
                    LocalDateTime createdAt,
                    String guildId) {
        this.id = id;
        this.author = author;
        this.mentions = mentions;
        this.text = text;
        this.status = status;
        this.attachmentUrl = attachmentUrl;
        this.type = type;
        this.createdAt = createdAt;
        this.guildId = guildId;
    }
}
