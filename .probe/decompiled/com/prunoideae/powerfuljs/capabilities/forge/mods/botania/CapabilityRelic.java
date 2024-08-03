package com.prunoideae.powerfuljs.capabilities.forge.mods.botania;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import dev.latvian.mods.rhino.util.HideFromJS;
import java.util.List;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;
import vazkii.botania.api.BotaniaForgeCapabilities;
import vazkii.botania.api.item.Relic;
import vazkii.botania.common.helper.ItemNBTHelper;
import vazkii.botania.common.item.relic.RelicImpl;

public class CapabilityRelic {

    public CapabilityRelic.NormalRelicBuilder normalRelic() {
        return this.normalRelic(null);
    }

    public CapabilityRelic.NormalRelicBuilder normalRelic(ResourceLocation advancement) {
        return new CapabilityRelic.NormalRelicBuilder(advancement);
    }

    public CapabilityRelic.CustomRelicBuilder customRelic() {
        return new CapabilityRelic.CustomRelicBuilder();
    }

    public void addRelicTooltipForItem(ItemStack stack, List<Component> tooltip) {
        RelicImpl.addDefaultTooltip(stack, tooltip);
    }

    public static class CustomRelicBuilder extends CapabilityBuilderForge<ItemStack, Relic> {

        private static final String TAG_SOULBIND_UUID = "soulbindUUID";

        private CapabilityRelic.RelicTick tick;

        private ResourceLocation advancement;

        private boolean shouldDamageWrongPlayer = true;

        public CapabilityRelic.CustomRelicBuilder setAdvancement(ResourceLocation advancement) {
            this.advancement = advancement;
            return this;
        }

        public CapabilityRelic.CustomRelicBuilder shouldDamageWrongPlayer(boolean shouldDamageWrongPlayer) {
            this.shouldDamageWrongPlayer = shouldDamageWrongPlayer;
            return this;
        }

        public CapabilityRelic.CustomRelicBuilder onTick(CapabilityRelic.RelicTick tick) {
            this.tick = tick;
            return this;
        }

        @HideFromJS
        public Relic getCapability(ItemStack instance) {
            return new Relic() {

                public void bindToUUID(UUID uuid) {
                    ItemNBTHelper.setString(instance, "soulbindUUID", uuid.toString());
                }

                @Nullable
                public UUID getSoulbindUUID() {
                    if (ItemNBTHelper.verifyExistance(instance, "soulbindUUID")) {
                        try {
                            return UUID.fromString(ItemNBTHelper.getString(instance, "soulbindUUID", ""));
                        } catch (IllegalArgumentException var2) {
                            ItemNBTHelper.removeEntry(instance, "soulbindUUID");
                        }
                    }
                    return null;
                }

                public void tickBinding(Player player) {
                    if (CustomRelicBuilder.this.tick != null) {
                        CustomRelicBuilder.this.tick.tick(instance, this, player);
                    }
                }

                public boolean isRightPlayer(Player player) {
                    return player.m_20148_().equals(this.getSoulbindUUID());
                }

                @Nullable
                public ResourceLocation getAdvancement() {
                    return CustomRelicBuilder.this.advancement;
                }

                public boolean shouldDamageWrongPlayer() {
                    return CustomRelicBuilder.this.shouldDamageWrongPlayer;
                }
            };
        }

        @HideFromJS
        public Capability<Relic> getCapabilityKey() {
            return BotaniaForgeCapabilities.RELIC;
        }

        @HideFromJS
        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:relic_custom_botania");
        }
    }

    public static class NormalRelicBuilder extends CapabilityBuilderForge<ItemStack, Relic> {

        private final ResourceLocation advancement;

        public NormalRelicBuilder(ResourceLocation advancement) {
            this.advancement = advancement;
        }

        public Relic getCapability(ItemStack instance) {
            return new RelicImpl(instance, this.advancement);
        }

        public Capability<Relic> getCapabilityKey() {
            return BotaniaForgeCapabilities.RELIC;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:relic_botania");
        }
    }

    @FunctionalInterface
    public interface RelicTick {

        void tick(ItemStack var1, Relic var2, Player var3);
    }
}