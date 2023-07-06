package bts.delation.model.dto;

import bts.delation.model.enums.FeedbackType;

import java.util.Date;
import java.util.Set;

public record FeedbackDTO(
       Long id,
       String author,
       String moder,
       Set<String> mentions,
       String text,
       String status,
       FeedbackType type,
       String attUrl,
       String comment,
       Date date
    ){}