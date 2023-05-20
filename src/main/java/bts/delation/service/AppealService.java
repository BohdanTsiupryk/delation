package bts.delation.service;

import bts.delation.exception.NotFoundException;
import bts.delation.model.Appeal;
import bts.delation.model.AppealStatus;
import bts.delation.repo.AppealRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AppealService {

    private final AppealRepo repo;


    public List<Appeal> getAll() {

        return repo.findAll();
    }

    public List<Appeal> getByAuthor(String username) {

        return repo.findByAuthor(username);
    }

    public void save(Appeal appeal) {

        repo.save(appeal);
    }

    public void moveInProgress(String taskId) {
        Appeal appeal = getAppealById(taskId);

        appeal.setStatus(AppealStatus.IN_PROGRESS);

        save(appeal);
    }


    public void moveToDone(String taskId) {
        Appeal appeal = getAppealById(taskId);

        appeal.setStatus(AppealStatus.DONE);

        save(appeal);
    }

    private Appeal getAppealById(String taskId) {
        return repo.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Appeal not found"));
    }
}
