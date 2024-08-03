package com.simibubi.create.content.contraptions.chassis;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.Tags;

public abstract class AbstractChassisBlock extends RotatedPillarBlock implements IWrenchable, IBE<ChassisBlockEntity>, ITransformableBlock {

    public AbstractChassisBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.PASS;
        } else {
            ItemStack heldItem = player.m_21120_(handIn);
            boolean isSlimeBall = heldItem.is(Tags.Items.SLIMEBALLS) || AllItems.SUPER_GLUE.isIn(heldItem);
            BooleanProperty affectedSide = this.getGlueableSide(state, hit.getDirection());
            if (affectedSide == null) {
                return InteractionResult.PASS;
            } else if (isSlimeBall && (Boolean) state.m_61143_(affectedSide)) {
                for (Direction face : Iterate.directions) {
                    BooleanProperty glueableSide = this.getGlueableSide(state, face);
                    if (glueableSide != null && !(Boolean) state.m_61143_(glueableSide) && this.glueAllowedOnSide(worldIn, pos, state, face)) {
                        if (worldIn.isClientSide) {
                            Vec3 vec = hit.m_82450_();
                            worldIn.addParticle(ParticleTypes.ITEM_SLIME, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
                            return InteractionResult.SUCCESS;
                        }
                        AllSoundEvents.SLIME_ADDED.playOnServer(worldIn, pos, 0.5F, 1.0F);
                        state = (BlockState) state.m_61124_(glueableSide, true);
                    }
                }
                if (!worldIn.isClientSide) {
                    worldIn.setBlockAndUpdate(pos, state);
                }
                return InteractionResult.SUCCESS;
            } else if ((!heldItem.isEmpty() || !player.m_6144_()) && !isSlimeBall) {
                return InteractionResult.PASS;
            } else if ((Boolean) state.m_61143_(affectedSide) == isSlimeBall) {
                return InteractionResult.PASS;
            } else if (!this.glueAllowedOnSide(worldIn, pos, state, hit.getDirection())) {
                return InteractionResult.PASS;
            } else if (worldIn.isClientSide) {
                Vec3 vec = hit.m_82450_();
                worldIn.addParticle(ParticleTypes.ITEM_SLIME, vec.x, vec.y, vec.z, 0.0, 0.0, 0.0);
                return InteractionResult.SUCCESS;
            } else {
                AllSoundEvents.SLIME_ADDED.playOnServer(worldIn, pos, 0.5F, 1.0F);
                worldIn.setBlockAndUpdate(pos, (BlockState) state.m_61124_(affectedSide, isSlimeBall));
                return InteractionResult.SUCCESS;
            }
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        if (rotation == Rotation.NONE) {
            return state;
        } else {
            BlockState rotated = super.rotate(state, rotation);
            for (Direction face : Iterate.directions) {
                BooleanProperty glueableSide = this.getGlueableSide(rotated, face);
                if (glueableSide != null) {
                    rotated = (BlockState) rotated.m_61124_(glueableSide, false);
                }
            }
            for (Direction facex : Iterate.directions) {
                BooleanProperty glueableSide = this.getGlueableSide(state, facex);
                if (glueableSide != null && (Boolean) state.m_61143_(glueableSide)) {
                    Direction rotatedFacing = rotation.rotate(facex);
                    BooleanProperty rotatedGlueableSide = this.getGlueableSide(rotated, rotatedFacing);
                    if (rotatedGlueableSide != null) {
                        rotated = (BlockState) rotated.m_61124_(rotatedGlueableSide, true);
                    }
                }
            }
            return rotated;
        }
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        if (mirrorIn == Mirror.NONE) {
            return state;
        } else {
            BlockState mirrored = state;
            for (Direction face : Iterate.directions) {
                BooleanProperty glueableSide = this.getGlueableSide(mirrored, face);
                if (glueableSide != null) {
                    mirrored = (BlockState) mirrored.m_61124_(glueableSide, false);
                }
            }
            for (Direction facex : Iterate.directions) {
                BooleanProperty glueableSide = this.getGlueableSide(state, facex);
                if (glueableSide != null && (Boolean) state.m_61143_(glueableSide)) {
                    Direction mirroredFacing = mirrorIn.mirror(facex);
                    BooleanProperty mirroredGlueableSide = this.getGlueableSide(mirrored, mirroredFacing);
                    if (mirroredGlueableSide != null) {
                        mirrored = (BlockState) mirrored.m_61124_(mirroredGlueableSide, true);
                    }
                }
            }
            return mirrored;
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.mirror(state, transform.mirror);
        }
        return transform.rotationAxis == Direction.Axis.Y ? this.rotate(state, transform.rotation) : this.transformInner(state, transform);
    }

    protected BlockState transformInner(BlockState state, StructureTransform transform) {
        if (transform.rotation == Rotation.NONE) {
            return state;
        } else {
            BlockState rotated = (BlockState) state.m_61124_(f_55923_, transform.rotateAxis((Direction.Axis) state.m_61143_(f_55923_)));
            AbstractChassisBlock block = (AbstractChassisBlock) state.m_60734_();
            for (Direction face : Iterate.directions) {
                BooleanProperty glueableSide = block.getGlueableSide(rotated, face);
                if (glueableSide != null) {
                    rotated = (BlockState) rotated.m_61124_(glueableSide, false);
                }
            }
            for (Direction facex : Iterate.directions) {
                BooleanProperty glueableSide = block.getGlueableSide(state, facex);
                if (glueableSide != null && (Boolean) state.m_61143_(glueableSide)) {
                    Direction rotatedFacing = transform.rotateFacing(facex);
                    BooleanProperty rotatedGlueableSide = block.getGlueableSide(rotated, rotatedFacing);
                    if (rotatedGlueableSide != null) {
                        rotated = (BlockState) rotated.m_61124_(rotatedGlueableSide, true);
                    }
                }
            }
            return rotated;
        }
    }

    public abstract BooleanProperty getGlueableSide(BlockState var1, Direction var2);

    protected boolean glueAllowedOnSide(BlockGetter world, BlockPos pos, BlockState state, Direction side) {
        return true;
    }

    @Override
    public Class<ChassisBlockEntity> getBlockEntityClass() {
        return ChassisBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends ChassisBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ChassisBlockEntity>) AllBlockEntityTypes.CHASSIS.get();
    }
}