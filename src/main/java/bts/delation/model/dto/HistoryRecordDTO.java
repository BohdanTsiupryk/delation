package bts.delation.model.dto;

import java.util.Date;

public record HistoryRecordDTO(
        Long id,
        String type,
        String author,
        String before,
        String after,
        String comment,
        Date time
) {
}
