package ca.fxco.memoryleakfix.config;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Remap {

    String[] fabric() default {};

    String[] forge() default {};

    String[] mcp() default {};

    boolean excludeDev() default false;

    MinecraftRequirement mcVersions() default @MinecraftRequirement({});
}