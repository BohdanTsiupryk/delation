package bts.delation.repo;

import bts.delation.model.DiscordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DiscordUserRepo extends JpaRepository<DiscordUser, String> {

    Optional<DiscordUser> findDiscordUserByDiscordUsername(String username);

    @Query(value = "from DiscordUser du where du.syncWithMine = false")
    List<DiscordUser> findNotSynced();
}
