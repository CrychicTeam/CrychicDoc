package dev.xkmc.l2hostility.content.item.curio.misc;

import dev.xkmc.l2complements.content.item.curios.CurioItem;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.compat.curios.EntitySlotAccess;
import dev.xkmc.l2hostility.content.item.traits.SealedItem;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

public class PocketOfRestoration extends CurioItem implements ICurioItem {

    public static final String ROOT = "UnsealRoot";

    public static final String KEY = "SealedSlotKey";

    public static final String START = "UnsealStartTime";

    public static void setData(ItemStack stack, ItemStack sealed, String id, long time) {
        Tag data = sealed.getOrCreateTag().get("sealedItem");
        if (data != null) {
            CompoundTag tag = stack.getOrCreateTagElement("UnsealRoot");
            tag.putInt("sealTime", sealed.getOrCreateTag().getInt("sealTime"));
            tag.put("sealedItem", data);
            tag.putString("SealedSlotKey", id);
            tag.putLong("UnsealStartTime", time);
        }
    }

    public PocketOfRestoration(Item.Properties properties, int durability) {
        super(properties, durability);
    }

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {
        LivingEntity le = slotContext.entity();
        if (!le.m_9236_().isClientSide) {
            List<EntitySlotAccess> list = CurioCompat.getItemAccess(le);
            if (stack.getTag() != null && stack.getTag().contains("UnsealRoot")) {
                CompoundTag tag = stack.getOrCreateTagElement("UnsealRoot");
                long time = tag.getLong("UnsealStartTime");
                int dur = tag.getInt("sealTime");
                if (le.m_9236_().getGameTime() >= time + (long) dur) {
                    ItemStack result = ItemStack.of(tag.getCompound("sealedItem"));
                    EntitySlotAccess slot = CurioCompat.decode(tag.getString("SealedSlotKey"), le);
                    if (slot != null && slot.get().isEmpty()) {
                        slot.set(result);
                        stack.getTag().remove("UnsealRoot");
                    } else if (le instanceof Player player && player.addItem(result)) {
                        stack.getTag().remove("UnsealRoot");
                    }
                }
            } else if (stack.getDamageValue() + 1 < stack.getMaxDamage()) {
                for (EntitySlotAccess e : list) {
                    if (e.get().getItem() instanceof SealedItem) {
                        ItemStack sealed = e.get();
                        e.set(ItemStack.EMPTY);
                        String id = e.getID();
                        long time = le.m_9236_().getGameTime();
                        stack.hurtAndBreak(1, le, x -> {
                        });
                        setData(stack, sealed, id, time);
                        return;
                    }
                }
            }
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.POCKET_OF_RESTORATION.get().withStyle(ChatFormatting.GOLD));
        if (stack.getTag() != null && stack.getTag().contains("UnsealRoot")) {
            list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
            list.add(ItemStack.of(stack.getOrCreateTagElement("UnsealRoot").getCompound("sealedItem")).getHoverName());
        }
    }
}