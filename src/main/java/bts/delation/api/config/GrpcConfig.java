package bts.delation.api.config;

import bts.delation.api.FeedbackApi;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfig {

    @Bean
    public Server serverConfig(@Autowired FeedbackApi feedbackApi) {
        return ServerBuilder.forPort(9900)
                .addService(feedbackApi)
                .build();
    }
}
