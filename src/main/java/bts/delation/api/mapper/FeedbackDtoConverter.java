package bts.delation.api.mapper;

import bts.delation.grpc.FeedbackOuterClass;
import bts.delation.model.Feedback;
import com.google.protobuf.Timestamp;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class FeedbackDtoConverter implements Converter<Feedback, FeedbackOuterClass.Feedback> {

    @Override
    public FeedbackOuterClass.Feedback convert(MappingContext<Feedback, FeedbackOuterClass.Feedback> context) {

        Feedback feedback = context.getSource();
        Instant instant = feedback.getCreatedAt().atZone(ZoneId.systemDefault()).toInstant();
        Timestamp timestamp = Timestamp.newBuilder()
                .setSeconds(instant.getEpochSecond())
                .setNanos(instant.getNano())
                .build();

        return FeedbackOuterClass.Feedback.newBuilder()
                .setId(feedback.getId().toString())
                .setAuthor(feedback.getAuthor().getId())
                .addAllMentions(feedback.getMentions())
                .setText(feedback.getText())
                .setAttUrl(feedback.getAttachmentUrl())
                .setStatus(feedback.getStatus().name())
                .setType(feedback.getType().name())
                .setDate(timestamp)
                .setGuildId(feedback.getGuildId())
                .build();
    }
}