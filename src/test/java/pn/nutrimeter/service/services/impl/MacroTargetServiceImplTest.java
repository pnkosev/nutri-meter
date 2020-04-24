package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.data.repositories.MacroTargetRepository;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.factories.macro_target.MacroTargetServiceModelFactory;
import pn.nutrimeter.service.models.MacroTargetServiceModel;
import pn.nutrimeter.service.services.api.MacroTargetService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MacroTargetServiceImplTest {

    private static final String ID = "id";

    @MockBean
    MacroTargetRepository repository;

    @MockBean
    MacroTargetServiceModelFactory factory;

    @Autowired
    MacroTargetService service;

    MacroTargetServiceModel model;

    @BeforeEach
    void setUp() {
        this.model = mock(MacroTargetServiceModel.class);
    }

    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        this.service.create(this.model);

        ArgumentCaptor<MacroTarget> captor = ArgumentCaptor.forClass(MacroTarget.class);
        verify(this.repository).saveAndFlush(captor.capture());

        MacroTarget macroTarget = captor.getValue();
        assertNotNull(macroTarget);
    }

    @Test
    public void create_withNullModel_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> this.service.create(null));
    }

    @Test
    public void getByLifeGroupId_withExistingId_shouldReturnCorrect() {
        MacroTarget macroTarget = this.getMacroTarget();

        when(this.repository.findByLifeStageGroupId(ID)).thenReturn(Optional.of(macroTarget));

        MacroTargetServiceModel actual = this.service.getByLifeGroupId(ID);

        verify(this.repository).findByLifeStageGroupId(ID);
        assertEquals(macroTarget.getId(), actual.getId());
        assertEquals(macroTarget.getLifeStageGroupId(), actual.getLifeStageGroupId());
    }

    @Test
    public void getByLifeGroupId_withNonExistingId_shouldReturnCorrect() {
        assertThrows(IdNotFoundException.class, () -> this.service.getByLifeGroupId(ID));
    }

    @Test
    public void getByUserId_withExistingId_shouldReturnCorrect() {
        MacroTarget macroTarget = this.getMacroTarget();
        double weight = 100.0;

        when(this.repository.findByUserId(ID)).thenReturn(macroTarget);
        when(this.factory.create(macroTarget, weight)).thenReturn(this.model);

        MacroTargetServiceModel actual = this.service.getByUserId(ID, weight);

        assertEquals(this.model, actual);
    }

    private MacroTarget getMacroTarget() {
        MacroTarget macroTarget = mock(MacroTarget.class);
        macroTarget.setId(ID);
        macroTarget.setLifeStageGroupId(ID);
        return macroTarget;
    }
}