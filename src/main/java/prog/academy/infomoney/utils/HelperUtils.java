package prog.academy.infomoney.utils;

import prog.academy.infomoney.entity.Profile;

import java.util.List;

public class HelperUtils {

    private HelperUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean checkIfProfileNameExist(String name, List<Profile> user) {
        return user.stream()
                .anyMatch(p -> p.getName().equals(name));
    }
}
