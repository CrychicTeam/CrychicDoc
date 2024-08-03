package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.reflect.KCallable;
import info.journeymap.shaded.kotlin.kotlin.reflect.KMutableProperty0;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty0;

public abstract class MutablePropertyReference0 extends MutablePropertyReference implements KMutableProperty0 {

    public MutablePropertyReference0() {
    }

    @SinceKotlin(version = "1.1")
    public MutablePropertyReference0(Object receiver) {
        super(receiver);
    }

    @SinceKotlin(version = "1.4")
    public MutablePropertyReference0(Object receiver, Class owner, String name, String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty0(this);
    }

    @Override
    public Object invoke() {
        return this.get();
    }

    @Override
    public KProperty0.Getter getGetter() {
        return ((KMutableProperty0) this.getReflected()).getGetter();
    }

    @Override
    public KMutableProperty0.Setter getSetter() {
        return ((KMutableProperty0) this.getReflected()).getSetter();
    }

    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate() {
        return ((KMutableProperty0) this.getReflected()).getDelegate();
    }
}