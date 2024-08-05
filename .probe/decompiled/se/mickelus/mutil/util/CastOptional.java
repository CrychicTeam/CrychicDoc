package se.mickelus.mutil.util;

import java.util.Optional;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class CastOptional {

    public static <T> Optional<T> cast(@Nullable Object object, Class<T> clazz) {
        return Optional.ofNullable(object).filter(clazz::isInstance).map(clazz::cast);
    }
}