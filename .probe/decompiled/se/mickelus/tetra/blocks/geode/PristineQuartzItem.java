package se.mickelus.tetra.blocks.geode;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class PristineQuartzItem extends TetraItem {

    public static final String identifier = "pristine_quartz";

    @ObjectHolder(registryName = "item", value = "tetra:pristine_quartz")
    public static PristineQuartzItem instance;

    public PristineQuartzItem() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag advanced) {
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Component.translatable("item.tetra.pristine_gem.description").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }
}