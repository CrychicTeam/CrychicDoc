package icyllis.modernui.mc.forge;

import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.testforge.TestContainerMenu;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class MuiRegistries {

    public static final SoundEvent BUTTON_CLICK_1 = SoundEvent.createVariableRangeEvent(ModernUIMod.location("button1"));

    public static final SoundEvent BUTTON_CLICK_2 = SoundEvent.createVariableRangeEvent(ModernUIMod.location("button2"));

    public static final ResourceLocation TEST_MENU_KEY = ModernUIMod.location("test");

    public static final RegistryObject<MenuType<TestContainerMenu>> TEST_MENU = RegistryObject.createOptional(TEST_MENU_KEY, ForgeRegistries.MENU_TYPES.getRegistryKey(), "modernui");

    public static final ResourceLocation PROJECT_BUILDER_ITEM_KEY = ModernUIMod.location("project_builder");

    public static final RegistryObject<Item> PROJECT_BUILDER_ITEM = RegistryObject.createOptional(PROJECT_BUILDER_ITEM_KEY, ForgeRegistries.ITEMS.getRegistryKey(), "modernui");
}