package pn.nutrimeter.service.services.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.DailyStory;
import pn.nutrimeter.data.repositories.DailyStoryRepository;
import pn.nutrimeter.service.models.DailyStoryServiceModel;
import pn.nutrimeter.service.services.api.DailyStoryService;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class DailyStoryServiceImpl implements DailyStoryService {

    private final DailyStoryRepository dailyStoryRepository;

    private final ModelMapper modelMapper;

    public DailyStoryServiceImpl(DailyStoryRepository dailyStoryRepository, ModelMapper modelMapper) {
        this.dailyStoryRepository = dailyStoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public DailyStoryServiceModel getByDateAndUserId(LocalDate date, String id) {
        Optional<DailyStory> dailyStoryOptional = this.dailyStoryRepository.findByDateAndUserId(date, id);

        DailyStory dailyStory = null;

        if (dailyStoryOptional.isPresent()) {
            dailyStory = dailyStoryOptional.get();
        }

        return this.modelMapper.map(dailyStory, DailyStoryServiceModel.class);
    }
}
