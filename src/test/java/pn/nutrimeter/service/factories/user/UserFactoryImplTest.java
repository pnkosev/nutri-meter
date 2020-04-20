package pn.nutrimeter.service.factories.user;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.User;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.service.models.UserRegisterServiceModel;
import pn.nutrimeter.service.services.api.HashingService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserFactoryImplTest {

    @MockBean
    HashingService hashingService;

    @MockBean
    LifeStageGroupRepository lifeStageGroupRepository;

    @MockBean
    MicroTargetRepository microTargetRepository;

    @MockBean
    MacroTargetRepository macroTargetRepository;

    @MockBean
    ModelMapper modelMapper;

    @Autowired
    UserFactory userFactory;


    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        UserRegisterServiceModel userModel = new UserRegisterServiceModel();
        User user = new User();

        when(this.modelMapper.map(userModel, User.class)).thenReturn(user);


    }
}