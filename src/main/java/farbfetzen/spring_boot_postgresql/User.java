package farbfetzen.spring_boot_postgresql;

import java.time.Instant;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceCreator;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.annotation.Version;
import org.springframework.data.relational.core.mapping.Table;

// See https://docs.spring.io/spring-data/relational/reference/object-mapping.html#mapping.object-creation
// for information about object instantiation. For example, the constructor used by Spring Data should not be private.
// I'm setting all fields that are populated either by the database or by Spring Data to final
// because the app shouldn't modify those.
// ReadOnlyProperty is also used for fields that are exclusively set by the database. This prevents Spring Data
// to set them to null during inserts and updates which would cause errors. This is not necessary for the id.
@Table("users")
@Data
@AllArgsConstructor(onConstructor_ = {@PersistenceCreator})
@NoArgsConstructor(force = true)
public class User {

    @Id
    private final UUID id;

    @Version
    private final int version;

    @ReadOnlyProperty
    private final Instant createdAt;

    @ReadOnlyProperty
    private final Instant updatedAt;

    private String email;

    private String name;
}
