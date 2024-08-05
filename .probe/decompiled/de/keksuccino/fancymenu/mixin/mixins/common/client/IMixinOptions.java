package de.keksuccino.fancymenu.mixin.mixins.common.client;

import java.util.Set;
import net.minecraft.client.Options;
import net.minecraft.world.entity.player.PlayerModelPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin({ Options.class })
public interface IMixinOptions {

    @Accessor("modelParts")
    Set<PlayerModelPart> getModelPartsFancyMenu();

    @Invoker("processOptions")
    void invokeProcessOptionsFancyMenu(Options.FieldAccess var1);
}