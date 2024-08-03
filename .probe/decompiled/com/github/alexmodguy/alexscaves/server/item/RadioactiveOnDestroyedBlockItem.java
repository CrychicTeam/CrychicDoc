package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.client.particle.ACParticleRegistry;
import com.github.alexmodguy.alexscaves.server.potion.ACEffectRegistry;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class RadioactiveOnDestroyedBlockItem extends RadioactiveBlockItem {

    public RadioactiveOnDestroyedBlockItem(RegistryObject<Block> blockSupplier, Item.Properties props, float randomChanceOfRadiation) {
        super(blockSupplier, props, randomChanceOfRadiation);
    }

    public void onDestroyed(ItemEntity itemEntity, DamageSource damageSource) {
        super.onDestroyed(itemEntity, damageSource);
        if (!damageSource.isCreativePlayer() && !itemEntity.m_213877_()) {
            itemEntity.m_146870_();
            AreaEffectCloud cloud = new AreaEffectCloud(itemEntity.m_9236_(), itemEntity.m_20185_(), itemEntity.m_20186_(), itemEntity.m_20189_());
            cloud.setParticle(ACParticleRegistry.GAMMAROACH.get());
            cloud.setFixedColor(7853582);
            cloud.addEffect(new MobEffectInstance(ACEffectRegistry.IRRADIATED.get(), 9600, 1));
            cloud.setRadius(2.0F);
            cloud.setDuration(12000);
            cloud.setWaitTime(0);
            cloud.setRadiusPerTick(-cloud.getRadius() / (float) cloud.getDuration());
            itemEntity.m_9236_().m_7967_(cloud);
            itemEntity.m_9236_().explode(null, itemEntity.m_20185_(), itemEntity.m_20186_() + 0.5, itemEntity.m_20189_(), 1.8F, Level.ExplosionInteraction.BLOCK);
        }
    }
}