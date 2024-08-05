package se.mickelus.tetra.blocks.geode;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.items.TetraItem;

@ParametersAreNonnullByDefault
public class GeodeItem extends TetraItem {

    public static final String identifier = "geode";

    @ObjectHolder(registryName = "item", value = "tetra:geode")
    public static GeodeItem instance;

    public GeodeItem() {
        super(new Item.Properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("item.tetra.geode.tooltip").withStyle(ChatFormatting.GRAY));
    }
}