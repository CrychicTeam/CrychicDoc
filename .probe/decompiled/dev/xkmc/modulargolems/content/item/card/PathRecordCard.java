package dev.xkmc.modulargolems.content.item.card;

import dev.xkmc.l2library.util.nbt.ItemCompoundTag;
import dev.xkmc.l2serial.serialization.codec.TagCodec;
import dev.xkmc.modulargolems.content.client.outline.BlockOutliner;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import org.jetbrains.annotations.Nullable;

public class PathRecordCard extends Item {

    private static final String KEY = "RecordedPath";

    public static List<PathRecordCard.Pos> getList(ItemStack stack) {
        List<PathRecordCard.Pos> ans = new ArrayList();
        CompoundTag root = stack.getTag();
        if (root == null) {
            return ans;
        } else if (!root.contains("RecordedPath")) {
            return ans;
        } else {
            ListTag list = root.getList("RecordedPath", 10);
            for (int i = 0; i < list.size(); i++) {
                ans.add((PathRecordCard.Pos) TagCodec.valueFromTag(list.getCompound(i), PathRecordCard.Pos.class));
            }
            return ans;
        }
    }

    public static void addPos(ItemStack stack, PathRecordCard.Pos pos) {
        ItemCompoundTag.of(stack).getSubList("RecordedPath", 10).addCompound().setTag((CompoundTag) TagCodec.valueToTag(pos));
    }

    public static void setList(ItemStack stack, List<PathRecordCard.Pos> pos) {
        ItemCompoundTag.of(stack).getSubList("RecordedPath", 10).clear();
        for (PathRecordCard.Pos e : pos) {
            addPos(stack, e);
        }
    }

    public static boolean togglePos(ItemStack stack, PathRecordCard.Pos pos) {
        List<PathRecordCard.Pos> ans = getList(stack);
        if (!ans.contains(pos)) {
            addPos(stack, pos);
            return true;
        } else {
            ans.remove(pos);
            setList(stack, ans);
            return false;
        }
    }

    public PathRecordCard(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        ItemStack stack = ctx.getItemInHand();
        Level level = ctx.getLevel();
        if (!level.isClientSide()) {
            BlockPos pos = ctx.getClickedPos();
            BlockState state = level.getBlockState(pos);
            if (!state.m_60808_(level, pos).isEmpty()) {
                pos = pos.relative(ctx.getClickedFace());
            }
            Player player = ctx.getPlayer();
            if (togglePos(stack, new PathRecordCard.Pos(level.dimension().location(), pos))) {
                if (player != null) {
                    player.m_213846_(MGLangData.PATH_ADD.get());
                }
            } else if (player != null) {
                player.m_213846_(MGLangData.PATH_REMOVE.get());
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int slot, boolean selected) {
        if (ModList.get().isLoaded("create")) {
            if (selected && entity instanceof Player player && level.isClientSide()) {
                BlockOutliner.drawOutlines(player, getList(stack));
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(MGLangData.PATH_COUNT.get(Component.literal(getList(stack).size() + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
        list.add(MGLangData.PATH.get().withStyle(ChatFormatting.GRAY));
    }

    public static record Pos(ResourceLocation level, BlockPos pos) {
    }
}