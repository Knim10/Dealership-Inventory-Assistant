package util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordHashGenerator {
    public static void main(String[] args) {
        String plain = (args.length > 0) ? args[0] : "password";
        // cost 10 is a good default for dev; raise it for production if hardware allows
        String hash = BCrypt.hashpw(plain, BCrypt.gensalt(10));
        System.out.println("Plain: " + plain);
        System.out.println("BCrypt: " + hash);
        String hashNew = "$2a$10$5lbbV9z/nbVFuw8pxBhw0ejq59GZaOZCVB.T6gO4m4lvrbFMvejWa"; // replace with your DB value
        System.out.println(BCrypt.checkpw("password", hashNew)); // should print true
    }
}
