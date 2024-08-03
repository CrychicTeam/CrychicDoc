package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.function.BiConsumer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.block.Avatar;
import vazkii.botania.api.item.AvatarWieldable;

public class CapabilityAvatar {

    public CapabilityAvatar.AvatarBehaviorBuilder wieldable() {
        return new CapabilityAvatar.AvatarBehaviorBuilder();
    }

    public static class AvatarBehaviorBuilder extends CapabilityBuilderForge<ItemStack, AvatarWieldable> {

        private BiConsumer<ItemStack, Avatar> onUpdate;

        private ResourceLocation overlay = new ResourceLocation("botania:textures/model/avatar.png");

        public CapabilityAvatar.AvatarBehaviorBuilder onUpdate(BiConsumer<ItemStack, Avatar> onUpdate) {
            this.onUpdate = onUpdate;
            return this;
        }

        public CapabilityAvatar.AvatarBehaviorBuilder setOverlay(ResourceLocation overlay) {
            this.overlay = overlay;
            return this;
        }

        @HideFromJS
        public AvatarWieldable getCapability(ItemStack instance) {
            return new AvatarWieldable() {

                public void onAvatarUpdate(Avatar tile) {
                    BlockEntity t = (BlockEntity) tile;
                    if (t.hasLevel()) {
                        if (AvatarBehaviorBuilder.this.onUpdate != null && t.getLevel().getGameTime() % 10L == 0L) {
                            AvatarBehaviorBuilder.this.onUpdate.accept(instance, tile);
                        }
                    }
                }

                public ResourceLocation getOverlayResource(Avatar tile) {
                    return AvatarBehaviorBuilder.this.overlay;
                }
            };
        }

        @HideFromJS
        public Capability<AvatarWieldable> getCapabilityKey() {
            return BotaniaForgeCapabilities.AVATAR_WIELDABLE;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:botania_avatar_wieldable");
        }
    }
}