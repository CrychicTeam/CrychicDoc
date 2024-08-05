package dev.xkmc.l2archery.content.item;

import dev.xkmc.l2archery.content.feature.FeatureList;
import dev.xkmc.l2archery.content.feature.core.PotionArrowFeature;
import dev.xkmc.l2archery.init.registrate.ArcheryItems;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpectralArrowItem;
import net.minecraft.world.item.TippedArrowItem;
import net.minecraft.world.item.alchemy.PotionUtils;

public record ArrowData(Item item, @Nullable CompoundTag tag) {

    public static ArrowData of(Item item) {
        return new ArrowData(item, null);
    }

    public static ArrowData of(Item item, ItemStack stack) {
        return new ArrowData(item, stack.getTag());
    }

    public FeatureList getFeatures() {
        if (this.item instanceof GenericArrowItem gen) {
            return gen.getFeatures();
        } else {
            FeatureList ans = new FeatureList();
            if (this.item instanceof SpectralArrowItem) {
                ans.add(new PotionArrowFeature(List.of(new MobEffectInstance(MobEffects.GLOWING, 200, 0))));
            } else if (this.item instanceof TippedArrowItem) {
                ItemStack stack = new ItemStack(this.item);
                stack.setTag(this.tag);
                List<MobEffectInstance> list = new ArrayList();
                for (MobEffectInstance ins : PotionUtils.getMobEffects(stack)) {
                    list.add(new MobEffectInstance(ins.getEffect(), Math.max(ins.getDuration() / 8, 1), ins.getAmplifier(), ins.isAmbient(), ins.isVisible()));
                }
                ans.add(new PotionArrowFeature(list));
            }
            return ans;
        }
    }

    public ItemStack stack() {
        if (this.item instanceof TippedArrowItem) {
            ItemStack ans = new ItemStack(this.item);
            ans.setTag(this.tag);
            return ans;
        } else {
            return this.item.getDefaultInstance();
        }
    }

    public GenericArrowItem getItem() {
        return this.item instanceof GenericArrowItem gen ? gen : (GenericArrowItem) ArcheryItems.STARTER_ARROW.get();
    }
}