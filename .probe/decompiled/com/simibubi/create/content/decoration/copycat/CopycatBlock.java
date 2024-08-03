package com.simibubi.create.content.decoration.copycat;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.model.data.ModelDataManager;

public abstract class CopycatBlock extends Block implements IBE<CopycatBlockEntity>, IWrenchable {

    public CopycatBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public <S extends BlockEntity> BlockEntityTicker<S> getTicker(Level level0, BlockState blockState1, BlockEntityType<S> blockEntityTypeS2) {
        return null;
    }

    @Override
    public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        this.onWrenched(state, context);
        return IWrenchable.super.onSneakWrenched(state, context);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        return this.onBlockEntityUse(context.getLevel(), context.getClickedPos(), ufte -> {
            ItemStack consumedItem = ufte.getConsumedItem();
            if (!ufte.hasCustomMaterial()) {
                return InteractionResult.PASS;
            } else {
                Player player = context.getPlayer();
                if (!player.isCreative()) {
                    player.getInventory().placeItemBackInInventory(consumedItem);
                }
                context.getLevel().m_46796_(2001, context.getClickedPos(), Block.getId(ufte.m_58900_()));
                ufte.setMaterial(AllBlocks.COPYCAT_BASE.getDefaultState());
                ufte.setConsumedItem(ItemStack.EMPTY);
                return InteractionResult.SUCCESS;
            }
        });
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        if (pPlayer == null) {
            return InteractionResult.PASS;
        } else {
            Direction face = pHit.getDirection();
            ItemStack itemInHand = pPlayer.m_21120_(pHand);
            BlockState materialIn = this.getAcceptedBlockState(pLevel, pPos, itemInHand, face);
            if (materialIn != null) {
                materialIn = this.prepareMaterial(pLevel, pPos, pState, pPlayer, pHand, pHit, materialIn);
            }
            if (materialIn == null) {
                return InteractionResult.PASS;
            } else {
                BlockState material = materialIn;
                return this.onBlockEntityUse(pLevel, pPos, ufte -> {
                    if (ufte.getMaterial().m_60713_(material.m_60734_())) {
                        if (!ufte.cycleMaterial()) {
                            return InteractionResult.PASS;
                        } else {
                            ufte.m_58904_().playSound(null, ufte.m_58899_(), SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.BLOCKS, 0.75F, 0.95F);
                            return InteractionResult.SUCCESS;
                        }
                    } else if (ufte.hasCustomMaterial()) {
                        return InteractionResult.PASS;
                    } else if (pLevel.isClientSide()) {
                        return InteractionResult.SUCCESS;
                    } else {
                        ufte.setMaterial(material);
                        ufte.setConsumedItem(itemInHand);
                        ufte.m_58904_().playSound(null, ufte.m_58899_(), material.m_60827_().getPlaceSound(), SoundSource.BLOCKS, 1.0F, 0.75F);
                        if (pPlayer.isCreative()) {
                            return InteractionResult.SUCCESS;
                        } else {
                            itemInHand.shrink(1);
                            if (itemInHand.isEmpty()) {
                                pPlayer.m_21008_(pHand, ItemStack.EMPTY);
                            }
                            return InteractionResult.SUCCESS;
                        }
                    }
                });
            }
        }
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        if (pPlacer != null) {
            ItemStack offhandItem = pPlacer.getItemInHand(InteractionHand.OFF_HAND);
            BlockState appliedState = this.getAcceptedBlockState(pLevel, pPos, offhandItem, Direction.orderedByNearest(pPlacer)[0]);
            if (appliedState != null) {
                this.withBlockEntityDo(pLevel, pPos, ufte -> {
                    if (!ufte.hasCustomMaterial()) {
                        ufte.setMaterial(appliedState);
                        ufte.setConsumedItem(offhandItem);
                        if (pPlacer instanceof Player player && player.isCreative()) {
                            return;
                        }
                        offhandItem.shrink(1);
                        if (offhandItem.isEmpty()) {
                            pPlacer.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                        }
                    }
                });
            }
        }
    }

    @Nullable
    public BlockState getAcceptedBlockState(Level pLevel, BlockPos pPos, ItemStack item, Direction face) {
        if (item.getItem() instanceof BlockItem bi) {
            Block block = bi.getBlock();
            if (block instanceof CopycatBlock) {
                return null;
            } else {
                BlockState appliedState = block.defaultBlockState();
                boolean hardCodedAllow = this.isAcceptedRegardless(appliedState);
                if (!AllTags.AllBlockTags.COPYCAT_ALLOW.matches(block) && !hardCodedAllow) {
                    if (AllTags.AllBlockTags.COPYCAT_DENY.matches(block)) {
                        return null;
                    }
                    if (block instanceof EntityBlock) {
                        return null;
                    }
                    if (block instanceof StairBlock) {
                        return null;
                    }
                    if (pLevel != null) {
                        VoxelShape shape = appliedState.m_60808_(pLevel, pPos);
                        if (shape.isEmpty() || !shape.bounds().equals(Shapes.block().bounds())) {
                            return null;
                        }
                        VoxelShape collisionShape = appliedState.m_60812_(pLevel, pPos);
                        if (collisionShape.isEmpty()) {
                            return null;
                        }
                    }
                }
                if (face != null) {
                    Direction.Axis axis = face.getAxis();
                    if (appliedState.m_61138_(BlockStateProperties.FACING)) {
                        appliedState = (BlockState) appliedState.m_61124_(BlockStateProperties.FACING, face);
                    }
                    if (appliedState.m_61138_(BlockStateProperties.HORIZONTAL_FACING) && axis != Direction.Axis.Y) {
                        appliedState = (BlockState) appliedState.m_61124_(BlockStateProperties.HORIZONTAL_FACING, face);
                    }
                    if (appliedState.m_61138_(BlockStateProperties.AXIS)) {
                        appliedState = (BlockState) appliedState.m_61124_(BlockStateProperties.AXIS, axis);
                    }
                    if (appliedState.m_61138_(BlockStateProperties.HORIZONTAL_AXIS) && axis != Direction.Axis.Y) {
                        appliedState = (BlockState) appliedState.m_61124_(BlockStateProperties.HORIZONTAL_AXIS, axis);
                    }
                }
                return appliedState;
            }
        } else {
            return null;
        }
    }

    public boolean isAcceptedRegardless(BlockState material) {
        return false;
    }

    public BlockState prepareMaterial(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer, InteractionHand pHand, BlockHitResult pHit, BlockState material) {
        return material;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if (pState.m_155947_() && pState.m_60734_() != pNewState.m_60734_()) {
            if (!pIsMoving) {
                this.withBlockEntityDo(pLevel, pPos, ufte -> Block.popResource(pLevel, pPos, ufte.getConsumedItem()));
            }
            pLevel.removeBlockEntity(pPos);
        }
    }

    @Override
    public void playerWillDestroy(Level pLevel, BlockPos pPos, BlockState pState, Player pPlayer) {
        super.playerWillDestroy(pLevel, pPos, pState, pPlayer);
        if (pPlayer.isCreative()) {
            this.withBlockEntityDo(pLevel, pPos, ufte -> ufte.setConsumedItem(ItemStack.EMPTY));
        }
    }

    @Override
    public Class<CopycatBlockEntity> getBlockEntityClass() {
        return CopycatBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CopycatBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CopycatBlockEntity>) AllBlockEntityTypes.COPYCAT.get();
    }

    @OnlyIn(Dist.CLIENT)
    public BlockState getAppearance(BlockState state, BlockAndTintGetter level, BlockPos pos, Direction side, BlockState queryState, BlockPos queryPos) {
        if (this.isIgnoredConnectivitySide(level, state, side, pos, queryPos)) {
            return state;
        } else {
            ModelDataManager modelDataManager = level.getModelDataManager();
            return modelDataManager == null ? getMaterial(level, pos) : CopycatModel.getMaterial(modelDataManager.getAt(pos));
        }
    }

    public boolean isIgnoredConnectivitySide(BlockAndTintGetter reader, BlockState state, Direction face, BlockPos fromPos, BlockPos toPos) {
        return false;
    }

    public abstract boolean canConnectTexturesToward(BlockAndTintGetter var1, BlockPos var2, BlockPos var3, BlockState var4);

    public static BlockState getMaterial(BlockGetter reader, BlockPos targetPos) {
        return reader.getBlockEntity(targetPos) instanceof CopycatBlockEntity cbe ? cbe.getMaterial() : Blocks.AIR.defaultBlockState();
    }

    public boolean canFaceBeOccluded(BlockState state, Direction face) {
        return false;
    }

    public boolean shouldFaceAlwaysRender(BlockState state, Direction face) {
        return false;
    }

    public SoundType getSoundType(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).m_60827_();
    }

    public float getFriction(BlockState state, LevelReader level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).getFriction(level, pos, entity);
    }

    public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
        return getMaterial(level, pos).getLightEmission(level, pos);
    }

    public boolean canHarvestBlock(BlockState state, BlockGetter level, BlockPos pos, Player player) {
        return getMaterial(level, pos).canHarvestBlock(level, pos, player);
    }

    public float getExplosionResistance(BlockState state, BlockGetter level, BlockPos pos, Explosion explosion) {
        return getMaterial(level, pos).getExplosionResistance(level, pos, explosion);
    }

    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter level, BlockPos pos, Player player) {
        BlockState material = getMaterial(level, pos);
        return !AllBlocks.COPYCAT_BASE.has(material) && (player == null || !player.m_6144_()) ? material.getCloneItemStack(target, level, pos, player) : new ItemStack(this);
    }

    public boolean addLandingEffects(BlockState state1, ServerLevel level, BlockPos pos, BlockState state2, LivingEntity entity, int numberOfParticles) {
        return getMaterial(level, pos).addLandingEffects(level, pos, state2, entity, numberOfParticles);
    }

    public boolean addRunningEffects(BlockState state, Level level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).addRunningEffects(level, pos, entity);
    }

    public float getEnchantPowerBonus(BlockState state, LevelReader level, BlockPos pos) {
        return getMaterial(level, pos).getEnchantPowerBonus(level, pos);
    }

    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return getMaterial(level, pos).canEntityDestroy(level, pos, entity);
    }

    public boolean isValidSpawn(BlockState state, BlockGetter level, BlockPos pos, SpawnPlacements.Type type, EntityType<?> entityType) {
        return false;
    }

    @Override
    public void fallOn(Level pLevel, BlockState pState, BlockPos pPos, Entity pEntity, float float0) {
        BlockState material = getMaterial(pLevel, pPos);
        material.m_60734_().fallOn(pLevel, material, pPos, pEntity, float0);
    }

    @Override
    public float getDestroyProgress(BlockState pState, Player pPlayer, BlockGetter pLevel, BlockPos pPos) {
        return getMaterial(pLevel, pPos).m_60625_(pPlayer, pLevel, pPos);
    }

    @OnlyIn(Dist.CLIENT)
    public static BlockColor wrappedColor() {
        return new CopycatBlock.WrappedBlockColor();
    }

    @OnlyIn(Dist.CLIENT)
    public static class WrappedBlockColor implements BlockColor {

        @Override
        public int getColor(BlockState pState, @Nullable BlockAndTintGetter pLevel, @Nullable BlockPos pPos, int pTintIndex) {
            return pLevel != null && pPos != null ? Minecraft.getInstance().getBlockColors().getColor(CopycatBlock.getMaterial(pLevel, pPos), pLevel, pPos, pTintIndex) : GrassColor.get(0.5, 1.0);
        }
    }
}