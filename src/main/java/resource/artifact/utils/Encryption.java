package resource.artifact.utils;

import java.util.Base64;

public class Encryption {
    public static String code(String password){
        return Base64.getEncoder().encodeToString(password.getBytes());
    }
}
