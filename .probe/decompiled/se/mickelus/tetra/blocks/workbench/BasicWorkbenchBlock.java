package se.mickelus.tetra.blocks.workbench;

import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.tetra.advancements.BlockUseCriterion;

@ParametersAreNonnullByDefault
public class BasicWorkbenchBlock extends AbstractWorkbenchBlock {

    public static final String identifier = "basic_workbench";

    @ObjectHolder(registryName = "block", value = "tetra:basic_workbench")
    public static AbstractWorkbenchBlock instance;

    public BasicWorkbenchBlock() {
        super(BlockBehaviour.Properties.of().strength(2.5F).sound(SoundType.WOOD));
    }

    public static InteractionResult upgradeWorkbench(Player player, Level world, BlockPos pos, InteractionHand hand, Direction facing) {
        ItemStack itemStack = player.m_21120_(hand);
        if (!player.mayUseItemAt(pos.relative(facing), facing, itemStack)) {
            return InteractionResult.FAIL;
        } else if (world.getBlockState(pos).m_60734_().equals(Blocks.CRAFTING_TABLE)) {
            world.playSound(player, pos, SoundEvents.WOOD_PLACE, SoundSource.BLOCKS, 1.0F, 0.5F);
            if (!world.isClientSide) {
                world.setBlockAndUpdate(pos, instance.m_49966_());
                BlockUseCriterion.trigger((ServerPlayer) player, instance.m_49966_(), ItemStack.EMPTY);
            }
            return InteractionResult.sidedSuccess(world.isClientSide);
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter world, List<Component> tooltip, TooltipFlag advanced) {
        tooltip.add(Component.translatable("block.tetra.basic_workbench.description").withStyle(ChatFormatting.GRAY));
    }
}