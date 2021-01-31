package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class SMSNotificationTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SMSNotification.class);
        SMSNotification sMSNotification1 = new SMSNotification();
        sMSNotification1.setId(1L);
        SMSNotification sMSNotification2 = new SMSNotification();
        sMSNotification2.setId(sMSNotification1.getId());
        assertThat(sMSNotification1).isEqualTo(sMSNotification2);
        sMSNotification2.setId(2L);
        assertThat(sMSNotification1).isNotEqualTo(sMSNotification2);
        sMSNotification1.setId(null);
        assertThat(sMSNotification1).isNotEqualTo(sMSNotification2);
    }
}
