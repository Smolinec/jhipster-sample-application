package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PushNotificationTokenTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PushNotificationToken.class);
        PushNotificationToken pushNotificationToken1 = new PushNotificationToken();
        pushNotificationToken1.setId(1L);
        PushNotificationToken pushNotificationToken2 = new PushNotificationToken();
        pushNotificationToken2.setId(pushNotificationToken1.getId());
        assertThat(pushNotificationToken1).isEqualTo(pushNotificationToken2);
        pushNotificationToken2.setId(2L);
        assertThat(pushNotificationToken1).isNotEqualTo(pushNotificationToken2);
        pushNotificationToken1.setId(null);
        assertThat(pushNotificationToken1).isNotEqualTo(pushNotificationToken2);
    }
}
