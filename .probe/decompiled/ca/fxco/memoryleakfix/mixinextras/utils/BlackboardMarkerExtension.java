package ca.fxco.memoryleakfix.mixinextras.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.IMixinTransformer;
import org.spongepowered.asm.mixin.transformer.ext.Extensions;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

class BlackboardMarkerExtension implements IExtension, Supplier<Map<String, Object>> {

    private final Map<String, Object> map = new HashMap();

    public Map<String, Object> get() {
        return this.map;
    }

    public boolean checkActive(MixinEnvironment environment) {
        return false;
    }

    public void preApply(ITargetClassContext context) {
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }

    static Map<String, Object> getImpl() {
        IMixinTransformer transformer = (IMixinTransformer) MixinEnvironment.getDefaultEnvironment().getActiveTransformer();
        Extensions extensions = (Extensions) transformer.getExtensions();
        for (IExtension extension : extensions.getExtensions()) {
            if (extension.getClass().getName().endsWith(".BlackboardMarkerExtension")) {
                return (Map<String, Object>) ((Supplier) extension).get();
            }
        }
        BlackboardMarkerExtension newImpl = new BlackboardMarkerExtension();
        extensions.add(newImpl);
        return newImpl.get();
    }
}