package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexthe666.citadel.item.BlockItemWithSupplier;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockItemWithSupplierLore extends BlockItemWithSupplier {

    private final RegistryObject<Block> block;

    public BlockItemWithSupplierLore(RegistryObject<Block> blockSupplier, Item.Properties props) {
        super(blockSupplier, props);
        this.block = blockSupplier;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        String blockName = this.block.getId().getNamespace() + "." + this.block.getId().getPath();
        tooltip.add(Component.translatable("block." + blockName + ".desc").withStyle(ChatFormatting.GRAY));
        super.m_7373_(stack, worldIn, tooltip, flagIn);
    }
}