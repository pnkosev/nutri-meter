package pn.nutrimeter.service.services.impl;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pn.nutrimeter.base.TestBase;
import pn.nutrimeter.data.models.MicroTarget;
import pn.nutrimeter.data.repositories.MicroTargetRepository;
import pn.nutrimeter.error.IdNotFoundException;
import pn.nutrimeter.service.models.MicroTargetServiceModel;
import pn.nutrimeter.service.services.api.MicroTargetService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MicroTargetServiceImplTest extends TestBase {

    private static final String ID = "id";

    @MockBean
    MicroTargetRepository repository;

    @Autowired
    MicroTargetService service;

    MicroTargetServiceModel model;

    @Override
    protected void beforeEach() {
        this.model = mock(MicroTargetServiceModel.class);
    }

    @Test
    public void create_withValidModel_shouldReturnCorrect() {
        this.service.create(this.model);

        ArgumentCaptor<MicroTarget> captor = ArgumentCaptor.forClass(MicroTarget.class);
        verify(this.repository).saveAndFlush(captor.capture());

        MicroTarget microTarget = captor.getValue();
        assertNotNull(microTarget);
    }

    @Test
    public void create_withNullModel_shouldThrow() {
        assertThrows(IllegalArgumentException.class, () -> this.service.create(null));
    }

    @Test
    public void getByLifeStageGroupId_withValidId_shouldReturnCorrect() {
        MicroTarget microTarget = mock(MicroTarget.class);
        microTarget.setLifeStageGroupId(ID);
        microTarget.setId(ID);

        when(this.repository.findByLifeStageGroupId(ID)).thenReturn(Optional.of(microTarget));

        MicroTargetServiceModel actual = this.service.getByLifeStageGroupId(ID);

        verify(this.repository).findByLifeStageGroupId(ID);
        assertEquals(microTarget.getId(), actual.getId());
        assertEquals(microTarget.getLifeStageGroupId(), actual.getLifeStageGroupId());
    }

    @Test
    public void getByLifeStageGroupId_withNonExistingId_shouldThrow() {
        assertThrows(IdNotFoundException.class, () -> this.service.getByLifeStageGroupId(ID));
    }

    @Test
    public void getByUserId_withExistingId_shouldReturnCorrect() {
        MicroTarget microTarget = mock(MicroTarget.class);
        microTarget.setId(ID);
        microTarget.setLifeStageGroupId(ID);

        when(this.repository.findByUserId(ID)).thenReturn(microTarget);

        MicroTargetServiceModel actual = this.service.getByUserId(ID);

        verify(this.repository).findByUserId(ID);
        assertEquals(microTarget.getId(), actual.getId());
        assertEquals(microTarget.getLifeStageGroupId(), actual.getLifeStageGroupId());
    }
}