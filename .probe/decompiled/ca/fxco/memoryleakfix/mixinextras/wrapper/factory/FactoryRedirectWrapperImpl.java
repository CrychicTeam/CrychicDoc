package ca.fxco.memoryleakfix.mixinextras.wrapper.factory;

import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import ca.fxco.memoryleakfix.mixinextras.wrapper.InjectorWrapperImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;

public class FactoryRedirectWrapperImpl extends InjectorWrapperImpl {

    private final InjectionInfo delegate;

    private final MethodNode handler;

    protected FactoryRedirectWrapperImpl(InjectionInfo wrapper, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(wrapper, mixin, method, annotation, true);
        method.visibleAnnotations.remove(annotation);
        method.visibleAnnotations.add((AnnotationNode) Annotations.getValue(annotation, "original"));
        this.handler = method;
        this.delegate = InjectionInfo.parse(mixin, method);
    }

    @Override
    protected InjectionInfo getDelegate() {
        return this.delegate;
    }

    @Override
    protected MethodNode getHandler() {
        return this.handler;
    }

    @Override
    protected void granularInject(InjectorWrapperImpl.HandlerCallCallback callback) {
        Map<InjectionNode, List<InjectionNode>> replacements = new HashMap();
        for (Entry<Target, List<InjectionNode>> entry : MixinInternals.getTargets(this.delegate).entrySet()) {
            for (InjectionNode source : (List) entry.getValue()) {
                this.findReplacedNodes((Target) entry.getKey(), source, it -> ((List) replacements.computeIfAbsent(source, k -> new ArrayList())).add(it));
            }
        }
        super.granularInject((target, sourceNode, call) -> {
            callback.onFound(target, sourceNode, call);
            List<InjectionNode> replacedNodes = (List<InjectionNode>) replacements.get(sourceNode);
            if (replacedNodes != null) {
                for (InjectionNode replaced : replacedNodes) {
                    replaced.replace(call);
                }
            }
        });
    }

    private void findReplacedNodes(Target target, InjectionNode source, Consumer<InjectionNode> sink) {
        if (!source.isRemoved() && source.getCurrentTarget().getOpcode() == 187) {
            sink.accept(source);
            sink.accept(target.addInjectionNode(target.findInitNodeFor((TypeInsnNode) source.getCurrentTarget())));
        }
    }
}