package se.mickelus.tetra.items;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import se.mickelus.tetra.Tooltips;

@ParametersAreNonnullByDefault
public class ReverberatingPearlItem extends TetraItem {

    private static final String unlocalizedName = "reverberating_pearl";

    public ReverberatingPearlItem() {
        super(new Item.Properties());
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("item.reverberating_pearl.tooltip"));
        tooltip.add(Component.literal(" "));
        if (Screen.hasShiftDown()) {
            tooltip.add(Tooltips.expanded);
            tooltip.add(Tooltips.reveal);
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("item.reverberating_pearl.tooltip_extended"));
        } else {
            tooltip.add(Tooltips.expand);
        }
    }
}