package net.mehvahdjukaar.supplementaries.common.misc.explosion;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.util.ArrayList;
import net.mehvahdjukaar.moonlight.api.block.ILightable;
import net.mehvahdjukaar.moonlight.api.platform.ForgeHelper;
import net.mehvahdjukaar.supplementaries.common.block.blocks.BellowsBlock;
import net.mehvahdjukaar.supplementaries.integration.CompatObjects;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class GunpowderExplosion extends Explosion {

    private float radius2;

    public GunpowderExplosion(Level world, Entity entity, double x, double y, double z, float size) {
        super(world, entity, null, null, x, y, z, size, false, Explosion.BlockInteraction.DESTROY);
        this.radius2 = size;
    }

    @Override
    public void explode() {
        int px = Mth.floor(this.f_46013_);
        int py = Mth.floor(this.f_46014_);
        int pz = Mth.floor(this.f_46015_);
        this.radius2 *= 2.0F;
        ForgeHelper.onExplosionDetonate(this.f_46012_, this, new ArrayList(), (double) this.radius2);
        this.explodeBlock(px + 1, py, pz);
        this.explodeBlock(px - 1, py, pz);
        this.explodeBlock(px, py + 1, pz);
        this.explodeBlock(px, py - 1, pz);
        this.explodeBlock(px, py, pz + 1);
        this.explodeBlock(px, py, pz - 1);
        BlockPos pos = new BlockPos(px, py, pz);
        BlockState newFire = BaseFireBlock.getState(this.f_46012_, pos);
        BlockState s = this.f_46012_.getBlockState(pos);
        if ((s.m_247087_() || s.m_60713_((Block) ModRegistry.GUNPOWDER_BLOCK.get())) && (this.hasFlammableNeighbours(pos) || ForgeHelper.isFireSource(this.f_46012_.getBlockState(pos.below()), this.f_46012_, pos, Direction.UP) || newFire.m_60734_() != Blocks.FIRE)) {
            this.f_46012_.setBlockAndUpdate(pos, newFire);
        }
    }

    private boolean hasFlammableNeighbours(BlockPos pos) {
        for (Direction direction : Direction.values()) {
            BlockState state = this.f_46012_.getBlockState(pos.relative(direction));
            if (state.m_278200_() || state.m_60734_() == ModRegistry.BELLOWS.get() && (Integer) state.m_61143_(BellowsBlock.POWER) != 0 && state.m_61143_(BellowsBlock.FACING) == direction.getOpposite()) {
                return true;
            }
        }
        return false;
    }

    private void explodeBlock(int i, int j, int k) {
        BlockPos pos = new BlockPos(i, j, k);
        FluidState fluidstate = this.f_46012_.getFluidState(pos);
        if (fluidstate.getType() == Fluids.EMPTY) {
            BlockState state = this.f_46012_.getBlockState(pos);
            Block block = state.m_60734_();
            if (ForgeHelper.getExplosionResistance(state, this.f_46012_, pos, this) == 0.0F) {
                if (block instanceof TntBlock) {
                    this.getToBlow().add(pos);
                } else if (block == CompatObjects.NUKE_BLOCK.get()) {
                    igniteTntHack(this.f_46012_, pos, block);
                }
            }
            if (block instanceof ILightable lightable) {
                lightable.lightUp(null, state, pos, this.f_46012_, ILightable.FireSourceType.FLAMING_ARROW);
            } else if (canLight(state)) {
                this.f_46012_.setBlock(pos, (BlockState) state.m_61124_(BlockStateProperties.LIT, Boolean.TRUE), 11);
                ILightable.FireSourceType.FLAMING_ARROW.play(this.f_46012_, pos);
            }
        }
    }

    private static boolean canLight(BlockState state) {
        Block b = state.m_60734_();
        if (b instanceof AbstractCandleBlock) {
            return !AbstractCandleBlock.isLit(state);
        } else {
            return state.m_61138_(BlockStateProperties.LIT) && state.m_204336_(ModTags.LIGHTABLE_BY_GUNPOWDER) ? !(Boolean) state.m_61143_(BlockStateProperties.LIT) && (!state.m_61138_(BlockStateProperties.WATERLOGGED) || !(Boolean) state.m_61143_(BlockStateProperties.WATERLOGGED)) : false;
        }
    }

    public ObjectArrayList<BlockPos> getToBlow() {
        return (ObjectArrayList<BlockPos>) super.getToBlow();
    }

    @Override
    public void finalizeExplosion(boolean spawnFire) {
        ObjectArrayList<Pair<ItemStack, BlockPos>> drops = new ObjectArrayList();
        Util.shuffle(this.getToBlow(), this.f_46012_.random);
        ObjectListIterator pos = this.getToBlow().iterator();
        while (pos.hasNext()) {
            BlockPos blockpos = (BlockPos) pos.next();
            BlockState blockstate = this.f_46012_.getBlockState(blockpos);
            BlockPos immutable = blockpos.immutable();
            this.f_46012_.getProfiler().push("explosion_blocks");
            if (ForgeHelper.canDropFromExplosion(blockstate, this.f_46012_, blockpos, this) && this.f_46012_ instanceof ServerLevel serverLevel) {
                BlockEntity blockEntity = blockstate.m_155947_() ? this.f_46012_.getBlockEntity(blockpos) : null;
                LootParams.Builder builder = new LootParams.Builder(serverLevel).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos)).withParameter(LootContextParams.TOOL, ItemStack.EMPTY).withOptionalParameter(LootContextParams.BLOCK_ENTITY, blockEntity).withOptionalParameter(LootContextParams.THIS_ENTITY, null);
                builder.withParameter(LootContextParams.EXPLOSION_RADIUS, this.radius2);
                blockstate.m_287290_(builder).forEach(d -> m_46067_(drops, d, immutable));
            }
            ForgeHelper.onBlockExploded(blockstate, this.f_46012_, blockpos, this);
            this.f_46012_.getProfiler().pop();
        }
        pos = drops.iterator();
        while (pos.hasNext()) {
            Pair<ItemStack, BlockPos> pair = (Pair<ItemStack, BlockPos>) pos.next();
            Block.popResource(this.f_46012_, (BlockPos) pair.getSecond(), (ItemStack) pair.getFirst());
        }
        if (spawnFire) {
            BlockPos posx = BlockPos.containing(this.f_46013_, this.f_46014_, this.f_46015_);
            if (this.f_46012_.getBlockState(posx).m_60795_() && this.f_46012_.getBlockState(posx.below()).m_60804_(this.f_46012_, posx.below())) {
                this.f_46012_.setBlockAndUpdate(posx, BaseFireBlock.getState(this.f_46012_, posx));
            }
        }
    }

    public static void igniteTntHack(Level level, BlockPos blockpos, Block tnt) {
        Arrow dummyArrow = new Arrow(level, (double) blockpos.m_123341_() + 0.5, (double) blockpos.m_123342_() + 0.5, (double) blockpos.m_123343_() + 0.5);
        dummyArrow.m_20254_(20);
        BlockState old = level.getBlockState(blockpos);
        tnt.m_5581_(level, tnt.defaultBlockState(), new BlockHitResult(new Vec3(0.5, 0.5, 0.5), Direction.UP, blockpos, true), dummyArrow);
        level.setBlockAndUpdate(blockpos, old);
    }
}