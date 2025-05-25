package farbfetzen.spring_boot_postgresql;

import static java.time.temporal.ChronoUnit.MINUTES;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.within;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.databind.MappingIterator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

class UserIT extends IntegrationTest {

    private static final String BASE_URL = "/users";

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldGetAll() throws Exception {
        final var responseBody = mockMvc
                .perform(get(BASE_URL))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        try (final MappingIterator<User> userIterator = mapper.readerFor(User.class).readValues(responseBody)) {
            final List<User> users = userIterator.readAll();
            assertThat(users).singleElement().satisfies(user -> {
                assertThat(user.getId()).isNotNull();
                assertThat(user.getVersion()).isOne();
                assertThat(user.getCreatedAt()).isCloseTo(Instant.now(), within(5, MINUTES));
                assertThat(user.getUpdatedAt()).isNull();
                assertThat(user.getName()).isEqualTo("Theo Test");
                assertThat(user.getEmail()).isEqualTo("theo@example.org");
            });
        }
    }

    @Test
    @Transactional
    void shouldCreateUser() throws Exception {
        final var dto = new NewUserDto("foo@example.org", "Foo");
        mockMvc
                .perform(put(BASE_URL).content(mapper.writeValueAsString(dto)).contentType(APPLICATION_JSON_VALUE))
                .andExpect(status().isCreated())
                .andExpect(content().string(""));
        assertThat(userRepository.findByEmail(dto.email())).isPresent().get().satisfies(user -> {
            assertThat(user.getId()).isNotNull();
            assertThat(user.getVersion()).isOne();
            assertThat(user.getCreatedAt()).isCloseTo(Instant.now(), within(5, SECONDS));
            assertThat(user.getUpdatedAt()).isNull();
            assertThat(user.getName()).isEqualTo("Foo");
            assertThat(user.getEmail()).isEqualTo("foo@example.org");
        });
    }

    @Test
    @Transactional
    void shouldUpdateUser() throws Exception {
        final User user = userRepository.findByEmail("theo@example.org").orElseGet(Assertions::fail);
        mockMvc
                .perform(post(BASE_URL).param("id", user.getId().toString()))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));
        assertThat(userRepository.findByEmail("theo@example.org")).isPresent().get().satisfies(updatedUser -> {
            assertThat(updatedUser.getId()).isEqualTo(user.getId());
            assertThat(updatedUser.getVersion()).isEqualTo(user.getVersion() + 1);
            assertThat(updatedUser.getCreatedAt()).isEqualTo(user.getCreatedAt());
            assertThat(updatedUser.getUpdatedAt()).isCloseTo(Instant.now(), within(5, SECONDS));
            assertThat(updatedUser.getName()).isEqualTo(user.getName() + "x");
            assertThat(updatedUser.getEmail()).isEqualTo(user.getEmail());
        });
    }
}
