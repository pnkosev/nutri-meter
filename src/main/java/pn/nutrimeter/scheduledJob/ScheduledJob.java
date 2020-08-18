package pn.nutrimeter.scheduledJob;

import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.UserRepository;

import java.util.List;

@EnableAsync
@Component
public class ScheduledJob {

    private final UserRepository userRepository;

    public ScheduledJob(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Async
    @Scheduled(cron = "0 41 15 1/1 * ?")
    public void checkForUserAge() {
        System.out.println("yoyo");
    }

//    private void yoyo() {
//        this.userRepository
//                .findAll()
//                .forEach(u -> {
//                    u.bi
//                });
//    }
}
