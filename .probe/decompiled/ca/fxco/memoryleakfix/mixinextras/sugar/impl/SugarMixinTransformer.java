package ca.fxco.memoryleakfix.mixinextras.sugar.impl;

import ca.fxco.memoryleakfix.mixinextras.transformer.MixinTransformer;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class SugarMixinTransformer implements MixinTransformer {

    @Override
    public void transform(IMixinInfo mixinInfo, ClassNode mixinNode) {
        SugarInjector.prepareMixin(mixinInfo, mixinNode);
    }
}