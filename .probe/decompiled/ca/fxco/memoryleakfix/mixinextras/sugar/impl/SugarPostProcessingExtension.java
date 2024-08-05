package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class SugarPostProcessingExtension implements IExtension {

    private static final Map<String, List<Runnable>> POST_PROCESSING_TASKS = new HashMap();

    static void enqueuePostProcessing(SugarApplicator applicator, Runnable task) {
        ((List) POST_PROCESSING_TASKS.computeIfAbsent(applicator.info.getClassNode().name, k -> new ArrayList())).add(task);
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
    }

    public void postApply(ITargetClassContext context) {
        String targetName = context.getClassNode().name;
        List<Runnable> tasks = (List<Runnable>) POST_PROCESSING_TASKS.get(targetName);
        if (tasks != null) {
            tasks.forEach(Runnable::run);
            POST_PROCESSING_TASKS.remove(targetName);
        }
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}