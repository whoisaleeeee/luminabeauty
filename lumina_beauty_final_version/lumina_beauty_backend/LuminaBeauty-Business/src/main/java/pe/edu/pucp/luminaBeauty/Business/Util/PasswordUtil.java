package pe.edu.pucp.luminaBeauty.Business.Util;

import org.mindrot.jbcrypt.BCrypt;

public final class PasswordUtil {

    private static final int BCRYPT_COST = 12;

    private PasswordUtil() {
    }

    public static String hash(String password) {
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("La contraseña no puede estar vacía.");
        }

        return BCrypt.hashpw(password, BCrypt.gensalt(BCRYPT_COST));
    }

    public static boolean verify(String password, String passwordHash) {
        if (password == null || passwordHash == null || passwordHash.isBlank()) {
            return false;
        }

        try {
            return BCrypt.checkpw(password, passwordHash);
        } catch (IllegalArgumentException ex) {
            return false;
        }
    }
}
