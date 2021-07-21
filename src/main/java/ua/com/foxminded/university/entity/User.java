package ua.com.foxminded.university.entity;

import java.util.Objects;

public class User {

    private Long id;
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        User user = (User) o;
        return Objects.equals(id, user.id) &&
                Objects.equals(email, user.email) &&
                Objects.equals(password, user.password) &&
                Objects.equals(firstName, user.firstName) &&
                Objects.equals(lastName, user.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, password, firstName, lastName);
    }

    protected User(UserBuilder<? extends UserBuilder> userUserBuilder) {
        this.id = userUserBuilder.id;
        this.email = userUserBuilder.email;
        this.password = userUserBuilder.password;
        this.firstName = userUserBuilder.firstName;
        this.lastName = userUserBuilder.lastName;

    }

    public static class UserBuilder<SELF extends UserBuilder<SELF>> {
        private Long id;
        private String email;
        private String password;
        private String firstName;
        private String lastName;

        protected UserBuilder() {
        }

        public SELF self() {
            return (SELF) this;
        }

        public User build() {
            return new User(self());
        }

        public SELF withId(Long id) {
            this.id = id;
            return self();
        }

        public SELF withEmail(String email) {
            this.email = email;
            return self();
        }

        public SELF withPassword(String password) {
            this.password = password;
            return self();
        }

        public SELF withFirstName(String firstName) {
            this.firstName = firstName;
            return self();
        }

        public SELF withLastName(String lastName) {
            this.lastName = lastName;
            return self();
        }
    }

}
