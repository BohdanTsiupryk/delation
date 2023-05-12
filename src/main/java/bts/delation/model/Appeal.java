package bts.delation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@Entity
@Table(name = "appeal")
@Getter
@Setter
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
}
