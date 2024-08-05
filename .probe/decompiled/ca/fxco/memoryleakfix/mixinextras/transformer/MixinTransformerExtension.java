package ca.fxco.memoryleakfix.mixinextras.transformer;

import ca.fxco.memoryleakfix.mixinextras.lib.apache.commons.tuple.Pair;
import ca.fxco.memoryleakfix.mixinextras.sugar.impl.SugarMixinTransformer;
import ca.fxco.memoryleakfix.mixinextras.utils.MixinInternals;
import ca.fxco.memoryleakfix.mixinextras.wrapper.factory.FactoryRedirectWrapperMixinTransformer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.WeakHashMap;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;
import org.spongepowered.asm.mixin.transformer.ext.IExtension;
import org.spongepowered.asm.mixin.transformer.ext.ITargetClassContext;

public class MixinTransformerExtension implements IExtension {

    private final Set<ClassNode> preparedMixins = Collections.newSetFromMap(new WeakHashMap());

    private final List<MixinTransformer> transformers = Arrays.asList(new FactoryRedirectWrapperMixinTransformer(), new SugarMixinTransformer());

    public boolean checkActive(MixinEnvironment environment) {
        return true;
    }

    public void preApply(ITargetClassContext context) {
        for (Pair<IMixinInfo, ClassNode> pair : MixinInternals.getMixinsFor(context)) {
            IMixinInfo info = pair.getLeft();
            ClassNode node = pair.getRight();
            if (!this.preparedMixins.contains(node)) {
                for (MixinTransformer transformer : this.transformers) {
                    transformer.transform(info, node);
                }
                this.preparedMixins.add(node);
            }
        }
    }

    public void postApply(ITargetClassContext context) {
    }

    public void export(MixinEnvironment env, String name, boolean force, ClassNode classNode) {
    }
}