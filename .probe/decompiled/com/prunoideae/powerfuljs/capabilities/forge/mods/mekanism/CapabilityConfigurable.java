package com.prunoideae.powerfuljs.capabilities.forge.mods.mekanism;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiFunction;
import mekanism.api.IConfigurable;
import mekanism.common.capabilities.Capabilities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityConfigurable {

    public CapabilityConfigurable.BlockEntityBuilder blockEntity() {
        return new CapabilityConfigurable.BlockEntityBuilder();
    }

    public static class BlockEntityBuilder extends CapabilityBuilderForge<BlockEntity, IConfigurable> {

        private BiFunction<BlockEntity, Player, String> onSneakRightClick;

        private BiFunction<BlockEntity, Player, String> onRightClick;

        public CapabilityConfigurable.BlockEntityBuilder onSneakRightClick(BiFunction<BlockEntity, Player, String> onSneakRightClick) {
            this.onSneakRightClick = onSneakRightClick;
            return this;
        }

        public CapabilityConfigurable.BlockEntityBuilder onRightClick(BiFunction<BlockEntity, Player, String> onRightClick) {
            this.onRightClick = onRightClick;
            return this;
        }

        public IConfigurable getCapability(BlockEntity instance) {
            return new IConfigurable() {

                public InteractionResult onSneakRightClick(Player player) {
                    return BlockEntityBuilder.this.onSneakRightClick != null ? InteractionResult.valueOf((String) BlockEntityBuilder.this.onSneakRightClick.apply(instance, player)) : InteractionResult.FAIL;
                }

                public InteractionResult onRightClick(Player player) {
                    return BlockEntityBuilder.this.onRightClick != null ? InteractionResult.valueOf((String) BlockEntityBuilder.this.onRightClick.apply(instance, player)) : InteractionResult.FAIL;
                }
            };
        }

        public Capability<IConfigurable> getCapabilityKey() {
            return Capabilities.CONFIGURABLE;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:mek_configurable_be");
        }
    }
}