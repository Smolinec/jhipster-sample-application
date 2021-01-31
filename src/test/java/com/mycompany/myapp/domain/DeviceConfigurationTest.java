package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class DeviceConfigurationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceConfiguration.class);
        DeviceConfiguration deviceConfiguration1 = new DeviceConfiguration();
        deviceConfiguration1.setId(1L);
        DeviceConfiguration deviceConfiguration2 = new DeviceConfiguration();
        deviceConfiguration2.setId(deviceConfiguration1.getId());
        assertThat(deviceConfiguration1).isEqualTo(deviceConfiguration2);
        deviceConfiguration2.setId(2L);
        assertThat(deviceConfiguration1).isNotEqualTo(deviceConfiguration2);
        deviceConfiguration1.setId(null);
        assertThat(deviceConfiguration1).isNotEqualTo(deviceConfiguration2);
    }
}
