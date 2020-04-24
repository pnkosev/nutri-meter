package pn.nutrimeter.service.factories.macro_target;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pn.nutrimeter.data.models.MacroTarget;
import pn.nutrimeter.service.models.MacroTargetServiceModel;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class MacroTargetServiceModelFactoryImplTest {

    private static final double WEIGHT = 100.0;

    @Autowired
    MacroTargetServiceModelFactory factory;

    @Test
    public void create_withNullMacroTarget_shouldReturnCorrect() {
        assertThrows(IllegalArgumentException.class, () -> factory.create(null, WEIGHT));
    }

    @Test
    public void create_withValidMacroTarget_shouldReturnCorrect() {
        MacroTarget macroTarget = mock(MacroTarget.class);

        when(macroTarget.getCysteineMethionineRDA()).thenReturn(1.0);
        when(macroTarget.getHistidineRDA()).thenReturn(1.0);
        when(macroTarget.getIsoleucineRDA()).thenReturn(1.0);
        when(macroTarget.getLeucineRDA()).thenReturn(1.0);
        when(macroTarget.getLysineRDA()).thenReturn(1.0);
        when(macroTarget.getPhenylalineTyrosineRDA()).thenReturn(1.0);
        when(macroTarget.getThreonineRDA()).thenReturn(1.0);
        when(macroTarget.getTryptophanRDA()).thenReturn(1.0);
        when(macroTarget.getValineRDA()).thenReturn(1.0);
        when(macroTarget.getOmega3RDA()).thenReturn(1.0);
        when(macroTarget.getOmega6RDA()).thenReturn(1.0);

        MacroTargetServiceModel actual = factory.create(macroTarget, WEIGHT);

        assertEquals(100, actual.getCysteineMethionineRDA());
        assertEquals(100, actual.getHistidineRDA());
        assertEquals(100, actual.getIsoleucineRDA());
        assertEquals(100, actual.getLeucineRDA());
        assertEquals(100, actual.getLysineRDA());
        assertEquals(100, actual.getPhenylalineTyrosineRDA());
        assertEquals(100, actual.getThreonineRDA());
        assertEquals(100, actual.getTryptophanRDA());
        assertEquals(100, actual.getValineRDA());
        assertEquals(100, actual.getOmega3RDA());
        assertEquals(100, actual.getOmega6RDA());
    }
}