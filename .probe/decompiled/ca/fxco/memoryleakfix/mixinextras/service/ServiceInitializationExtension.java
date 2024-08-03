package ca.fxco.memoryleakfix.mixinextras.service;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

class ServiceInitializationExtension implements IExtension {

    private final MixinExtrasService service;

    private boolean initialized;

    public ServiceInitializationExtension(MixinExtrasService service) {
        this.service = service;
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
        if (!this.initialized) {
            this.service.initialize();
            this.initialized = true;
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}