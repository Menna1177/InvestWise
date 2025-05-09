import java.io.Serializable;

public class UltraSimpleUserStorage {
    public static class User implements Serializable {
        private static final long serialVersionUID = 1L;
        private static int user_id = 0;

        private int id;
        private String name;
        private String email;
        private String username;
        private String password;

        public void register(String name, String email, String username, String password) {
            this.id = ++user_id;
            this.name = name;
            this.email = email;
            this.username = username;
            this.password = password;
        }

        public boolean authenticate(String username, String password) {
            return username != null && username.equals(this.username) && password != null && password.equals(this.password);
        }

        // Getters
        public int getId() { return id; }
        public String getUsername() { return username; }

        @Override
        public String toString() {
            return String.format("User{id=%d, name='%s', email='%s'}", id, name, email);
        }
    }
}