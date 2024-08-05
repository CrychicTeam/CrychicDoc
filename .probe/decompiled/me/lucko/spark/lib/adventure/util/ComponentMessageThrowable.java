package me.lucko.spark.lib.adventure.util;

import me.lucko.spark.lib.adventure.text.Component;
import org.jetbrains.annotations.Nullable;

public interface ComponentMessageThrowable {

    @Nullable
    static Component getMessage(@Nullable final Throwable throwable) {
        return throwable instanceof ComponentMessageThrowable ? ((ComponentMessageThrowable) throwable).componentMessage() : null;
    }

    @Nullable
    static Component getOrConvertMessage(@Nullable final Throwable throwable) {
        if (throwable instanceof ComponentMessageThrowable) {
            return ((ComponentMessageThrowable) throwable).componentMessage();
        } else {
            if (throwable != null) {
                String message = throwable.getMessage();
                if (message != null) {
                    return Component.text(message);
                }
            }
            return null;
        }
    }

    @Nullable
    Component componentMessage();
}