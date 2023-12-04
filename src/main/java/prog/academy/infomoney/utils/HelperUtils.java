package prog.academy.infomoney.utils;

import prog.academy.infomoney.entity.User;

public class HelperUtils {

    private HelperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean checkIfProfileNameExist(String name, User user) {
        return user.getProfiles().stream()
                .anyMatch(p -> p.getName().equals(name));
    }
}
