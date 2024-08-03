package org.violetmoon.quark.content.tools.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.content.tools.module.TorchArrowModule;

public class TorchArrow extends AbstractArrow {

    public TorchArrow(EntityType<TorchArrow> type, Level level) {
        super(type, level);
    }

    public TorchArrow(Level level, double x, double y, double z) {
        super(TorchArrowModule.torchArrowType, x, y, z, level);
    }

    public TorchArrow(Level level, LivingEntity shooter) {
        super(TorchArrowModule.torchArrowType, shooter, level);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.f_36703_ && this.m_9236_().isClientSide && this.f_19797_ > 2) {
            Vec3 motion = this.m_20184_();
            double rs = 0.03;
            double ms = 0.08;
            double sprd = 0.1;
            int parts = 6;
            for (int i = 0; i < parts; i++) {
                double px = this.m_20185_() - motion.x * (double) ((float) i / (float) parts) + (Math.random() - 0.5) * sprd;
                double py = this.m_20186_() - motion.y * (double) ((float) i / (float) parts) + (Math.random() - 0.5) * sprd;
                double pz = this.m_20189_() - motion.z * (double) ((float) i / (float) parts) + (Math.random() - 0.5) * sprd;
                double mx = (Math.random() - 0.5) * rs - motion.x * ms;
                double my = (Math.random() - 0.5) * rs - motion.y * ms;
                double mz = (Math.random() - 0.5) * rs - motion.z * ms;
                this.m_9236_().addParticle(ParticleTypes.FLAME, px, py, pz, mx, my, mz);
            }
        }
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        if (!this.m_9236_().isClientSide) {
            BlockPos pos = result.getBlockPos();
            Direction direction = result.getDirection();
            BlockPos finalPos = pos.relative(direction);
            BlockState state = this.m_9236_().getBlockState(finalPos);
            if ((state.m_60795_() || state.m_247087_()) && direction != Direction.DOWN) {
                if (this.m_19749_() instanceof Player p && !Quark.FLAN_INTEGRATION.canPlace(p, finalPos)) {
                    return;
                }
                BlockState setState;
                if (direction == Direction.UP) {
                    setState = Blocks.TORCH.defaultBlockState();
                } else {
                    setState = (BlockState) Blocks.WALL_TORCH.defaultBlockState().m_61124_(WallTorchBlock.FACING, direction);
                }
                if (setState.m_60710_(this.m_9236_(), finalPos)) {
                    this.m_9236_().setBlock(finalPos, setState, 2);
                    this.m_216990_(setState.m_60827_().getPlaceSound());
                    this.m_146870_();
                    return;
                }
            }
        }
        super.onHitBlock(result);
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        this.m_20254_(1);
        super.onHitEntity(result);
        this.m_20254_(0);
    }

    @NotNull
    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(TorchArrowModule.extinguishOnMiss ? Items.ARROW : TorchArrowModule.torch_arrow);
    }
}