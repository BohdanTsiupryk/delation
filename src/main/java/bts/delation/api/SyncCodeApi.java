package bts.delation.api;

import bts.delation.grpc.SyncApiGrpc;
import bts.delation.grpc.SyncCode;
import bts.delation.service.SyncService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SyncCodeApi extends SyncApiGrpc.SyncApiImplBase {

    private SyncService syncService;

    @Override
    public void checkCode(SyncCode.Request request, StreamObserver<SyncCode.Response> responseObserver) {
        boolean accepted = syncService.checkCode(request.getId(), request.getCode());

        responseObserver.onNext(
                SyncCode.Response.newBuilder()
                        .setAccepted(accepted)
                        .build()
        );
        responseObserver.onCompleted();
    }
}
