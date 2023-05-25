package bts.delation.repo;

import bts.delation.model.DiscordUser;
import bts.delation.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscordUserRepo extends JpaRepository<DiscordUser, String> {

    Optional<DiscordUser> findDiscordUserByDiscordUsername(String username);
}
