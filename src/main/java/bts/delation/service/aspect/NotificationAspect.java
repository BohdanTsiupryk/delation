package bts.delation.service.aspect;

import bts.delation.model.enums.Status;
import bts.delation.service.DiscordNotificationService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class NotificationAspect {

    private final DiscordNotificationService discordNotificationService;

    @Pointcut("execution(* bts.delation.service.FeedbackFlowService.manageStatusFlow(..))")
    public void notifyUsers() {
    }

    @After("notifyUsers()")
    public void sendNotification(JoinPoint joinPoint) {

        Object[] args = joinPoint.getArgs();

        discordNotificationService.notifyTaskStatusChanged((Long) args[0], (Status) args[1]);
    }
}
