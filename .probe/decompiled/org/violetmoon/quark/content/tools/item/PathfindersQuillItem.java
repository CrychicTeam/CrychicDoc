package org.violetmoon.quark.content.tools.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.mobs.module.StonelingsModule;
import org.violetmoon.quark.content.tools.module.PathfinderMapsModule;
import org.violetmoon.quark.content.world.module.GlimmeringWealdModule;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.registry.CreativeTabManager;
import org.violetmoon.zeta.util.ItemNBTHelper;

public class PathfindersQuillItem extends ZetaItem implements CreativeTabManager.AppendsUniquely {

    private static final Direction[] DIRECTIONS = new Direction[] { Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.NORTH };

    public static final String TAG_BIOME = "targetBiome";

    public static final String TAG_COLOR = "targetBiomeColor";

    public static final String TAG_UNDERGROUND = "targetBiomeUnderground";

    protected static final String TAG_IS_SEARCHING = "isSearchingForBiome";

    protected static final String TAG_SOURCE_X = "searchSourceX";

    protected static final String TAG_SOURCE_Z = "searchSourceZ";

    protected static final String TAG_POS_X = "searchPosX";

    protected static final String TAG_POS_Z = "searchPosZ";

    protected static final String TAG_POS_LEG = "searchPosLeg";

    protected static final String TAG_POS_LEG_INDEX = "searchPosLegIndex";

    private static final Map<PathfindersQuillItem.Key, InteractionResultHolder<BlockPos>> RESULTS = new ConcurrentHashMap();

    private static final Set<PathfindersQuillItem.Key> COMPUTING = ConcurrentHashMap.newKeySet();

    protected static final ExecutorService EXECUTORS = Executors.newCachedThreadPool();

    public PathfindersQuillItem(ZetaModule module, Item.Properties properties) {
        super("pathfinders_quill", module, properties);
        CreativeTabManager.addToCreativeTabNextTo(CreativeModeTabs.TOOLS_AND_UTILITIES, this, Items.MAP, false);
    }

    public PathfindersQuillItem(ZetaModule module) {
        this(module, new Item.Properties().stacksTo(1));
    }

    public static ResourceLocation getTargetBiome(ItemStack stack) {
        String str = ItemNBTHelper.getString(stack, "targetBiome", "");
        return str.isEmpty() ? null : new ResourceLocation(str);
    }

    public static int getOverlayColor(ItemStack stack) {
        return ItemNBTHelper.getInt(stack, "targetBiomeColor", 16777215);
    }

    public static ItemStack forBiome(String biome, int color) {
        ItemStack stack = new ItemStack(PathfinderMapsModule.pathfinders_quill);
        setBiome(stack, biome, color, false);
        return stack;
    }

    public static void setBiome(ItemStack stack, String biome, int color, boolean underground) {
        ItemNBTHelper.setString(stack, "targetBiome", biome);
        ItemNBTHelper.setInt(stack, "targetBiomeColor", color);
        ItemNBTHelper.setBoolean(stack, "targetBiomeUnderground", underground);
    }

    @Nullable
    public static ItemStack getActiveQuill(Player player) {
        for (ItemStack stack : player.getInventory().items) {
            if (stack.getItem() instanceof PathfindersQuillItem) {
                boolean searching = ItemNBTHelper.getBoolean(stack, "isSearchingForBiome", false);
                if (searching) {
                    return stack;
                }
            }
        }
        for (ItemStack stackx : player.getInventory().offhand) {
            if (stackx.getItem() instanceof PathfindersQuillItem) {
                boolean searching = ItemNBTHelper.getBoolean(stackx, "isSearchingForBiome", false);
                if (searching) {
                    return stackx;
                }
            }
        }
        for (ItemStack stackxx : player.getInventory().armor) {
            if (stackxx.getItem() instanceof PathfindersQuillItem) {
                boolean searching = ItemNBTHelper.getBoolean(stackxx, "isSearchingForBiome", false);
                if (searching) {
                    return stackxx;
                }
            }
        }
        return null;
    }

    @Override
    public boolean shouldCauseReequipAnimationZeta(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (this.getTarget(stack) == null) {
            return InteractionResultHolder.pass(stack);
        } else {
            ItemStack active = getActiveQuill(player);
            if (active != null) {
                player.displayClientMessage(Component.translatable("quark.misc.only_one_quill"), true);
                return InteractionResultHolder.fail(stack);
            } else {
                Vec3 pos = player.m_20318_(1.0F);
                level.playSound(null, pos.x, pos.y, pos.z, SoundEvents.BOOK_PAGE_TURN, SoundSource.PLAYERS, 0.5F, 1.0F);
                ItemNBTHelper.setBoolean(stack, "isSearchingForBiome", true);
                ItemNBTHelper.setInt(stack, "searchSourceX", player.m_146903_());
                ItemNBTHelper.setInt(stack, "searchSourceZ", player.m_146907_());
                return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
            }
        }
    }

    public ResourceLocation getTarget(ItemStack stack) {
        return getTargetBiome(stack);
    }

