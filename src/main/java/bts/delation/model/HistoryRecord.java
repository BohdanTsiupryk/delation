package bts.delation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoryRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private HistoryType type;

    private String author;

    private String before;

    private String after;

    @Column(length = 3000)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime time;

    @ManyToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    public enum HistoryType {
        CREATED, ASSIGNED_USER, CHANGE_STATUS, COMMENT_ADDED
    }
}
