package bts.delation.repo.dto;

import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;

import java.util.List;

public record FeedbackSearchQuery(List<FeedbackType> types,
                                  List<Status> statuses,
                                  int page,
                                  int size
) {
}
