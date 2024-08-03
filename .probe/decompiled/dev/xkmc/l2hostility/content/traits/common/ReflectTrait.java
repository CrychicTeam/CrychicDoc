package dev.xkmc.l2hostility.content.traits.common;

import dev.xkmc.l2damagetracker.init.data.L2DamageTypes;
import dev.xkmc.l2hostility.compat.curios.CurioCompat;
import dev.xkmc.l2hostility.content.traits.base.MobTrait;
import dev.xkmc.l2hostility.init.data.LHConfig;
import dev.xkmc.l2hostility.init.registrate.LHItems;
import dev.xkmc.l2library.init.events.GeneralEventHandler;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class ReflectTrait extends MobTrait {

    public ReflectTrait(ChatFormatting style) {
        super(style);
    }

    @Override
    public void onHurtByOthers(int level, LivingEntity entity, LivingHurtEvent event) {
        if (event.getSource().getDirectEntity() instanceof LivingEntity le && event.getSource().is(L2DamageTypes.DIRECT)) {
            if (CurioCompat.hasItemInCurio(le, (Item) LHItems.ABRAHADABRA.get())) {
                return;
            }
            float factor = (float) ((double) level * LHConfig.COMMON.reflectFactor.get());
            GeneralEventHandler.schedule(() -> le.hurt(entity.m_9236_().damageSources().indirectMagic(entity, null), event.getAmount() * factor));
        }
    }

    @Override
    public void addDetail(List<Component> list) {
        list.add(Component.translatable(this.getDescriptionId() + ".desc", this.mapLevel(i -> Component.literal((int) Math.round(100.0 * (double) i.intValue() * LHConfig.COMMON.reflectFactor.get()) + "").withStyle(ChatFormatting.AQUA))).withStyle(ChatFormatting.GRAY));
    }
}