package com.gloic.freebird.webservices.response.dto;

import com.gloic.freebird.SpringTestConfiguration;
import com.gloic.freebird.commons.enumerations.Authority;
import com.gloic.freebird.persistence.model.User;
import com.gloic.freebird.webservices.response.mapper.GenericMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringTestConfiguration.class)
public class DTOMapperTest {

    @Test
    public void shouldMapUser() {
        User user = new User();
        user.setId(1L);
        user.setUsername("dummy");
        user.setPassword("dummy_password");
        user.setEnabled(true);
        user.setLastPasswordResetDate(new Date());
        user.getAuthorities().add(Authority.ROLE_USER);

        UserDTO dto = GenericMapper.toDTO(user);
        assertNotNull(dto);
        assertEquals(user.getId(), dto.getId());
        assertEquals(user.getUsername(), dto.getUsername());
        assertEquals(user.getLastPasswordResetDate(), dto.getLastPasswordResetDate());
        assertEquals(user.getAuthorities(), dto.getAuthorities());
        assertEquals(user.isEnabled(), dto.isEnabled());
    }

    @Test
    public void shouldMapEpisode() {

    }

    @Test
    public void shouldMapEpisodeLight() {

    }


}
