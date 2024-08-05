package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import java.util.List;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.entity.PartEntity;

public class VoidArrowFeature implements OnHitFeature {

    @Override
    public void onHitEntity(GenericArrowEntity genericArrow, Entity target, EntityHitResult hit) {
        target.hurt(target.level().damageSources().fellOutOfWorld(), Float.MAX_VALUE);
        genericArrow.m_146870_();
        if (target instanceof LivingEntity le) {
            this.onHitLivingEntity(genericArrow, le, hit);
        } else if (target instanceof PartEntity<?> part) {
            this.onHitEntity(genericArrow, part.getParent(), hit);
        }
    }

    @Override
    public void onHitLivingEntity(GenericArrowEntity genericArrow, LivingEntity target, EntityHitResult hit) {
        if (target.isAlive()) {
            target.setHealth(0.0F);
        }
        if (target.isAlive()) {
            target.kill();
        }
        if (target.isAlive()) {
            target.m_146870_();
        }
    }

    @Override
    public void postHurtEntity(GenericArrowEntity genericArrow, LivingEntity target) {
    }

    @Override
    public void onHitBlock(GenericArrowEntity genericArrow, BlockHitResult result) {
        genericArrow.m_9236_().setBlockAndUpdate(result.getBlockPos(), Blocks.AIR.defaultBlockState());
        genericArrow.m_146870_();
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
    }
}