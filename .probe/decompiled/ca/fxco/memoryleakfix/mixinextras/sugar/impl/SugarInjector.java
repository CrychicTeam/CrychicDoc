package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.injector.StackExtension;
import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.tuple.Pair;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers.HandlerInfo;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers.HandlerTransformer;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;
import org.spongepowered.asm.mixin.injection.struct.Target;
import org.spongepowered.asm.mixin.injection.struct.InjectionNodes.InjectionNode;
import org.spongepowered.asm.util.Bytecode;

class SugarInjector {

    private final InjectionInfo injectionInfo;

    private final IMixinInfo mixin;

    private final MethodNode handler;

    private final List<AnnotationNode> sugarAnnotations;

    private final List<Type> parameterGenerics;

    private Map<Target, List<InjectionNode>> targets;

    private final List<SugarParameter> strippedSugars = new ArrayList();

    private final List<SugarApplicator> applicators = new ArrayList();

    private final List<SugarApplicationException> exceptions = new ArrayList();

    SugarInjector(InjectionInfo injectionInfo, IMixinInfo mixin, MethodNode handler, List<AnnotationNode> sugarAnnotations, List<Type> parameterGenerics) {
        this.injectionInfo = injectionInfo;
        this.mixin = mixin;
        this.handler = handler;
        this.sugarAnnotations = sugarAnnotations;
        this.parameterGenerics = parameterGenerics;
    }

    void setTargets(Map<Target, List<InjectionNode>> targets) {
        this.targets = targets;
    }

    static void prepareMixin(IMixinInfo mixinInfo, ClassNode mixinNode) {
        for (MethodNode method : mixinNode.methods) {
            if (hasSugar(method)) {
                wrapInjectorAnnotation(mixinInfo, method);
            }
        }
    }

    static HandlerInfo getHandlerInfo(IMixinInfo mixin, MethodNode handler, List<AnnotationNode> sugarAnnotations, List<Type> generics) {
        List<HandlerTransformer> transformers = new ArrayList();
        for (SugarParameter sugar : findSugars(handler, sugarAnnotations, generics)) {
            HandlerTransformer transformer = HandlerTransformer.create(mixin, sugar);
            if (transformer != null && transformer.isRequired(handler)) {
                transformers.add(transformer);
            }
        }
        if (transformers.isEmpty()) {
            return null;
        } else {
            HandlerInfo handlerInfo = new HandlerInfo();
            for (HandlerTransformer transformer : transformers) {
                transformer.transform(handlerInfo);
            }
            return handlerInfo;
        }
    }

