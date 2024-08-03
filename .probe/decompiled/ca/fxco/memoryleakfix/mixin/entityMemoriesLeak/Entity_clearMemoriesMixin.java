package ca.fxco.memoryleakfix.mixin.entityMemoriesLeak;

import ca.fxco.memoryleakfix.config.MinecraftRequirement;
import ca.fxco.memoryleakfix.config.Remap;
import ca.fxco.memoryleakfix.config.VersionRange;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@MinecraftRequirement({ @VersionRange(maxVersion = "1.19.3") })
@Mixin({ Entity.class })
public abstract class Entity_clearMemoriesMixin {

    @Remap(fabric = { "method_5650" }, forge = { "m_142687_" }, mcp = { "func_70106_y" }, excludeDev = true)
    @Inject(method = { "remove" }, at = { @At("TAIL") }, allow = 1, remap = false)
    protected void memoryLeakFix$OnEntityRemoved(CallbackInfo ci) {
    }
}