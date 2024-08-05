package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.reflect.KClass;
import info.journeymap.shaded.kotlin.kotlin.reflect.KDeclarationContainer;

public class FunctionReferenceImpl extends FunctionReference {

    public FunctionReferenceImpl(int arity, KDeclarationContainer owner, String name, String signature) {
        super(arity, NO_RECEIVER, ((ClassBasedDeclarationContainer) owner).getJClass(), name, signature, owner instanceof KClass ? 0 : 1);
    }

    @SinceKotlin(version = "1.4")
    public FunctionReferenceImpl(int arity, Class owner, String name, String signature, int flags) {
        super(arity, NO_RECEIVER, owner, name, signature, flags);
    }

    @SinceKotlin(version = "1.4")
    public FunctionReferenceImpl(int arity, Object receiver, Class owner, String name, String signature, int flags) {
        super(arity, receiver, owner, name, signature, flags);
    }
}