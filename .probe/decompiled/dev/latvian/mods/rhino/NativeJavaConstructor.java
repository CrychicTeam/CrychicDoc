package dev.latvian.mods.rhino;

public class NativeJavaConstructor extends BaseFunction {

    MemberBox ctor;

    public NativeJavaConstructor(MemberBox ctor) {
        this.ctor = ctor;
    }

    @Override
    public Object call(Context cx, Scriptable scope, Scriptable thisObj, Object[] args) {
        return NativeJavaClass.constructSpecific(cx, scope, args, this.ctor);
    }

    @Override
    public String getFunctionName() {
        String sig = JavaMembers.liveConnectSignature(this.ctor.argTypes);
        return "<init>".concat(sig);
    }

    @Override
    public String toString() {
        return "[JavaConstructor " + this.ctor.getName() + "]";
    }
}