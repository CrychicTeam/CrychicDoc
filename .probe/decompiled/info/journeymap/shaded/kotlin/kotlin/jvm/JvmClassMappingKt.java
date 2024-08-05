package info.journeymap.shaded.kotlin.kotlin.jvm;

import info.journeymap.shaded.kotlin.kotlin.Metadata;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.ClassBasedDeclarationContainer;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Intrinsics;
import info.journeymap.shaded.kotlin.kotlin.jvm.internal.Reflection;
import info.journeymap.shaded.kotlin.kotlin.reflect.KClass;
import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import info.journeymap.shaded.org.jetbrains.annotations.Nullable;
import java.lang.annotation.Annotation;

@Metadata(mv = { 1, 6, 0 }, k = 2, xi = 48, d1 = { "\u0000,\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u001b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010\u0000\n\u0002\b\u000b\n\u0002\u0010\u000b\n\u0002\u0010\u0011\n\u0002\b\u0002\u001a\u001f\u0010\u0018\u001a\u00020\u0019\"\n\b\u0000\u0010\u0002\u0018\u0001*\u00020\r*\u0006\u0012\u0002\b\u00030\u001a¢\u0006\u0002\u0010\u001b\"'\u0010\u0000\u001a\n\u0012\u0006\b\u0001\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\u0003*\u0002H\u00028F¢\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005\"-\u0010\u0006\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\u0004\b\u0000\u0010\u0002*\b\u0012\u0004\u0012\u0002H\u00020\u00018G¢\u0006\f\u0012\u0004\b\b\u0010\t\u001a\u0004\b\n\u0010\u000b\"&\u0010\f\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\u0002H\u00028Æ\u0002¢\u0006\u0006\u001a\u0004\b\n\u0010\u000e\";\u0010\f\u001a\u000e\u0012\n\u0012\b\u0012\u0004\u0012\u0002H\u00020\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018Ç\u0002X\u0087\u0004¢\u0006\f\u0012\u0004\b\u000f\u0010\t\u001a\u0004\b\u0010\u0010\u000b\"+\u0010\u0011\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0012\u0010\u000b\"-\u0010\u0013\u001a\n\u0012\u0004\u0012\u0002H\u0002\u0018\u00010\u0007\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00018F¢\u0006\u0006\u001a\u0004\b\u0014\u0010\u000b\"+\u0010\u0015\u001a\b\u0012\u0004\u0012\u0002H\u00020\u0001\"\b\b\u0000\u0010\u0002*\u00020\r*\b\u0012\u0004\u0012\u0002H\u00020\u00078G¢\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017¨\u0006\u001c" }, d2 = { "annotationClass", "Linfo/journeymap/shaded/kotlin/kotlin/reflect/KClass;", "T", "", "getAnnotationClass", "(Ljava/lang/annotation/Annotation;)Lkotlin/reflect/KClass;", "java", "Ljava/lang/Class;", "getJavaClass$annotations", "(Lkotlin/reflect/KClass;)V", "getJavaClass", "(Lkotlin/reflect/KClass;)Ljava/lang/Class;", "javaClass", "", "(Ljava/lang/Object;)Ljava/lang/Class;", "getRuntimeClassOfKClassInstance$annotations", "getRuntimeClassOfKClassInstance", "javaObjectType", "getJavaObjectType", "javaPrimitiveType", "getJavaPrimitiveType", "info.journeymap.shaded.kotlin.kotlin", "getKotlinClass", "(Ljava/lang/Class;)Lkotlin/reflect/KClass;", "isArrayOf", "", "", "([Ljava/lang/Object;)Z", "info.journeymap.shaded.kotlin.kotlin-stdlib" })
@JvmName(name = "JvmClassMappingKt")
public final class JvmClassMappingKt {

