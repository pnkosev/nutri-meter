package pn.nutrimeter.service.factories.user;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.HashingService;

@Service
public class UserFactoryImpl implements UserFactory {

    private final HashingService hashingService;

    private final LifeStageGroupRepository lifeStageGroupRepository;

    private final MicroTargetRepository microTargetRepository;

    private final MacroTargetRepository macroTargetRepository;

    private final ModelMapper modelMapper;

    public UserFactoryImpl(HashingService hashingService,
                           LifeStageGroupRepository lifeStageGroupRepository,
                           MicroTargetRepository microTargetRepository,
                           MacroTargetRepository macroTargetRepository,
                           ModelMapper modelMapper) {
        this.hashingService = hashingService;
        this.lifeStageGroupRepository = lifeStageGroupRepository;
        this.microTargetRepository = microTargetRepository;
        this.macroTargetRepository = macroTargetRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User create(UserRegisterServiceModel userRegisterServiceModel) {
        User user = this.modelMapper.map(userRegisterServiceModel, User.class);

        user.setPassword(this.hashingService.hash(userRegisterServiceModel.getPassword()));
        user.setTargetWeight(user.getWeight());

        user.setAgeCategory(user.updateAgeCategory());

        double yearsOld = user.getYearsOld();
        LifeStageGroup lifeStageGroup = this.lifeStageGroupRepository.findLifeStageGroupBySexAndAge(user.getSex(), yearsOld);
        user.setLifeStageGroup(lifeStageGroup);
        user.setMicroTarget(this.microTargetRepository.findByLifeStageGroupId(lifeStageGroup.getId()));
        user.setMacroTarget(this.macroTargetRepository.findByLifeStageGroupId(lifeStageGroup.getId()));

        user.setBmr(user.calculateBMR());
        user.setBmi(user.calculateBMI());
        user.setBodyFat(user.calculateBodyFat());
        user.setKcalFromActivityLevel(user.calculateKcalFromActivityLevel());
        user.setKilosPerWeek(UserConstants.DEFAULT_KILOS_PER_WEEK);
        user.setKcalFromTarget(user.calculateKcalFromWeightTarget());
        user.setTotalKcalTarget(user.calculateTotalKcal());
        user.setProteinTargetInPercentage(UserConstants.DEFAULT_PROTEIN_PERCENTAGE);
        user.setProteinTargetInKcal(user.calculateKcalFromTotal(user.getProteinTargetInPercentage()));
        user.setCarbohydrateTargetInPercentage(UserConstants.DEFAULT_CARBOHYDRATE_PERCENTAGE);
        user.setCarbohydrateTargetInKcal(user.calculateKcalFromTotal(user.getCarbohydrateTargetInPercentage()));
        user.setLipidTargetInPercentage(UserConstants.DEFAULT_LIPID_PERCENTAGE);
        user.setLipidTargetInKcal(user.calculateKcalFromTotal(user.getLipidTargetInPercentage()));

        return user;
    }
}
