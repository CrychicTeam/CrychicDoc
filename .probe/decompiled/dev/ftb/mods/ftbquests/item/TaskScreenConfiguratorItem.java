package dev.ftb.mods.ftbquests.item;

import dev.ftb.mods.ftbquests.block.TaskScreenBlock;
import dev.ftb.mods.ftbquests.block.entity.ITaskScreen;
import dev.ftb.mods.ftbquests.net.TaskScreenConfigRequest;
import java.util.List;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TaskScreenConfiguratorItem extends Item {

    public TaskScreenConfiguratorItem() {
        super(FTBQuestsItems.defaultProps());
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        if (ctx.getPlayer() instanceof ServerPlayer sp) {
            if (!(level.getBlockEntity(ctx.getClickedPos()) instanceof ITaskScreen)) {
                return this.tryUseOn(sp, ctx.getItemInHand()) ? InteractionResult.CONSUME : InteractionResult.FAIL;
            }
            storeBlockPos(ctx.getItemInHand(), ctx.getLevel(), ctx.getClickedPos());
            ctx.getPlayer().m_9236_().playSound(null, ctx.getClickedPos(), SoundEvents.NOTE_BLOCK_CHIME.value(), SoundSource.BLOCKS, 1.0F, 1.0F);
            ctx.getPlayer().displayClientMessage(Component.translatable("ftbquests.message.configurator_bound", posToString(ctx.getClickedPos())), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (player instanceof ServerPlayer sp) {
            return this.tryUseOn(sp, stack) ? InteractionResultHolder.consume(stack) : InteractionResultHolder.fail(stack);
        } else {
            return InteractionResultHolder.success(stack);
        }
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
        list.add(Component.translatable("item.ftbquests.task_screen_configurator.tooltip").withStyle(ChatFormatting.GRAY));
        readBlockPos(itemStack).ifPresent(gPos -> {
            String str = gPos.dimension().location() + " / " + posToString(gPos.pos());
            list.add(Component.translatable("ftbquests.message.configurator_bound", str).withStyle(ChatFormatting.DARK_AQUA));
        });
    }

    private boolean tryUseOn(ServerPlayer player, ItemStack stack) {
        return (Boolean) readBlockPos(stack).map(gPos -> {
            Level level = player.m_20194_().getLevel(gPos.dimension());
            if (level == player.m_9236_() && player.m_9236_().isLoaded(gPos.pos())) {
                if (level.getBlockEntity(gPos.pos()) instanceof ITaskScreen taskScreen) {
                    if (TaskScreenBlock.hasPermissionToEdit(player, taskScreen)) {
                        taskScreen.getCoreScreen().ifPresent(coreScreen -> new TaskScreenConfigRequest(coreScreen.m_58899_()).sendTo(player));
                        return true;
                    }
                    player.displayClientMessage(Component.translatable("block.ftbquests.screen.no_permission").withStyle(ChatFormatting.RED), true);
                } else {
                    player.displayClientMessage(Component.translatable("ftbquests.message.missing_task_screen").withStyle(ChatFormatting.RED), true);
                }
                return false;
            } else {
                player.displayClientMessage(Component.translatable("ftbquests.message.task_screen_inaccessible").withStyle(ChatFormatting.RED), true);
                return false;
            }
        }).orElse(false);
    }

    public static void storeBlockPos(ItemStack itemInHand, Level level, BlockPos clickedPos) {
        itemInHand.getOrCreateTag().putLong("pos", clickedPos.asLong());
        itemInHand.getOrCreateTag().putString("dim", level.dimension().location().toString());
    }

    public static Optional<GlobalPos> readBlockPos(ItemStack stack) {
        if (stack.getItem() instanceof TaskScreenConfiguratorItem && stack.hasTag() && stack.getTag().contains("pos") && stack.getTag().contains("dim")) {
            ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, new ResourceLocation(stack.getTag().getString("dim")));
            return Optional.of(GlobalPos.of(dim, BlockPos.of(stack.getTag().getLong("pos"))));
        } else {
            return Optional.empty();
        }
    }

    private static String posToString(BlockPos pos) {
        return String.format("[%d,%d,%d]", pos.m_123341_(), pos.m_123342_(), pos.m_123343_());
    }
}