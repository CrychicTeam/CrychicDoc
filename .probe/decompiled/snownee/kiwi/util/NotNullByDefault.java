package snownee.kiwi.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import javax.annotation.meta.TypeQualifierDefault;
import org.jetbrains.annotations.NotNull;

@TypeQualifierDefault({ ElementType.METHOD, ElementType.PARAMETER })
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@NotNull
public @interface NotNullByDefault {
}