    private static boolean hasSugar(MethodNode method) {
        List<AnnotationNode>[] annotations = method.invisibleParameterAnnotations;
        if (annotations == null) {
            return false;
        } else {
            for (List<AnnotationNode> paramAnnotations : annotations) {
                if (isSugar(paramAnnotations)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean isSugar(List<AnnotationNode> paramAnnotations) {
        if (paramAnnotations == null) {
            return false;
        } else {
            for (AnnotationNode annotation : paramAnnotations) {
                if (SugarApplicator.isSugar(annotation.desc)) {
                    return true;
                }
            }
            return false;
        }
    }

    private static void wrapInjectorAnnotation(IMixinInfo mixin, MethodNode method) {
        AnnotationNode injectorAnnotation = InjectionInfo.getInjectorAnnotation(mixin, method);
        if (injectorAnnotation != null) {
            AnnotationNode wrapped = new AnnotationNode(Type.getDescriptor(SugarWrapper.class));
            wrapped.visit("original", injectorAnnotation);
            wrapped.visit("signature", method.signature == null ? "" : method.signature);
            wrapped.visit("sugars", stripSugarAnnotations(method));
            method.visibleAnnotations.remove(injectorAnnotation);
            method.visibleAnnotations.add(wrapped);
        }
    }

    private static List<AnnotationNode> stripSugarAnnotations(MethodNode method) {
        List<AnnotationNode> result = new ArrayList();
        for (List<AnnotationNode> annotations : method.invisibleParameterAnnotations) {
            AnnotationNode sugar = findSugar(annotations);
            if (sugar != null) {
                result.add(sugar);
                annotations.remove(sugar);
            } else {
                result.add(new AnnotationNode(Type.getDescriptor(Deprecated.class)));
            }
        }
        return result;
    }

    void stripSugar() {
        this.strippedSugars.addAll(findSugars(this.handler, this.sugarAnnotations, this.parameterGenerics));
        List<Type> params = new ArrayList();
        boolean foundSugar = false;
        int i = 0;
        for (Type type : Type.getArgumentTypes(this.handler.desc)) {
            if (!SugarApplicator.isSugar(((AnnotationNode) this.sugarAnnotations.get(i)).desc)) {
                if (foundSugar) {
                    throw new IllegalStateException(String.format("Found non-trailing sugared parameters on %s", this.handler.name + this.handler.desc));
                }
                params.add(type);
            } else {
                foundSugar = true;
            }
            i++;
        }
        this.handler.desc = Type.getMethodDescriptor(Type.getReturnType(this.handler.desc), (Type[]) params.toArray(new Type[0]));
    }

    void prepareSugar() {
        this.makeApplicators();
        this.validateApplicators();
        this.prepareApplicators();
    }

    private void makeApplicators() {
        for (SugarParameter sugar : this.strippedSugars) {
            SugarApplicator applicator = SugarApplicator.create(this.injectionInfo, sugar);
            this.applicators.add(applicator);
        }
    }

    private void validateApplicators() {
        for (SugarApplicator applicator : this.applicators) {
            for (Entry<Target, List<InjectionNode>> entry : this.targets.entrySet()) {
                Target target = (Target) entry.getKey();
                ListIterator<InjectionNode> it = ((List) entry.getValue()).listIterator();
                while (it.hasNext()) {
                    InjectionNode node = (InjectionNode) it.next();
                    try {
                        applicator.validate(target, node);
                    } catch (SugarApplicationException var9) {
                        this.exceptions.add(new SugarApplicationException(String.format("Failed to validate sugar %s %s on method %s from mixin %s in target method %s at instruction %s", ASMUtils.annotationToString(applicator.sugar), ASMUtils.typeToString(applicator.paramType), this.handler, this.mixin, target, node), var9));
                        it.remove();
                    }
                }
            }
        }
    }

    private void prepareApplicators() {
        for (Entry<Target, List<InjectionNode>> entry : this.targets.entrySet()) {
            Target target = (Target) entry.getKey();
            for (InjectionNode node : (List) entry.getValue()) {
                try {
                    for (SugarApplicator applicator : this.applicators) {
                        applicator.prepare(target, node);
                    }
                } catch (Exception var8) {
                    throw new SugarApplicationException(String.format("Failed to prepare sugar for method %s from mixin %s in target method %s at instruction %s", this.handler, this.mixin, target, node), var8);
                }
            }
        }
    }

    List<SugarApplicationException> getExceptions() {
        return this.exceptions;
    }

    void reSugarHandler() {
        List<Type> paramTypes = new ArrayList(Arrays.asList(Type.getArgumentTypes(this.handler.desc)));
        for (SugarParameter parameter : this.strippedSugars) {
            paramTypes.add(parameter.type);
        }
        this.handler.desc = Type.getMethodDescriptor(Type.getReturnType(this.handler.desc), (Type[]) paramTypes.toArray(new Type[0]));
    }

    void transformHandlerCalls(Map<Target, List<Pair<InjectionNode, MethodInsnNode>>> calls) {
        for (Entry<Target, List<Pair<InjectionNode, MethodInsnNode>>> entry : calls.entrySet()) {
            Target target = (Target) entry.getKey();
            StackExtension stack = new StackExtension(target);
            for (Pair<InjectionNode, MethodInsnNode> pair : (List) entry.getValue()) {
                InjectionNode sourceNode = pair.getLeft();
                MethodInsnNode handlerCall = pair.getRight();
                InjectionNode node = target.addInjectionNode(handlerCall);
                Map<String, Object> decorations = MixinInternals.getDecorations(sourceNode);
                for (Entry<String, Object> decoration : decorations.entrySet()) {
                    if (((String) decoration.getKey()).startsWith("mixinextras_persistent_")) {
                        node.decorate((String) decoration.getKey(), decoration.getValue());
                    }
                }
                try {
                    for (SugarApplicator applicator : this.applicators) {
                        applicator.inject(target, node, stack);
                    }
                } catch (Exception var14) {
                    throw new SugarApplicationException(String.format("Failed to apply sugar to method %s from mixin %s in target method %s at instruction %s", this.handler, this.mixin, target, node), var14);
                }
                handlerCall.desc = this.handler.desc;
            }
        }
    }

    private static List<SugarParameter> findSugars(MethodNode handler, List<AnnotationNode> sugarAnnotations, List<Type> generics) {
        List<SugarParameter> result = new ArrayList();
        Type[] paramTypes = Type.getArgumentTypes(handler.desc);
        int i = 0;
        int index = Bytecode.isStatic(handler) ? 0 : 1;
        for (Type paramType : paramTypes) {
            AnnotationNode sugar = (AnnotationNode) sugarAnnotations.get(i);
            if (SugarApplicator.isSugar(sugar.desc)) {
                result.add(new SugarParameter(sugar, paramType, (Type) generics.get(i), index, i));
            }
            i++;
            index += paramType.getSize();
        }
        return result;
    }

    private static AnnotationNode findSugar(List<AnnotationNode> annotations) {
        if (annotations == null) {
            return null;
        } else {
            AnnotationNode result = null;
            for (AnnotationNode annotation : annotations) {
                if (SugarApplicator.isSugar(annotation.desc)) {
                    if (result != null) {
                        throw new IllegalStateException("Found multiple sugars on the same parameter! Got " + (String) annotations.stream().map(ASMUtils::annotationToString).collect(Collectors.joining(" ")));
                    }
                    result = annotation;
                }
            }
            return result;
        }
    }

    private static List<AnnotationNode> getParamAnnotations(MethodNode handler, int paramIndex) {
        List<AnnotationNode>[] invisible = handler.invisibleParameterAnnotations;
        return invisible != null && invisible.length >= paramIndex ? invisible[paramIndex] : null;
    }
}