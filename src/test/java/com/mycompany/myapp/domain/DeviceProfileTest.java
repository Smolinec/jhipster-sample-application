package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class DeviceProfileTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DeviceProfile.class);
        DeviceProfile deviceProfile1 = new DeviceProfile();
        deviceProfile1.setId(1L);
        DeviceProfile deviceProfile2 = new DeviceProfile();
        deviceProfile2.setId(deviceProfile1.getId());
        assertThat(deviceProfile1).isEqualTo(deviceProfile2);
        deviceProfile2.setId(2L);
        assertThat(deviceProfile1).isNotEqualTo(deviceProfile2);
        deviceProfile1.setId(null);
        assertThat(deviceProfile1).isNotEqualTo(deviceProfile2);
    }
}
