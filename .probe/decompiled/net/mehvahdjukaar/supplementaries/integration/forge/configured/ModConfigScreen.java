package net.mehvahdjukaar.supplementaries.integration.forge.configured;

import com.mrcrayfish.configured.api.IModConfig;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.integration.configured.CustomConfigScreen;
import net.mehvahdjukaar.moonlight.api.integration.configured.CustomConfigSelectScreen;
import net.mehvahdjukaar.moonlight.api.set.wood.WoodTypeRegistry;
import net.mehvahdjukaar.supplementaries.configs.ConfigUtils;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;

public class ModConfigScreen extends CustomConfigScreen {

    private static final Map<String, ItemStack> CUSTOM_ICONS = new HashMap();

    public ModConfigScreen(CustomConfigSelectScreen parent, IModConfig config) {
        super(parent, config);
        this.icons.putAll(CUSTOM_ICONS);
    }

    public ModConfigScreen(String modId, ItemStack mainIcon, ResourceLocation background, Component title, Screen parent, IModConfig config) {
        super(modId, mainIcon, background, title, parent, config);
        this.icons.putAll(CUSTOM_ICONS);
    }

    private static void addIcon(String s, ItemLike i) {
        CUSTOM_ICONS.put(s, i.asItem().getDefaultInstance());
    }

    @Override
    public void onSave() {
        if (this.config.getFileName().contains("common")) {
            ConfigUtils.clientRequestServerConfigReload();
        }
    }

    @Override
    public CustomConfigScreen createSubScreen(Component title) {
        return new ModConfigScreen(this.modId, this.mainIcon, this.background, title, this, this.config);
    }

    static {
        addIcon("blocks", Items.OXIDIZED_COPPER);
        addIcon("entities", Items.RABBIT_HIDE);
        addIcon("general", Items.BOOKSHELF);
        addIcon("particles", Items.BLAZE_POWDER);
        addIcon("items", Items.AMETHYST_SHARD);
        addIcon("spawns", Items.TURTLE_EGG);
        addIcon("tweaks", (ItemLike) ModRegistry.WRENCH.get());
        addIcon("turn particles", (ItemLike) ModRegistry.TURN_TABLE.get());
        addIcon("captured mobs", (ItemLike) ModRegistry.CAGE_ITEM.get());
        addIcon("flag", (ItemLike) ((Supplier) ModRegistry.FLAGS.get(DyeColor.WHITE)).get());
        addIcon("way sign", (ItemLike) ModRegistry.SIGN_POST_ITEMS.get(WoodTypeRegistry.OAK_TYPE));
        addIcon("dispenser tweaks", Items.DISPENSER);
        addIcon("golden apple disenchant", Items.ENCHANTED_GOLDEN_APPLE);
        addIcon("throwable bricks", Items.BRICK);
        addIcon("throwable bricks", Items.BRICK);
        addIcon("rope", (ItemLike) ModRegistry.ROPE.get());
        addIcon("placeable sticks", Items.STICK);
        addIcon("item lore", Items.NAME_TAG);
        addIcon("noteblock scare", Items.NOTE_BLOCK);
        addIcon("timber frame", (ItemLike) ModRegistry.TIMBER_BRACE_ITEM.get());
        addIcon("bottle xp", Items.EXPERIENCE_BOTTLE);
        addIcon("map tweaks", Items.FILLED_MAP);
        addIcon("initialization", (ItemLike) ModRegistry.COG_BLOCK.get());
        addIcon("zombie horse", Items.ZOMBIE_HORSE_SPAWN_EGG);
        addIcon("placeable gunpowder", Items.GUNPOWDER);
        addIcon("mixins", Items.HOPPER);
        addIcon("server protection", Items.COMMAND_BLOCK);
        addIcon("placeable books", Items.ENCHANTED_BOOK);
        addIcon("sign post", (ItemLike) ModRegistry.SIGN_POST_ITEMS.get(WoodTypeRegistry.OAK_TYPE));
        addIcon("wattle and daub", ModRegistry.DAUB_BRACE.get());
        addIcon("shulker helmet", Items.SHULKER_SHELL);
        addIcon("jar tab", (ItemLike) ModRegistry.JAR_ITEM.get());
        addIcon("dispensers", Items.DISPENSER);
        addIcon("blue bomb", (ItemLike) ModRegistry.BOMB_BLUE_ITEM_ON.get());
        addIcon("dispensers", Items.DISPENSER);
        addIcon("cave urns", (ItemLike) ModRegistry.URN.get());
        addIcon("structures", Items.BRICKS);
        addIcon("soap", (ItemLike) ModRegistry.SOAP.get());
        addIcon("mob head tweaks", Items.SKELETON_SKULL);
        addIcon("lantern tweaks", Items.LANTERN);
        addIcon("conditional sign registration", Items.BARRIER);
        addIcon("dispenser minecart", (ItemLike) ModRegistry.DISPENSER_MINECART_ITEM.get());
        addIcon("traders open doors", Items.WANDERING_TRADER_SPAWN_EGG);
        addIcon("basalt ash", Items.BASALT);
        addIcon("cave urns", Items.BONE);
        addIcon("way sign", (ItemLike) ModRegistry.SIGN_POST_ITEMS.get(WoodTypeRegistry.getValue(new ResourceLocation("spruce"))));
        addIcon("stasis", Items.ENCHANTED_BOOK);
        addIcon("banner pattern tooltip", Items.CREEPER_BANNER_PATTERN);
        addIcon("paintings tooltip", Items.PAINTING);
        addIcon("clock right click", Items.CLOCK);
        addIcon("compass right click", Items.COMPASS);
        addIcon("crossbows colors", Items.CROSSBOW);
        addIcon("mob head shaders", Items.DRAGON_HEAD);
        addIcon("placeable books glint", Items.BOOK);
        addIcon("redstone", Items.REDSTONE);
        addIcon("building", Items.OXIDIZED_COPPER);
        addIcon("utilities", Items.BARREL);
        addIcon("functional", Items.AMETHYST_SHARD);
        addIcon("tools", Items.GOLDEN_PICKAXE);
        addIcon("enhanced hanging signs", Items.OAK_HANGING_SIGN);
    }
}