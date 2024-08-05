package org.violetmoon.quark.content.tools.module;

import com.mojang.blaze3d.platform.Window;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.content.tools.item.PathfindersQuillItem;
import org.violetmoon.quark.content.tools.loot.InBiomeCondition;
import org.violetmoon.zeta.advancement.ManualTrigger;
import org.violetmoon.zeta.client.event.load.ZAddItemColorHandlers;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.client.event.play.ZRenderGuiOverlay;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.config.type.IConfigType;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZConfigChanged;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZLivingTick;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerTick;
import org.violetmoon.zeta.event.play.loading.ZVillagerTrades;
import org.violetmoon.zeta.event.play.loading.ZWandererTrades;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;
import org.violetmoon.zeta.util.ItemNBTHelper;

@ZetaLoadModule(category = "tools")
public class PathfinderMapsModule extends ZetaModule {

    public static final String TAG_IS_PATHFINDER = "quark:is_pathfinder";

    private static final String TAG_CHECKED_FOR_PATHFINDER = "quark:checked_pathfinder";

    private static final Object mutex = new Object();

    public static List<PathfinderMapsModule.TradeInfo> builtinTrades = new ArrayList();

    public static List<PathfinderMapsModule.TradeInfo> customTrades = new ArrayList();

    public static List<PathfinderMapsModule.TradeInfo> tradeList = new ArrayList();

    @Config(description = "In this section you can add custom Pathfinder Maps. This works for both vanilla and modded biomes.\nEach custom map must be on its own line.\nThe format for a custom map is as follows:\n<id>,<level>,<min_price>,<max_price>,<color>,<name>\n\nWith the following descriptions:\n - <id> being the biome's ID NAME. You can find vanilla names here - https://minecraft.wiki/w/Biome#Biome_IDs\n - <level> being the Cartographer villager level required for the map to be unlockable\n - <min_price> being the cheapest (in Emeralds) the map can be\n - <max_price> being the most expensive (in Emeralds) the map can be\n - <color> being a hex color (without the #) for the map to display. You can generate one here - https://htmlcolorcodes.com/\n\nHere's an example of a map to locate Ice Mountains:\nminecraft:ice_mountains,2,8,14,7FE4FF")
    private List<String> customs = new ArrayList();

    public static LootItemFunctionType pathfinderMapType;

    public static LootItemConditionType inBiomeConditionType;

    public static ManualTrigger pathfinderMapTrigger;

    @Hint
    public static Item pathfinders_quill;

    @Config(description = "Set to false to make it so the default quark Pathfinder Map Built-In don't get added, and only the custom ones do")
    public static boolean applyDefaultTrades = true;

    @Config(description = "How many steps in the search should the Pathfinder's Quill do per tick? The higher this value, the faster it'll find a result, but the higher chance it'll lag the game while doing so")
    public static int pathfindersQuillSpeed = 32;

    @Config(description = "Experimental. Determines if quills should be multithreaded instead. Will ignore quill speed. This could drastically improve performance as it execute the logic off the main thread ideally causing no lag at all")
    public static boolean multiThreaded = true;

    @Config(description = "Allows retrying after a pathfinder quill fails to find a biome nearby. Turn off if you think its op")
    public static boolean allowRetrying = true;

    @Config
    public static int searchRadius = 6400;

    @Config
    public static int xpFromTrade = 5;

    @Config
    public static boolean addToCartographer = true;

    @Config
    public static boolean addToWanderingTraderForced = true;

    @Config
    public static boolean addToWanderingTraderGeneric = false;

    @Config
    public static boolean addToWanderingTraderRare = false;

    @Config
    public static boolean drawHud = true;

    @Config
    public static boolean hudOnTop = false;

