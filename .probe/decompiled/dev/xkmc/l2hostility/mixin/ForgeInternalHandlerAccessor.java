package dev.xkmc.l2hostility.mixin;

import net.minecraftforge.common.ForgeInternalHandler;
import net.minecraftforge.common.loot.LootModifierManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ ForgeInternalHandler.class })
public interface ForgeInternalHandlerAccessor {

    @Invoker(value = "getLootModifierManager", remap = false)
    static LootModifierManager callGetLootModifierManager() {
        throw new AssertionError();
    }
}