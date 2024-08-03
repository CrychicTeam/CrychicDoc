package com.github.alexmodguy.alexscaves.server.entity.item;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.GuanoLayerBlock;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Fallable;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.PlayMessages;

public class FallingGuanoEntity extends FallingBlockEntity {

    private BlockState guanoState = ACBlockRegistry.GUANO_LAYER.get().defaultBlockState();

    public FallingGuanoEntity(EntityType entityType, Level level) {
        super(entityType, level);
    }

    public FallingGuanoEntity(PlayMessages.SpawnEntity spawnEntity, Level level) {
        this(ACEntityRegistry.FALLING_GUANO.get(), level);
        this.m_20011_(this.m_142242_());
    }

    private FallingGuanoEntity(Level level, double x, double y, double z, BlockState state) {
        this(ACEntityRegistry.FALLING_GUANO.get(), level);
        this.guanoState = state;
        this.f_19850_ = true;
        this.m_6034_(x, y, z);
        this.m_20256_(Vec3.ZERO);
        this.f_19854_ = x;
        this.f_19855_ = y;
        this.f_19856_ = z;
        this.m_31959_(this.m_20183_());
    }

    public static FallingGuanoEntity fall(Level level, BlockPos pos, BlockState state) {
        FallingGuanoEntity fallingblockentity = new FallingGuanoEntity(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_(), (double) pos.m_123343_() + 0.5, state.m_61138_(BlockStateProperties.WATERLOGGED) ? (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, false) : state);
        level.setBlock(pos, state.m_60819_().createLegacyBlock(), 3);
        level.m_7967_(fallingblockentity);
        return fallingblockentity;
    }

    @Override
    public void tick() {
        if (this.getBlockState().m_60795_()) {
            this.m_146870_();
        } else {
            Block block = this.getBlockState().m_60734_();
            this.f_31942_++;
            if (!this.m_20068_()) {
                this.m_20256_(this.m_20184_().add(0.0, -0.04, 0.0));
            }
            this.m_6478_(MoverType.SELF, this.m_20184_());
            if (!this.m_9236_().isClientSide) {
                BlockPos blockpos = this.m_20183_();
                if (!this.m_20096_()) {
                    if (!this.m_9236_().isClientSide && (this.f_31942_ > 100 && (blockpos.m_123342_() <= this.m_9236_().m_141937_() || blockpos.m_123342_() > this.m_9236_().m_151558_()) || this.f_31942_ > 600)) {
                        if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                            this.m_19998_(block);
                        }
                        this.m_146870_();
                    }
                } else {
                    BlockState blockstate = this.m_9236_().getBlockState(blockpos);
                    this.m_20256_(this.m_20184_().multiply(0.7, -0.5, 0.7));
                    if (blockstate.m_60713_(Blocks.MOVING_PISTON)) {
                        this.m_146870_();
                        this.m_149650_(block, blockpos);
                    } else {
                        boolean flag2 = blockstate.m_60629_(new DirectionalPlaceContext(this.m_9236_(), blockpos, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
                        boolean flag3 = FallingBlock.isFree(this.m_9236_().getBlockState(blockpos.below()));
                        boolean flag4 = this.guanoState.m_60710_(this.m_9236_(), blockpos) && (!flag3 || this.m_9236_().getBlockState(blockpos.below()).m_60713_(ACBlockRegistry.GUANO_LAYER.get()));
                        BlockState setState = this.getBlockState();
                        BlockState setAboveState = null;
                        if (blockstate.m_60713_(ACBlockRegistry.GUANO_LAYER.get())) {
                            flag2 = true;
                            flag4 = true;
                            int belowLayers = (Integer) blockstate.m_61143_(GuanoLayerBlock.f_56581_);
                            int fallingLayers = this.guanoState.m_60713_(ACBlockRegistry.GUANO_LAYER.get()) ? (Integer) this.guanoState.m_61143_(GuanoLayerBlock.f_56581_) : 8;
                            int together = belowLayers + fallingLayers;
                            setState = (BlockState) ACBlockRegistry.GUANO_LAYER.get().defaultBlockState().m_61124_(GuanoLayerBlock.f_56581_, Math.min(together, 8));
                            if (together > 8) {
                                int prev = 0;
                                if (this.m_9236_().getBlockState(blockpos.above()).m_60713_(ACBlockRegistry.GUANO_LAYER.get())) {
                                    prev = (Integer) this.m_9236_().getBlockState(blockpos.above()).m_61143_(GuanoLayerBlock.f_56581_);
                                }
                                setAboveState = (BlockState) ACBlockRegistry.GUANO_LAYER.get().defaultBlockState().m_61124_(GuanoLayerBlock.f_56581_, Math.min(together - 8 + prev, 8));
                            }
                        }
                        if (flag2 && flag4) {
                            boolean flag5 = false;
                            if (this.m_9236_().setBlockAndUpdate(blockpos, setState)) {
                                this.m_146870_();
                                if (block instanceof Fallable) {
                                    ((Fallable) block).onLand(this.m_9236_(), blockpos, setState, blockstate, this);
                                }
                                flag5 = true;
                            }
                            if (setAboveState != null) {
                                BlockPos abovePos = blockpos.above();
                                BlockState aboveState = this.m_9236_().getBlockState(abovePos);
                                if (aboveState.m_60629_(new DirectionalPlaceContext(this.m_9236_(), abovePos, Direction.DOWN, ItemStack.EMPTY, Direction.UP))) {
                                    this.m_9236_().setBlockAndUpdate(abovePos, setAboveState);
                                } else {
                                    this.m_19998_(block);
                                }
                                this.m_146870_();
                                flag5 = true;
                            }
                            if (!flag5 && this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.m_146870_();
                                this.m_149650_(block, blockpos);
                                this.m_19998_(block);
                            }
                        } else {
                            this.m_146870_();
                            if (this.f_31943_ && this.m_9236_().getGameRules().getBoolean(GameRules.RULE_DOENTITYDROPS)) {
                                this.m_149650_(block, blockpos);
                                this.m_19998_(block);
                            }
                        }
                    }
                }
            }
            this.m_20256_(this.m_20184_().scale(0.98));
        }
    }

    private boolean isFullGuanoBlock(BlockState state) {
        return state.m_60713_(ACBlockRegistry.GUANO_LAYER.get()) && (Integer) state.m_61143_(GuanoLayerBlock.f_56581_) == 8;
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("BlockState", NbtUtils.writeBlockState(this.guanoState));
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.guanoState = NbtUtils.readBlockState(this.m_9236_().m_246945_(Registries.BLOCK), tag.getCompound("BlockState"));
    }

    @Override
    public BlockState getBlockState() {
        return this.guanoState;
    }

    @Override
    public void recreateFromPacket(ClientboundAddEntityPacket clientboundAddEntityPacket) {
        super.recreateFromPacket(clientboundAddEntityPacket);
        this.guanoState = Block.stateById(clientboundAddEntityPacket.getData());
    }
}