package com.mna.tools;

import com.mna.ManaAndArtifice;
import com.mna.api.config.GeneralConfigValues;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.StateHolder;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.TierSortingRegistry;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.FakePlayerFactory;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class BlockUtils {

    private static final int blockBreakLimit = 25;

    public static Pair<Boolean, List<ItemStack>> breakTreeRecursive(Player player, Level world, BlockPos origin, boolean captureDrops) {
        return breakTreeRecursive(player, world, origin, new ArrayList(), 0, captureDrops);
    }

    private static Pair<Boolean, List<ItemStack>> breakTreeRecursive(Player player, Level world, BlockPos origin, ArrayList<Long> checkedBlocks, int blockBreakCount, boolean captureDrops) {
        int radius = 1;
        int leafRadius = 2;
        boolean success = true;
        List<ItemStack> drops = new ArrayList();
        for (int x = -radius; x <= radius; x++) {
            for (int y = 0; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    BlockPos pos = origin.offset(new BlockPos(x, y, z));
                    if (!checkedBlocks.contains(pos.asLong())) {
                        checkedBlocks.add(pos.asLong());
                        BlockUtils.TreeBlockTypes block = isLogOrLeaf(world.getBlockState(pos));
                        if (block == BlockUtils.TreeBlockTypes.LOG) {
                            blockBreakCount++;
                            if (captureDrops) {
                                drops.addAll(destroyBlockCaptureDrops(player, world, pos, false, 0, (Tier) TierSortingRegistry.getSortedTiers().get(TierSortingRegistry.getSortedTiers().size() - 1)));
                            } else if (!destroyBlock(player, world, pos, !captureDrops, (Tier) TierSortingRegistry.getSortedTiers().get(TierSortingRegistry.getSortedTiers().size() - 1))) {
                                return new Pair(success, drops);
                            }
                            for (int lx = -leafRadius; lx <= leafRadius; lx++) {
                                for (int ly = 0; ly <= leafRadius; ly++) {
                                    for (int lz = -leafRadius; lz <= leafRadius; lz++) {
                                        BlockPos leafPos = pos.offset(new BlockPos(lx, ly, lz));
                                        block = isLogOrLeaf(world.getBlockState(leafPos));
                                        if (block == BlockUtils.TreeBlockTypes.LEAF) {
                                            if (captureDrops) {
                                                drops.addAll(destroyBlockCaptureDrops(player, world, leafPos, false, 0, (Tier) TierSortingRegistry.getSortedTiers().get(TierSortingRegistry.getSortedTiers().size() - 1)));
                                            } else {
                                                destroyBlock(player, world, leafPos, !captureDrops, (Tier) TierSortingRegistry.getSortedTiers().get(TierSortingRegistry.getSortedTiers().size() - 1));
                                            }
                                        }
                                    }
                                }
                            }
                            if (blockBreakCount <= 25) {
                                Pair<Boolean, List<ItemStack>> sub = breakTreeRecursive(player, world, pos, checkedBlocks, blockBreakCount, captureDrops);
                                drops.addAll((Collection) sub.getSecond());
                                success &= sub.getFirst();
                            }
                        }
                    }
                }
            }
        }
        return new Pair(success, drops);
    }

    public static BlockUtils.TreeBlockTypes isLogOrLeaf(BlockState state) {
        if (state.m_204336_(BlockTags.LOGS)) {
            return BlockUtils.TreeBlockTypes.LOG;
        } else {
            return !state.m_204336_(BlockTags.LEAVES) && !state.m_204336_(BlockTags.WART_BLOCKS) ? BlockUtils.TreeBlockTypes.INVALID : BlockUtils.TreeBlockTypes.LEAF;
        }
    }

    public static boolean placeBlock(ServerLevel world, BlockPos pos, Direction face, BlockState state, @Nullable Player player) {
        if (world.m_46749_(pos) && !ForgeEventFactory.onBlockPlace(player, BlockSnapshot.create(world.m_46472_(), world, pos), face)) {
            world.m_46597_(pos, state);
            return true;
        } else {
            return false;
        }
    }

    public static BlockPos[] getBlocksInFrontOfCharacter(LivingEntity entity, int numBlocks, BlockPos firstBlock) {
        float speed = 0.1F;
        float factor = (float) (Math.PI / 180.0);
        float sinYawRadians = Mth.sin(entity.m_146908_() * factor);
        float cosYawRadians = Mth.cos(entity.m_146908_() * factor);
        float sinPitchRadians = Mth.sin(entity.m_146909_() * factor);
        float cosPitchRadians = Mth.cos(entity.m_146909_() * factor);
        double motionZ = (double) (cosYawRadians * cosPitchRadians * speed);
        double motionX = (double) (-sinYawRadians * cosPitchRadians * speed);
        double motionY = (double) (-sinPitchRadians * speed);
        double curX = (double) firstBlock.m_123341_();
        double curY = (double) firstBlock.m_123342_();
        double curZ = (double) firstBlock.m_123343_();
        float minimum = 0.01F;
        if (Math.abs(motionX) < (double) minimum) {
            motionX = 0.0;
        }
        if (Math.abs(motionY) < (double) minimum) {
            motionY = 0.0;
        }
        if (Math.abs(motionZ) < (double) minimum) {
            motionZ = 0.0;
        }
        int lastX = firstBlock.m_123341_();
        int lastY = firstBlock.m_123342_();
        int lastZ = firstBlock.m_123343_();
        BlockPos[] list = new BlockPos[numBlocks];
        list[0] = new BlockPos(firstBlock);
        int count = 1;
        while (count < numBlocks) {
            curX += motionX;
            curY += motionY;
            curZ += motionZ;
            if ((int) Math.round(curX) != lastX || (int) Math.round(curY) != lastY || (int) Math.round(curZ) != lastZ) {
                lastX = (int) Math.round(curX);
                lastY = (int) Math.round(curY);
                lastZ = (int) Math.round(curZ);
                list[count++] = new BlockPos(lastX, lastY, lastZ);
            }
        }
        return list;
    }

    public static void stepThroughBlocksLinear(BlockPos a, BlockPos b, Consumer<BlockPos> action) {
        float stepDist = 0.2F;
        Vec3 curPos = Vec3.atCenterOf(a);
        Vec3 endPos = Vec3.atCenterOf(b);
        Vec3 step = endPos.subtract(curPos).normalize().scale((double) stepDist);
        action.accept(BlockPos.containing(curPos.x(), curPos.y(), curPos.z()));
        while (!curPos.closerThan(endPos, (double) stepDist)) {
            curPos = curPos.add(step);
            BlockPos cbp = BlockPos.containing(curPos.x(), curPos.y(), curPos.z());
            action.accept(cbp);
        }
    }

    public static BlockPos Vector3dToBlockPosRound(Vec3 vec) {
        return BlockPos.containing(Math.floor(vec.x), Math.floor(vec.y), Math.floor(vec.z));
    }

    public static void IterateBlocksInCube(int radius, BlockPos center, Consumer<BlockPos> callable) {
        for (int i = -radius; i <= radius; i++) {
            for (int j = -radius; j <= radius; j++) {
                for (int k = -radius; k <= radius; k++) {
                    BlockPos pos = new BlockPos(i, j, k).offset(center);
                    callable.accept(pos);
                }
            }
        }
    }

    public static void stepThroughBlocksInCube(Vec3 a, Vec3 b, Consumer<BlockPos> action) {
        Vec3 dimensions = b.subtract(a);
        Vec3 step = dimensions.normalize();
        ArrayList<BlockPos> processed = new ArrayList();
        for (double x = a.x; b.x - x > step.x; x += step.x) {
            for (double y = a.y; b.y - y > step.y; y += step.y) {
                for (double z = a.z; b.z - z > step.z; z += step.z) {
                    BlockPos cur = Vector3dToBlockPosRound(new Vec3(x, y, z));
                    if (!processed.contains(cur)) {
                        action.accept(cur);
                        processed.add(cur);
                    }
                }
            }
        }
    }

    public static boolean destroyBlock(LivingEntity caster, Level world, BlockPos pos, boolean doDrops, Tier harvestLevel) {
        return destroyBlock(caster, world, pos, doDrops, false, -1, harvestLevel);
    }

    public static boolean destroyBlock(LivingEntity caster, Level world, BlockPos pos, boolean doDrops, boolean silkTouch, int fortuneLevel, Tier harvestLevel) {
        if (!(world instanceof ServerLevel)) {
            return false;
        } else {
            Player playerEntity = (Player) (caster instanceof Player ? (Player) caster : FakePlayerFactory.getMinecraft((ServerLevel) world));
            BlockState state = world.getBlockState(pos);
            float hardness = state.m_60800_(world, pos);
            if (hardness < 0.0F) {
                return false;
            } else if (state.m_60834_() && !TierSortingRegistry.isCorrectTierForDrops(harvestLevel, state)) {
                return false;
            } else {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, playerEntity);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return false;
                } else {
                    if (fortuneLevel > 0) {
                        ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
                        stack.enchant(Enchantments.BLOCK_FORTUNE, fortuneLevel);
                        Block.dropResources(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), playerEntity, stack);
                        doDrops = false;
                    } else if (silkTouch) {
                        ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
                        stack.enchant(Enchantments.SILK_TOUCH, 1);
                        Block.dropResources(world.getBlockState(pos), world, pos, world.getBlockEntity(pos), playerEntity, stack);
                        doDrops = false;
                    }
                    if (!silkTouch) {
                        int xp = state.getExpDrop(world, world.getRandom(), pos, 0, 0);
                        if (xp > 0) {
                            state.m_60734_().popExperience((ServerLevel) world, pos, xp);
                        }
                    }
                    if (doDrops) {
                        if (world.m_46953_(pos, true, playerEntity)) {
                            if (!silkTouch) {
                                int xp = state.getExpDrop(world, world.getRandom(), pos, 0, 0);
                                if (xp > 0) {
                                    state.m_60734_().popExperience((ServerLevel) world, pos, xp);
                                }
                            }
                            updateBlockState(world, pos);
                            return true;
                        } else {
                            return false;
                        }
                    } else if (world.m_46961_(pos, false)) {
                        updateBlockState(world, pos);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }
    }

    public static List<ItemStack> destroyBlockCaptureDrops(LivingEntity caster, Level world, BlockPos pos, boolean silkTouch, int fortuneLevel, Tier harvestLevel) {
        List<ItemStack> drops = new ArrayList();
        if (!(world instanceof ServerLevel)) {
            return drops;
        } else {
            Player playerEntity = (Player) (caster instanceof Player ? (Player) caster : FakePlayerFactory.getMinecraft((ServerLevel) world));
            BlockState state = world.getBlockState(pos);
            float hardness = state.m_60800_(world, pos);
            if (hardness < 0.0F) {
                return drops;
            } else if (state.m_60834_() && !TierSortingRegistry.isCorrectTierForDrops(harvestLevel, state)) {
                return drops;
            } else {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, playerEntity);
                if (MinecraftForge.EVENT_BUS.post(event)) {
                    return drops;
                } else {
                    ItemStack stack = new ItemStack(Items.DIAMOND_PICKAXE);
                    if (fortuneLevel > 0) {
                        stack.enchant(Enchantments.BLOCK_FORTUNE, fortuneLevel);
                        drops.addAll(Block.getDrops(state, (ServerLevel) world, pos, world.getBlockEntity(pos), playerEntity, stack));
                    } else if (silkTouch) {
                        stack.enchant(Enchantments.SILK_TOUCH, 1);
                        drops.addAll(Block.getDrops(state, (ServerLevel) world, pos, world.getBlockEntity(pos), playerEntity, stack));
                    } else {
                        drops.addAll(Block.getDrops(state, (ServerLevel) world, pos, world.getBlockEntity(pos), playerEntity, stack));
                    }
                    if (world.m_46953_(pos, false, playerEntity)) {
                        state.m_60734_().playerWillDestroy(world, pos, state, playerEntity);
                        if (!silkTouch) {
                            int xp = state.getExpDrop(world, world.getRandom(), pos, 0, 0);
                            if (xp > 0) {
                                state.m_60734_().popExperience((ServerLevel) world, pos, xp);
                            }
                        }
                        updateBlockState(world, pos);
                    } else {
                        drops.clear();
                    }
                    return drops;
                }
            }
        }
    }

    public static boolean canDestroyBlock(LivingEntity caster, Level world, BlockPos pos, Tier harvestLevel) {
        if (!(world instanceof ServerLevel)) {
            return false;
        } else {
            Player playerEntity = (Player) (caster instanceof Player ? (Player) caster : FakePlayerFactory.getMinecraft((ServerLevel) world));
            BlockState state = world.getBlockState(pos);
            float hardness = state.m_60800_(world, pos);
            if (hardness < 0.0F) {
                return false;
            } else if (state.m_60834_() && !TierSortingRegistry.isCorrectTierForDrops(harvestLevel, state)) {
                return false;
            } else {
                BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(world, pos, state, playerEntity);
                return !MinecraftForge.EVENT_BUS.post(event);
            }
        }
    }

    public static void updateBlockState(Level world, BlockPos pos, BlockState state) {
        if (!world.m_151570_(pos)) {
            world.sendBlockUpdated(pos, state, state, 3);
        }
    }

    public static void updateBlockState(Level world, BlockPos pos) {
        updateBlockState(world, pos, world.getBlockState(pos));
    }

    public static boolean tryDestroyBlockSilent(Level world, BlockPos pos, boolean dropBlock) {
        return destroyBlockSilent(world, pos, dropBlock, null);
    }

    public static boolean tryDestroyBlockSilent(Level world, BlockPos pos, boolean dropBlock, @Nullable LivingEntity caster) {
        if (!(world instanceof ServerLevel)) {
            return false;
        } else {
            Player playerEntity = (Player) (caster instanceof Player ? (Player) caster : FakePlayerFactory.getMinecraft((ServerLevel) world));
            return MinecraftForge.EVENT_BUS.post(new BlockEvent.BreakEvent(world, pos, world.getBlockState(pos), playerEntity)) ? false : destroyBlockSilent(world, pos, dropBlock);
        }
    }

    private static boolean destroyBlockSilent(Level world, BlockPos pos, boolean dropBlock) {
        return destroyBlockSilent(world, pos, dropBlock, (Entity) null);
    }

    private static boolean destroyBlockSilent(Level world, BlockPos pos, boolean isMoving, @Nullable Entity entityIn) {
        BlockState blockstate = world.getBlockState(pos);
        if (world.m_46859_(pos)) {
            return false;
        } else {
            FluidState ifluidstate = world.getFluidState(pos);
            if (isMoving) {
                BlockEntity tileentity = blockstate.m_155947_() ? world.getBlockEntity(pos) : null;
                Block.dropResources(blockstate, world, pos, tileentity, entityIn, ItemStack.EMPTY);
            }
            return world.setBlock(pos, ifluidstate.createLegacyBlock(), 3);
        }
    }

    public static Tier tierFromHarvestLevel(int harvestLevel) {
        ArrayList<String> configuredTiers = new ArrayList(GeneralConfigValues.BreakMagnitudeMapping);
        if (configuredTiers.size() == 0) {
            configuredTiers.add("minecraft:stone");
        }
        if (harvestLevel < 0) {
            harvestLevel = 0;
        }
        if (harvestLevel >= configuredTiers.size()) {
            harvestLevel = configuredTiers.size() - 1;
        }
        ResourceLocation rLoc = null;
        try {
            rLoc = new ResourceLocation((String) configuredTiers.get(harvestLevel));
        } catch (Throwable var4) {
            rLoc = new ResourceLocation("minecraft:stone");
        }
        Tier resolved = TierSortingRegistry.byName(rLoc);
        if (resolved == null) {
            resolved = (Tier) TierSortingRegistry.getSortedTiers().get(0);
        }
        return resolved;
    }

    public static BlockState readBlockState(CompoundTag pTag) {
        if (!pTag.contains("Name", 8)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            ResourceLocation resourcelocation = new ResourceLocation(pTag.getString("Name"));
            Block block = ForgeRegistries.BLOCKS.getValue(resourcelocation);
            if (block == null) {
                return Blocks.AIR.defaultBlockState();
            } else {
                BlockState blockstate = block.defaultBlockState();
                if (pTag.contains("Properties", 10)) {
                    CompoundTag compoundtag = pTag.getCompound("Properties");
                    StateDefinition<Block, BlockState> statedefinition = block.getStateDefinition();
                    for (String s : compoundtag.getAllKeys()) {
                        Property<?> property = statedefinition.getProperty(s);
                        if (property != null) {
                            blockstate = setValueHelper(blockstate, property, s, compoundtag, pTag);
                        }
                    }
                }
                return blockstate;
            }
        }
    }

    private static <S extends StateHolder<?, S>, T extends Comparable<T>> S setValueHelper(S pStateHolder, Property<T> pProperty, String pPropertyName, CompoundTag pPropertiesTag, CompoundTag pBlockStateTag) {
        Optional<T> optional = pProperty.getValue(pPropertiesTag.getString(pPropertyName));
        if (optional.isPresent()) {
            return pStateHolder.setValue(pProperty, (Comparable) optional.get());
        } else {
            ManaAndArtifice.LOGGER.warn("Unable to read property: {} with value: {} for blockstate: {}", pPropertyName, pPropertiesTag.getString(pPropertyName), pBlockStateTag.toString());
            return pStateHolder;
        }
    }

    public static enum TreeBlockTypes {

        LOG, LEAF, INVALID
    }
}