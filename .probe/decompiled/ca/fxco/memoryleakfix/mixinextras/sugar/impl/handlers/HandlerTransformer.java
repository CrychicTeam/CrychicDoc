package ca.fxco.memoryleakfix.mixinextras.sugar.impl.handlers;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.tuple.Pair;
import ca.fxco.memoryleakfix.mixinextras.service.MixinExtrasService;
import ca.fxco.memoryleakfix.mixinextras.sugar.Local;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarParameter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.objectweb.asm.tree.MethodNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public abstract class HandlerTransformer {

    private static final Map<String, Class<? extends HandlerTransformer>> MAP = new HashMap();

    protected final IMixinInfo mixin;

    protected final SugarParameter parameter;

    HandlerTransformer(IMixinInfo mixin, SugarParameter parameter) {
        this.mixin = mixin;
        this.parameter = parameter;
    }

    public abstract boolean isRequired(MethodNode var1);

    public abstract void transform(HandlerInfo var1);

    public static HandlerTransformer create(IMixinInfo mixin, SugarParameter parameter) {
        try {
            Class<? extends HandlerTransformer> clazz = (Class<? extends HandlerTransformer>) MAP.get(parameter.sugar.desc);
            if (clazz == null) {
                return null;
            } else {
                Constructor<? extends HandlerTransformer> ctor = clazz.getDeclaredConstructor(IMixinInfo.class, SugarParameter.class);
                return (HandlerTransformer) ctor.newInstance(mixin, parameter);
            }
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException var4) {
            throw new RuntimeException(var4);
        }
    }

    static {
        for (Pair<Class<? extends Annotation>, Class<? extends HandlerTransformer>> pair : Arrays.asList(Pair.of(Local.class, LocalHandlerTransformer.class))) {
            for (String name : MixinExtrasService.getInstance().getAllClassNames(pair.getLeft().getName())) {
                MAP.put('L' + name.replace('.', '/') + ';', pair.getRight());
            }
        }
    }
}