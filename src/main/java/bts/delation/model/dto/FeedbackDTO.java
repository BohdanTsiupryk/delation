package bts.delation.model.dto;

import java.util.Date;
import java.util.Set;

public record FeedbackDTO(
       String id,
       String author,
       String moder,
       Set<String> mentions,
       String text,
       String status,
       String type,
       Date date
    ){}