package ca.fxco.memoryleakfix.mixinextras.wrapper;

import ca.fxco.memoryleakfix.mixinextras.injector.LateApplyingInjectorInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.MixinExtrasInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import ca.fxco.memoryleakfix.mixinextras.utils.ProxyUtils;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;

public abstract class WrapperInjectionInfo extends MixinExtrasInjectionInfo implements LateApplyingInjectorInfo {

    final InjectorWrapperImpl impl;

    private final InjectionInfo delegate;

    private final boolean lateApply;

    protected WrapperInjectionInfo(InjectorWrapperImpl.Factory implFactory, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation);
        this.impl = implFactory.create(this, mixin, method, annotation);
        this.delegate = this.impl.getDelegate();
        boolean lateApply = LateApplyingInjectorInfo.wrap(this.delegate, this);
        if (this.delegate instanceof WrapperInjectionInfo) {
            WrapperInjectionInfo inner = (WrapperInjectionInfo) this.delegate;
            lateApply = inner.lateApply;
        } else if (!lateApply && this.impl.usesGranularInject()) {
            this.checkDelegate();
        }
        this.lateApply = lateApply;
    }

    protected void readAnnotation() {
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        throw new AssertionError();
    }

    public boolean isValid() {
        return this.impl.isValid();
    }

    public void prepare() {
        this.impl.prepare();
    }

    public void preInject() {
        this.impl.preInject();
    }

    public void inject() {
        if (this.lateApply) {
            this.delegate.inject();
        } else {
            this.impl.inject();
        }
    }

    public void postInject() {
        if (!this.lateApply) {
            this.impl.doPostInject(this.delegate::postInject);
        }
    }

    public void addCallbackInvocation(MethodNode handler) {
        this.impl.addCallbackInvocation(handler);
    }

    @Override
    public void lateInject() {
        this.impl.inject();
    }

    @Override
    public void latePostInject() {
        this.impl.doPostInject(ProxyUtils.getProxy(this.delegate, LateApplyingInjectorInfo.class)::latePostInject);
    }

    @Override
    public void wrap(LateApplyingInjectorInfo outer) {
        LateApplyingInjectorInfo.wrap(this.delegate, outer);
    }

    private void checkDelegate() {
        try {
            if (this.delegate.getClass().getMethod("inject").getDeclaringClass() != InjectionInfo.class) {
                throw this.impl.granularInjectNotSupported();
            }
        } catch (NoSuchMethodException var2) {
            throw new RuntimeException(var2);
        }
    }

    public Map<Target, List<InjectionNode>> getTargetMap() {
        return MixinInternals.getTargets(this.delegate);
    }
}