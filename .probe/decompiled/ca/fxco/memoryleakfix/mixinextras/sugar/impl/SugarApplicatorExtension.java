package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import org.apache.commons.lang3.tuple.Pair;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class SugarApplicatorExtension implements IExtension {

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
        for (Pair<IMixinInfo, ClassNode> pair : MixinInternals.getMixinsFor(context)) {
            SugarInjector.prepareMixin((IMixinInfo) pair.getLeft(), (ClassNode) pair.getRight());
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}