package dev.xkmc.l2hostility.content.item.traits;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class SealedItem extends Item {

    public static final String TIME = "sealTime";

    public static final String DATA = "sealedItem";

    public static ItemStack sealItem(ItemStack stack, int time) {
        if (stack.is((Item) LHItems.SEAL.get())) {
            stack.getOrCreateTag().putInt("sealTime", Math.max(stack.getOrCreateTag().getInt("sealTime"), time));
            return stack;
        } else {
            ItemStack ans = LHItems.SEAL.asStack();
            ans.getOrCreateTag().putInt("sealTime", time);
            ans.getOrCreateTag().put("sealedItem", stack.save(new CompoundTag()));
            return ans;
        }
    }

    public SealedItem(Item.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.m_21120_(hand);
        player.m_6672_(hand);
        return InteractionResultHolder.consume(stack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        user.stopUsingItem();
        return ItemStack.of(stack.getOrCreateTag().getCompound("sealedItem"));
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return stack.getOrCreateTag().getInt("sealTime");
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.EAT;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.TOOLTIP_SEAL_DATA.get().withStyle(ChatFormatting.GRAY));
        list.add(ItemStack.of(stack.getOrCreateTag().getCompound("sealedItem")).getHoverName());
        int time = stack.getOrCreateTag().getInt("sealTime");
        list.add(LangData.TOOLTIP_SEAL_TIME.get(Component.literal(time / 20 + "").withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.RED));
    }
}