    @JvmName(name = "getJavaClass")
    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull KClass<T> $this$java) {
        Intrinsics.checkNotNullParameter($this$java, "<this>");
        return (Class<T>) ((ClassBasedDeclarationContainer) $this$java).getJClass();
    }

    @Nullable
    public static final <T> Class<T> getJavaPrimitiveType(@NotNull KClass<T> $this$javaPrimitiveType) {
        Intrinsics.checkNotNullParameter($this$javaPrimitiveType, "<this>");
        Class thisJClass = ((ClassBasedDeclarationContainer) $this$javaPrimitiveType).getJClass();
        if (thisJClass.isPrimitive()) {
            return thisJClass;
        } else {
            String var2 = thisJClass.getName();
            if (var2 != null) {
                switch(var2.hashCode()) {
                    case -2056817302:
                        if (var2.equals("java.lang.Integer")) {
                            return int.class;
                        }
                        break;
                    case -527879800:
                        if (var2.equals("java.lang.Float")) {
                            return float.class;
                        }
                        break;
                    case -515992664:
                        if (var2.equals("java.lang.Short")) {
                            return short.class;
                        }
                        break;
                    case 155276373:
                        if (var2.equals("java.lang.Character")) {
                            return char.class;
                        }
                        break;
                    case 344809556:
                        if (var2.equals("java.lang.Boolean")) {
                            return boolean.class;
                        }
                        break;
                    case 398507100:
                        if (var2.equals("java.lang.Byte")) {
                            return byte.class;
                        }
                        break;
                    case 398795216:
                        if (var2.equals("java.lang.Long")) {
                            return long.class;
                        }
                        break;
                    case 399092968:
                        if (var2.equals("java.lang.Void")) {
                            return void.class;
                        }
                        break;
                    case 761287205:
                        if (var2.equals("java.lang.Double")) {
                            return double.class;
                        }
                }
            }
            return null;
        }
    }

    @NotNull
    public static final <T> Class<T> getJavaObjectType(@NotNull KClass<T> $this$javaObjectType) {
        Intrinsics.checkNotNullParameter($this$javaObjectType, "<this>");
        Class thisJClass = ((ClassBasedDeclarationContainer) $this$javaObjectType).getJClass();
        if (!thisJClass.isPrimitive()) {
            return thisJClass;
        } else {
            String var2 = thisJClass.getName();
            if (var2 != null) {
                switch(var2.hashCode()) {
                    case -1325958191:
                        if (var2.equals("double")) {
                            return (Class<T>) Double.class;
                        }
                        break;
                    case 104431:
                        if (var2.equals("int")) {
                            return (Class<T>) Integer.class;
                        }
                        break;
                    case 3039496:
                        if (var2.equals("byte")) {
                            return (Class<T>) Byte.class;
                        }
                        break;
                    case 3052374:
                        if (var2.equals("char")) {
                            return (Class<T>) Character.class;
                        }
                        break;
                    case 3327612:
                        if (var2.equals("long")) {
                            return (Class<T>) Long.class;
                        }
                        break;
                    case 3625364:
                        if (var2.equals("void")) {
                            return (Class<T>) Void.class;
                        }
                        break;
                    case 64711720:
                        if (var2.equals("boolean")) {
                            return (Class<T>) Boolean.class;
                        }
                        break;
                    case 97526364:
                        if (var2.equals("float")) {
                            return (Class<T>) Float.class;
                        }
                        break;
                    case 109413500:
                        if (var2.equals("short")) {
                            return (Class<T>) Short.class;
                        }
                }
            }
            return thisJClass;
        }
    }

    @JvmName(name = "getKotlinClass")
    @NotNull
    public static final <T> KClass<T> getKotlinClass(@NotNull Class<T> $this$kotlin) {
        Intrinsics.checkNotNullParameter($this$kotlin, "<this>");
        return Reflection.getOrCreateKotlinClass($this$kotlin);
    }

    @NotNull
    public static final <T> Class<T> getJavaClass(@NotNull T $this$javaClass) {
        Intrinsics.checkNotNullParameter($this$javaClass, "<this>");
        int $i$f$getJavaClass = 0;
        Class var2 = $this$javaClass.getClass();
        if (var2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.Class<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>");
        } else {
            return var2;
        }
    }

    /**
     * @deprecated
     */
    @JvmName(name = "getRuntimeClassOfKClassInstance")
    @NotNull
    public static final <T> Class<KClass<T>> getRuntimeClassOfKClassInstance(@NotNull KClass<T> $this$javaClass) {
        Intrinsics.checkNotNullParameter($this$javaClass, "<this>");
        int $i$f$getRuntimeClassOfKClassInstance = 0;
        Class var2 = $this$javaClass.getClass();
        if (var2 == null) {
            throw new NullPointerException("null cannot be cast to non-null type java.lang.Class<kotlin.reflect.KClass<T of kotlin.jvm.JvmClassMappingKt.<get-javaClass>>>");
        } else {
            return var2;
        }
    }

    @NotNull
    public static final <T extends Annotation> KClass<? extends T> getAnnotationClass(@NotNull T $this$annotationClass) {
        Intrinsics.checkNotNullParameter($this$annotationClass, "<this>");
        Class var1 = $this$annotationClass.annotationType();
        Intrinsics.checkNotNullExpressionValue(var1, "this as java.lang.annota…otation).annotationType()");
        return getKotlinClass(var1);
    }
}