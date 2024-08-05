package sereneseasons.init;

import com.google.common.collect.ImmutableList;
import java.lang.reflect.Field;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import sereneseasons.api.SSBlocks;
import sereneseasons.api.SSItems;
import sereneseasons.core.SereneSeasons;

public class ModCreativeTab {

    private static final List<RegistryObject<Item>> ITEM_BLACKLIST = ImmutableList.of(SSItems.SS_ICON);

    private static final List<RegistryObject<Block>> BLOCK_BLACKLIST = ImmutableList.of();

    public static void setup() {
        SereneSeasons.CREATIVE_TAB_REGISTER.register("main", () -> CreativeModeTab.builder().icon(() -> new ItemStack(SSItems.SS_ICON.get())).title(Component.translatable("itemGroup.tabSereneSeasons")).displayItems((displayParameters, output) -> {
            for (Field field : SSBlocks.class.getFields()) {
                if (field.getType() == RegistryObject.class) {
                    try {
                        RegistryObject<Block> block = (RegistryObject<Block>) field.get(null);
                        if (!BLOCK_BLACKLIST.contains(block)) {
                            output.accept(new ItemStack(block.get()));
                        }
                    } catch (IllegalAccessException var8) {
                    }
                }
            }
            for (Field fieldx : SSItems.class.getFields()) {
                if (fieldx.getType() == RegistryObject.class) {
                    try {
                        RegistryObject<Item> item = (RegistryObject<Item>) fieldx.get(null);
                        if (!ITEM_BLACKLIST.contains(item)) {
                            output.accept(new ItemStack(item.get()));
                        }
                    } catch (IllegalAccessException var7) {
                    }
                }
            }
        }).build());
    }
}