package dev.xkmc.l2hostility.content.item.curio.ring;

import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2hostility.content.item.curio.core.CurseCurioItem;
import dev.xkmc.l2hostility.content.item.traits.DurabilityEater;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.data.LangData;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import org.jetbrains.annotations.Nullable;

public class RingOfCorrosion extends CurseCurioItem {

    public RingOfCorrosion(Item.Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> list, TooltipFlag flag) {
        list.add(LangData.ITEM_RING_CORROSION.get(Math.round(LHConfig.COMMON.ringOfCorrosionFactor.get() * 100.0)).withStyle(ChatFormatting.GOLD));
        list.add(LangData.ITEM_RING_CORROSION_NEG.get(Math.round(LHConfig.COMMON.ringOfCorrosionFactor.get() * 100.0)).withStyle(ChatFormatting.RED));
    }

    @Override
    public void onHurtTarget(ItemStack stack, LivingEntity user, AttackCache cache) {
        LivingEntity target = cache.getAttackTarget();
        for (EquipmentSlot e : EquipmentSlot.values()) {
            DurabilityEater.flat(target, e, LHConfig.COMMON.ringOfCorrosionFactor.get());
        }
    }

    @Override
    public void onDamage(ItemStack stack, LivingEntity user, LivingDamageEvent event) {
        for (EquipmentSlot e : EquipmentSlot.values()) {
            DurabilityEater.flat(user, e, LHConfig.COMMON.ringOfCorrosionPenalty.get());
        }
    }
}