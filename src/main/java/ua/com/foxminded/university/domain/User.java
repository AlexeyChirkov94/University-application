package ua.com.foxminded.university.domain;

public class User {

    private String email;
    private String password;
    private String firstName;
    private String lastName;


    protected User(UserBuilder<? extends UserBuilder> userUserBuilder) {
        this.email = userUserBuilder.email;
        this.password = userUserBuilder.password;
        this.firstName = userUserBuilder.firstName;
        this.lastName = userUserBuilder.lastName;
    }

    public static class UserBuilder<SELF extends UserBuilder<SELF>> {
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
