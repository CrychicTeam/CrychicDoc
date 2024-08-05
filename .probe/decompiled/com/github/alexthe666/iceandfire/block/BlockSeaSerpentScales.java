package com.github.alexthe666.iceandfire.block;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockSeaSerpentScales extends Block {

    ChatFormatting color;

    String name;

    public BlockSeaSerpentScales(String name, ChatFormatting color) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(30.0F, 500.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
        this.color = color;
        this.name = name;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("sea_serpent." + this.name).withStyle(this.color));
    }
}