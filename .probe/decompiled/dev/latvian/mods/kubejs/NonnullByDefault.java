package dev.latvian.mods.kubejs;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.jetbrains.annotations.NotNull;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@NotNull
public @interface NonnullByDefault {
}