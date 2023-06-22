package bts.delation.repo;

import bts.delation.model.SyncCode;
import bts.delation.model.enums.SyncTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SyncCodeRepo extends JpaRepository<SyncCode, Long> {
    Optional<SyncCode> findByCode(String code);

    Optional<SyncCode> findByUserIdAndTarget(String userId, SyncTarget target);
}
