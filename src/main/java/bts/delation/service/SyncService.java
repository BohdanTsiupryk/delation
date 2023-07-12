package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.SyncCode;
import bts.delation.model.User;
import bts.delation.model.enums.SyncTarget;
import bts.delation.repo.SyncCodeRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SyncService {

    private final SyncCodeRepo codeRepo;


    public SyncCode createCode(User user, SyncTarget target) {

        SyncCode syncCode = codeRepo.findByUserIdAndTarget(user.getId(), target)
                .orElse(new SyncCode(target, UUID.randomUUID().toString(), user.getId()));

        codeRepo.save(syncCode);
        return syncCode;
    }

    public Optional<SyncCode> getByCode(String code) {
        return codeRepo.findByCode(code);
    }

    public Optional<SyncCode> getDiscordCode(User user) {
        return codeRepo.findByUserIdAndTarget(user.getId(), SyncTarget.DISCORD);
    }

    public void remove(SyncCode code) {
        codeRepo.delete(code);
    }
}
