package dev.xkmc.modulargolems.content.modifier.base;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class TargetBonusModifier extends GolemModifier {

    private final Predicate<LivingEntity> pred;

    public TargetBonusModifier(Predicate<LivingEntity> pred) {
        super(StatFilterType.ATTACK, 2);
        this.pred = pred;
    }

    @Override
    public void onHurtTarget(AbstractGolemEntity<?, ?> entity, LivingHurtEvent event, int level) {
        if (this.pred.test(event.getEntity())) {
            event.setAmount((float) ((double) event.getAmount() * (1.0 + (double) level * MGConfig.COMMON.targetDamageBonus.get())));
        }
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        int perc = (int) (MGConfig.COMMON.targetDamageBonus.get() * 100.0 * (double) v);
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", perc).withStyle(ChatFormatting.GREEN));
    }
}