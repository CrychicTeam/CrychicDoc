package dev.xkmc.l2complements.content.item.create;

import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;

public class ShadowSteelItem extends NoGravMagicalDohickyItem {

    public ShadowSteelItem(Item.Properties properties, Supplier<MutableComponent> sup) {
        super(properties, sup);
    }

    @Override
    protected void onCreated(ItemEntity entity, CompoundTag persistentData) {
        super.onCreated(entity, persistentData);
        float yMotion = (entity.f_19789_ + 3.0F) / 50.0F;
        entity.m_20334_(0.0, (double) yMotion, 0.0);
    }

    @Override
    protected float getIdleParticleChance(ItemEntity entity) {
        return (float) (Mth.clamp((double) (entity.getItem().getCount() - 10), Mth.clamp(entity.m_20184_().y * 20.0, 5.0, 20.0), 100.0) / 64.0);
    }
}