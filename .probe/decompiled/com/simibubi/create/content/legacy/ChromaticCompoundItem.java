package com.simibubi.create.content.legacy;

import com.simibubi.create.AllItems;
import com.simibubi.create.content.kinetics.belt.behaviour.TransportedItemStackHandlerBehaviour;
import com.simibubi.create.content.kinetics.belt.transport.TransportedItemStack;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.VecHelper;
import com.simibubi.create.infrastructure.config.AllConfigs;
import com.simibubi.create.infrastructure.config.CRecipes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ChromaticCompoundItem extends Item {

    public ChromaticCompoundItem(Item.Properties properties) {
        super(properties);
    }

    public int getLight(ItemStack stack) {
        return stack.getOrCreateTag().getInt("CollectingLight");
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        return this.getLight(stack) > 0;
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return Math.round(13.0F * (float) this.getLight(stack) / (float) AllConfigs.server().recipes.lightSourceCountForRefinedRadiance.get().intValue());
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return Color.mixColors(4275305, 16777215, (float) this.getLight(stack) / (float) AllConfigs.server().recipes.lightSourceCountForRefinedRadiance.get().intValue());
    }

    public int getMaxStackSize(ItemStack stack) {
        return this.isBarVisible(stack) ? 1 : 16;
    }

    public boolean onEntityItemUpdate(ItemStack stack, ItemEntity entity) {
        Level world = entity.m_9236_();
        CompoundTag itemData = entity.getItem().getOrCreateTag();
        Vec3 positionVec = entity.m_20182_();
        CRecipes config = AllConfigs.server().recipes;
        if (world.isClientSide) {
            int light = itemData.getInt("CollectingLight");
            if (world.random.nextInt(config.lightSourceCountForRefinedRadiance.get() + 20) < light) {
                Vec3 start = VecHelper.offsetRandomly(positionVec, world.random, 3.0F);
                Vec3 motion = positionVec.subtract(start).normalize().scale(0.2F);
                world.addParticle(ParticleTypes.END_ROD, start.x, start.y, start.z, motion.x, motion.y, motion.z);
            }
            return false;
        } else {
            double y = entity.m_20186_();
            double yMotion = entity.m_20184_().y;
            int minHeight = world.m_141937_();
            CompoundTag data = entity.getPersistentData();
            if (y < (double) minHeight && y - yMotion < (double) (-10 + minHeight) && config.enableShadowSteelRecipe.get()) {
                ItemStack newStack = AllItems.SHADOW_STEEL.asStack();
                newStack.setCount(stack.getCount());
                data.putBoolean("JustCreated", true);
                entity.setItem(newStack);
            }
            if (!config.enableRefinedRadianceRecipe.get()) {
                return false;
            } else if (itemData.getInt("CollectingLight") >= config.lightSourceCountForRefinedRadiance.get()) {
                ItemStack newStack = AllItems.REFINED_RADIANCE.asStack();
                ItemEntity newEntity = new ItemEntity(world, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), newStack);
                newEntity.m_20256_(entity.m_20184_());
                newEntity.getPersistentData().putBoolean("JustCreated", true);
                itemData.remove("CollectingLight");
                world.m_7967_(newEntity);
                stack.split(1);
                entity.setItem(stack);
                if (stack.isEmpty()) {
                    entity.m_146870_();
                }
                return false;
            } else {
                boolean isOverBeacon = false;
                int entityX = Mth.floor(entity.m_20185_());
                int entityZ = Mth.floor(entity.m_20189_());
                int localWorldHeight = world.getHeight(Heightmap.Types.WORLD_SURFACE, entityX, entityZ);
                BlockPos.MutableBlockPos testPos = new BlockPos.MutableBlockPos(entityX, Math.min(Mth.floor(entity.m_20186_()), localWorldHeight), entityZ);
                while (testPos.m_123342_() > 0) {
                    testPos.move(Direction.DOWN);
                    BlockState state = world.getBlockState(testPos);
                    if (state.m_60739_(world, testPos) >= 15 && state.m_60734_() != Blocks.BEDROCK) {
                        break;
                    }
                    if (state.m_60734_() == Blocks.BEACON) {
                        if (world.getBlockEntity(testPos) instanceof BeaconBlockEntity bte && !bte.beamSections.isEmpty()) {
                            isOverBeacon = true;
                        }
                        break;
                    }
                }
                if (isOverBeacon) {
                    ItemStack newStack = AllItems.REFINED_RADIANCE.asStack();
                    newStack.setCount(stack.getCount());
                    data.putBoolean("JustCreated", true);
                    entity.setItem(newStack);
                    return false;
                } else {
                    RandomSource r = world.random;
                    int range = 3;
                    float rate = 0.5F;
                    if (r.nextFloat() > rate) {
                        return false;
                    } else {
                        BlockPos randomOffset = BlockPos.containing(VecHelper.offsetRandomly(positionVec, r, (float) range));
                        BlockState statex = world.getBlockState(randomOffset);
                        TransportedItemStackHandlerBehaviour behaviour = BlockEntityBehaviour.get(world, randomOffset, TransportedItemStackHandlerBehaviour.TYPE);
                        if (behaviour == null) {
                            if (this.checkLight(stack, entity, world, itemData, positionVec, randomOffset, statex)) {
                                world.m_46961_(randomOffset, false);
                            }
                            return false;
                        } else {
                            MutableBoolean success = new MutableBoolean(false);
                            behaviour.handleProcessingOnAllItems(ts -> {
                                ItemStack heldStack = ts.stack;
                                if (!(heldStack.getItem() instanceof BlockItem)) {
                                    return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                                } else {
                                    BlockItem blockItem = (BlockItem) heldStack.getItem();
                                    if (blockItem.getBlock() == null) {
                                        return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                                    } else {
                                        BlockState stateToCheck = blockItem.getBlock().defaultBlockState();
                                        if (!success.getValue() && this.checkLight(stack, entity, world, itemData, positionVec, randomOffset, stateToCheck)) {
                                            success.setTrue();
                                            if (ts.stack.getCount() == 1) {
                                                return TransportedItemStackHandlerBehaviour.TransportedResult.removeItem();
                                            } else {
                                                TransportedItemStack left = ts.copy();
                                                left.stack.shrink(1);
                                                return TransportedItemStackHandlerBehaviour.TransportedResult.convertTo(left);
                                            }
                                        } else {
                                            return TransportedItemStackHandlerBehaviour.TransportedResult.doNothing();
                                        }
                                    }
                                }
                            });
                            return false;
                        }
                    }
                }
            }
        }
    }

    public boolean checkLight(ItemStack stack, ItemEntity entity, Level world, CompoundTag itemData, Vec3 positionVec, BlockPos randomOffset, BlockState state) {
        if (state.getLightEmission(world, randomOffset) == 0) {
            return false;
        } else if (state.m_60800_(world, randomOffset) == -1.0F) {
            return false;
        } else if (state.m_60734_() == Blocks.BEACON) {
            return false;
        } else {
            ClipContext context = new ClipContext(positionVec.add(new Vec3(0.0, 0.5, 0.0)), VecHelper.getCenterOf(randomOffset), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity);
            if (!randomOffset.equals(world.m_45547_(context).getBlockPos())) {
                return false;
            } else {
                ItemStack newStack = stack.split(1);
                newStack.getOrCreateTag().putInt("CollectingLight", itemData.getInt("CollectingLight") + 1);
                ItemEntity newEntity = new ItemEntity(world, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), newStack);
                newEntity.m_20256_(entity.m_20184_());
                newEntity.setDefaultPickUpDelay();
                world.m_7967_(newEntity);
                entity.lifespan = 6000;
                if (stack.isEmpty()) {
                    entity.m_146870_();
                }
                return true;
            }
        }
    }
}