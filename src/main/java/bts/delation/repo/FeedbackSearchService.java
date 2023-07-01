package bts.delation.repo;

import bts.delation.model.Feedback;
import bts.delation.repo.dto.FeedbackSearchQuery;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackSearchService {

    private final FeedbackRepo repo;

    public List<Feedback> searchByQuery(FeedbackSearchQuery query) {

        Specification<Feedback> specification = createSpecification(query);
        return repo.findAll(specification, PageRequest.of(query.page(),query.size())).getContent();
    }

    public long countByQuery(FeedbackSearchQuery query) {
        return repo.count(createSpecification(query));
    }

    private Specification<Feedback> createSpecification(FeedbackSearchQuery request) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (!request.statuses().isEmpty()) {
                predicates.add(root.get("status").in(request.statuses()));
            }
            if (!request.types().isEmpty()) {
                predicates.add(root.get("type").in(request.types()));
            }


            return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
        };
    }
}
