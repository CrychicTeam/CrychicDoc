package org.violetmoon.quark.content.tweaks.module;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.GrowingPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.api.event.SimpleHarvestEvent;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.network.message.HarvestMessage;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.play.entity.player.ZRightClickBlock;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.MiscUtil;

@ZetaLoadModule(category = "tweaks")
public class SimpleHarvestModule extends ZetaModule {

    @Config(description = "Can players harvest crops with empty hand clicks?")
    public static boolean emptyHandHarvest = true;

    @Config(description = "Does harvesting crops with a hoe cost durability?")
    public static boolean harvestingCostsDurability = false;

    @Config(description = "Should Quark look for(nonvanilla) crops, and handle them?")
    public static boolean doHarvestingSearch = true;

    @Config(description = "Should villagers use simple harvest instead of breaking crops?")
    public static boolean villagersUseSimpleHarvest = true;

    @Config(description = "Which crops can be harvested?\nFormat is: \"harvestState[,afterHarvest]\", i.e. \"minecraft:wheat[age=7]\" or \"minecraft:cocoa[age=2,facing=north],minecraft:cocoa[age=0,facing=north]\"")
    public static List<String> harvestableBlocks = Lists.newArrayList(new String[] { "minecraft:wheat[age=7]", "minecraft:carrots[age=7]", "minecraft:potatoes[age=7]", "minecraft:beetroots[age=3]", "minecraft:nether_wart[age=3]", "minecraft:cocoa[age=2,facing=north],minecraft:cocoa[age=0,facing=north]", "minecraft:cocoa[age=2,facing=south],minecraft:cocoa[age=0,facing=south]", "minecraft:cocoa[age=2,facing=east],minecraft:cocoa[age=0,facing=east]", "minecraft:cocoa[age=2,facing=west],minecraft:cocoa[age=0,facing=west]" });

    @Config(description = "Which blocks should right click harvesting simulate a click on instead of breaking?\nThis is for blocks like sweet berry bushes, which have right click harvesting built in.")
    public static List<String> rightClickableBlocks = Lists.newArrayList(new String[] { "minecraft:sweet_berry_bush", "minecraft:cave_vines" });

    public static final Map<BlockState, BlockState> crops = Maps.newHashMap();

    private static final Set<Block> cropBlocks = Sets.newHashSet();

    public static final Set<Block> rightClickCrops = Sets.newHashSet();

    public static TagKey<Block> simpleHarvestBlacklistedTag;

    public static boolean staticEnabled;

    private boolean isHarvesting = false;

    @LoadEvent
    public void setup(ZCommonSetup event) {
        simpleHarvestBlacklistedTag = BlockTags.create(new ResourceLocation("quark", "simple_harvest_blacklisted"));
    }

    @LoadEvent
    public void configChanged(ZConfigChanged event) {
        crops.clear();
        cropBlocks.clear();
        rightClickCrops.clear();
        staticEnabled = this.enabled;
        if (doHarvestingSearch) {
            for (Block b : BuiltInRegistries.BLOCK) {
                if (!this.isVanilla(b)) {
                    if (b instanceof CropBlock c) {
                        crops.put(c.getStateForAge(c.getMaxAge()), c.m_49966_());
                    } else if ((b instanceof BushBlock || b instanceof GrowingPlantBlock) && b instanceof BonemealableBlock) {
                        rightClickCrops.add(b);
                    }
                }
            }
        }
        for (String harvestKey : harvestableBlocks) {
            String[] split = this.tokenize(harvestKey);
            BlockState initial = MiscUtil.fromString(split[0]);
            BlockState result;
            if (split.length > 1) {
                result = MiscUtil.fromString(split[1]);
            } else {
                result = initial.m_60734_().defaultBlockState();
            }
            if (initial.m_60734_() != Blocks.AIR) {
                crops.put(initial, result);
            }
        }
        for (String blockName : rightClickableBlocks) {
            Block block = BuiltInRegistries.BLOCK.get(new ResourceLocation(blockName));
            if (block != Blocks.AIR) {
                rightClickCrops.add(block);
            }
        }
        crops.values().forEach(bl -> cropBlocks.add(bl.m_60734_()));
    }

    private String[] tokenize(String harvestKey) {
        boolean inBracket = false;
        for (int i = 0; i < harvestKey.length(); i++) {
            char charAt = harvestKey.charAt(i);
            if (charAt == '[') {
                inBracket = true;
            } else if (charAt == ']') {
                inBracket = false;
            } else if (charAt == ',' && !inBracket) {
                return new String[] { harvestKey.substring(0, i), harvestKey.substring(i + 1) };
            }
        }
        return new String[] { harvestKey };
    }

    private boolean isVanilla(Block entry) {
        ResourceLocation loc = BuiltInRegistries.BLOCK.getKey(entry);
        return loc.getNamespace().equals("minecraft");
    }

    private static boolean harvestAndReplant(Level level, BlockPos pos, BlockState inWorld, @Nullable LivingEntity entity, @Nullable InteractionHand hand) {
        BlockState newBlock = (BlockState) crops.get(inWorld);
        if (newBlock == null) {
            return false;
        } else {
            if (level instanceof ServerLevel serverLevel) {
                ItemStack copy;
                if (entity != null && hand != null) {
                    copy = entity.getItemInHand(hand).copy();
                } else {
                    copy = new ItemStack(Items.STICK);
                }
                MutableBoolean hasTaken = new MutableBoolean(false);
                Item blockItem = inWorld.m_60734_().asItem();
                Block.getDrops(inWorld, serverLevel, pos, level.getBlockEntity(pos), entity, copy).forEach(stack -> {
                    if (stack.getItem() == blockItem && !hasTaken.getValue()) {
                        stack.shrink(1);
                        hasTaken.setValue(true);
                    }
                    if (!stack.isEmpty()) {
                        Block.popResource(level, pos, stack);
                    }
                });
                boolean dropXp = entity instanceof Player;
                inWorld.m_222967_(serverLevel, pos, copy, dropXp);
                level.m_46796_(2001, pos, Block.getId(newBlock));
                level.setBlockAndUpdate(pos, newBlock);
                level.m_220407_(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(entity, inWorld));
            }
            return true;
        }
    }

