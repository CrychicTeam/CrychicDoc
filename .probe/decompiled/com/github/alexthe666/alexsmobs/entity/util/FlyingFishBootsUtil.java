package com.github.alexthe666.alexsmobs.entity.util;

import com.github.alexthe666.alexsmobs.item.AMItemRegistry;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.CitadelEntityData;
import com.github.alexthe666.citadel.server.message.PropertiesMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class FlyingFishBootsUtil {

    private static final String BOOST_TICKS = "FlyingFishBoostAlexsMobs";

    private static final int MIN_BOOST_TIME = 35;

    public static void setBoostTicks(LivingEntity entity, int ticks) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(entity);
        lassoedTag.putInt("FlyingFishBoostAlexsMobs", ticks);
        CitadelEntityData.setCitadelTag(entity, lassoedTag);
        if (!entity.m_9236_().isClientSide) {
            Citadel.sendMSGToAll(new PropertiesMessage("CitadelPatreonConfig", lassoedTag, entity.m_19879_()));
        } else {
            Citadel.sendMSGToServer(new PropertiesMessage("CitadelPatreonConfig", lassoedTag, entity.m_19879_()));
        }
    }

    public static int getBoostTicks(LivingEntity entity) {
        CompoundTag lassoedTag = CitadelEntityData.getOrCreateCitadelTag(entity);
        return lassoedTag.contains("FlyingFishBoostAlexsMobs") ? lassoedTag.getInt("FlyingFishBoostAlexsMobs") : 0;
    }

    public static boolean isWearing(LivingEntity entity) {
        return entity.getItemBySlot(EquipmentSlot.FEET).getItem() == AMItemRegistry.FLYING_FISH_BOOTS.get();
    }

    public static void tickFlyingFishBoots(LivingEntity fishy) {
        int boostTime = getBoostTicks(fishy);
        if (boostTime <= 15 && fishy.m_20072_() && !fishy.m_20096_() && fishy.m_204036_(FluidTags.WATER) < 0.2F && fishy.jumping && (!(fishy instanceof Player) || !((Player) fishy).getAbilities().flying)) {
            RandomSource rand = fishy.getRandom();
            boostTime = 35;
            Vec3 forward = new Vec3(0.0, 0.0, (double) (0.5F + rand.nextFloat() * 1.2F)).xRot(-fishy.m_146909_() * (float) (Math.PI / 180.0)).yRot(-fishy.getYHeadRot() * (float) (Math.PI / 180.0));
            Vec3 delta = fishy.m_20184_().add(forward);
            fishy.m_20334_(delta.x, 0.3 + (double) (rand.nextFloat() * 0.3F), delta.z);
            fishy.m_146922_(fishy.getYHeadRot());
        }
        if (boostTime > 0) {
            if (!fishy.m_20072_() && !fishy.m_20096_()) {
                if (fishy.m_20184_().y < 0.0) {
                    fishy.m_20256_(fishy.m_20184_().multiply(1.0, 0.75, 1.0));
                }
                fishy.m_20124_(Pose.FALL_FLYING);
            }
            setBoostTicks(fishy, boostTime - 1);
        }
    }
}