package bts.delation.api.config;

import bts.delation.grpc.NotificationApiGrpc;
import io.grpc.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class GrpcConfig {

    @Value("${grpc.discord.url}")
    private String discordUrl;

    @Bean
    public ManagedChannel channel() {
        return ManagedChannelBuilder.forTarget(discordUrl)
                .usePlaintext().build();
    }

    @Bean
    public Server serverConfig(@Autowired List<BindableService> feedbackApi) {
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9900);

        feedbackApi.forEach(serverBuilder::addService);

        return serverBuilder.build();
    }

    @Bean
    public NotificationApiGrpc.NotificationApiFutureStub userApi(ManagedChannel channel) {
        return NotificationApiGrpc.newFutureStub(channel);
    }
}
