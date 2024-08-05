package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

import ca.fxco.memoryleakfix.mixinextras.injector.LateApplyingInjectorInfo;
import ca.fxco.memoryleakfix.mixinextras.injector.MixinExtrasInjectionInfo;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.CompatibilityHelper;
import ca.fxco.memoryleakfix.mixinextras.utils.InjectorUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinExtrasLogger;
import java.util.List;
import java.util.ListIterator;
import java.util.Map.Entry;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.code.Injector;
import org.spongepowered.asm.mixin.injection.points.BeforeConstant;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.AnnotationType;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo.HandlerPrefix;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.Bytecode;

@AnnotationType(WrapOperation.class)
@HandlerPrefix("wrapOperation")
public class WrapOperationInjectionInfo extends MixinExtrasInjectionInfo implements LateApplyingInjectorInfo {

    private static final MixinExtrasLogger LOGGER = MixinExtrasLogger.get("WrapOperation");

    private LateApplyingInjectorInfo injectionInfoToQueue = this;

    public WrapOperationInjectionInfo(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(mixin, method, annotation, determineAtKey(mixin, method, annotation));
    }

    protected Injector parseInjector(AnnotationNode injectAnnotation) {
        return new WrapOperationInjector(this);
    }

    public void prepare() {
        super.prepare();
        InjectorUtils.checkForDupedNews(this.targetNodes);
        for (Entry<Target, List<InjectionNode>> entry : this.targetNodes.entrySet()) {
            Target target = (Target) entry.getKey();
            ListIterator<InjectionNode> it = ((List) entry.getValue()).listIterator();
            while (it.hasNext()) {
                InjectionNode node = (InjectionNode) it.next();
                AbstractInsnNode currentTarget = node.getCurrentTarget();
                if (currentTarget.getOpcode() == 187) {
                    MethodInsnNode initCall = ASMUtils.findInitNodeFor(target, (TypeInsnNode) currentTarget);
                    if (initCall == null) {
                        LOGGER.warn("NEW node {} in {} has no init call?", Bytecode.describeNode(currentTarget), target);
                        it.remove();
                    } else {
                        node.decorate("mixinextras_newArgTypes", Type.getArgumentTypes(initCall.desc));
                    }
                }
            }
        }
    }

    public void inject() {
        WrapOperationApplicatorExtension.offerInjection(this.mixin.getTarget(), this.injectionInfoToQueue);
    }

    public void postInject() {
    }

    @Override
    public void lateInject() {
        super.inject();
    }

    @Override
    public void latePostInject() {
        super.postInject();
    }

    @Override
    public void wrap(LateApplyingInjectorInfo outer) {
        this.injectionInfoToQueue = outer;
    }

    protected void parseInjectionPoints(List<AnnotationNode> ats) {
        if (this.atKey.equals("at")) {
            super.parseInjectionPoints(ats);
        } else {
            Type returnType = Type.getReturnType(this.method.desc);
            for (AnnotationNode at : ats) {
                this.injectionPoints.add(new BeforeConstant(CompatibilityHelper.getMixin(this), at, returnType.getDescriptor()));
            }
        }
    }

    private static String determineAtKey(MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        boolean at = Annotations.getValue(annotation, "at") != null;
        boolean constant = Annotations.getValue(annotation, "constant") != null;
        if (at == constant) {
            throw new IllegalStateException(String.format("@WrapOperation injector %s::%s must specify exactly one of `at` and `constant`, got %s.", mixin.getMixin().getClassName(), method.name, at ? "both" : "neither"));
        } else {
            return at ? "at" : "constant";
        }
    }
}