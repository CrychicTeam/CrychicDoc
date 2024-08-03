package com.simibubi.create.foundation.utility;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.processing.burner.BlazeBurnerBlock;
import com.simibubi.create.foundation.blockEntity.IMergeableBE;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseRailBlock;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.IceBlock;
import net.minecraft.world.level.block.SlimeBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;

public class BlockHelper {

    public static BlockState setZeroAge(BlockState blockState) {
        if (blockState.m_61138_(BlockStateProperties.AGE_1)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_1, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_2)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_2, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_3)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_3, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_5)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_5, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_7)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_7, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_15)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_15, 0);
        } else if (blockState.m_61138_(BlockStateProperties.AGE_25)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.AGE_25, 0);
        } else if (blockState.m_61138_(BlockStateProperties.LEVEL_HONEY)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.LEVEL_HONEY, 0);
        } else if (blockState.m_61138_(BlockStateProperties.HATCH)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.HATCH, 0);
        } else if (blockState.m_61138_(BlockStateProperties.STAGE)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.STAGE, 0);
        } else if (blockState.m_204336_(BlockTags.CAULDRONS)) {
            return Blocks.CAULDRON.defaultBlockState();
        } else if (blockState.m_61138_(BlockStateProperties.LEVEL_COMPOSTER)) {
            return (BlockState) blockState.m_61124_(BlockStateProperties.LEVEL_COMPOSTER, 0);
        } else {
            return blockState.m_61138_(BlockStateProperties.EXTENDED) ? (BlockState) blockState.m_61124_(BlockStateProperties.EXTENDED, false) : blockState;
        }
    }

    public static int findAndRemoveInInventory(BlockState block, Player player, int amount) {
        int amountFound = 0;
        Item required = getRequiredItem(block).getItem();
        boolean needsTwo = block.m_61138_(BlockStateProperties.SLAB_TYPE) && block.m_61143_(BlockStateProperties.SLAB_TYPE) == SlabType.DOUBLE;
        if (needsTwo) {
            amount *= 2;
        }
        if (block.m_61138_(BlockStateProperties.EGGS)) {
            amount *= block.m_61143_(BlockStateProperties.EGGS);
        }
        if (block.m_61138_(BlockStateProperties.PICKLES)) {
            amount *= block.m_61143_(BlockStateProperties.PICKLES);
        }
        int preferredSlot = player.getInventory().selected;
        ItemStack itemstack = player.getInventory().getItem(preferredSlot);
        int count = itemstack.getCount();
        if (itemstack.getItem() == required && count > 0) {
            int taken = Math.min(count, amount - amountFound);
            player.getInventory().setItem(preferredSlot, new ItemStack(itemstack.getItem(), count - taken));
            amountFound += taken;
        }
        for (int i = 0; i < player.getInventory().getContainerSize() && amountFound != amount; i++) {
            itemstack = player.getInventory().getItem(i);
            count = itemstack.getCount();
            if (itemstack.getItem() == required && count > 0) {
                int taken = Math.min(count, amount - amountFound);
                player.getInventory().setItem(i, new ItemStack(itemstack.getItem(), count - taken));
                amountFound += taken;
            }
        }
        if (needsTwo) {
            if (amountFound % 2 != 0) {
                player.getInventory().add(new ItemStack(required));
            }
            amountFound /= 2;
        }
        return amountFound;
    }

    public static ItemStack getRequiredItem(BlockState state) {
        ItemStack itemStack = new ItemStack(state.m_60734_());
        Item item = itemStack.getItem();
        if (item == Items.FARMLAND || item == Items.DIRT_PATH) {
            itemStack = new ItemStack(Items.DIRT);
        }
        return itemStack;
    }

    public static void destroyBlock(Level world, BlockPos pos, float effectChance) {
        destroyBlock(world, pos, effectChance, stack -> Block.popResource(world, pos, stack));
    }

    public static void destroyBlock(Level world, BlockPos pos, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        destroyBlockAs(world, pos, null, ItemStack.EMPTY, effectChance, droppedItemCallback);
    }

    public static void destroyBlockAs(Level world, BlockPos pos, @Nullable Player player, ItemStack usedTool, float effectChance, Consumer<ItemStack> droppedItemCallback) {
        FluidState fluidState = world.getFluidState(pos);
        BlockState state = world.getBlockState(pos);
        if (world.random.nextFloat() < effectChance) {
            world.m_46796_(2001, pos, Block.getId(state));
        }
        BlockEntity blockEntity = state.m_155947_() ? world.getBlockEntity(pos) : null;
        if (player != null) {
            BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, player);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return;
            }
            if (event.getExpToDrop() > 0 && world instanceof ServerLevel) {
                state.m_60734_().popExperience((ServerLevel) world, pos, event.getExpToDrop());
            }
            usedTool.mineBlock(world, state, pos, player);
            player.awardStat(Stats.BLOCK_MINED.get(state.m_60734_()));
        }
        if (world instanceof ServerLevel && world.getGameRules().getBoolean(GameRules.RULE_DOBLOCKDROPS) && !world.restoringBlockSnapshots && (player == null || !player.isCreative())) {
            for (ItemStack itemStack : Block.getDrops(state, (ServerLevel) world, pos, blockEntity, player, usedTool)) {
                droppedItemCallback.accept(itemStack);
            }
            if (state.m_60734_() instanceof IceBlock && usedTool.getEnchantmentLevel(Enchantments.SILK_TOUCH) == 0) {
                if (world.dimensionType().ultraWarm()) {
                    return;
                }
                BlockState blockstate = world.getBlockState(pos.below());
                if (blockstate.m_280555_() || blockstate.m_278721_()) {
                    world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
                }
                return;
            }
            state.m_222967_((ServerLevel) world, pos, ItemStack.EMPTY, true);
        }
        world.setBlockAndUpdate(pos, fluidState.createLegacyBlock());
    }

    public static boolean isSolidWall(BlockGetter reader, BlockPos fromPos, Direction toDirection) {
        return hasBlockSolidSide(reader.getBlockState(fromPos.relative(toDirection)), reader, fromPos.relative(toDirection), toDirection.getOpposite());
    }

    public static boolean noCollisionInSpace(BlockGetter reader, BlockPos pos) {
        return reader.getBlockState(pos).m_60812_(reader, pos).isEmpty();
    }

    private static void placeRailWithoutUpdate(Level world, BlockState state, BlockPos target) {
        LevelChunk chunk = world.getChunkAt(target);
        int idx = chunk.m_151564_(target.m_123342_());
        LevelChunkSection chunksection = chunk.m_183278_(idx);
        if (chunksection == null) {
            chunksection = new LevelChunkSection(world.registryAccess().registryOrThrow(Registries.BIOME));
            chunk.m_7103_()[idx] = chunksection;
        }
        BlockState old = chunksection.setBlockState(SectionPos.sectionRelative(target.m_123341_()), SectionPos.sectionRelative(target.m_123342_()), SectionPos.sectionRelative(target.m_123343_()), state);
        chunk.m_8092_(true);
        world.markAndNotifyBlock(target, chunk, old, state, 82, 512);
        world.setBlock(target, state, 82);
        world.neighborChanged(target, world.getBlockState(target.below()).m_60734_(), target.below());
    }

    public static CompoundTag prepareBlockEntityData(BlockState blockState, BlockEntity blockEntity) {
        CompoundTag data = null;
        if (blockEntity == null) {
            return data;
        } else {
            if (AllTags.AllBlockTags.SAFE_NBT.matches(blockState)) {
                data = blockEntity.saveWithFullMetadata();
                data = NBTProcessors.process(blockEntity, data, true);
            } else if (blockEntity instanceof IPartialSafeNBT) {
                data = new CompoundTag();
                ((IPartialSafeNBT) blockEntity).writeSafe(data);
                data = NBTProcessors.process(blockEntity, data, true);
            }
            return data;
        }
    }

    public static void placeSchematicBlock(Level world, BlockState state, BlockPos target, ItemStack stack, @Nullable CompoundTag data) {
        BlockEntity existingBlockEntity = world.getBlockEntity(target);
        if (state.m_61138_(BlockStateProperties.EXTENDED)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.EXTENDED, Boolean.FALSE);
        }
        if (state.m_61138_(BlockStateProperties.WATERLOGGED)) {
            state = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, Boolean.FALSE);
        }
        if (state.m_60734_() == Blocks.COMPOSTER) {
            state = Blocks.COMPOSTER.defaultBlockState();
        } else if (state.m_60734_() != Blocks.SEA_PICKLE && state.m_60734_() instanceof IPlantable) {
            state = ((IPlantable) state.m_60734_()).getPlant(world, target);
        } else if (state.m_204336_(BlockTags.CAULDRONS)) {
            state = Blocks.CAULDRON.defaultBlockState();
        }
        if (world.dimensionType().ultraWarm() && state.m_60819_().is(FluidTags.WATER)) {
            int i = target.m_123341_();
            int j = target.m_123342_();
            int k = target.m_123343_();
            world.playSound(null, target, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 0.5F, 2.6F + (world.random.nextFloat() - world.random.nextFloat()) * 0.8F);
            for (int l = 0; l < 8; l++) {
                world.addParticle(ParticleTypes.LARGE_SMOKE, (double) i + Math.random(), (double) j + Math.random(), (double) k + Math.random(), 0.0, 0.0, 0.0);
            }
            Block.dropResources(state, world, target);
        } else {
            if (state.m_60734_() instanceof BaseRailBlock) {
                placeRailWithoutUpdate(world, state, target);
            } else if (AllBlocks.BELT.has(state)) {
                world.setBlock(target, state, 2);
            } else {
                world.setBlock(target, state, 18);
            }
            if (data != null) {
                if (existingBlockEntity instanceof IMergeableBE mergeable) {
                    BlockEntity loaded = BlockEntity.loadStatic(target, state, data);
                    if (existingBlockEntity.getType().equals(loaded.getType())) {
                        mergeable.accept(loaded);
                        return;
                    }
                }
                BlockEntity blockEntity = world.getBlockEntity(target);
                if (blockEntity != null) {
                    data.putInt("x", target.m_123341_());
                    data.putInt("y", target.m_123342_());
                    data.putInt("z", target.m_123343_());
                    if (blockEntity instanceof KineticBlockEntity) {
                        ((KineticBlockEntity) blockEntity).warnOfMovement();
                    }
                    blockEntity.load(data);
                }
            }
            try {
                state.m_60734_().setPlacedBy(world, target, state, null, stack);
            } catch (Exception var10) {
            }
        }
    }

    public static double getBounceMultiplier(Block block) {
        if (block instanceof SlimeBlock) {
            return 0.8;
        } else {
            return block instanceof BedBlock ? 0.528 : 0.0;
        }
    }

    public static boolean hasBlockSolidSide(BlockState p_220056_0_, BlockGetter p_220056_1_, BlockPos p_220056_2_, Direction p_220056_3_) {
        return !p_220056_0_.m_204336_(BlockTags.LEAVES) && Block.isFaceFull(p_220056_0_.m_60812_(p_220056_1_, p_220056_2_), p_220056_3_);
    }

    public static boolean extinguishFire(Level world, @Nullable Player p_175719_1_, BlockPos p_175719_2_, Direction p_175719_3_) {
        p_175719_2_ = p_175719_2_.relative(p_175719_3_);
        if (world.getBlockState(p_175719_2_).m_60734_() == Blocks.FIRE) {
            world.m_5898_(p_175719_1_, 1009, p_175719_2_, 0);
            world.removeBlock(p_175719_2_, false);
            return true;
        } else {
            return false;
        }
    }

    public static BlockState copyProperties(BlockState fromState, BlockState toState) {
        for (Property<?> property : fromState.m_61147_()) {
            toState = copyProperty(property, fromState, toState);
        }
        return toState;
    }

    public static <T extends Comparable<T>> BlockState copyProperty(Property<T> property, BlockState fromState, BlockState toState) {
        return fromState.m_61138_(property) && toState.m_61138_(property) ? (BlockState) toState.m_61124_(property, fromState.m_61143_(property)) : toState;
    }

    public static boolean isNotUnheated(BlockState state) {
        if (state.m_204336_(BlockTags.CAMPFIRES) && state.m_61138_(CampfireBlock.LIT)) {
            return (Boolean) state.m_61143_(CampfireBlock.LIT);
        } else {
            return state.m_61138_(BlazeBurnerBlock.HEAT_LEVEL) ? state.m_61143_(BlazeBurnerBlock.HEAT_LEVEL) != BlazeBurnerBlock.HeatLevel.NONE : true;
        }
    }
}