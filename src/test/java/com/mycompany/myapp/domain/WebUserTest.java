package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class WebUserTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WebUser.class);
        WebUser webUser1 = new WebUser();
        webUser1.setId(1L);
        WebUser webUser2 = new WebUser();
        webUser2.setId(webUser1.getId());
        assertThat(webUser1).isEqualTo(webUser2);
        webUser2.setId(2L);
        assertThat(webUser1).isNotEqualTo(webUser2);
        webUser1.setId(null);
        assertThat(webUser1).isNotEqualTo(webUser2);
    }
}
