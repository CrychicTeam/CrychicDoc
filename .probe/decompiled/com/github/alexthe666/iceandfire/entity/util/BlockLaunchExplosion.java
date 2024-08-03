package com.github.alexthe666.iceandfire.entity.util;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class BlockLaunchExplosion extends Explosion {

    private final float size;

    private final Level world;

    private final double x;

    private final double y;

    private final double z;

    private final Explosion.BlockInteraction mode;

    public BlockLaunchExplosion(Level world, Mob entity, double x, double y, double z, float size) {
        this(world, entity, x, y, z, size, Explosion.BlockInteraction.DESTROY);
    }

    public BlockLaunchExplosion(Level world, Mob entity, double x, double y, double z, float size, Explosion.BlockInteraction mode) {
        this(world, entity, null, x, y, z, size, mode);
    }

    public BlockLaunchExplosion(Level world, Mob entity, DamageSource source, double x, double y, double z, float size, Explosion.BlockInteraction mode) {
        super(world, entity, source, null, x, y, z, size, false, mode);
        this.world = world;
        this.size = size;
        this.x = x;
        this.y = y;
        this.z = z;
        this.mode = mode;
    }

    private static void handleExplosionDrops(ObjectArrayList<Pair<ItemStack, BlockPos>> dropPositionArray, ItemStack stack, BlockPos pos) {
        int i = dropPositionArray.size();
        for (int j = 0; j < i; j++) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) dropPositionArray.get(j);
            ItemStack itemstack = (ItemStack) pair.getFirst();
            if (ItemEntity.areMergable(itemstack, stack)) {
                ItemStack itemstack1 = ItemEntity.merge(itemstack, stack, 16);
                dropPositionArray.set(j, Pair.of(itemstack1, (BlockPos) pair.getSecond()));
                if (stack.isEmpty()) {
                    return;
                }
            }
        }
        dropPositionArray.add(Pair.of(stack, pos));
    }

    @Override
    public void finalizeExplosion(boolean spawnParticles) {
        if (this.world.isClientSide) {
            this.world.playLocalSound(this.x, this.y, this.z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 4.0F, (1.0F + (this.world.random.nextFloat() - this.world.random.nextFloat()) * 0.2F) * 0.7F, false);
        }
        boolean flag = this.mode != Explosion.BlockInteraction.KEEP;
        if (spawnParticles) {
            if (!(this.size < 2.0F) && flag) {
                this.world.addParticle(ParticleTypes.EXPLOSION_EMITTER, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            } else {
                this.world.addParticle(ParticleTypes.EXPLOSION, this.x, this.y, this.z, 1.0, 0.0, 0.0);
            }
        }
        if (flag) {
            ObjectArrayList<Pair<ItemStack, BlockPos>> objectarraylist = new ObjectArrayList();
            Collections.shuffle(this.m_46081_(), ThreadLocalRandom.current());
            for (BlockPos blockpos : this.m_46081_()) {
                BlockState blockstate = this.world.getBlockState(blockpos);
                if (!blockstate.m_60795_()) {
                    BlockPos blockpos1 = blockpos.immutable();
                    this.world.getProfiler().push("explosion_blocks");
                    Vec3 Vector3d = new Vec3(this.x, this.y, this.z);
                    blockstate.onBlockExploded(this.world, blockpos, this);
                    FallingBlockEntity fallingBlockEntity = new FallingBlockEntity(EntityType.FALLING_BLOCK, this.world);
                    fallingBlockEntity.setStartPos(blockpos1);
                    fallingBlockEntity.m_6034_((double) blockpos1.m_123341_() + 0.5, (double) blockpos1.m_123342_() + 0.5, (double) blockpos1.m_123343_() + 0.5);
                    double d5 = fallingBlockEntity.m_20185_() - this.x;
                    double d7 = fallingBlockEntity.m_20188_() - this.y;
                    double d9 = fallingBlockEntity.m_20189_() - this.z;
                    float f3 = this.size * 2.0F;
                    double d12 = Math.sqrt(fallingBlockEntity.m_20238_(Vector3d)) / (double) f3;
                    double d14 = (double) m_46064_(Vector3d, fallingBlockEntity);
                    double d11 = (1.0 - d12) * d14;
                    fallingBlockEntity.m_20256_(fallingBlockEntity.m_20184_().add(d5 * d11, d7 * d11, d9 * d11));
                    this.world.getProfiler().pop();
                }
            }
            ObjectListIterator var23 = objectarraylist.iterator();
            while (var23.hasNext()) {
                Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) var23.next();
                Block.popResource(this.world, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
            }
        }
    }
}