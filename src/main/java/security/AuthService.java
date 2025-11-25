package security;

import dao.UserDao;
import model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AuthService {
    private static final Logger log = Logger.getLogger(AuthService.class.getName());
    private final UserDao userDao;

    public AuthService(UserDao userDao) {
        this.userDao = userDao;
    }

    public Optional<User> authenticate(String username, String plainPassword) {
        log.info("Login attempt for username: " + username);

        Optional<User> opt = userDao.findByUsername(username);

        // Username not found
        if (opt.isEmpty()) {
            log.warning("Login failed: no such user '" + username + "'");
            return Optional.empty();
        }

        User u = opt.get();

        // Password validation
        boolean ok = BCrypt.checkpw(plainPassword, u.getPasswordHash());
        if (!ok) {
            log.warning("Login failed: wrong password for '" + username + "'");
            return Optional.empty();
        }

        // SUCCESS logging
        log.log(Level.INFO,
                "Login success: username={0}, role={1}, salespersonId={2}",
                new Object[]{ u.getUsername(), u.getRole(), u.getSalespersonId() });

        return Optional.of(u);
    }
}
