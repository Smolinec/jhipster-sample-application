package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class TemperatureTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Temperature.class);
        Temperature temperature1 = new Temperature();
        temperature1.setId(1L);
        Temperature temperature2 = new Temperature();
        temperature2.setId(temperature1.getId());
        assertThat(temperature1).isEqualTo(temperature2);
        temperature2.setId(2L);
        assertThat(temperature1).isNotEqualTo(temperature2);
        temperature1.setId(null);
        assertThat(temperature1).isNotEqualTo(temperature2);
    }
}
