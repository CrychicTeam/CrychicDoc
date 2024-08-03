package net.mehvahdjukaar.supplementaries.reg;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.RegHelper;
import net.mehvahdjukaar.supplementaries.Supplementaries;
import net.mehvahdjukaar.supplementaries.common.inventories.CannonContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.NoticeBoardContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.PresentContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.PulleyContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.RedMerchantMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.SafeContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.TrappedPresentContainerMenu;
import net.mehvahdjukaar.supplementaries.common.inventories.VariableSizeContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class ModMenuTypes {

    public static final Supplier<MenuType<PresentContainerMenu>> PRESENT_BLOCK = RegHelper.registerMenuType(Supplementaries.res("present"), PresentContainerMenu::create);

    public static final Supplier<MenuType<TrappedPresentContainerMenu>> TRAPPED_PRESENT_BLOCK = RegHelper.registerMenuType(Supplementaries.res("trapped_present"), TrappedPresentContainerMenu::new);

    public static final Supplier<MenuType<NoticeBoardContainerMenu>> NOTICE_BOARD = RegHelper.registerMenuType(Supplementaries.res("notice_board"), NoticeBoardContainerMenu::new);

    public static final Supplier<MenuType<VariableSizeContainerMenu>> VARIABLE_SIZE = RegHelper.registerMenuType(Supplementaries.res("sack"), VariableSizeContainerMenu::new);

    public static final Supplier<MenuType<SafeContainerMenu>> SAFE = RegHelper.registerMenuType(Supplementaries.res("safe"), SafeContainerMenu::new);

    public static final Supplier<MenuType<PulleyContainerMenu>> PULLEY_BLOCK = RegHelper.registerMenuType(Supplementaries.res("pulley_block"), PulleyContainerMenu::new);

    public static final Supplier<MenuType<CannonContainerMenu>> CANNON = RegHelper.registerMenuType(Supplementaries.res("cannon"), CannonContainerMenu::new);

    public static final Supplier<MenuType<RedMerchantMenu>> RED_MERCHANT = RegHelper.registerMenuType(Supplementaries.res("red_merchant"), RedMerchantMenu::new);

    public static void init() {
    }
}