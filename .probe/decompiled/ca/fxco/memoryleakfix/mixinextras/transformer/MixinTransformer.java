package ca.fxco.memoryleakfix.mixinextras.transformer;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public interface MixinTransformer {

    void transform(IMixinInfo var1, ClassNode var2);
}