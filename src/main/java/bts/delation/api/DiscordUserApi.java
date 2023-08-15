package bts.delation.api;

import bts.delation.grpc.DiscordUserApiGrpc;
import bts.delation.grpc.Discorduser;
import bts.delation.service.DiscordUserService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DiscordUserApi extends DiscordUserApiGrpc.DiscordUserApiImplBase {

    private final DiscordUserService discordUserService;

    @Override
    public void save(Discorduser.IdUsernameRequest request, StreamObserver<Empty> responseObserver) {

        discordUserService.saveAndSync(request.getId(), request.getUsername());

        responseObserver.onNext(Empty.newBuilder().build());
        responseObserver.onCompleted();
    }

    @Override
    public void checkAuthorize(Discorduser.IdUsernameRequest request, StreamObserver<Discorduser.authorizeResponse> responseObserver) {
        boolean authorized = discordUserService.checkUserAutorize(request.getId(), request.getUsername());

        responseObserver.onNext(
                Discorduser.authorizeResponse.newBuilder()
                        .setAuthorize(authorized)
                        .build()
        );
        responseObserver.onCompleted();
    }
}