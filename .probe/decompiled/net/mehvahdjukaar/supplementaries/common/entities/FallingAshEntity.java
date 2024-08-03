package net.mehvahdjukaar.supplementaries.common.entities;

import net.mehvahdjukaar.moonlight.api.entity.ImprovedFallingBlockEntity;
import net.mehvahdjukaar.supplementaries.common.block.blocks.AshLayerBlock;
import net.mehvahdjukaar.supplementaries.reg.ModEntities;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class FallingAshEntity extends ImprovedFallingBlockEntity {

    public FallingAshEntity(EntityType<FallingAshEntity> type, Level level) {
        super(type, level);
    }

    public FallingAshEntity(Level level) {
        super((EntityType<? extends FallingBlockEntity>) ModEntities.FALLING_ASH.get(), level);
    }

    public FallingAshEntity(Level level, BlockPos pos, BlockState blockState) {
        super((EntityType<? extends FallingBlockEntity>) ModEntities.FALLING_ASH.get(), level, pos, blockState, false);
    }

    public static FallingAshEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingAshEntity entity = new FallingAshEntity(level, pos, state);
        level.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
        level.m_7967_(entity);
        return entity;
    }

    @Nullable
    @Override
    public ItemEntity spawnAtLocation(ItemLike pItem) {
        this.dropBlockContent(this.m_31980_(), this.m_20183_());
        return null;
    }

    @Override
    public void tick() {
        Level level = this.m_9236_();
        if (level.isClientSide) {
            super.m_8119_();
        } else {
            BlockState blockState = this.m_31980_();
            if (!blockState.m_60713_((Block) ModRegistry.ASH_BLOCK.get())) {
                this.m_146870_();
            } else {
                Block block = blockState.m_60734_();
                if (!this.m_20068_()) {
                    this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
                }
                this.m_6478_(MoverType.SELF, this.m_20184_());
                BlockPos pos = this.m_20183_();
                if (level.getFluidState(pos).is(FluidTags.WATER)) {
                    this.m_146870_();
                    return;
                }
                if (this.m_20184_().lengthSqr() > 1.0) {
                    BlockHitResult blockhitresult = level.m_45547_(new ClipContext(new Vec3(this.f_19854_, this.f_19855_, this.f_19856_), this.m_20182_(), ClipContext.Block.COLLIDER, ClipContext.Fluid.SOURCE_ONLY, this));
                    if (blockhitresult.getType() != HitResult.Type.MISS && level.getFluidState(blockhitresult.getBlockPos()).is(FluidTags.WATER)) {
                        this.m_146870_();
                        return;
                    }
                }
                if (!this.m_20096_()) {
                    if (this.f_31942_ > 100 && (pos.m_123342_() <= level.m_141937_() || pos.m_123342_() > level.m_151558_()) || this.f_31942_ > 600) {
                        if (this.f_31943_ && level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.spawnAtLocation(block);
                        }
                        this.m_146870_();
                    }
                } else {
                    BlockState onState = level.getBlockState(pos);
                    this.m_20256_(this.m_20184_().multiply(0.7, -0.5, 0.7));
                    if (!onState.m_60713_(Blocks.MOVING_PISTON)) {
                        boolean canBeReplaced = onState.m_60629_(new DirectionalPlaceContext(level, pos, Direction.DOWN, new ItemStack(blockState.m_60734_().asItem()), Direction.UP));
                        boolean isFree = isFree(level.getBlockState(pos.below()));
                        boolean canSurvive = blockState.m_60710_(level, pos) && !isFree;
                        if (canBeReplaced && canSurvive) {
                            int remaining = 0;
                            if (onState.m_60713_(blockState.m_60734_())) {
                                int layers = (Integer) blockState.m_61143_(AshLayerBlock.LAYERS);
                                int toLayers = (Integer) onState.m_61143_(AshLayerBlock.LAYERS);
                                int total = layers + toLayers;
                                int target = Mth.clamp(total, 1, 8);
                                remaining = total - target;
                                blockState = (BlockState) blockState.m_61124_(AshLayerBlock.LAYERS, target);
                            }
                            if (level.setBlock(pos, blockState, 3)) {
                                ((ServerLevel) level).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(pos, level.getBlockState(pos)));
                                if (block instanceof Fallable fallable) {
                                    fallable.onLand(level, pos, blockState, onState, this);
                                }
                                this.m_146870_();
                                if (remaining != 0) {
                                    BlockPos above = pos.above();
                                    blockState = (BlockState) blockState.m_61124_(AshLayerBlock.LAYERS, remaining);
                                    if (level.getBlockState(above).m_247087_() && !level.setBlock(above, blockState, 3)) {
                                        ((ServerLevel) level).getChunkSource().chunkMap.broadcast(this, new ClientboundBlockUpdatePacket(above, level.getBlockState(above)));
                                        this.dropBlockContent(blockState, pos);
                                    }
                                }
                                return;
                            }
                        }
                        this.m_146870_();
                        if (level.getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.m_149650_(block, pos);
                            this.dropBlockContent(blockState, pos);
                        }
                    }
                }
                this.m_20256_(this.m_20184_().scale(0.98));
            }
        }
    }

    public static boolean isFree(BlockState pState) {
        return pState.m_60795_() || pState.m_204336_(BlockTags.FIRE) || pState.m_278721_() || pState.m_247087_() && !(pState.m_60734_() instanceof AshLayerBlock);
    }

    private void dropBlockContent(BlockState state, BlockPos pos) {
        Block.dropResources(state, this.m_9236_(), pos, null, null, ItemStack.EMPTY);
        this.m_9236_().m_5898_(null, 2001, pos, Block.getId(state));
    }
}