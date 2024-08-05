package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Slice;

@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface WrapOperation {

    String[] method();

    At[] at() default {};

    Constant[] constant() default {};

    Slice[] slice() default {};

    boolean remap() default true;

    int require() default -1;

    int expect() default 1;

    int allow() default -1;
}