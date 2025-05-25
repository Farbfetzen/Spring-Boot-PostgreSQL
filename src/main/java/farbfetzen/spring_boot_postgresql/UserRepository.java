package farbfetzen.spring_boot_postgresql;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.repository.ListCrudRepository;

public interface UserRepository extends ListCrudRepository<User, UUID> {

    Optional<User> findByEmail(String email);
}
