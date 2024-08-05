package info.journeymap.shaded.kotlin.kotlin.annotation;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import java.lang.annotation.ElementType;
import java.lang.annotation.RetentionPolicy;

@Target(allowedTargets = { AnnotationTarget.ANNOTATION_CLASS })
@java.lang.annotation.Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ ElementType.ANNOTATION_TYPE })
@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u0012\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0087\u0002\u0018\u00002\u00020\u0001B\n\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003R\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\u0004¨\u0006\u0005" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/annotation/Retention;", "", "value", "Linfo/journeymap/shaded/kotlin/kotlin/annotation/AnnotationRetention;", "()Lkotlin/annotation/AnnotationRetention;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public @interface Retention {

    AnnotationRetention value() default AnnotationRetention.RUNTIME;
}