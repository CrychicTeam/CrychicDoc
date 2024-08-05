package ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers;

import ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation.WrapOperation;
import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarParameter;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.ref.LocalRefUtils;
import ca.fxco.memoryleakfix.mixinextras.utils.ASMUtils;
import ca.fxco.memoryleakfix.mixinextras.wrapper.factory.FactoryRedirectWrapper;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.struct.InjectionInfo;

class LocalHandlerTransformer extends HandlerTransformer {

    private static final Set<String> TARGET_INJECTORS = new HashSet(Arrays.asList(Type.getDescriptor(ModifyConstant.class), Type.getDescriptor(Redirect.class), Type.getDescriptor(FactoryRedirectWrapper.class)));

    LocalHandlerTransformer(IMixinInfo mixin, SugarParameter parameter) {
        super(mixin, parameter);
    }

    @Override
    public boolean isRequired(MethodNode handler) {
        AnnotationNode annotation = InjectionInfo.getInjectorAnnotation(this.mixin, handler);
        return annotation != null && TARGET_INJECTORS.contains(annotation.desc) && LocalRefUtils.getTargetType(this.parameter.type, this.parameter.genericType) == this.parameter.type;
    }

    @Override
    public void transform(HandlerInfo info) {
        Type wrapperType = Type.getType(LocalRefUtils.getInterfaceFor(this.parameter.type));
        info.wrapParameter(this.parameter, wrapperType, ASMUtils.isPrimitive(this.parameter.type) ? null : this.parameter.type, (insns, load) -> LocalRefUtils.generateUnwrapping(insns, this.parameter.type, load));
    }

    static {
        for (String name : MixinExtrasService.getInstance().getAllClassNames(WrapOperation.class.getName())) {
            TARGET_INJECTORS.add('L' + name.replace('.', '/') + ';');
        }
    }
}