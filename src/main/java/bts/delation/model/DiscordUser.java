package bts.delation.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiscordUser {

    @Id
    private String id;

    private String discordUsername;

    private String mineUsername;

    private boolean syncWithMine;

    @OneToMany(mappedBy = "author")
    private List<Feedback> feedbacks;

    public DiscordUser(String id, String discordUsername) {
        this.id = id;
        this.discordUsername = discordUsername;
    }

    public DiscordUser(String id, String discordUsername, String mineUsername, boolean syncWithMine) {
        this.id = id;
        this.discordUsername = discordUsername;
        this.mineUsername = mineUsername;
        this.syncWithMine = syncWithMine;
    }
}
