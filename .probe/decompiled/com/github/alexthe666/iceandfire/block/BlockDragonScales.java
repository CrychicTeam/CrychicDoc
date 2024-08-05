package com.github.alexthe666.iceandfire.block;

import com.github.alexthe666.iceandfire.enums.EnumDragonEgg;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;

public class BlockDragonScales extends Block implements IDragonProof {

    EnumDragonEgg type;

    public BlockDragonScales(EnumDragonEgg type) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM).dynamicShape().strength(30.0F, 500.0F).sound(SoundType.STONE).requiresCorrectToolForDrops());
        this.type = type;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable BlockGetter worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        tooltip.add(Component.translatable("dragon." + this.type.toString().toLowerCase()).withStyle(this.type.color));
    }
}