    @LoadEvent
    public final void register(ZRegister event) {
        this.loadTradeInfo(Biomes.SNOWY_PLAINS, true, 4, 8, 14, 8381695);
        this.loadTradeInfo(Biomes.WINDSWEPT_HILLS, true, 4, 8, 14, 9079434);
        this.loadTradeInfo(Biomes.DARK_FOREST, true, 4, 8, 14, 22794);
        this.loadTradeInfo(Biomes.DESERT, true, 4, 8, 14, 13416782);
        this.loadTradeInfo(Biomes.SAVANNA, true, 4, 8, 14, 10200418);
        this.loadTradeInfo(Biomes.SWAMP, true, 4, 12, 18, 2242319);
        this.loadTradeInfo(Biomes.MANGROVE_SWAMP, true, 4, 12, 18, 2242319);
        this.loadTradeInfo(Biomes.OLD_GROWTH_PINE_TAIGA, true, 4, 12, 18, 5980703);
        this.loadTradeInfo(Biomes.FLOWER_FOREST, true, 5, 16, 22, 13518562);
        this.loadTradeInfo(Biomes.JUNGLE, true, 5, 16, 22, 2274816);
        this.loadTradeInfo(Biomes.BAMBOO_JUNGLE, true, 5, 16, 22, 4055575);
        this.loadTradeInfo(Biomes.BADLANDS, true, 5, 16, 22, 13008674);
        this.loadTradeInfo(Biomes.MUSHROOM_FIELDS, true, 5, 20, 26, 5063283);
        this.loadTradeInfo(Biomes.ICE_SPIKES, true, 5, 20, 26, 2015433);
        this.loadTradeInfo(Biomes.CHERRY_GROVE, true, 5, 20, 26, 15313384);
        inBiomeConditionType = new LootItemConditionType(new InBiomeCondition.InBiomeSerializer());
        Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, new ResourceLocation("quark", "in_biome"), inBiomeConditionType);
        pathfinderMapTrigger = event.getAdvancementModifierRegistry().registerManualTrigger("pathfinder_map_center");
        pathfinders_quill = new PathfindersQuillItem(this);
    }

    @PlayEvent
    public void onTradesLoaded(ZVillagerTrades event) {
        if (event.getType() == VillagerProfession.CARTOGRAPHER && addToCartographer) {
            synchronized (mutex) {
                Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
                for (PathfinderMapsModule.TradeInfo info : tradeList) {
                    if (info != null) {
                        ((List) trades.get(info.level)).add(new PathfinderMapsModule.PathfinderQuillTrade(info, true));
                    }
                }
            }
        }
    }

    @PlayEvent
    public void onWandererTradesLoaded(ZWandererTrades event) {
        if (!addToWanderingTraderForced && (addToWanderingTraderGeneric || addToWanderingTraderRare)) {
            synchronized (mutex) {
                if (!tradeList.isEmpty()) {
                    List<PathfinderMapsModule.PathfinderQuillTrade> quillTrades = (List<PathfinderMapsModule.PathfinderQuillTrade>) tradeList.stream().map(info -> new PathfinderMapsModule.PathfinderQuillTrade(info, false)).collect(Collectors.toList());
                    PathfinderMapsModule.MultiTrade mt = new PathfinderMapsModule.MultiTrade(quillTrades);
                    if (addToWanderingTraderGeneric) {
                        event.getGenericTrades().add(mt);
                    }
                    if (addToWanderingTraderRare) {
                        event.getRareTrades().add(mt);
                    }
                }
            }
        }
    }

    @PlayEvent
    public void livingTick(ZLivingTick event) {
        if (event.getEntity() instanceof WanderingTrader wt && addToWanderingTraderForced && !wt.getPersistentData().getBoolean("quark:checked_pathfinder")) {
            boolean hasPathfinder = false;
            MerchantOffers offers = wt.m_6616_();
            for (MerchantOffer offer : offers) {
                if (offer.getResult().is(pathfinders_quill)) {
                    hasPathfinder = true;
                    break;
                }
            }
            if (!hasPathfinder && !tradeList.isEmpty()) {
                PathfinderMapsModule.TradeInfo info = (PathfinderMapsModule.TradeInfo) tradeList.get(wt.m_9236_().random.nextInt(tradeList.size()));
                PathfinderMapsModule.PathfinderQuillTrade trade = new PathfinderMapsModule.PathfinderQuillTrade(info, false);
                MerchantOffer offerx = trade.getOffer(wt, wt.m_9236_().random);
                if (offerx != null) {
                    offers.add(0, offerx);
                }
            }
            wt.getPersistentData().putBoolean("quark:checked_pathfinder", true);
        }
    }

    @PlayEvent
    public void playerTick(ZPlayerTick.Start event) {
        Player player = event.getPlayer();
        if (player instanceof ServerPlayer) {
            if (!this.tryCheckCenter(player, InteractionHand.MAIN_HAND)) {
                this.tryCheckCenter(player, InteractionHand.OFF_HAND);
            }
        }
    }

    private boolean tryCheckCenter(Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (stack.getItem() == Items.FILLED_MAP && stack.hasTag() && ItemNBTHelper.getBoolean(stack, "quark:is_pathfinder", false)) {
            for (Tag tag : stack.getTag().getList("Decorations", stack.getTag().getId())) {
                if (tag instanceof CompoundTag) {
                    CompoundTag cmp = (CompoundTag) tag;
                    String id = cmp.getString("id");
                    if (id.equals("+")) {
                        double x = cmp.getDouble("x");
                        double z = cmp.getDouble("z");
                        Vec3 pp = player.m_20182_();
                        double px = pp.x;
                        double pz = pp.z;
                        double distSq = (px - x) * (px - x) + (pz - z) * (pz - z);
                        if (distSq < 200.0) {
                            pathfinderMapTrigger.trigger((ServerPlayer) player);
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @LoadEvent
    public final void configChanged(ZConfigChanged event) {
        synchronized (mutex) {
            tradeList.clear();
            customTrades.clear();
            this.loadCustomMaps(this.customs);
            if (applyDefaultTrades) {
                tradeList.addAll(builtinTrades);
            }
            tradeList.addAll(customTrades);
        }
    }

    private void loadTradeInfo(ResourceKey<Biome> biome, boolean enabled, int level, int minPrice, int maxPrice, int color) {
        builtinTrades.add(new PathfinderMapsModule.TradeInfo(biome.location(), enabled, level, minPrice, maxPrice, color));
    }

    private void loadCustomTradeInfo(ResourceLocation biome, boolean enabled, int level, int minPrice, int maxPrice, int color) {
        customTrades.add(new PathfinderMapsModule.TradeInfo(biome, enabled, level, minPrice, maxPrice, color));
    }

    private void loadCustomTradeInfo(String line) throws IllegalArgumentException {
        String[] tokens = line.split(",");
        if (tokens.length != 5 && tokens.length != 6) {
            throw new IllegalArgumentException("Wrong number of parameters " + tokens.length + " (expected 5)");
        } else {
            ResourceLocation biomeName = new ResourceLocation(tokens[0]);
            int level = Integer.parseInt(tokens[1]);
            int minPrice = Integer.parseInt(tokens[2]);
            int maxPrice = Integer.parseInt(tokens[3]);
            int color = Integer.parseInt(tokens[4], 16);
            this.loadCustomTradeInfo(biomeName, true, level, minPrice, maxPrice, color);
        }
    }

    private void loadCustomMaps(Iterable<String> lines) {
        for (String s : lines) {
            try {
                this.loadCustomTradeInfo(s);
            } catch (IllegalArgumentException var5) {
                Quark.LOG.warn("[Custom Pathfinder Maps] Error while reading custom map string \"{}\"", s);
                Quark.LOG.warn("[Custom Pathfinder Maps] - {}", var5.getMessage());
            }
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends PathfinderMapsModule {

        @LoadEvent
        public void clientSetup(ZClientSetup e) {
            e.enqueueWork(() -> ItemProperties.register(pathfinders_quill, new ResourceLocation("has_biome"), (stack, world, entity, i) -> PathfindersQuillItem.getTargetBiome(stack) != null ? 1.0F : 0.0F));
        }

        @PlayEvent
        public void drawHUD(ZRenderGuiOverlay.Hotbar.Post event) {
            if (drawHud) {
                Minecraft mc = Minecraft.getInstance();
                GuiGraphics guiGraphics = event.getGuiGraphics();
                if (mc.screen != null) {
                    return;
                }
                ItemStack quill = PathfindersQuillItem.getActiveQuill(mc.player);
                if (quill != null) {
                    Window window = event.getWindow();
                    int x = 5;
                    int y = PathfinderMapsModule.hudOnTop ? 20 : window.getGuiScaledHeight() - 15;
                    guiGraphics.drawString(mc.font, PathfindersQuillItem.getSearchingComponent(), x, y, 16777215, true);
                    int qy = y - 15;
                    float speed = 0.1F;
                    float total = QuarkClient.ticker.total * speed;
                    float offX = (float) (Math.sin((double) total) + 1.0) * 20.0F;
                    float offY = (float) (Math.sin((double) (total * 8.0F)) - 1.0);
                    if (Math.cos((double) total) < 0.0) {
                        offY = 0.0F;
                    }
                    int qx = x + (int) offX;
                    qy += (int) offY;
                    guiGraphics.renderItem(quill, qx, qy);
                }
            }
        }

        @LoadEvent
        public void registerItemColors(ZAddItemColorHandlers event) {
            ItemColor color = (stack, id) -> id == 0 ? 16777215 : PathfindersQuillItem.getOverlayColor(stack);
            event.register(color, pathfinders_quill);
        }
    }

    private static record MultiTrade(List<? extends VillagerTrades.ItemListing> parents) implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            int idx = random.nextInt(this.parents.size());
            return ((VillagerTrades.ItemListing) this.parents.get(idx)).getOffer(entity, random);
        }
    }

    private static record PathfinderQuillTrade(PathfinderMapsModule.TradeInfo info, boolean hasCompass) implements VillagerTrades.ItemListing {

        @Override
        public MerchantOffer getOffer(@NotNull Entity entity, @NotNull RandomSource random) {
            if (!this.info.enabled) {
                return null;
            } else {
                int i = random.nextInt(this.info.maxPrice - this.info.minPrice + 1) + this.info.minPrice;
                ItemStack itemstack = PathfindersQuillItem.forBiome(this.info.biome.toString(), this.info.color);
                if (itemstack.isEmpty()) {
                    return null;
                } else {
                    int xp = PathfinderMapsModule.xpFromTrade * Math.max(1, this.info.level - 1);
                    return this.hasCompass ? new MerchantOffer(new ItemStack(Items.EMERALD, i), new ItemStack(Items.COMPASS), itemstack, 12, xp, 0.2F) : new MerchantOffer(new ItemStack(Items.EMERALD, i), itemstack, 12, xp, 0.2F);
                }
            }
        }
    }

    public static class TradeInfo implements Predicate<Holder<Biome>>, IConfigType {

        public final ResourceLocation biome;

        public final int color;

        @Config
        public boolean enabled;

        @Config
        public final int level;

        @Config
        public final int minPrice;

        @Config
        public final int maxPrice;

        TradeInfo(ResourceLocation biome, boolean enabled, int level, int minPrice, int maxPrice, int color) {
            this.biome = biome;
            this.enabled = enabled;
            this.level = level;
            this.minPrice = minPrice;
            this.maxPrice = maxPrice;
            this.color = color;
        }

        public boolean test(Holder<Biome> biomeHolder) {
            return biomeHolder.is(this.biome);
        }
    }
}