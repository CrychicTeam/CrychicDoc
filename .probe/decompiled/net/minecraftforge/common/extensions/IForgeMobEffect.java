package net.minecraftforge.common.extensions;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public interface IForgeMobEffect {

    private MobEffect self() {
        return (MobEffect) this;
    }

    default List<ItemStack> getCurativeItems() {
        ArrayList<ItemStack> ret = new ArrayList();
        ret.add(new ItemStack(Items.MILK_BUCKET));
        return ret;
    }

    default int getSortOrder(MobEffectInstance effectInstance) {
        return this.self().getColor();
    }
}