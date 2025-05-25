package farbfetzen.spring_boot_postgresql;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

class NewUserDtoTest {

    @Test
    void shouldCreateUser() {
        final var dto = new NewUserDto("theo@example.com", "Theo Test");
        final var user = dto.toUser();
        assertThat(user.getId()).isNull();
        assertThat(user.getVersion()).isZero();
        assertThat(user.getCreatedAt()).isNull();
        assertThat(user.getUpdatedAt()).isNull();
        assertThat(user.getName()).isEqualTo("Theo Test");
        assertThat(user.getEmail()).isEqualTo("theo@example.com");
    }
}
