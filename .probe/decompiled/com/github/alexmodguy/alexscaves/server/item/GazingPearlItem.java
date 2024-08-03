package com.github.alexmodguy.alexscaves.server.item;

import com.github.alexmodguy.alexscaves.server.entity.util.DeepOneReaction;
import com.github.alexmodguy.alexscaves.server.level.storage.ACWorldData;
import java.awt.Color;
import java.util.List;
import java.util.Locale;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

public class GazingPearlItem extends Item {

    public GazingPearlItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    public static int getPearlColor(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.getBoolean("HasReputation")) {
            float shine = (float) (Math.sin((double) ((float) System.currentTimeMillis() / 4000.0F)) + 1.0) * 0.5F;
            int reputation = tag.getInt("Reputation");
            int color = 100 - reputation;
            return Color.HSBtoRGB((float) color / 200.0F, shine * 0.3F + 0.7F, 1.0F);
        } else {
            float hue = (float) (System.currentTimeMillis() % 10000L) / 10000.0F;
            float shine = (float) (Math.sin((double) ((float) System.currentTimeMillis() / 4000.0F)) + 1.0) * 0.5F;
            return Color.HSBtoRGB(hue, shine * 0.3F + 0.7F, 1.0F);
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CompoundTag tag = stack.getTag();
        if (tag != null && tag.getBoolean("HasReputation")) {
            int reputation = tag.getInt("Reputation");
            DeepOneReaction reaction = DeepOneReaction.fromReputation(reputation);
            String key = "item.alexscaves.gazing_pearl.desc_" + reaction.name().toLowerCase(Locale.ROOT);
            tooltip.add(Component.translatable(key).withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
    }

    @Override
    public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean held) {
        super.inventoryTick(stack, level, entity, i, held);
        if (!level.isClientSide) {
            CompoundTag tag = stack.getOrCreateTag();
            long lastReputationTimestamp = tag.getLong("LastReputationTimestamp");
            if (lastReputationTimestamp <= 0L || level.getGameTime() - lastReputationTimestamp > 100L) {
                ACWorldData acWorldData = ACWorldData.get(level);
                if (acWorldData != null) {
                    tag.putLong("LastReputationTimestamp", level.getGameTime());
                    tag.putBoolean("HasReputation", true);
                    tag.putInt("Reputation", acWorldData.getDeepOneReputation(entity.getUUID()));
                }
            }
        }
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return !oldStack.is(ACItemRegistry.GAZING_PEARL.get()) || !newStack.is(ACItemRegistry.GAZING_PEARL.get());
    }
}