    @PlayEvent
    public void onClick(ZRightClickBlock event) {
        if (!this.isHarvesting) {
            this.isHarvesting = true;
            if (click(event.getPlayer(), event.getHand(), event.getPos(), event.getHitVec())) {
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(event.getLevel().isClientSide));
            }
            this.isHarvesting = false;
        }
    }

    public static boolean tryHarvestOrClickCrop(Level level, BlockPos pos, @Nullable LivingEntity entity, @Nullable InteractionHand hand, boolean canRightClick) {
        if (entity instanceof Player p && !level.mayInteract(p, pos)) {
            return false;
        }
        BlockState worldBlock = level.getBlockState(pos);
        SimpleHarvestEvent.ActionType action = getActionForBlock(worldBlock, canRightClick);
        if (action != SimpleHarvestEvent.ActionType.NONE) {
            SimpleHarvestEvent event = new SimpleHarvestEvent(worldBlock, pos, level, hand, entity, action);
            MinecraftForge.EVENT_BUS.post(event);
            if (event.isCanceled()) {
                return false;
            }
            BlockPos newPos = event.getTargetPos();
            if (newPos != pos) {
                worldBlock = level.getBlockState(newPos);
            }
            action = event.getAction();
            if (action == SimpleHarvestEvent.ActionType.HARVEST) {
                if (entity instanceof Player p && !Quark.FLAN_INTEGRATION.canBreak(p, pos)) {
                    return false;
                }
                return harvestAndReplant(level, pos, worldBlock, entity, hand);
            }
            if (action == SimpleHarvestEvent.ActionType.CLICK && entity instanceof Player p) {
                if (!Quark.FLAN_INTEGRATION.canInteract(p, pos)) {
                    return false;
                }
                BlockHitResult hitResult = new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, true);
                if (hand == null) {
                    hand = InteractionHand.MAIN_HAND;
                }
                if (entity instanceof ServerPlayer player) {
                    return player.gameMode.useItemOn(player, player.m_9236_(), player.m_21120_(hand), hand, hitResult).consumesAction();
                }
                return Quark.proxy.clientUseItem(p, level, hand, hitResult).consumesAction();
            }
        }
        return false;
    }

    private static SimpleHarvestEvent.ActionType getActionForBlock(BlockState state, boolean closeEnoughToRightClick) {
        if (state.m_204336_(simpleHarvestBlacklistedTag)) {
            return SimpleHarvestEvent.ActionType.NONE;
        } else if (closeEnoughToRightClick && rightClickCrops.contains(state.m_60734_())) {
            return SimpleHarvestEvent.ActionType.CLICK;
        } else {
            return crops.containsKey(state) ? SimpleHarvestEvent.ActionType.HARVEST : SimpleHarvestEvent.ActionType.NONE;
        }
    }

    public static boolean click(Player player, InteractionHand hand, BlockPos pos, BlockHitResult pick) {
        if (player != null && hand != null && !player.isSpectator()) {
            if (pick.getType() == HitResult.Type.BLOCK && pick.getBlockPos().equals(pos)) {
                Level level = player.m_9236_();
                BlockState stateAt = level.getBlockState(pos);
                BlockState modifiedState = Quark.ZETA.blockExtensions.get(stateAt).getToolModifiedStateZeta(stateAt, new UseOnContext(player, hand, pick), "hoe_till", true);
                if (modifiedState != null) {
                    return false;
                } else {
                    ItemStack inHand = player.m_21120_(hand);
                    boolean isHoe = HoeHarvestingModule.isHoe(inHand);
                    if (!emptyHandHarvest && !isHoe) {
                        return false;
                    } else {
                        BlockState stateAbove = level.getBlockState(pos.above());
                        if (isHoe && getActionForBlock(stateAt, true) == SimpleHarvestEvent.ActionType.NONE && getActionForBlock(stateAbove, true) == SimpleHarvestEvent.ActionType.NONE) {
                            return false;
                        } else {
                            int range = HoeHarvestingModule.getRange(inHand);
                            boolean hasHarvested = false;
                            for (int x = 1 - range; x < range; x++) {
                                for (int z = 1 - range; z < range; z++) {
                                    BlockPos shiftPos = pos.offset(x, 0, z);
                                    if (!tryHarvestOrClickCrop(level, shiftPos, player, hand, range > 1)) {
                                        shiftPos = shiftPos.above();
                                        if (tryHarvestOrClickCrop(level, shiftPos, player, hand, range > 1)) {
                                            hasHarvested = true;
                                        }
                                    } else {
                                        hasHarvested = true;
                                    }
                                }
                            }
                            if (!hasHarvested) {
                                return false;
                            } else {
                                if (level.isClientSide) {
                                    if (inHand.isEmpty()) {
                                        QuarkClient.ZETA_CLIENT.sendToServer(new HarvestMessage(pos, hand));
                                    }
                                } else if (harvestingCostsDurability && isHoe) {
                                    inHand.hurtAndBreak(1, player, p -> p.m_21190_(InteractionHand.MAIN_HAND));
                                }
                                return true;
                            }
                        }
                    }
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}