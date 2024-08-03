package com.rekindled.embers.compat.curios;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.capabilities.EmbersCapabilities;
import com.rekindled.embers.api.power.IEmberCapability;
import com.rekindled.embers.block.ExplosionPedestalBlock;
import com.rekindled.embers.blockentity.ExplosionPedestalBlockEntity;
import com.rekindled.embers.datagen.EmbersSounds;
import com.rekindled.embers.item.DawnstoneMailItem;
import com.rekindled.embers.item.EmberBulbItem;
import com.rekindled.embers.item.EmberDiscountBaubleItem;
import com.rekindled.embers.item.EmberStorageItem;
import com.rekindled.embers.item.ExplosionCharmItem;
import com.rekindled.embers.item.GenericCurioItemItem;
import com.rekindled.embers.item.NonbeleiverAmuletItem;
import com.rekindled.embers.research.ResearchBase;
import com.rekindled.embers.research.ResearchManager;
import com.rekindled.embers.research.subtypes.ResearchShowItem;
import java.util.Map;
import java.util.function.Predicate;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;

public class CuriosCompat {

    public static final RegistryObject<Block> EXPLOSION_PEDESTAL = RegistryManager.BLOCKS.register("explosion_pedestal", () -> new ExplosionPedestalBlock(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_ORANGE).sound(EmbersSounds.CAMINITE).requiresCorrectToolForDrops().strength(1.6F).noOcclusion(), EmbersSounds.MULTIBLOCK_EXTRA));

    public static final RegistryObject<Item> EMBER_RING = RegistryManager.ITEMS.register("ember_ring", () -> new EmberDiscountBaubleItem(new Item.Properties().stacksTo(1), 0.15));

    public static final RegistryObject<Item> EMBER_BELT = RegistryManager.ITEMS.register("ember_belt", () -> new EmberDiscountBaubleItem(new Item.Properties().stacksTo(1), 0.25));

    public static final RegistryObject<Item> EMBER_AMULET = RegistryManager.ITEMS.register("ember_amulet", () -> new EmberDiscountBaubleItem(new Item.Properties().stacksTo(1), 0.2));

    public static final RegistryObject<Item> EMBER_BULB = RegistryManager.ITEMS.register("ember_bulb", () -> new EmberBulbItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> DAWNSTONE_MAIL = RegistryManager.ITEMS.register("dawnstone_mail", () -> new DawnstoneMailItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> ASHEN_AMULET = RegistryManager.ITEMS.register("ashen_amulet", () -> new GenericCurioItemItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> NONBELEIVER_AMULET = RegistryManager.ITEMS.register("nonbeliever_amulet", () -> new NonbeleiverAmuletItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EXPLOSION_CHARM = RegistryManager.ITEMS.register("explosion_charm", () -> new ExplosionCharmItem(new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> EXPLOSION_PEDESTAL_ITEM = RegistryManager.ITEMS.register("explosion_pedestal", () -> new BlockItem(EXPLOSION_PEDESTAL.get(), new Item.Properties()));

    public static final RegistryObject<BlockEntityType<ExplosionPedestalBlockEntity>> EXPLOSION_PEDESTAL_ENTITY = RegistryManager.BLOCK_ENTITY_TYPES.register("explosion_pedestal", () -> BlockEntityType.Builder.of(ExplosionPedestalBlockEntity::new, EXPLOSION_PEDESTAL.get()).build(null));

    public static void init() {
    }

    @OnlyIn(Dist.CLIENT)
    public static void registerColorHandler(RegisterColorHandlersEvent.Item event, ItemColor itemColor) {
        event.register(itemColor, EMBER_BULB.get());
    }

    public static boolean checkForCurios(LivingEntity living, Predicate<ItemStack> predicate) {
        LazyOptional<ICuriosItemHandler> inv = CuriosApi.getCuriosInventory(living);
        if (inv.isPresent()) {
            Map<String, ICurioStacksHandler> curios = ((ICuriosItemHandler) inv.resolve().get()).getCurios();
            for (ICurioStacksHandler curio : curios.values()) {
                for (int i = 0; i < curio.getStacks().getSlots(); i++) {
                    if (predicate.test(curio.getStacks().getStackInSlot(i))) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static double getEmberCapacityTotal(LivingEntity living) {
        double amount = 0.0;
        LazyOptional<ICuriosItemHandler> inv = CuriosApi.getCuriosInventory(living);
        if (inv.isPresent()) {
            Map<String, ICurioStacksHandler> curios = ((ICuriosItemHandler) inv.resolve().get()).getCurios();
            for (ICurioStacksHandler curio : curios.values()) {
                for (int i = 0; i < curio.getStacks().getSlots(); i++) {
                    IEmberCapability capability = (IEmberCapability) curio.getStacks().getStackInSlot(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
                    if (capability != null) {
                        amount += capability.getEmberCapacity();
                    }
                }
            }
        }
        return amount;
    }

    public static double getEmberTotal(LivingEntity living) {
        double amount = 0.0;
        LazyOptional<ICuriosItemHandler> inv = CuriosApi.getCuriosInventory(living);
        if (inv.isPresent()) {
            Map<String, ICurioStacksHandler> curios = ((ICuriosItemHandler) inv.resolve().get()).getCurios();
            for (ICurioStacksHandler curio : curios.values()) {
                for (int i = 0; i < curio.getStacks().getSlots(); i++) {
                    IEmberCapability capability = (IEmberCapability) curio.getStacks().getStackInSlot(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
                    if (capability != null) {
                        amount += capability.getEmber();
                    }
                }
            }
        }
        return amount;
    }

    public static double removeEmber(LivingEntity living, double amount) {
        LazyOptional<ICuriosItemHandler> inv = CuriosApi.getCuriosInventory(living);
        if (inv.isPresent()) {
            Map<String, ICurioStacksHandler> curios = ((ICuriosItemHandler) inv.resolve().get()).getCurios();
            for (ICurioStacksHandler curio : curios.values()) {
                for (int i = 0; i < curio.getStacks().getSlots(); i++) {
                    IEmberCapability capability = (IEmberCapability) curio.getStacks().getStackInSlot(i).getCapability(EmbersCapabilities.EMBER_CAPABILITY, null).orElse(null);
                    if (capability != null) {
                        amount -= capability.removeAmount(amount, true);
                        if (amount <= 0.0) {
                            return amount;
                        }
                    }
                }
            }
        }
        return amount;
    }

    public static void initCuriosCategory() {
        ItemStack fullBulb = EmberStorageItem.withFill(EMBER_BULB.get(), ((EmberBulbItem) EMBER_BULB.get()).getCapacity());
        ResearchManager.cost_reduction = new ResearchShowItem("cost_reduction", new ItemStack(EMBER_AMULET.get()), 5.0, 5.0).addItem(new ResearchShowItem.DisplayItem(new ItemStack(EMBER_AMULET.get()), new ItemStack(EMBER_BELT.get()), new ItemStack(EMBER_RING.get())));
        ResearchManager.mantle_bulb = new ResearchBase("mantle_bulb", fullBulb, 7.0, 3.0);
        ResearchManager.dawnstone_mail = new ResearchBase("dawnstone_mail", new ItemStack(DAWNSTONE_MAIL.get()), 3.0, 7.0);
        ResearchManager.ashen_amulet = new ResearchBase("ashen_amulet", new ItemStack(ASHEN_AMULET.get()), 4.0, 3.0);
        ResearchManager.nonbeliever_amulet = new ResearchBase("nonbeliever_amulet", new ItemStack(NONBELEIVER_AMULET.get()), 1.0, 3.0);
        ResearchManager.explosion_charm = new ResearchBase("explosion_charm", new ItemStack(EXPLOSION_CHARM.get()), 9.0, 2.0);
        ResearchManager.explosion_pedestal = new ResearchBase("explosion_pedestal", new ItemStack(EXPLOSION_PEDESTAL_ITEM.get()), 11.0, 1.0).addAncestor(ResearchManager.explosion_charm);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.cost_reduction);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.mantle_bulb);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.dawnstone_mail);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.ashen_amulet);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.nonbeliever_amulet);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.explosion_charm);
        ResearchManager.subCategoryBaubles.addResearch(ResearchManager.explosion_pedestal);
    }
}