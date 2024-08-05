package fr.frinn.custommachinery.common.init;

import fr.frinn.custommachinery.common.util.Utils;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

public class BoxCreatorItem extends Item {

    private static final String FIRST_BLOCK_KEY = "firstBlock";

    private static final String SECOND_BLOCK_KEY = "secondBlock";

    public BoxCreatorItem(Item.Properties properties) {
        super(properties);
    }

    @Nullable
    public static BlockPos getSelectedBlock(boolean first, ItemStack stack) {
        CompoundTag nbt = stack.getTagElement("custommachinery");
        return nbt != null && nbt.getLong(first ? "firstBlock" : "secondBlock") != 0L ? BlockPos.of(nbt.getLong(first ? "firstBlock" : "secondBlock")) : null;
    }

    public static void setSelectedBlock(boolean first, ItemStack stack, BlockPos pos) {
        long packed = pos.asLong();
        stack.getOrCreateTagElement("custommachinery").putLong(first ? "firstBlock" : "secondBlock", packed);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, world, tooltip, flag);
        BlockPos block1 = getSelectedBlock(true, stack);
        if (block1 != null) {
            tooltip.add(Component.translatable("custommachinery.box_creator.first_block", block1.m_123344_()).withStyle(ChatFormatting.BLUE));
        } else {
            tooltip.add(Component.translatable("custommachinery.box_creator.select_first_block").withStyle(ChatFormatting.BLUE));
        }
        BlockPos block2 = getSelectedBlock(false, stack);
        if (block2 != null) {
            tooltip.add(Component.translatable("custommachinery.box_creator.second_block", block2.m_123344_()).withStyle(ChatFormatting.RED));
        } else {
            tooltip.add(Component.translatable("custommachinery.box_creator.select_second_block").withStyle(ChatFormatting.RED));
        }
        if (block1 != null && block2 != null) {
            tooltip.add(Component.translatable("custommachinery.box_creator.select_machine").withStyle(ChatFormatting.GREEN));
        }
        tooltip.add(Component.translatable("custommachinery.box_creator.reset").withStyle(ChatFormatting.GOLD));
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getPlayer() == null) {
            return InteractionResult.PASS;
        } else {
            ItemStack stack = context.getItemInHand();
            BlockPos pos = context.getClickedPos();
            BlockPos block1 = getSelectedBlock(true, stack);
            BlockPos block2 = getSelectedBlock(false, stack);
            if (!(context.getLevel().getBlockState(pos).m_60734_() instanceof CustomMachineBlock)) {
                setSelectedBlock(false, stack, pos);
                return InteractionResult.SUCCESS;
            } else if (block1 != null && block2 != null && !context.getLevel().isClientSide()) {
                AABB aabb = new AABB(block1, block2);
                aabb = aabb.move((double) (-pos.m_123341_()), (double) (-pos.m_123342_()), (double) (-pos.m_123343_()));
                Direction direction = (Direction) context.getLevel().getBlockState(pos).m_61143_(BlockStateProperties.HORIZONTAL_FACING);
                aabb = Utils.rotateBox(aabb, direction);
                String boxString = "[" + (int) aabb.minX + ", " + (int) aabb.minY + ", " + (int) aabb.minZ + ", " + (int) aabb.maxX + ", " + (int) aabb.maxY + ", " + (int) aabb.maxZ + "]";
                Component boxText = Component.literal(boxString).withStyle(Style.EMPTY.applyFormat(ChatFormatting.AQUA).withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.translatable("custommachinery.box_creator.copy"))).withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, boxString)));
                Component message = Component.translatable("custommachinery.box_creator.create_box", boxText);
                context.getPlayer().m_213846_(message);
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.PASS;
            }
        }
    }

    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        if (player.m_6047_() && stack.getItem() == this) {
            stack.removeTagKey("custommachinery");
            return InteractionResultHolder.success(stack);
        } else {
            return super.use(level, player, hand);
        }
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return true;
    }
}