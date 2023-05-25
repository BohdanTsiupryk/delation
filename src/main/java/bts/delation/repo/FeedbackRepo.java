package bts.delation.repo;

import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, String> {

    List<Feedback> findByAuthor(DiscordUser user);
}
