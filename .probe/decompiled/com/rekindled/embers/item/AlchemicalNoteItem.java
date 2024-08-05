package com.rekindled.embers.item;

import com.rekindled.embers.render.AlchemicalNoteItemRenderer;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

public class AlchemicalNoteItem extends Item {

    public AlchemicalNoteItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new AlchemicalNoteItem.AlchemicalNoteItemExtensions());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag isAdvanced) {
        ItemStack result = AlchemyHintItem.getResult(stack);
        if (!result.isEmpty()) {
            tooltip.add(Component.translatable(result.getDescriptionId()).withStyle(ChatFormatting.GRAY));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class AlchemicalNoteItemExtensions implements IClientItemExtensions {

        @Override
        public BlockEntityWithoutLevelRenderer getCustomRenderer() {
            Minecraft minecraft = Minecraft.getInstance();
            return new AlchemicalNoteItemRenderer(minecraft.getBlockEntityRenderDispatcher(), minecraft.getEntityModels());
        }
    }
}