package dev.xkmc.l2hostility.content.item.wand;

import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class TargetSelectWand extends BaseWand {

    private static final String KEY = "cachedMobID";

    public TargetSelectWand(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void clickTarget(ItemStack stack, Player player, LivingEntity entity) {
        if (stack.getOrCreateTag().contains("cachedMobID") && entity.m_9236_().getEntity(stack.getOrCreateTag().getInt("cachedMobID")) instanceof LivingEntity le && le != entity) {
            boolean succeed = false;
            if (entity instanceof Mob mob) {
                mob.setTarget(le);
                succeed = true;
            }
            if (le instanceof Mob mob) {
                mob.setTarget(entity);
                succeed = true;
            }
            stack.getOrCreateTag().remove("cachedMobID");
            if (succeed) {
                player.m_213846_(LangData.MSG_SET_TARGET.get(entity.m_5446_(), le.m_5446_()));
            } else {
                player.m_213846_(LangData.MSG_TARGET_FAIL.get(entity.m_5446_(), le.m_5446_()));
            }
        } else {
            stack.getOrCreateTag().putInt("cachedMobID", entity.m_19879_());
            player.m_213846_(LangData.MSG_TARGET_RECORD.get(entity.m_5446_()));
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        if (level != null && stack.getOrCreateTag().contains("cachedMobID")) {
            if (level.getEntity(stack.getOrCreateTag().getInt("cachedMobID")) instanceof LivingEntity le) {
                list.add(LangData.MSG_TARGET_RECORD.get(le.m_5446_().copy().withStyle(ChatFormatting.AQUA)).withStyle(ChatFormatting.GRAY));
            }
        } else {
            list.add(LangData.ITEM_WAND_TARGET.get().withStyle(ChatFormatting.GRAY));
        }
    }
}