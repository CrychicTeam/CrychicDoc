package info.journeymap.shaded.kotlin.kotlin.jvm.internal;

import info.journeymap.shaded.kotlin.kotlin.SinceKotlin;
import info.journeymap.shaded.kotlin.kotlin.reflect.KCallable;
import info.journeymap.shaded.kotlin.kotlin.reflect.KProperty;

public abstract class PropertyReference extends CallableReference implements KProperty {

    public PropertyReference() {
    }

    @SinceKotlin(version = "1.1")
    public PropertyReference(Object receiver) {
        super(receiver);
    }

    @SinceKotlin(version = "1.4")
    public PropertyReference(Object receiver, Class owner, String name, String signature, int flags) {
        super(receiver, owner, name, signature, (flags & 1) == 1);
    }

    @SinceKotlin(version = "1.1")
    protected KProperty getReflected() {
        return (KProperty) super.getReflected();
    }

    @SinceKotlin(version = "1.1")
    @Override
    public boolean isLateinit() {
        return this.getReflected().isLateinit();
    }

    @SinceKotlin(version = "1.1")
    @Override
    public boolean isConst() {
        return this.getReflected().isConst();
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof PropertyReference)) {
            return obj instanceof KProperty ? obj.equals(this.compute()) : false;
        } else {
            PropertyReference other = (PropertyReference) obj;
            return this.getOwner().equals(other.getOwner()) && this.getName().equals(other.getName()) && this.getSignature().equals(other.getSignature()) && Intrinsics.areEqual(this.getBoundReceiver(), other.getBoundReceiver());
        }
    }

    public int hashCode() {
        return (this.getOwner().hashCode() * 31 + this.getName().hashCode()) * 31 + this.getSignature().hashCode();
    }

    public String toString() {
        KCallable reflected = this.compute();
        return reflected != this ? reflected.toString() : "property " + this.getName() + " (Kotlin reflection is not available)";
    }
}