package security;

import dao.UserDao;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;

public class AuthService {
    private final UserDao userDao;
    public AuthService(UserDao userDao) { this.userDao = userDao; }

    public Optional<User> authenticate(String username, String plainPassword) {
        return userDao.findByUsername(username)
                .filter(u -> BCrypt.checkpw(plainPassword, u.getPasswordHash()));
    }
}
