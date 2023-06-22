package bts.delation.model;

import bts.delation.model.enums.SyncTarget;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SyncCode {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SyncTarget target;

    @Column(unique = true)
    private String code;

    private String userId;

    public SyncCode(SyncTarget target, String code, String userId) {
        this.target = target;
        this.code = code;
        this.userId = userId;
    }
}