    protected int getIterations() {
        return PathfinderMapsModule.pathfindersQuillSpeed;
    }

    protected boolean isMultiThreaded() {
        return PathfinderMapsModule.multiThreaded;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean held) {
        if (!level.isClientSide && level instanceof ServerLevel sl && ItemNBTHelper.getBoolean(stack, "isSearchingForBiome", false) && entity instanceof Player player && getActiveQuill(player) == stack) {
            ItemStack runningStack = this.search(stack, sl, player, slot);
            if (runningStack != stack) {
                String msg;
                if (runningStack.isEmpty()) {
                    if (PathfinderMapsModule.allowRetrying) {
                        runningStack = this.resetSearchingTags(stack);
                        msg = this.getRetryMessage();
                    } else {
                        msg = this.getFailMessage();
                    }
                } else {
                    msg = this.getSuccessMessage();
                }
                player.displayClientMessage(Component.translatable(msg), true);
                Vec3 pos = player.m_20318_(1.0F);
                level.playSound(null, pos.x, pos.y, pos.z, (SoundEvent) SoundEvents.NOTE_BLOCK_CHIME.get(), SoundSource.PLAYERS, 0.5F, 1.0F);
                if (player.m_21206_() == stack) {
                    player.m_21008_(InteractionHand.OFF_HAND, runningStack);
                } else {
                    player.getInventory().setItem(slot, runningStack);
                }
            }
        }
    }

    protected ItemStack resetSearchingTags(ItemStack stack) {
        stack.removeTagKey("searchSourceX");
        stack.removeTagKey("searchSourceZ");
        stack.removeTagKey("isSearchingForBiome");
        stack.removeTagKey("searchPosZ");
        stack.removeTagKey("searchPosZ");
        stack.removeTagKey("searchPosLeg");
        stack.removeTagKey("searchPosLegIndex");
        return stack;
    }

    protected String getRetryMessage() {
        return "quark.misc.quill_retry";
    }

    protected String getSuccessMessage() {
        return "quark.misc.quill_finished";
    }

    protected String getFailMessage() {
        return "quark.misc.quill_failed";
    }

    protected ItemStack search(ItemStack stack, ServerLevel level, Player player, int slot) {
        ResourceLocation searchKey = this.getTarget(stack);
        if (searchKey == null) {
            return ItemStack.EMPTY;
        } else {
            InteractionResultHolder<BlockPos> result;
            if (this.isMultiThreaded()) {
                result = this.searchConcurrent(searchKey, stack, level, player);
            } else {
                result = this.searchIterative(searchKey, stack, level, player, this.getIterations());
            }
            if (result.getResult() == InteractionResult.FAIL) {
                return ItemStack.EMPTY;
            } else if (result.getResult() == InteractionResult.PASS) {
                return stack;
            } else {
                BlockPos found = result.getObject();
                return this.createMap(level, found, searchKey, stack);
            }
        }
    }

    protected InteractionResultHolder<BlockPos> searchConcurrent(ResourceLocation searchKey, ItemStack stack, ServerLevel level, Player player) {
        int sourceX = ItemNBTHelper.getInt(stack, "searchSourceX", 0);
        int sourceZ = ItemNBTHelper.getInt(stack, "searchSourceZ", 0);
        BlockPos centerPos = new BlockPos(sourceX, 64, sourceZ);
        PathfindersQuillItem.Key key = new PathfindersQuillItem.Key(GlobalPos.of(level.m_46472_(), centerPos), searchKey);
        if (COMPUTING.contains(key)) {
            return InteractionResultHolder.pass(BlockPos.ZERO);
        } else if (RESULTS.containsKey(key)) {
            InteractionResultHolder<BlockPos> ret = (InteractionResultHolder<BlockPos>) RESULTS.get(key);
            return ret.getResult() == InteractionResult.PASS ? InteractionResultHolder.fail(BlockPos.ZERO) : ret;
        } else {
            ItemStack dummy = stack.copy();
            EXECUTORS.submit(() -> {
                COMPUTING.add(key);
                RESULTS.put(key, this.searchIterative(searchKey, dummy, level, player, Integer.MAX_VALUE));
                COMPUTING.remove(key);
            });
            return InteractionResultHolder.pass(BlockPos.ZERO);
        }
    }

