package bts.delation.repo;

import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.enums.FeedbackType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepo extends JpaRepository<Feedback, Long>, JpaSpecificationExecutor<Feedback> {

    List<Feedback> findByAuthor(DiscordUser user);

    List<Feedback> findAllByTypeNotIn(List<FeedbackType> type);

    @Query(value = "from Feedback f where f.status != 'DONE' and f.status != 'CANCELED'")
    List<Feedback> findAllNotDone();

    @Query(value = "from Feedback f where f.type = :type")
    List<Feedback> findAllByType(@Param("type") FeedbackType type);
}
