package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityDragonSkull;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemDragonSkull extends Item {

    private final int dragonType;

    public ItemDragonSkull(int dragonType) {
        super(new Item.Properties().stacksTo(1));
        this.dragonType = dragonType;
    }

    static String getName(int type) {
        return "dragon_skull_%s".formatted(getType(type));
    }

    private static String getType(int type) {
        if (type == 2) {
            return "lightning";
        } else {
            return type == 1 ? "ice" : "fire";
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
    }

    @Override
    public void inventoryTick(ItemStack stack, @NotNull Level worldIn, @NotNull Entity entityIn, int itemSlot, boolean isSelected) {
        if (stack.getTag() == null) {
            stack.setTag(new CompoundTag());
            stack.getTag().putInt("Stage", 4);
            stack.getTag().putInt("DragonAge", 75);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        String iceorfire = "dragon." + getType(this.dragonType);
        tooltip.add(Component.translatable(iceorfire).withStyle(ChatFormatting.GRAY));
        if (stack.getTag() != null) {
            tooltip.add(Component.translatable("dragon.stage").withStyle(ChatFormatting.GRAY).append(Component.literal(" " + stack.getTag().getInt("Stage"))));
        }
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        ItemStack stack = context.getPlayer().m_21120_(context.getHand());
        if (stack.getTag() != null) {
            EntityDragonSkull skull = new EntityDragonSkull(IafEntityRegistry.DRAGON_SKULL.get(), context.getLevel());
            skull.setDragonType(this.dragonType);
            skull.setStage(stack.getTag().getInt("Stage"));
            skull.setDragonAge(stack.getTag().getInt("DragonAge"));
            BlockPos offset = context.getClickedPos().relative(context.getClickedFace(), 1);
            skull.m_7678_((double) offset.m_123341_() + 0.5, (double) offset.m_123342_(), (double) offset.m_123343_() + 0.5, 0.0F, 0.0F);
            float yaw = context.getPlayer().m_146908_();
            if (context.getClickedFace() != Direction.UP) {
                yaw = context.getPlayer().m_6350_().toYRot();
            }
            skull.setYaw(yaw);
            if (stack.hasCustomHoverName()) {
                skull.m_6593_(stack.getHoverName());
            }
            if (!context.getLevel().isClientSide) {
                context.getLevel().m_7967_(skull);
            }
            if (!context.getPlayer().isCreative()) {
                stack.shrink(1);
            }
        }
        return InteractionResult.SUCCESS;
    }
}