package net.mehvahdjukaar.supplementaries.integration;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.integration.forge.CCCompatImpl;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class CCCompat {

    @ExpectPlatform
    @Transformed
    public static void setup() {
        CCCompatImpl.setup();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isPrintedBook(Item item) {
        return CCCompatImpl.isPrintedBook(item);
    }

    @ExpectPlatform
    @Transformed
    public static int getPages(ItemStack itemstack) {
        return CCCompatImpl.getPages(itemstack);
    }

    @ExpectPlatform
    @Transformed
    public static String[] getText(ItemStack itemstack) {
        return CCCompatImpl.getText(itemstack);
    }
}