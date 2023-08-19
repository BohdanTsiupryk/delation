package bts.delation.service;

import bts.delation.grpc.NotificationApiGrpc;
import bts.delation.model.Feedback;
import bts.delation.model.enums.Status;
import discord4j.common.util.Snowflake;
import discord4j.core.object.component.ActionRow;
import discord4j.core.object.component.Button;
import discord4j.core.spec.MessageCreateSpec;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DiscordNotificationService {

    private final NotificationApiGrpc.NotificationApiFutureStub notificationApi;

    @Value("${host.base-url}")
    private String baseHostUrl;

    public void notifyTaskStatusChanged(Long feedbackId, Status newStatus) {
        String url = baseHostUrl + "/moder/feedback/" + feedbackId;

        notificationApi.sendTaskChanged(bts.delation.grpc.Notification.TaskChangedRequest.newBuilder()
                        .setTaskId(feedbackId)
                        .setStatus(newStatus.name())
                        .setUrl(url)
                        .build())
                .addListener(() -> {}, Runnable::run);
    }
}
