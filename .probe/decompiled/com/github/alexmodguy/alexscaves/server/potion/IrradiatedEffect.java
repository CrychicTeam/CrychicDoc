package com.github.alexmodguy.alexscaves.server.potion;

import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexmodguy.alexscaves.server.item.HazmatArmorItem;
import com.github.alexmodguy.alexscaves.server.misc.ACDamageTypes;
import java.util.List;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class IrradiatedEffect extends MobEffect {

    public static final int BLUE_LEVEL = 4;

    protected IrradiatedEffect() {
        super(MobEffectCategory.HARMFUL, 7853582);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int tick) {
        int hazmat = HazmatArmorItem.getWornAmount(entity);
        float damageScale = 1.0F - (float) hazmat * 0.25F;
        if (entity instanceof Player player && hazmat == 0) {
            player.causeFoodExhaustion(0.4F);
        }
        if (!(entity instanceof RaycatEntity) && entity.m_9236_().random.nextFloat() < damageScale + 0.1F) {
            entity.hurt(ACDamageTypes.causeRadiationDamage(entity.m_9236_().registryAccess()), damageScale);
        }
    }

    @Override
    public boolean isDurationEffectTick(int tick1, int level) {
        if (level <= 0) {
            return false;
        } else {
            int j = 200 / level;
            return j > 1 ? tick1 % j == j / 2 : true;
        }
    }

    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}