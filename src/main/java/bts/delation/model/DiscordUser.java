package bts.delation.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    public DiscordUser(String id, String discordUsername) {
        this.id = id;
        this.discordUsername = discordUsername;
    }
}
