package model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Long id;
    private String name;
    private String surname;
    private String phone;

    private String email;
    private String password;

    private Boolean admin;

    public boolean isAdmin() {
        return admin;
    }

    public void makeAdmin() {
        admin = true;
    }

    public void makeUser() {
        admin = false;
    }

    public static class Builder {
        private User user;

        public Builder() {
            user = new User();
        }

        public Builder setName(String name) {
            user.setName(name);
            return this;
        }

        public Builder setSurname(String surname) {
            user.setSurname(surname);
            return this;
        }

        public Builder setPhone(String phone) {
            user.setPhone(phone);
            return this;
        }

        public Builder setEmail(String email) {
            user.setEmail(email);
            return this;
        }

        public Builder setPassword(String password) {
            user.setPassword(password);
            return this;
        }

        public Builder isAdmin(boolean admin) {
            user.setAdmin(admin);
            return this;
        }

        public User build() {
            return user;
        }

    }
}
