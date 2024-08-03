package com.prunoideae.powerfuljs.capabilities.forge.mods.pnc;

import com.prunoideae.powerfuljs.capabilities.forge.CapabilityBuilderForge;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;
import me.desht.pneumaticcraft.api.PNCCapabilities;
import me.desht.pneumaticcraft.api.tileentity.IAirHandlerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.NotNull;

public class CapabilityAir {

    public static final String AIR_TAG = "pnc:air";

    public CapabilityAir.ItemStackBuilder itemStack(int capacity, float maxPressure) {
        return new CapabilityAir.ItemStackBuilder(capacity, maxPressure);
    }

    public CapabilityAir.ItemStackBuilderCustom itemStackCustom() {
        return new CapabilityAir.ItemStackBuilderCustom();
    }

    public static class ItemStackBuilder extends CapabilityBuilderForge<ItemStack, IAirHandlerItem> {

        private final int capacity;

        private final float maxPressure;

        public ItemStackBuilder(int capacity, float maxPressure) {
            this.capacity = capacity;
            this.maxPressure = maxPressure;
        }

        public IAirHandlerItem getCapability(ItemStack instance) {
            return new IAirHandlerItem() {

                @NotNull
                public ItemStack getContainer() {
                    return instance;
                }

                public float getPressure() {
                    return (float) this.getVolume() / (float) this.getAir();
                }

                public int getAir() {
                    return instance.getOrCreateTag().getInt("pnc:air");
                }

                public void addAir(int amount) {
                    instance.getOrCreateTag().putInt("pnc:air", this.getAir() + amount);
                }

                public int getBaseVolume() {
                    return ItemStackBuilder.this.capacity;
                }

                public void setBaseVolume(int newBaseVolume) {
                }

                public int getVolume() {
                    return ItemStackBuilder.this.capacity;
                }

                public float maxPressure() {
                    return ItemStackBuilder.this.maxPressure;
                }
            };
        }

        public Capability<IAirHandlerItem> getCapabilityKey() {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:air_pnc");
        }
    }

    public static class ItemStackBuilderCustom extends CapabilityBuilderForge<ItemStack, IAirHandlerItem> {

        private Function<ItemStack, Float> getPressure;

        private ToIntFunction<ItemStack> getAir;

        private BiConsumer<ItemStack, Integer> addAir;

        private ToIntFunction<ItemStack> getBaseVolume;

        private BiConsumer<ItemStack, Integer> setBaseVolume;

        private ToIntFunction<ItemStack> getVolume;

        private Function<ItemStack, Float> maxPressure;

        public CapabilityAir.ItemStackBuilderCustom getPressure(Function<ItemStack, Float> getPressure) {
            this.getPressure = getPressure;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom getAir(ToIntFunction<ItemStack> getAir) {
            this.getAir = getAir;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom addAir(BiConsumer<ItemStack, Integer> addAir) {
            this.addAir = addAir;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom getBaseVolume(ToIntFunction<ItemStack> getBaseVolume) {
            this.getBaseVolume = getBaseVolume;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom setBaseVolume(BiConsumer<ItemStack, Integer> setBaseVolume) {
            this.setBaseVolume = setBaseVolume;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom getVolume(ToIntFunction<ItemStack> getVolume) {
            this.getVolume = getVolume;
            return this;
        }

        public CapabilityAir.ItemStackBuilderCustom maxPressure(Function<ItemStack, Float> maxPressure) {
            this.maxPressure = maxPressure;
            return this;
        }

        public IAirHandlerItem getCapability(ItemStack instance) {
            return new IAirHandlerItem() {

                @NotNull
                public ItemStack getContainer() {
                    return instance;
                }

                public float getPressure() {
                    return ItemStackBuilderCustom.this.getPressure == null ? 0.0F : (Float) ItemStackBuilderCustom.this.getPressure.apply(instance);
                }

                public int getAir() {
                    return ItemStackBuilderCustom.this.getAir == null ? 0 : ItemStackBuilderCustom.this.getAir.applyAsInt(instance);
                }

                public void addAir(int amount) {
                    if (ItemStackBuilderCustom.this.addAir != null) {
                        ItemStackBuilderCustom.this.addAir.accept(instance, amount);
                    }
                }

                public int getBaseVolume() {
                    return ItemStackBuilderCustom.this.getBaseVolume == null ? 1 : ItemStackBuilderCustom.this.getBaseVolume.applyAsInt(instance);
                }

                public void setBaseVolume(int newBaseVolume) {
                    if (ItemStackBuilderCustom.this.setBaseVolume != null) {
                        ItemStackBuilderCustom.this.setBaseVolume.accept(instance, newBaseVolume);
                    }
                }

                public int getVolume() {
                    return ItemStackBuilderCustom.this.getVolume == null ? 1 : ItemStackBuilderCustom.this.getVolume.applyAsInt(instance);
                }

                public float maxPressure() {
                    return ItemStackBuilderCustom.this.maxPressure == null ? 1.0F : (Float) ItemStackBuilderCustom.this.maxPressure.apply(instance);
                }
            };
        }

        public Capability<IAirHandlerItem> getCapabilityKey() {
            return PNCCapabilities.AIR_HANDLER_ITEM_CAPABILITY;
        }

        @Override
        public ResourceLocation getResourceLocation() {
            return new ResourceLocation("powerful:air_pnc_item_custom");
        }
    }
}