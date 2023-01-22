package user;

import org.apache.commons.lang3.RandomStringUtils;

public class User {
    private String email;
    private String password;
    private String name;

    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static User getRandomUser() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@test.test",
                "QweRty123",
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getUserWithoutPasswordField() {
        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@test.test",
                "",
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getUserWithoutEmailField() {
        return new User(
                "",
                "QweRty123",
                RandomStringUtils.randomAlphabetic(10));
    }

    public static User getUserWithoutNameField() {

        return new User(
                RandomStringUtils.randomAlphanumeric(10) + "@test.test",
                "QweRty123",
                "");
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
