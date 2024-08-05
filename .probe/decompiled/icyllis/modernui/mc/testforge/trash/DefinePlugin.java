package icyllis.modernui.mc.testforge.trash;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface DefinePlugin {

    String value();
}