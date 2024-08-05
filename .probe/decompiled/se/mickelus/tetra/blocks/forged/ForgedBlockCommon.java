package se.mickelus.tetra.blocks.forged;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

@ParametersAreNonnullByDefault
public class ForgedBlockCommon {

    public static final BlockBehaviour.Properties propertiesSolid = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().sound(SoundType.NETHERITE_BLOCK).strength(12.0F, 2400.0F);

    public static final BlockBehaviour.Properties propertiesNotSolid = BlockBehaviour.Properties.of().requiresCorrectToolForDrops().noOcclusion().sound(SoundType.NETHERITE_BLOCK).isRedstoneConductor(ForgedBlockCommon::notSolid).isSuffocating(ForgedBlockCommon::notSolid).isViewBlocking(ForgedBlockCommon::notSolid).strength(12.0F, 600.0F);

    public static final Component locationTooltip = Component.translatable("item.tetra.forged_description").withStyle(ChatFormatting.GRAY);

    public static final Component unsettlingTooltip = Component.translatable("item.tetra.forged_unsettling").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC);

    private static boolean notSolid(BlockState state, BlockGetter reader, BlockPos pos) {
        return false;
    }
}