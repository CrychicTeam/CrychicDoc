package net.mehvahdjukaar.supplementaries.common.block.dispenser;

import java.util.HashSet;
import java.util.Set;
import net.mehvahdjukaar.moonlight.api.fluids.FluidContainerList;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluid;
import net.mehvahdjukaar.moonlight.api.fluids.SoftFluidRegistry;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.util.DispenserHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.entities.RopeArrowEntity;
import net.mehvahdjukaar.supplementaries.common.items.DispenserMinecartItem;
import net.mehvahdjukaar.supplementaries.common.items.KeyItem;
import net.mehvahdjukaar.supplementaries.common.misc.mob_container.BucketHelper;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.core.BlockSource;
import net.minecraft.core.Position;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.core.dispenser.DispenseItemBehavior;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;

public class DispenserBehaviorsManager {

    public static void registerBehaviors(RegistryAccess registryAccess) {
        boolean isForge = PlatHelper.getPlatform().isForge();
        if ((Boolean) CommonConfigs.General.DISPENSERS.get()) {
            for (SoftFluid f : SoftFluidRegistry.getRegistry(registryAccess)) {
                registerFluidBehavior(f);
            }
            if ((Boolean) CommonConfigs.Building.PANCAKES_ENABLED.get()) {
                DispenserHelper.registerCustomBehavior(new PancakeBehavior(Items.HONEY_BOTTLE));
            }
            if ((Boolean) CommonConfigs.Tweaks.ENDER_PEAR_DISPENSERS.get()) {
                DispenserHelper.registerCustomBehavior(new EnderPearlBehavior());
            }
            if ((Boolean) CommonConfigs.Redstone.DISPENSER_MINECART_ENABLED.get()) {
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.DISPENSER_MINECART_ITEM.get(), DispenserMinecartItem.DISPENSE_ITEM_BEHAVIOR);
            }
            if ((Boolean) CommonConfigs.Redstone.ENDERMAN_HEAD_ENABLED.get()) {
                DispenseItemBehavior armorBehavior = new OptionalDispenseItemBehavior() {

                    @Override
                    protected ItemStack execute(BlockSource source, ItemStack stack) {
                        this.m_123573_(ArmorItem.dispenseArmor(source, stack));
                        return stack;
                    }
                };
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.ENDERMAN_SKULL_ITEM.get(), armorBehavior);
            }
            if ((Boolean) CommonConfigs.Functional.FODDER_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.FODDER.get());
            }
            if ((Boolean) CommonConfigs.Functional.SOAP_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.BUBBLE_BLOCK.get());
            }
            if ((Boolean) CommonConfigs.Functional.SACK_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.SACK.get());
            }
            if ((Boolean) CommonConfigs.Functional.JAR_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.JAR_ITEM.get());
                DispenserHelper.registerCustomBehavior(new DispenserHelper.AddItemToInventoryBehavior(Items.COOKIE));
            }
            DispenserHelper.registerCustomBehavior(new FlintAndSteelBehavior(Items.FLINT_AND_STEEL));
            if ((Boolean) CommonConfigs.Functional.BAMBOO_SPIKES_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.BAMBOO_SPIKES_ITEM.get());
            }
            if ((Boolean) CommonConfigs.Functional.TIPPED_SPIKES_ENABLED.get()) {
                DispenserHelper.registerPlaceBlockBehavior((ItemLike) ModRegistry.BAMBOO_SPIKES_TIPPED_ITEM.get());
                DispenserHelper.registerCustomBehavior(new BambooSpikesBehavior(Items.LINGERING_POTION));
            }
            if (isForge) {
                DispenserHelper.registerCustomBehavior(new FakePlayerUseItemBehavior((Item) ModRegistry.SOAP.get()));
            }
            if ((Boolean) CommonConfigs.Tweaks.THROWABLE_BRICKS_ENABLED.get()) {
                BuiltInRegistries.ITEM.m_206058_(ModTags.BRICKS).iterator().forEachRemaining(h -> DispenserHelper.registerCustomBehavior(new ThrowableBricksBehavior((Item) h.value())));
            }
            if ((Boolean) CommonConfigs.Tools.BOMB_ENABLED.get()) {
                BombsBehavior bombBehavior = new BombsBehavior();
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_ITEM.get(), bombBehavior);
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_ITEM_ON.get(), bombBehavior);
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_BLUE_ITEM.get(), bombBehavior);
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_BLUE_ITEM_ON.get(), bombBehavior);
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_SPIKY_ITEM.get(), bombBehavior);
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.BOMB_SPIKY_ITEM_ON.get(), bombBehavior);
            }
            if ((Boolean) CommonConfigs.Tweaks.PLACEABLE_GUNPOWDER.get()) {
                DispenserHelper.registerCustomBehavior(new GunpowderBehavior(Items.GUNPOWDER));
            }
            if ((Boolean) CommonConfigs.Tools.ROPE_ARROW_ENABLED.get()) {
                DispenserBlock.registerBehavior((ItemLike) ModRegistry.ROPE_ARROW_ITEM.get(), new AbstractProjectileDispenseBehavior() {

                    @Override
                    protected Projectile getProjectile(Level world, Position pos, ItemStack stack) {
                        CompoundTag com = stack.getTag();
                        int charges = stack.getMaxDamage();
                        if (com != null && com.contains("Damage")) {
                            charges -= com.getInt("Damage");
                        }
                        RopeArrowEntity arrow = new RopeArrowEntity(world, pos.x(), pos.y(), pos.z(), charges);
                        arrow.f_36705_ = AbstractArrow.Pickup.ALLOWED;
                        return arrow;
                    }
                });
            }
            boolean axe = (Boolean) CommonConfigs.Tweaks.AXE_DISPENSER_BEHAVIORS.get();
            boolean jar = (Boolean) CommonConfigs.Functional.JAR_ENABLED.get();
            boolean key = CommonConfigs.isEnabled("key");
            if (axe || jar || key) {
                for (Item i : BuiltInRegistries.ITEM) {
                    try {
                        if (jar && BucketHelper.isFishBucket(i)) {
                            DispenserHelper.registerCustomBehavior(new FishBucketJarBehavior(i));
                        }
                        if (isForge && axe && i instanceof AxeItem) {
                            DispenserHelper.registerCustomBehavior(new FakePlayerUseItemBehavior(i));
                        }
                        if (key && i instanceof KeyItem) {
                            DispenserHelper.registerCustomBehavior(new KeyBehavior(i));
                        }
                    } catch (Exception var8) {
                        Supplementaries.LOGGER.warn("Error white registering dispenser behavior for item {}: {}", i, var8);
                    }
                }
            }
        }
    }

    public static void registerFluidBehavior(SoftFluid f) {
        Set<Item> itemSet = new HashSet();
        for (FluidContainerList.Category c : f.getContainerList().getCategories()) {
            Item empty = c.getEmptyContainer();
            if (empty != Items.AIR && !itemSet.contains(empty)) {
                DispenserHelper.registerCustomBehavior(new FillFluidHolderBehavior(empty));
                itemSet.add(empty);
            }
            for (Item full : c.getFilledItems()) {
                if (full != Items.AIR && !itemSet.contains(full)) {
                    DispenserHelper.registerCustomBehavior(new FillFluidHolderBehavior(full));
                    itemSet.add(full);
                }
            }
        }
    }
}