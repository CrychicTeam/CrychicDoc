package net.minecraft.world.inventory;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.flag.FeatureElement;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;

public class MenuType<T extends AbstractContainerMenu> implements FeatureElement {

    public static final MenuType<ChestMenu> GENERIC_9x1 = register("generic_9x1", ChestMenu::m_39234_);

    public static final MenuType<ChestMenu> GENERIC_9x2 = register("generic_9x2", ChestMenu::m_39243_);

    public static final MenuType<ChestMenu> GENERIC_9x3 = register("generic_9x3", ChestMenu::m_39255_);

    public static final MenuType<ChestMenu> GENERIC_9x4 = register("generic_9x4", ChestMenu::m_39258_);

    public static final MenuType<ChestMenu> GENERIC_9x5 = register("generic_9x5", ChestMenu::m_39262_);

    public static final MenuType<ChestMenu> GENERIC_9x6 = register("generic_9x6", ChestMenu::m_39266_);

    public static final MenuType<DispenserMenu> GENERIC_3x3 = register("generic_3x3", DispenserMenu::new);

    public static final MenuType<AnvilMenu> ANVIL = register("anvil", AnvilMenu::new);

    public static final MenuType<BeaconMenu> BEACON = register("beacon", BeaconMenu::new);

    public static final MenuType<BlastFurnaceMenu> BLAST_FURNACE = register("blast_furnace", BlastFurnaceMenu::new);

    public static final MenuType<BrewingStandMenu> BREWING_STAND = register("brewing_stand", BrewingStandMenu::new);

    public static final MenuType<CraftingMenu> CRAFTING = register("crafting", CraftingMenu::new);

    public static final MenuType<EnchantmentMenu> ENCHANTMENT = register("enchantment", EnchantmentMenu::new);

    public static final MenuType<FurnaceMenu> FURNACE = register("furnace", FurnaceMenu::new);

    public static final MenuType<GrindstoneMenu> GRINDSTONE = register("grindstone", GrindstoneMenu::new);

    public static final MenuType<HopperMenu> HOPPER = register("hopper", HopperMenu::new);

    public static final MenuType<LecternMenu> LECTERN = register("lectern", (p_39992_, p_39993_) -> new LecternMenu(p_39992_));

    public static final MenuType<LoomMenu> LOOM = register("loom", LoomMenu::new);

    public static final MenuType<MerchantMenu> MERCHANT = register("merchant", MerchantMenu::new);

    public static final MenuType<ShulkerBoxMenu> SHULKER_BOX = register("shulker_box", ShulkerBoxMenu::new);

    public static final MenuType<SmithingMenu> SMITHING = register("smithing", SmithingMenu::new);

    public static final MenuType<SmokerMenu> SMOKER = register("smoker", SmokerMenu::new);

    public static final MenuType<CartographyTableMenu> CARTOGRAPHY_TABLE = register("cartography_table", CartographyTableMenu::new);

    public static final MenuType<StonecutterMenu> STONECUTTER = register("stonecutter", StonecutterMenu::new);

    private final FeatureFlagSet requiredFeatures;

    private final MenuType.MenuSupplier<T> constructor;

    private static <T extends AbstractContainerMenu> MenuType<T> register(String string0, MenuType.MenuSupplier<T> menuTypeMenuSupplierT1) {
        return Registry.register(BuiltInRegistries.MENU, string0, new MenuType<>(menuTypeMenuSupplierT1, FeatureFlags.VANILLA_SET));
    }

    private static <T extends AbstractContainerMenu> MenuType<T> register(String string0, MenuType.MenuSupplier<T> menuTypeMenuSupplierT1, FeatureFlag... featureFlag2) {
        return Registry.register(BuiltInRegistries.MENU, string0, new MenuType<>(menuTypeMenuSupplierT1, FeatureFlags.REGISTRY.subset(featureFlag2)));
    }

    private MenuType(MenuType.MenuSupplier<T> menuTypeMenuSupplierT0, FeatureFlagSet featureFlagSet1) {
        this.constructor = menuTypeMenuSupplierT0;
        this.requiredFeatures = featureFlagSet1;
    }

    public T create(int int0, Inventory inventory1) {
        return this.constructor.create(int0, inventory1);
    }

    @Override
    public FeatureFlagSet requiredFeatures() {
        return this.requiredFeatures;
    }

    interface MenuSupplier<T extends AbstractContainerMenu> {

        T create(int var1, Inventory var2);
    }
}