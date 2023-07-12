package bts.delation.discord;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@EnableScheduling
public class DiscordDailyService {

    private final Map<String, AtomicInteger> dailyFeedbacks = new HashMap<>();
    private final int dailyFeedbackLimit = 10;

    @Scheduled(cron = "0 0 12 * * *")
    public void dailyClear() {
        dailyFeedbacks.clear();
    }

    public boolean checkDailyAndIncrement(String id) {
        if (dailyFeedbacks.containsKey(id)) {
            var count = dailyFeedbacks.get(id);
            int incremented = count.incrementAndGet();
            return incremented < dailyFeedbackLimit;
        }

        dailyFeedbacks.put(id, new AtomicInteger(0));
        return true;
    }
}
