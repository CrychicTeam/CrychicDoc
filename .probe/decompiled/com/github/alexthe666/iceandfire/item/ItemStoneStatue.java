package com.github.alexthe666.iceandfire.item;

import com.github.alexthe666.iceandfire.entity.EntityStoneStatue;
import com.github.alexthe666.iceandfire.entity.IafEntityRegistry;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class ItemStoneStatue extends Item {

    public ItemStoneStatue() {
        super(new Item.Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, @NotNull List<Component> tooltip, @NotNull TooltipFlag flagIn) {
        if (stack.getTag() != null) {
            boolean isPlayer = stack.getTag().getBoolean("IAFStoneStatuePlayerEntity");
            String id = stack.getTag().getString("IAFStoneStatueEntityID");
            if (EntityType.byString(id).orElse(null) != null) {
                EntityType type = (EntityType) EntityType.byString(id).orElse(null);
                MutableComponent untranslated = isPlayer ? Component.translatable("entity.minecraft.player") : Component.translatable(type.getDescriptionId());
                tooltip.add(untranslated.withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, @NotNull Level world, @NotNull Player player) {
        itemStack.setTag(new CompoundTag());
        itemStack.getTag().putBoolean("IAFStoneStatuePlayerEntity", true);
    }

    @NotNull
    @Override
    public InteractionResult useOn(UseOnContext context) {
        if (context.getClickedFace() != Direction.UP) {
            return InteractionResult.FAIL;
        } else {
            ItemStack stack = context.getPlayer().m_21120_(context.getHand());
            if (stack.getTag() != null) {
                String id = stack.getTag().getString("IAFStoneStatueEntityID");
                CompoundTag statueNBT = stack.getTag().getCompound("IAFStoneStatueNBT");
                EntityStoneStatue statue = new EntityStoneStatue(IafEntityRegistry.STONE_STATUE.get(), context.getLevel());
                statue.readAdditionalSaveData(statueNBT);
                statue.setTrappedEntityTypeString(id);
                double d1 = context.getPlayer().m_20185_() - ((double) context.getClickedPos().m_123341_() + 0.5);
                double d2 = context.getPlayer().m_20189_() - ((double) context.getClickedPos().m_123343_() + 0.5);
                float yaw = (float) (Mth.atan2(d2, d1) * 180.0F / (float) Math.PI) - 90.0F;
                statue.f_19859_ = yaw;
                statue.m_146922_(yaw);
                statue.f_20885_ = yaw;
                statue.f_20883_ = yaw;
                statue.f_20884_ = yaw;
                statue.m_19890_((double) context.getClickedPos().m_123341_() + 0.5, (double) (context.getClickedPos().m_123342_() + 1), (double) context.getClickedPos().m_123343_() + 0.5, yaw, 0.0F);
                if (!context.getLevel().isClientSide) {
                    context.getLevel().m_7967_(statue);
                    statue.readAdditionalSaveData(stack.getTag());
                }
                statue.setCrackAmount(0);
                if (!context.getPlayer().isCreative()) {
                    stack.shrink(1);
                }
                return InteractionResult.SUCCESS;
            } else {
                return InteractionResult.SUCCESS;
            }
        }
    }
}