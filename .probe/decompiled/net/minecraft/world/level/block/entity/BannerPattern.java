package net.minecraft.world.level.block.entity;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class BannerPattern {

    final String hashname;

    public BannerPattern(String string0) {
        this.hashname = string0;
    }

    public static ResourceLocation location(ResourceKey<BannerPattern> resourceKeyBannerPattern0, boolean boolean1) {
        String $$2 = boolean1 ? "banner" : "shield";
        return resourceKeyBannerPattern0.location().withPrefix("entity/" + $$2 + "/");
    }

    public String getHashname() {
        return this.hashname;
    }

    @Nullable
    public static Holder<BannerPattern> byHash(String string0) {
        return (Holder<BannerPattern>) BuiltInRegistries.BANNER_PATTERN.holders().filter(p_222704_ -> ((BannerPattern) p_222704_.value()).hashname.equals(string0)).findAny().orElse(null);
    }

    public static class Builder {

        private final List<Pair<Holder<BannerPattern>, DyeColor>> patterns = Lists.newArrayList();

        public BannerPattern.Builder addPattern(ResourceKey<BannerPattern> resourceKeyBannerPattern0, DyeColor dyeColor1) {
            return this.addPattern(BuiltInRegistries.BANNER_PATTERN.getHolderOrThrow(resourceKeyBannerPattern0), dyeColor1);
        }

        public BannerPattern.Builder addPattern(Holder<BannerPattern> holderBannerPattern0, DyeColor dyeColor1) {
            return this.addPattern(Pair.of(holderBannerPattern0, dyeColor1));
        }

        public BannerPattern.Builder addPattern(Pair<Holder<BannerPattern>, DyeColor> pairHolderBannerPatternDyeColor0) {
            this.patterns.add(pairHolderBannerPatternDyeColor0);
            return this;
        }

        public ListTag toListTag() {
            ListTag $$0 = new ListTag();
            for (Pair<Holder<BannerPattern>, DyeColor> $$1 : this.patterns) {
                CompoundTag $$2 = new CompoundTag();
                $$2.putString("Pattern", ((BannerPattern) ((Holder) $$1.getFirst()).value()).hashname);
                $$2.putInt("Color", ((DyeColor) $$1.getSecond()).getId());
                $$0.add($$2);
            }
            return $$0;
        }
    }
}