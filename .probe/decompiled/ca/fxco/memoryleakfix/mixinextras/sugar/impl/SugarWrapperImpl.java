package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.tuple.Pair;
import ca.fxco.memoryleakfix.mixinextras.sugar.SugarBridge;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers.HandlerInfo;
import ca.fxco.memoryleakfix.mixinextras.utils.CompatibilityHelper;
import ca.fxco.memoryleakfix.mixinextras.utils.GenericParamParser;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import ca.fxco.memoryleakfix.mixinextras.wrapper.InjectorWrapperImpl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.mixin.injection.throwables.InjectionError;
import org.spongepowered.asm.mixin.injection.throwables.InvalidInjectionException;
import org.spongepowered.asm.mixin.transformer.MixinTargetContext;
import org.spongepowered.asm.util.Annotations;
import org.spongepowered.asm.util.asm.MethodNodeEx;

public class SugarWrapperImpl extends InjectorWrapperImpl {

    private final InjectionInfo wrapperInfo;

    private final AnnotationNode originalAnnotation;

    private final List<AnnotationNode> sugarAnnotations;

    private final ArrayList<Type> generics;

    private final MethodNode handler;

    private final InjectionInfo delegate;

    private final SugarInjector sugarInjector;

    protected SugarWrapperImpl(InjectionInfo wrapper, MixinTargetContext mixin, MethodNode method, AnnotationNode annotation) {
        super(wrapper, mixin, method, annotation, true);
        this.wrapperInfo = wrapper;
        method.visibleAnnotations.remove(annotation);
        method.visibleAnnotations.add(this.originalAnnotation = (AnnotationNode) Annotations.getValue(annotation, "original"));
        this.sugarAnnotations = (List<AnnotationNode>) Annotations.getValue(annotation, "sugars");
        this.generics = new ArrayList(GenericParamParser.getParameterGenerics(method.desc, (String) Annotations.getValue(annotation, "signature")));
        this.handler = this.prepareHandler(method);
        this.sugarInjector = new SugarInjector(this.wrapperInfo, mixin.getMixin(), this.handler, this.sugarAnnotations, this.generics);
        this.sugarInjector.stripSugar();
        this.delegate = InjectionInfo.parse(mixin, this.handler);
        this.sugarInjector.setTargets(MixinInternals.getTargets(this.delegate));
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
    protected void prepare() {
        super.prepare();
        this.sugarInjector.prepareSugar();
    }

    @Override
    protected void granularInject(InjectorWrapperImpl.HandlerCallCallback callback) {
        Map<Target, List<Pair<InjectionNode, MethodInsnNode>>> handlerCallMap = new HashMap();
        super.granularInject((target, sourceNode, call) -> {
            callback.onFound(target, sourceNode, call);
            ((List) handlerCallMap.computeIfAbsent(target, k -> new ArrayList())).add(Pair.of(sourceNode, call));
        });
        this.sugarInjector.reSugarHandler();
        this.sugarInjector.transformHandlerCalls(handlerCallMap);
    }

    @Override
    protected void doPostInject(Runnable postInject) {
        try {
            super.doPostInject(postInject);
        } catch (InjectionError | InvalidInjectionException var5) {
            Throwable e = var5;
            for (SugarApplicationException sugarException : this.sugarInjector.getExceptions()) {
                e.addSuppressed(sugarException);
            }
            throw e;
        }
    }

    private MethodNode prepareHandler(MethodNode original) {
        IMixinInfo mixin = CompatibilityHelper.getMixin(this.wrapperInfo).getMixin();
        HandlerInfo handlerInfo = SugarInjector.getHandlerInfo(mixin, original, this.sugarAnnotations, this.generics);
        if (handlerInfo == null) {
            return original;
        } else {
            MethodNodeEx newMethod = new MethodNodeEx(original.access, MethodNodeEx.getName(original), original.desc, original.signature, (String[]) original.exceptions.toArray(new String[0]), mixin);
            original.accept(newMethod);
            original.visibleAnnotations.remove(this.originalAnnotation);
            newMethod.name = original.name;
            newMethod.tryCatchBlocks = null;
            newMethod.visitAnnotation(Type.getDescriptor(SugarBridge.class), false);
            handlerInfo.transformHandler(this.classNode, newMethod);
            handlerInfo.transformGenerics(this.generics);
            this.classNode.methods.add(newMethod);
            return newMethod;
        }
    }
}