package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.LifeStageGroup;
import pn.nutrimeter.data.repositories.LifeStageGroupRepository;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.LifeStageGroupServiceModel;
import pn.nutrimeter.service.services.api.LifeStageGroupService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LifeStageGroupServiceImplTest {

    private static final String ID = "id";

    @MockBean
    LifeStageGroupRepository repository;

    @Autowired
    LifeStageGroupService service;

    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        LifeStageGroupServiceModel model = mock(LifeStageGroupServiceModel.class);

        this.service.create(model);

        ArgumentCaptor<LifeStageGroup> captor = ArgumentCaptor.forClass(LifeStageGroup.class);
        verify(this.repository).saveAndFlush(captor.capture());

        LifeStageGroup lifeStageGroup = captor.getValue();
        assertNotNull(lifeStageGroup);
    }

    @Test
    public void create_withNullModel_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> this.service.create(null));
    }

    @Test
    public void getById_withExistingId_shouldReturnCorrect() {
        LifeStageGroup lifeStageGroup = mock(LifeStageGroup.class);
        lifeStageGroup.setId(ID);
        lifeStageGroup.setLowerAgeBound(0.0);
        lifeStageGroup.setUpperAgeBound(10.0);

        when(this.repository.findById(ID)).thenReturn(Optional.of(lifeStageGroup));

        LifeStageGroupServiceModel actual = this.service.getById(ID);

        assertEquals(lifeStageGroup.getId(), actual.getId());
        assertEquals(lifeStageGroup.getLowerAgeBound(), actual.getLowerAgeBound());
        assertEquals(lifeStageGroup.getUpperAgeBound(), actual.getUpperAgeBound());
    }

    @Test
    public void getById_withNonExistingId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.service.getById(ID));
    }

    @Test
    public void getAll_shouldReturnCorrect() {
        List<LifeStageGroup> lifeStageGroups = new ArrayList<>();
        lifeStageGroups.add(new LifeStageGroup());
        lifeStageGroups.add(new LifeStageGroup());

        when(this.repository.findAll()).thenReturn(lifeStageGroups);
        List<LifeStageGroupServiceModel> actual = this.service.getAll();

        assertEquals(lifeStageGroups.size(), actual.size());
    }
}