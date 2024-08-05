package org.violetmoon.quark.addons.oddities.module;

import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeableArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.violetmoon.quark.addons.oddities.client.screen.BackpackInventoryScreen;
import org.violetmoon.quark.addons.oddities.inventory.BackpackMenu;
import org.violetmoon.quark.addons.oddities.item.BackpackItem;
import org.violetmoon.quark.base.Quark;
import org.violetmoon.quark.base.QuarkClient;
import org.violetmoon.quark.base.client.handler.ModelHandler;
import org.violetmoon.quark.base.network.message.oddities.HandleBackpackMessage;
import org.violetmoon.zeta.block.ZetaBlock;
import org.violetmoon.zeta.client.event.load.ZAddItemColorHandlers;
import org.violetmoon.zeta.client.event.load.ZClientSetup;
import org.violetmoon.zeta.client.event.play.ZClientTick;
import org.violetmoon.zeta.client.event.play.ZScreen;
import org.violetmoon.zeta.config.Config;
import org.violetmoon.zeta.event.bus.LoadEvent;
import org.violetmoon.zeta.event.bus.PlayEvent;
import org.violetmoon.zeta.event.load.ZCommonSetup;
import org.violetmoon.zeta.event.load.ZRegister;
import org.violetmoon.zeta.event.play.entity.living.ZLivingDrops;
import org.violetmoon.zeta.event.play.entity.player.ZPlayerInteract;
import org.violetmoon.zeta.item.ZetaItem;
import org.violetmoon.zeta.module.ZetaLoadModule;
import org.violetmoon.zeta.module.ZetaModule;
import org.violetmoon.zeta.util.Hint;

@ZetaLoadModule(category = "oddities")
public class BackpackModule extends ZetaModule {

    @Config(description = "Set this to true to allow the backpacks to be unequipped even with items in them")
    public static boolean superOpMode = false;

    @Config(flag = "ravager_hide")
    public static boolean enableRavagerHide = true;

    @Config
    public static boolean itemsInBackpackTick = true;

    @Config
    public static int baseRavagerHideDrop = 1;

    @Config
    public static double extraChancePerLooting = 0.5;

    @Config
    public static boolean allowArmorStandUnloading = true;

    @Hint
    public static Item backpack;

    @Hint("ravager_hide")
    public static Item ravager_hide;

    public static Block bonded_ravager_hide;

    public static TagKey<Item> backpackBlockedTag;

    public static MenuType<BackpackMenu> menyType;

    private static ItemStack heldStack = null;

    @LoadEvent
    public final void register(ZRegister event) {
        backpack = new BackpackItem(this);
        ravager_hide = new ZetaItem("ravager_hide", this, new Item.Properties().rarity(Rarity.RARE)).setCondition(() -> enableRavagerHide).setCreativeTab(CreativeModeTabs.INGREDIENTS, Items.RABBIT_HIDE, false);
        menyType = IForgeMenuType.create(BackpackMenu::fromNetwork);
        Quark.ZETA.registry.register(menyType, "backpack", Registries.MENU);
        bonded_ravager_hide = new ZetaBlock("bonded_ravager_hide", this, BlockBehaviour.Properties.of().mapColor(DyeColor.BLACK).instrument(NoteBlockInstrument.GUITAR).strength(1.0F).sound(SoundType.WOOL).ignitedByLava()).setCondition(() -> enableRavagerHide).setCreativeTab(CreativeModeTabs.BUILDING_BLOCKS);
        CauldronInteraction.WATER.put(backpack, CauldronInteraction.DYED_ITEM);
    }

    @LoadEvent
    public final void setup(ZCommonSetup event) {
        backpackBlockedTag = ItemTags.create(new ResourceLocation("quark", "backpack_blocked"));
    }

