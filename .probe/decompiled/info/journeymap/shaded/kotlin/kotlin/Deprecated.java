package info.journeymap.shaded.kotlin.kotlin;

import info.journeymap.shaded.kotlin.kotlin.annotation.AnnotationTarget;
import info.journeymap.shaded.kotlin.kotlin.annotation.MustBeDocumented;
import info.journeymap.shaded.kotlin.kotlin.annotation.Target;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Target(allowedTargets = { AnnotationTarget.CLASS, AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY, AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CONSTRUCTOR, AnnotationTarget.PROPERTY_SETTER, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.TYPEALIAS })
@MustBeDocumented
@Documented
@Retention(RetentionPolicy.RUNTIME)
@java.lang.annotation.Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.ANNOTATION_TYPE })
@Metadata(mv = { 1, 6, 0 }, k = 1, xi = 48, d1 = { "\u0000\u001e\n\u0002\u0018\u0002\n\u0002\u0010\u001b\n\u0000\n\u0002\u0010\u000e\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\b\u0087\u0002\u0018\u00002\u00020\u0001B\u001c\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0005\u0012\b\b\u0002\u0010\u0006\u001a\u00020\u0007R\u000f\u0010\u0006\u001a\u00020\u0007¢\u0006\u0006\u001a\u0004\b\u0006\u0010\bR\u000f\u0010\u0002\u001a\u00020\u0003¢\u0006\u0006\u001a\u0004\b\u0002\u0010\tR\u000f\u0010\u0004\u001a\u00020\u0005¢\u0006\u0006\u001a\u0004\b\u0004\u0010\n¨\u0006\u000b" }, d2 = { "Linfo/journeymap/shaded/kotlin/kotlin/Deprecated;", "", "message", "", "replaceWith", "Linfo/journeymap/shaded/kotlin/kotlin/ReplaceWith;", "level", "Linfo/journeymap/shaded/kotlin/kotlin/DeprecationLevel;", "()Lkotlin/DeprecationLevel;", "()Ljava/lang/String;", "()Lkotlin/ReplaceWith;", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
public @interface Deprecated {

    String message();

    ReplaceWith replaceWith() default @ReplaceWith(expression = "", imports = {});

    DeprecationLevel level() default DeprecationLevel.WARNING;
}