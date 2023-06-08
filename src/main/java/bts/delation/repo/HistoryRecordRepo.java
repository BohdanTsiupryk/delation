package bts.delation.repo;

import bts.delation.model.Feedback;
import bts.delation.model.HistoryRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRecordRepo extends JpaRepository<HistoryRecord, Long> {

    List<HistoryRecord> getAllByFeedback(Feedback feedback);
}