    @PlayEvent
    public void onDrops(ZLivingDrops event) {
        LivingEntity entity = event.getEntity();
        if (enableRavagerHide && entity.m_6095_() == EntityType.RAVAGER) {
            int amount = baseRavagerHideDrop;
            double chance;
            for (chance = (double) event.getLootingLevel() * extraChancePerLooting; chance > (double) baseRavagerHideDrop; amount++) {
                chance--;
            }
            if (chance > 0.0 && entity.m_9236_().random.nextDouble() < chance) {
                amount++;
            }
            event.getDrops().add(new ItemEntity(entity.m_9236_(), entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), new ItemStack(ravager_hide, amount)));
        }
    }

    @PlayEvent
    public void onArmorStandInteract(ZPlayerInteract.EntityInteractSpecific event) {
        if (allowArmorStandUnloading && event.getTarget() instanceof ArmorStand as && event.getHand() == InteractionHand.MAIN_HAND && !event.getLevel().isClientSide) {
            Player player = event.getEntity();
            if (player.m_6047_() || !player.m_21205_().isEmpty() || !player.m_21206_().isEmpty()) {
                return;
            }
            ItemStack playerChest = player.getItemBySlot(EquipmentSlot.CHEST);
            ItemStack armorStandChest = as.getItemBySlot(EquipmentSlot.CHEST);
            if (playerChest.is(backpack) || armorStandChest.is(backpack)) {
                ItemStack playerCopy = playerChest.copy();
                ItemStack armorStandCopy = armorStandChest.copy();
                as.setItemSlot(EquipmentSlot.CHEST, playerCopy);
                player.setItemSlot(EquipmentSlot.CHEST, armorStandCopy);
                player.m_6674_(InteractionHand.MAIN_HAND);
                player.m_216990_(SoundEvents.ARMOR_EQUIP_LEATHER);
                event.setCanceled(true);
                event.setCancellationResult(InteractionResult.sidedSuccess(player.m_9236_().isClientSide));
            }
        }
    }

    public static void requestBackpack() {
        heldStack = Minecraft.getInstance().player.f_36095_.m_142621_();
        QuarkClient.ZETA_CLIENT.sendToServer(new HandleBackpackMessage(true));
    }

    public static boolean isEntityWearingBackpack(Entity e) {
        if (e instanceof LivingEntity living) {
            ItemStack chestArmor = living.getItemBySlot(EquipmentSlot.CHEST);
            return chestArmor.getItem() instanceof BackpackItem;
        } else {
            return false;
        }
    }

    public static boolean isEntityWearingBackpack(Entity e, ItemStack stack) {
        if (e instanceof LivingEntity living) {
            ItemStack chestArmor = living.getItemBySlot(EquipmentSlot.CHEST);
            return chestArmor == stack;
        } else {
            return false;
        }
    }

    @ZetaLoadModule(clientReplacement = true)
    public static class Client extends BackpackModule {

        private static boolean backpackRequested;

        @LoadEvent
        public void clientSetup(ZClientSetup e) {
            e.enqueueWork(() -> {
                MenuScreens.register(menyType, BackpackInventoryScreen::new);
                ItemProperties.register(backpack, new ResourceLocation("has_items"), (stack, world, entity, i) -> !BackpackModule.superOpMode && BackpackItem.doesBackpackHaveItems(stack) ? 1.0F : 0.0F);
                QuarkClient.ZETA_CLIENT.setHumanoidArmorModel(backpack.asItem(), (living, stack, slot, original) -> ModelHandler.armorModel(ModelHandler.backpack, slot));
            });
        }

        @PlayEvent
        public void onOpenGUI(ZScreen.Opening event) {
            Player player = Minecraft.getInstance().player;
            if (player != null && isInventoryGUI(event.getScreen()) && !player.getAbilities().instabuild && isEntityWearingBackpack(player) && !player.f_19817_) {
                requestBackpack();
                event.setCanceled(true);
            }
        }

        @PlayEvent
        public void clientTick(ZClientTick event) {
            Minecraft mc = Minecraft.getInstance();
            if (isInventoryGUI(mc.screen) && !backpackRequested && isEntityWearingBackpack(mc.player) && !mc.player.f_19817_) {
                requestBackpack();
                mc.player.f_36095_.m_142503_(mc.player.m_6844_(EquipmentSlot.CHEST));
                backpackRequested = true;
            } else if (mc.screen instanceof BackpackInventoryScreen) {
                if (BackpackModule.heldStack != null) {
                    mc.player.f_36095_.m_142503_(BackpackModule.heldStack);
                    BackpackModule.heldStack = null;
                }
                backpackRequested = false;
            }
        }

        @LoadEvent
        public void registerItemColors(ZAddItemColorHandlers event) {
            ItemColor color = (stack, i) -> i > 0 ? -1 : ((DyeableArmorItem) stack.getItem()).m_41121_(stack);
            event.register(color, backpack);
        }

        private static boolean isInventoryGUI(Screen gui) {
            return gui != null && gui.getClass() == InventoryScreen.class;
        }
    }
}