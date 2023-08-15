package bts.delation.api;

import bts.delation.grpc.FeedbackApiGrpc.FeedbackApiImplBase;
import bts.delation.grpc.FeedbackOuterClass;
import bts.delation.model.DiscordUser;
import bts.delation.model.Feedback;
import bts.delation.service.DiscordUserService;
import bts.delation.service.FeedbackService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class FeedbackApi extends FeedbackApiImplBase {

    private final FeedbackService feedbackService;
    private final DiscordUserService discordUserService;
    private final ModelMapper modelMapper;

    @Override
    public void getAllByUserId(FeedbackOuterClass.UserIdRequest request, StreamObserver<FeedbackOuterClass.FeedbackList> responseObserver) {

        List<Feedback> feedbacks = feedbackService.getByAuthor(request.getId());

        FeedbackOuterClass.FeedbackList.Builder builder = FeedbackOuterClass.FeedbackList.newBuilder();
        feedbacks
                .stream()
                .map(f -> modelMapper.map(f, FeedbackOuterClass.Feedback.class))
                .forEach(builder::addList);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }

    @Override
    public void save(FeedbackOuterClass.Feedback request, StreamObserver<FeedbackOuterClass.Feedback> responseObserver) {
        DiscordUser discordUser = discordUserService.getById(request.getAuthor());

        Feedback feedback = modelMapper.map(request, Feedback.class);
        feedback.setAuthor(discordUser);

        Feedback save = feedbackService.save(feedback);

        responseObserver.onNext(modelMapper.map(save, FeedbackOuterClass.Feedback.class));
        responseObserver.onCompleted();
    }
}

