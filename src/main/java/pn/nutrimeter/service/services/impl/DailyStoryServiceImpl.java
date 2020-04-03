package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.data.repositories.UserRepository;
import pn.nutrimeter.errors.UserNotFoundException;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyStoryServiceImpl implements DailyStoryService {

    private final DailyStoryRepository dailyStoryRepository;

    private final UserRepository userRepository;

    private final ModelMapper modelMapper;

    public DailyStoryServiceImpl(DailyStoryRepository dailyStoryRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.dailyStoryRepository = dailyStoryRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DailyStoryServiceModel getByDateAndUserId(LocalDate date, String id) {
        Optional<DailyStory> dailyStoryOptional = this.dailyStoryRepository.findByDateAndUserId(date, id);

        DailyStory dailyStory;

        if (dailyStoryOptional.isEmpty()) {
            dailyStory = new DailyStory();
            dailyStory.setDate(date);
            dailyStory.setUser(this.userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("No such user found!")));
            this.dailyStoryRepository.saveAndFlush(dailyStory);

            return this.modelMapper.map(dailyStory, DailyStoryServiceModel.class);
        }

        dailyStory = dailyStoryOptional.get();

        return this.modelMapper.map(dailyStory, DailyStoryServiceModel.class);
    }
}
