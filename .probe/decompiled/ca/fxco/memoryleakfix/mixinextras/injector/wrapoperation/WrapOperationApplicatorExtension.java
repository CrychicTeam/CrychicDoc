package ca.fxco.memoryleakfix.mixinextras.injector.wrapoperation;

import ca.fxco.memoryleakfix.mixinextras.injector.LateApplyingInjectorInfo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class WrapOperationApplicatorExtension implements IExtension {

    private static final Map<ITargetClassContext, List<Runnable[]>> QUEUED_INJECTIONS = Collections.synchronizedMap(new HashMap());

    static void offerInjection(ITargetClassContext targetClassContext, LateApplyingInjectorInfo injectorInfo) {
        ((List) QUEUED_INJECTIONS.computeIfAbsent(targetClassContext, k -> new ArrayList())).add(new Runnable[] { injectorInfo::lateInject, injectorInfo::latePostInject });
    }

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
    }

    public void postApply(ITargetClassContext context) {
        List<Runnable[]> queuedInjections = (List<Runnable[]>) QUEUED_INJECTIONS.get(context);
        if (queuedInjections != null) {
            for (Runnable[] injection : queuedInjections) {
                injection[0].run();
            }
            for (Runnable[] injection : queuedInjections) {
                injection[1].run();
            }
            QUEUED_INJECTIONS.remove(context);
        }
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}