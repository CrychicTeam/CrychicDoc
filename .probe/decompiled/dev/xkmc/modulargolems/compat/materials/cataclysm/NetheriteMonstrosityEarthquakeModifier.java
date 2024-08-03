package dev.xkmc.modulargolems.compat.materials.cataclysm;

import com.github.L_Ender.cataclysm.config.CMConfig;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.content.modifier.base.AttributeGolemModifier;
import dev.xkmc.modulargolems.init.registrate.GolemTypes;
import java.util.function.Consumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.block.state.BlockState;

public class NetheriteMonstrosityEarthquakeModifier extends AttributeGolemModifier {

    public static final double RANGE = 5.0;

    public NetheriteMonstrosityEarthquakeModifier() {
        super(1, new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ATTACK, () -> 5.0), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_ARMOR, () -> 5.0), new AttributeGolemModifier.AttrEntry(GolemTypes.STAT_TOUGH, () -> 5.0));
    }

    @Override
    public void onRegisterFlag(Consumer<GolemFlags> cons) {
        cons.accept(GolemFlags.EARTH_QUAKE);
    }

    @Override
    public void handleEvent(AbstractGolemEntity<?, ?> golem, int value, byte event) {
        if (event == 83) {
            makeParticles(golem, 0.0F, 0.0F);
        }
    }

    public static void performEarthQuake(AbstractGolemEntity<?, ?> golem) {
        earthQuake(golem);
        golem.m_9236_().broadcastEntityEvent(golem, (byte) 83);
    }

    public static void earthQuake(LivingEntity le) {
        le.m_5496_(SoundEvents.GENERIC_EXPLODE, 1.5F, 1.0F + le.getRandom().nextFloat() * 0.1F);
        for (LivingEntity entity : le.m_9236_().m_45976_(LivingEntity.class, le.m_20191_().inflate(7.0))) {
            if (!le.m_7307_(entity) && entity != le) {
                float damage = (float) (le.getAttributeValue(Attributes.ATTACK_DAMAGE) + (double) entity.getMaxHealth() * CMConfig.MonstrositysHpdamage);
                boolean flag = entity.hurt(le.m_269291_().mobAttack(le), damage);
                if (flag) {
                    launch(le, entity);
                }
            }
        }
    }

    public static void makeParticles(LivingEntity le, float vec, float math) {
        if (le.m_9236_().isClientSide) {
            for (int i1 = 0; i1 < 80 + le.getRandom().nextInt(12); i1++) {
                double DeltaMovementX = le.getRandom().nextGaussian() * 0.07;
                double DeltaMovementY = le.getRandom().nextGaussian() * 0.07;
                double DeltaMovementZ = le.getRandom().nextGaussian() * 0.07;
                float f = Mth.cos(le.yBodyRot * (float) (Math.PI / 180.0));
                float f1 = Mth.sin(le.yBodyRot * (float) (Math.PI / 180.0));
                float angle = (float) (Math.PI / 180.0) * le.yBodyRot + (float) i1;
                double extraX = (double) (2.0F * Mth.sin((float) (Math.PI + (double) angle)));
                double extraY = 0.3F;
                double extraZ = (double) (2.0F * Mth.cos(angle));
                double theta = (double) le.yBodyRot * (Math.PI / 180.0);
                double vecX = Math.cos(++theta);
                double vecZ = Math.sin(theta);
                int hitX = Mth.floor(le.m_20185_() + (double) vec * vecX + extraX);
                int hitY = Mth.floor(le.m_20186_());
                int hitZ = Mth.floor(le.m_20189_() + (double) vec * vecZ + extraZ);
                BlockPos hit = new BlockPos(hitX, hitY, hitZ);
                BlockState block = le.m_9236_().getBlockState(hit.below());
                le.m_9236_().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, block), le.m_20185_() + (double) vec * vecX + extraX + (double) (f * math), le.m_20186_() + extraY, le.m_20189_() + (double) vec * vecZ + extraZ + (double) (f1 * math), DeltaMovementX, DeltaMovementY, DeltaMovementZ);
            }
        }
    }

    private static void launch(LivingEntity le, Entity e) {
        double d0 = e.getX() - le.m_20185_();
        double d1 = e.getZ() - le.m_20189_();
        double d2 = Math.max(d0 * d0 + d1 * d1, 0.001);
        float f = 2.0F;
        e.push(d0 / d2 * (double) f, 0.75, d1 / d2 * (double) f);
    }
}