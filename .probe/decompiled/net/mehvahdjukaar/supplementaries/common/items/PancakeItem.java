package net.mehvahdjukaar.supplementaries.common.items;

import java.util.List;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacement;
import net.mehvahdjukaar.supplementaries.reg.ModRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.RecordItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

public class PancakeItem extends RecordItem {

    public PancakeItem(int i, SoundEvent soundEvent, Item.Properties properties, int seconds) {
        super(i, soundEvent, properties, seconds);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
    }

    @Override
    public String getDescriptionId() {
        return ((Block) ModRegistry.PANCAKE.get()).getDescriptionId();
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack itemStack = context.getItemInHand();
        int oldAmount = itemStack.getCount();
        itemStack.setCount(1);
        InteractionResult r = super.useOn(context);
        if (itemStack.isEmpty()) {
            itemStack.setCount(oldAmount - 1);
        } else {
            itemStack.setCount(oldAmount);
        }
        return !r.consumesAction() ? AdditionalItemPlacement.getBlockPlacer().mimicUseOn(context, (Block) ModRegistry.PANCAKE.get(), null) : r;
    }
}