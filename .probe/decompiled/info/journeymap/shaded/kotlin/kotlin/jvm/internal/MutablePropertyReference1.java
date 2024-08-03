package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.reflect.KCallable;
import info.journeymap.shaded.kotlin.kotlin.reflect.KMutableProperty1;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty1;

public abstract class MutablePropertyReference1 extends MutablePropertyReference implements KMutableProperty1 {

    public MutablePropertyReference1() {
    }

    @SinceKotlin(version = "1.1")
    public MutablePropertyReference1(Object receiver) {
        super(receiver);
    }

    @SinceKotlin(version = "1.4")
    public MutablePropertyReference1(Object receiver, Class owner, String name, String signature, int flags) {
        super(receiver, owner, name, signature, flags);
    }

    @Override
    protected KCallable computeReflected() {
        return Reflection.mutableProperty1(this);
    }

    @Override
    public Object invoke(Object receiver) {
        return this.get(receiver);
    }

    @Override
    public KProperty1.Getter getGetter() {
        return ((KMutableProperty1) this.getReflected()).getGetter();
    }

    @Override
    public KMutableProperty1.Setter getSetter() {
        return ((KMutableProperty1) this.getReflected()).getSetter();
    }

    @SinceKotlin(version = "1.1")
    @Override
    public Object getDelegate(Object receiver) {
        return ((KMutableProperty1) this.getReflected()).getDelegate(receiver);
    }
}