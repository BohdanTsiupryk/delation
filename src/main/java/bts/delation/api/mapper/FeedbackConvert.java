package bts.delation.api.mapper;

import bts.delation.grpc.FeedbackOuterClass;
import bts.delation.model.Feedback;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import com.google.protobuf.Timestamp;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;

@Component
public class FeedbackConvert implements Converter<FeedbackOuterClass.Feedback, Feedback> {

    @Override
    public Feedback convert(MappingContext<FeedbackOuterClass.Feedback, Feedback> context) {

        FeedbackOuterClass.Feedback source = context.getSource();

        Timestamp timestamp = source.getDate();
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return new Feedback(
                null,
                new HashSet<>(source.getMentionsList()),
                source.getText(),
                Status.valueOf(source.getStatus()),
                source.getAttUrl(),
                FeedbackType.valueOf(source.getType()),
                localDateTime,
                source.getGuildId()
        );
    }
}