package bts.delation.api;

import bts.delation.grpc.FeedbackApiGrpc.FeedbackApiImplBase;
import bts.delation.grpc.FeedbackOuterClass;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.model.enums.FeedbackType;
import bts.delation.model.enums.Status;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import com.google.protobuf.Timestamp;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FeedbackApi extends FeedbackApiImplBase {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;

    @Override
    public void getAllByUserId(FeedbackOuterClass.UserIdRequest request, StreamObserver<FeedbackOuterClass.FeedbackList> responseObserver) {

        List<Feedback> feedbacks = feedbackService.getByAuthor(request.getId());

        FeedbackOuterClass.FeedbackList.Builder builder = FeedbackOuterClass.FeedbackList.newBuilder();
        feedbacks
                .stream()
                .map(this::toDto)
                .forEach(builder::addList);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void save(FeedbackOuterClass.Feedback request, StreamObserver<FeedbackOuterClass.Feedback> responseObserver) {
        DiscordUser discordUser = discordUserService.getById(request.getAuthor());

        Feedback save = feedbackService.save(toEntity(request, discordUser));

        responseObserver.onNext(toDto(save));
        responseObserver.onCompleted();
    }

    private Feedback toEntity(FeedbackOuterClass.Feedback req, DiscordUser discordUser) {

        Timestamp timestamp = req.getDate();
        Instant instant = Instant.ofEpochSecond(timestamp.getSeconds(), timestamp.getNanos());
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());

        return new Feedback(
                discordUser,
                new HashSet<>(req.getMentionsList()),
                req.getText(),
                Status.valueOf(req.getStatus()),
                req.getAttUrl(),
                FeedbackType.valueOf(req.getType()),
                localDateTime,
                req.getGuildId()
        );
    }

    private FeedbackOuterClass.Feedback toDto(Feedback feedback) {

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

