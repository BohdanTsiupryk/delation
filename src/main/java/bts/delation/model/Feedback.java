package bts.delation.model;

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
    @JoinColumn(name="author_id", nullable=false)
    private DiscordUser author;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "mentions", joinColumns = @JoinColumn(name = "feedback_id"))
    @Column(name = "mention", nullable = false)
    private Set<String> mentions;

    private String text;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Enumerated(EnumType.STRING)
    private FeedbackType type;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
