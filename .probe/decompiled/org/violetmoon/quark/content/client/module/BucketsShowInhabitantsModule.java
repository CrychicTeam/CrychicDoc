package org.violetmoon.quark.content.client.module;

import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.IntUnaryOperator;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.mobs.module.CrabsModule;
import org.violetmoon.quark.content.tools.module.SlimeInABucketModule;
import org.violetmoon.zeta.client.event.load.ZAddItemColorHandlers;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.ItemNBTHelper;

@ZetaLoadModule(category = "client")
public class BucketsShowInhabitantsModule extends ZetaModule {

    @Config
    public boolean showAxolotls = true;

    @Config
    public boolean showCrabs = true;

    @Config
    public boolean showTropicalFish = true;

    @Config
    public boolean showShinySlime = true;

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends BucketsShowInhabitantsModule {

        @LoadEvent
        public void clientSetup(ZClientSetup e) {
            e.enqueueWork(() -> {
                ItemProperties.register(Items.AXOLOTL_BUCKET, new ResourceLocation("quark", "variant"), new BucketsShowInhabitantsModule.Client.MobBucketVariantProperty(Axolotl.Variant.values().length, () -> this.showAxolotls));
                ItemProperties.register(CrabsModule.crab_bucket, new ResourceLocation("quark", "variant"), new BucketsShowInhabitantsModule.Client.MobBucketVariantProperty(3, () -> this.showCrabs));
                ItemProperties.register(SlimeInABucketModule.slime_in_a_bucket, new ResourceLocation("quark", "shiny"), new BucketsShowInhabitantsModule.Client.ShinyMobBucketProperty(() -> this.showShinySlime && VariantAnimalTexturesModule.staticEnabled && VariantAnimalTexturesModule.enableShinySlime));
                ItemProperties.register(Items.TROPICAL_FISH_BUCKET, new ResourceLocation("quark", "base"), new BucketsShowInhabitantsModule.Client.TropicalFishBucketVariantProperty(b -> TropicalFish.getBaseColor(b).getId(), () -> this.showTropicalFish));
                ItemProperties.register(Items.TROPICAL_FISH_BUCKET, new ResourceLocation("quark", "pattern"), new BucketsShowInhabitantsModule.Client.TropicalFishBucketVariantProperty(p -> TropicalFish.getPattern(p).getPackedId(), () -> this.showTropicalFish));
            });
        }

        @LoadEvent
        public void registerItemColors(ZAddItemColorHandlers evt) {
            ItemColor parent = QuarkClient.ZETA_CLIENT.getItemColor(evt.getItemColors(), Items.TROPICAL_FISH_BUCKET);
            evt.register(new BucketsShowInhabitantsModule.Client.TropicalFishBucketColor(parent, () -> this.showTropicalFish), Items.TROPICAL_FISH_BUCKET);
        }

        private class MobBucketVariantProperty implements ItemPropertyFunction {

            private final int maxVariants;

            private final BooleanSupplier featureEnabled;

            public MobBucketVariantProperty(int maxVariants, BooleanSupplier featureEnabled) {
                this.maxVariants = maxVariants;
                this.featureEnabled = featureEnabled;
            }

            @Override
            public float call(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int id) {
                return Client.this.enabled && this.featureEnabled.getAsBoolean() ? (float) (ItemNBTHelper.getInt(stack, "Variant", 0) % this.maxVariants) : 0.0F;
            }
        }

        private class ShinyMobBucketProperty implements ItemPropertyFunction {

            private final BooleanSupplier featureEnabled;

            public ShinyMobBucketProperty(BooleanSupplier featureEnabled) {
                this.featureEnabled = featureEnabled;
            }

            @Override
            public float call(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int id) {
                if (Client.this.enabled && this.featureEnabled.getAsBoolean()) {
                    CompoundTag data = ItemNBTHelper.getCompound(stack, "slime_nbt", true);
                    if (data != null && data.hasUUID("UUID")) {
                        UUID uuid = data.getUUID("UUID");
                        if (VariantAnimalTexturesModule.Client.isShiny(uuid)) {
                            return 1.0F;
                        }
                    }
                    return 0.0F;
                } else {
                    return 0.0F;
                }
            }
        }

        private class TropicalFishBucketColor implements ItemColor {

            @Nullable
            private final ItemColor parent;

            private final BooleanSupplier featureEnabled;

            public TropicalFishBucketColor(@Nullable ItemColor parent, BooleanSupplier featureEnabled) {
                this.parent = parent;
                this.featureEnabled = featureEnabled;
            }

            @Override
            public int getColor(@NotNull ItemStack stack, int layer) {
                if (Client.this.enabled && this.featureEnabled.getAsBoolean() && (layer == 1 || layer == 2)) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null && tag.contains("BucketVariantTag", 3)) {
                        int variant = tag.getInt("BucketVariantTag");
                        DyeColor dyeColor = layer == 1 ? TropicalFish.getBaseColor(variant) : TropicalFish.getPatternColor(variant);
                        float[] colorComponents = dyeColor.getTextureDiffuseColors();
                        return (int) (colorComponents[0] * 255.0F) << 16 | (int) (colorComponents[1] * 255.0F) << 8 | (int) (colorComponents[2] * 255.0F);
                    }
                }
                return this.parent != null ? this.parent.getColor(stack, layer) : -1;
            }
        }

        private class TropicalFishBucketVariantProperty implements ItemPropertyFunction {

            private final IntUnaryOperator extractor;

            private final BooleanSupplier featureEnabled;

            public TropicalFishBucketVariantProperty(IntUnaryOperator extractor, BooleanSupplier featureEnabled) {
                this.extractor = extractor;
                this.featureEnabled = featureEnabled;
            }

            @Override
            public float call(@NotNull ItemStack stack, @Nullable ClientLevel level, @Nullable LivingEntity entity, int id) {
                if (Client.this.enabled && this.featureEnabled.getAsBoolean()) {
                    CompoundTag tag = stack.getTag();
                    if (tag != null && tag.contains("BucketVariantTag", 3)) {
                        int variant = tag.getInt("BucketVariantTag");
                        return (float) (this.extractor.applyAsInt(variant) + 1);
                    } else {
                        return 0.0F;
                    }
                } else {
                    return 0.0F;
                }
            }
        }
    }
}