package info.journeymap.shaded.org.eclipse.jetty.websocket.api.annotations;

import info.journeymap.shaded.org.eclipse.jetty.websocket.api.BatchMode;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface WebSocket {

    int inputBufferSize() default -2;

    int maxBinaryMessageSize() default -2;

    int maxIdleTime() default -2;

    int maxTextMessageSize() default -2;

    BatchMode batchMode() default BatchMode.AUTO;
}