    protected InteractionResultHolder<BlockPos> searchIterative(ResourceLocation searchKey, ItemStack stack, ServerLevel level, Player player, int maxIter) {
        int y = player.m_146904_();
        for (int i = 0; i < maxIter; i++) {
            int height = 64;
            BlockPos nextPos = nextPos(stack);
            if (nextPos == null) {
                return InteractionResultHolder.fail(BlockPos.ZERO);
            }
            int[] searchedHeights = Mth.outFromOrigin(y, level.m_141937_() + 1, level.m_151558_(), 64).toArray();
            int testX = nextPos.m_123341_();
            int testZ = nextPos.m_123343_();
            int quartX = QuartPos.fromBlock(testX);
            int quartZ = QuartPos.fromBlock(testZ);
            for (int testY : searchedHeights) {
                int quartY = QuartPos.fromBlock(testY);
                ServerChunkCache cache = level.getChunkSource();
                BiomeSource source = cache.getGenerator().getBiomeSource();
                Climate.Sampler sampler = cache.randomState().sampler();
                Holder<Biome> holder = source.getNoiseBiome(quartX, quartY, quartZ, sampler);
                if (holder.is(searchKey)) {
                    BlockPos mapPos = new BlockPos(testX, testY, testZ);
                    return InteractionResultHolder.sidedSuccess(mapPos, level.f_46443_);
                }
            }
        }
        return InteractionResultHolder.pass(BlockPos.ZERO);
    }

    protected static BlockPos nextPos(ItemStack stack) {
        int step = 32;
        int sourceX = ItemNBTHelper.getInt(stack, "searchSourceX", 0);
        int sourceZ = ItemNBTHelper.getInt(stack, "searchSourceZ", 0);
        int x = ItemNBTHelper.getInt(stack, "searchPosX", 0);
        int z = ItemNBTHelper.getInt(stack, "searchPosZ", 0);
        int leg = ItemNBTHelper.getInt(stack, "searchPosLeg", -1);
        int legIndex = ItemNBTHelper.getInt(stack, "searchPosLegIndex", 0);
        BlockPos cursor = new BlockPos(x, 0, z).relative(DIRECTIONS[(leg + 4) % 4]);
        int newX = cursor.m_123341_();
        int newZ = cursor.m_123343_();
        int legSize = leg / 2 + 1;
        int maxLegs = 4 * Math.floorDiv(PathfinderMapsModule.searchRadius, 32);
        if (legIndex >= legSize) {
            if (leg > maxLegs) {
                return null;
            }
            leg++;
            legIndex = 0;
        }
        legIndex++;
        ItemNBTHelper.setInt(stack, "searchPosX", newX);
        ItemNBTHelper.setInt(stack, "searchPosZ", newZ);
        ItemNBTHelper.setInt(stack, "searchPosLeg", leg);
        ItemNBTHelper.setInt(stack, "searchPosLegIndex", legIndex);
        int retX = sourceX + newX * 32;
        int retZ = sourceZ + newZ * 32;
        return new BlockPos(retX, 0, retZ);
    }

    public ItemStack createMap(ServerLevel level, BlockPos targetPos, ResourceLocation target, ItemStack original) {
        int color = getOverlayColor(original);
        Component biomeComponent = Component.translatable("biome." + target.getNamespace() + "." + target.getPath());
        ItemStack stack = MapItem.create(level, targetPos.m_123341_(), targetPos.m_123343_(), (byte) 2, true, true);
        MapItem.renderBiomePreviewMap(level, stack);
        MapItemSavedData.addTargetDecoration(stack, targetPos, "+", MapDecoration.Type.RED_X);
        stack.setHoverName(Component.translatable("item.quark.biome_map", biomeComponent));
        stack.getOrCreateTagElement("display").putInt("MapColor", color);
        ItemNBTHelper.setBoolean(stack, "quark:is_pathfinder", true);
        return stack;
    }

    @OnlyIn(Dist.CLIENT)
    public static MutableComponent getSearchingComponent() {
        MutableComponent comp = Component.translatable("quark.misc.quill_searching");
        int dots = QuarkClient.ticker.ticksInGame / 10 % 4;
        for (int i = 0; i < dots; i++) {
            comp.append(".");
        }
        return comp;
    }

    @Override
    public List<ItemStack> appendItemsToCreativeTab() {
        List<ItemStack> items = new ArrayList();
        boolean generatedWeald = false;
        for (PathfinderMapsModule.TradeInfo trade : PathfinderMapsModule.tradeList) {
            if (trade.biome.equals(GlimmeringWealdModule.BIOME_NAME)) {
                generatedWeald = true;
            }
            items.add(forBiome(trade.biome.toString(), trade.color));
        }
        if (!generatedWeald && Quark.ZETA.modules.isEnabled(StonelingsModule.class) && Quark.ZETA.modules.isEnabled(GlimmeringWealdModule.class) && StonelingsModule.wealdPathfinderMaps) {
            items.add(forBiome(GlimmeringWealdModule.BIOME_NAME.toString(), 3241286));
        }
        return items;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> comps, TooltipFlag flags) {
        ResourceLocation biome = this.getTarget(stack);
        if (biome != null) {
            if (ItemNBTHelper.getBoolean(stack, "isSearchingForBiome", false)) {
                comps.add(getSearchingComponent().withStyle(ChatFormatting.BLUE));
            }
            comps.add(Component.translatable("biome." + biome.getNamespace() + "." + biome.getPath()).withStyle(ChatFormatting.GRAY));
        } else {
            comps.add(Component.translatable("quark.misc.quill_blank").withStyle(ChatFormatting.GRAY));
        }
    }

    private static record Key(GlobalPos pos, ResourceLocation structure) {
    }
}