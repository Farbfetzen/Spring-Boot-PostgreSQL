package farbfetzen.spring_boot_postgresql;

public record NewUserDto(String email, String name) {

    public User toUser() {
        final var user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }
}
