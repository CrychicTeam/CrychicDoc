package com.github.alexthe666.iceandfire.entity.props;

import com.github.alexthe666.iceandfire.IafConfig;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import com.github.alexthe666.iceandfire.item.IafItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ChickenData {

    public int timeUntilNextEgg = -1;

    public void tickChicken(LivingEntity entity) {
        if (IafConfig.chickensLayRottenEggs && !entity.m_9236_().isClientSide() && ServerEvents.isChicken(entity) && !entity.isBaby()) {
            if (this.timeUntilNextEgg == -1) {
                this.timeUntilNextEgg = this.createDefaultTime(entity.getRandom());
            }
            if (this.timeUntilNextEgg == 0) {
                if (entity.f_19797_ > 30 && entity.getRandom().nextInt(IafConfig.cockatriceEggChance + 1) == 0) {
                    entity.m_5496_(SoundEvents.CHICKEN_HURT, 2.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                    entity.m_5496_(SoundEvents.CHICKEN_EGG, 1.0F, (entity.getRandom().nextFloat() - entity.getRandom().nextFloat()) * 0.2F + 1.0F);
                    entity.m_20000_(IafItemRegistry.ROTTEN_EGG.get(), 1);
                }
                this.timeUntilNextEgg = -1;
            } else {
                this.timeUntilNextEgg--;
            }
        }
    }

    public void setTime(int timeUntilNextEgg) {
        this.timeUntilNextEgg = timeUntilNextEgg;
    }

    public void serialize(CompoundTag tag) {
        CompoundTag chickenData = new CompoundTag();
        chickenData.putInt("timeUntilNextEgg", this.timeUntilNextEgg);
        tag.put("chickenData", chickenData);
    }

    public void deserialize(CompoundTag tag) {
        CompoundTag chickenData = tag.getCompound("chickenData");
        this.timeUntilNextEgg = chickenData.getInt("timeUntilNextEgg");
    }

    private int createDefaultTime(@NotNull RandomSource random) {
        return random.nextInt(6000) + 6000;
    }
}