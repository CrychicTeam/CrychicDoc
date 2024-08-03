package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.reflect.KClass;
import info.journeymap.shaded.kotlin.kotlin.reflect.KClassifier;
import info.journeymap.shaded.kotlin.kotlin.reflect.KDeclarationContainer;
import info.journeymap.shaded.kotlin.kotlin.reflect.KFunction;
import info.journeymap.shaded.kotlin.kotlin.reflect.KMutableProperty0;
import info.journeymap.shaded.kotlin.kotlin.reflect.KMutableProperty1;
import info.journeymap.shaded.kotlin.kotlin.reflect.KMutableProperty2;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty0;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty1;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty2;
import info.journeymap.shaded.kotlin.kotlin.reflect.KType;
import info.journeymap.shaded.kotlin.kotlin.reflect.KTypeParameter;
import info.journeymap.shaded.kotlin.kotlin.reflect.KTypeProjection;
import info.journeymap.shaded.kotlin.kotlin.reflect.KVariance;
import java.util.List;

public class ReflectionFactory {

    private static final String KOTLIN_JVM_FUNCTIONS = "info.journeymap.shaded.kotlin.kotlin.jvm.functions.";

    public KClass createKotlinClass(Class javaClass) {
        return new ClassReference(javaClass);
    }

    public KClass createKotlinClass(Class javaClass, String internalName) {
        return new ClassReference(javaClass);
    }

    public KDeclarationContainer getOrCreateKotlinPackage(Class javaClass, String moduleName) {
        return new PackageReference(javaClass, moduleName);
    }

    public KClass getOrCreateKotlinClass(Class javaClass) {
        return new ClassReference(javaClass);
    }

    public KClass getOrCreateKotlinClass(Class javaClass, String internalName) {
        return new ClassReference(javaClass);
    }

    @SinceKotlin(version = "1.1")
    public String renderLambdaToString(Lambda lambda) {
        return this.renderLambdaToString((FunctionBase) lambda);
    }

    @SinceKotlin(version = "1.3")
    public String renderLambdaToString(FunctionBase lambda) {
        String result = lambda.getClass().getGenericInterfaces()[0].toString();
        return result.startsWith("info.journeymap.shaded.kotlin.kotlin.jvm.functions.") ? result.substring("info.journeymap.shaded.kotlin.kotlin.jvm.functions.".length()) : result;
    }

    public KFunction function(FunctionReference f) {
        return f;
    }

    public KProperty0 property0(PropertyReference0 p) {
        return p;
    }

    public KMutableProperty0 mutableProperty0(MutablePropertyReference0 p) {
        return p;
    }

    public KProperty1 property1(PropertyReference1 p) {
        return p;
    }

    public KMutableProperty1 mutableProperty1(MutablePropertyReference1 p) {
        return p;
    }

    public KProperty2 property2(PropertyReference2 p) {
        return p;
    }

    public KMutableProperty2 mutableProperty2(MutablePropertyReference2 p) {
        return p;
    }

    @SinceKotlin(version = "1.4")
    public KType typeOf(KClassifier klass, List<KTypeProjection> arguments, boolean isMarkedNullable) {
        return new TypeReference(klass, arguments, isMarkedNullable);
    }

    @SinceKotlin(version = "1.4")
    public KTypeParameter typeParameter(Object container, String name, KVariance variance, boolean isReified) {
        return new TypeParameterReference(container, name, variance, isReified);
    }

    @SinceKotlin(version = "1.4")
    public void setUpperBounds(KTypeParameter typeParameter, List<KType> bounds) {
        ((TypeParameterReference) typeParameter).setUpperBounds(bounds);
    }

    @SinceKotlin(version = "1.6")
    public KType platformType(KType lowerBound, KType upperBound) {
        return new TypeReference(lowerBound.getClassifier(), lowerBound.getArguments(), upperBound, ((TypeReference) lowerBound).getFlags$kotlin_stdlib());
    }

    @SinceKotlin(version = "1.6")
    public KType mutableCollectionType(KType type) {
        TypeReference typeRef = (TypeReference) type;
        return new TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound$kotlin_stdlib(), typeRef.getFlags$kotlin_stdlib() | 2);
    }

    @SinceKotlin(version = "1.6")
    public KType nothingType(KType type) {
        TypeReference typeRef = (TypeReference) type;
        return new TypeReference(type.getClassifier(), type.getArguments(), typeRef.getPlatformTypeUpperBound$kotlin_stdlib(), typeRef.getFlags$kotlin_stdlib() | 4);
    }
}