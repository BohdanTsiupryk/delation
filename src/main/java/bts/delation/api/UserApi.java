package bts.delation.api;

import bts.delation.grpc.User;
import bts.delation.grpc.UserApiGrpc;
import bts.delation.service.UserService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserApi extends UserApiGrpc.UserApiImplBase{

    private final UserService userService;

    @Override
    public void getAdmins(Empty request, StreamObserver<User.AdminsResponse> responseObserver) {

        User.AdminsResponse.Builder builder = User.AdminsResponse.newBuilder();
        userService.findAdmins()
                .stream()
                .map(bts.delation.model.User::getId)
                .forEach(builder::addAdmins);

        responseObserver.onNext(builder.build());
        responseObserver.onCompleted();
    }
}
