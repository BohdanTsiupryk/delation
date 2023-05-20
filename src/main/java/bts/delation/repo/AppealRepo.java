package bts.delation.repo;

import bts.delation.model.Appeal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppealRepo extends JpaRepository<Appeal, String> {

    List<Appeal> findByAuthor(String username);
}
