package pn.nutrimeter.service.factories.user;

import org.modelmapper.ModelMapper;
import pn.nutrimeter.annotation.Factory;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.models.enums.Sex;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.error.ErrorConstants;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.error.LifeStageGroupNotFoundException;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.HashingService;
import pn.nutrimeter.service.services.api.UserCalculationService;

@Factory
public class UserFactoryImpl implements UserFactory {

    private static final Double DEFAULT_KILOS_PER_WEEK = 0.0;

    private final UserCalculationService userCalculationService;

    private final HashingService hashingService;

    private final LifeStageGroupRepository lifeStageGroupRepository;

    private final MicroTargetRepository microTargetRepository;

    private final MacroTargetRepository macroTargetRepository;

    private final ModelMapper modelMapper;

    public UserFactoryImpl(UserCalculationService userCalculationService,
                           HashingService hashingService,
                           LifeStageGroupRepository lifeStageGroupRepository,
                           MicroTargetRepository microTargetRepository,
                           MacroTargetRepository macroTargetRepository,
                           ModelMapper modelMapper) {
        this.userCalculationService = userCalculationService;
        this.hashingService = hashingService;
        this.lifeStageGroupRepository = lifeStageGroupRepository;
        this.microTargetRepository = microTargetRepository;
        this.macroTargetRepository = macroTargetRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public User create(UserRegisterServiceModel userRegisterServiceModel) {
        User user = this.modelMapper.map(userRegisterServiceModel, User.class);
        double userWeight = user.getWeight();
        double userHeight = user.getHeight();

        user.setPassword(this.hashingService.hash(userRegisterServiceModel.getPassword()));
        user.setTargetWeight(userWeight);

        double yearsOld = this.userCalculationService.getYearsOld(user.getBirthday());
        user.setAgeCategory(this.userCalculationService.updateAgeCategory(yearsOld));

        Sex userSex = yearsOld >= 9 ? user.getSex() : null;
        String userSexAsString = user.getSex().name();

        LifeStageGroup lifeStageGroup = this.lifeStageGroupRepository
                .findLifeStageGroupBySexAndAge(userSex, yearsOld)
                .orElseThrow(() -> new LifeStageGroupNotFoundException(ErrorConstants.LIFE_STAGE_GROUP_NOT_FOUND));
        user.setLifeStageGroup(lifeStageGroup);
        user.setMicroTarget(this.microTargetRepository
                .findByLifeStageGroupId(lifeStageGroup.getId())
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_LIFE_STAGE_GROUP_ID)));
        user.setMacroTarget(this.macroTargetRepository
                .findByLifeStageGroupId(lifeStageGroup.getId())
                .orElseThrow(() -> new IdNotFoundException(ErrorConstants.INVALID_LIFE_STAGE_GROUP_ID)));

        user.setBmr(this.userCalculationService.calculateBMR(userSexAsString, userWeight, userHeight, yearsOld));
        user.setBmi(this.userCalculationService.calculateBMI(userWeight, userHeight));
        user.setBodyFat(this.userCalculationService
                .calculateBodyFat(yearsOld, user.getAgeCategory().name(), userSexAsString, user.getBmi())
        );
        user.setKcalFromActivityLevel(this.userCalculationService
                .calculateKcalFromActivityLevel(user.getActivityLevel().name(), user.getBmr())
        );
        user.setKilosPerWeek(DEFAULT_KILOS_PER_WEEK);
        user.setKcalFromTarget(this.userCalculationService.calculateKcalFromWeightTarget(user.getKilosPerWeek()));
        user.setTotalKcalTarget(this.userCalculationService
                .calculateTotalKcal(
                        user.getBmr(),
                        user.getKcalFromActivityLevel(),
                        user.getTargetWeight(),
                        userWeight,
                        user.getKcalFromTarget()
                ));

        return user;
    }
}
