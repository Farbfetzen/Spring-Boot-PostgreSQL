package farbfetzen.spring_boot_postgresql;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NO_CONTENT;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserRepository userRepository;

    @GetMapping
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @PutMapping
    @ResponseStatus(CREATED)
    public void createUser(@RequestBody final NewUserDto dto) {
        final var user = dto.toUser();
        final var insertedUser = userRepository.save(user);
        logger.info("user:          {}", user);
        logger.info("inserted user: {}", insertedUser);
    }

    // For testing how and when the version is increased.
    @PostMapping
    @ResponseStatus(NO_CONTENT)
    public void updateUser(@RequestParam final String id) {
        final Optional<User> optionalUser = userRepository.findById(UUID.fromString(id));
        optionalUser.ifPresent(user -> {
            // Do some nonsense with the name to make a change:
            user.setName(user.getName() + "x");
            final User updatedUser = userRepository.save(user);
            final User userFromSelect = userRepository.findById(user.getId()).get();
            logger.info("updated user:                                {}", user);
            logger.info("updated user as returned by the save method: {}", updatedUser);
            logger.info("updated user as per select :                 {}", userFromSelect);
        });
    }
}
