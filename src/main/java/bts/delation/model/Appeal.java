package bts.delation.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "appeal")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Appeal {

    @Id
    private String id;

    private String author;

    @ElementCollection(targetClass = String.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "mentions", joinColumns = @JoinColumn(name = "appeal_id"))
    @Column(name = "mention", nullable = false)
    private List<String> mentions;

    private String text;

    @Enumerated(EnumType.STRING)
    private AppealStatus status;

    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime createdAt;
}
