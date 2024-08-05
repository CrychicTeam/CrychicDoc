package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2archery.content.feature.types.OnShootFeature;
import dev.xkmc.l2archery.init.data.LangData;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.LivingEntity;

public record FireArrowFeature(int time) implements OnShootFeature, OnHitFeature {

    @Override
    public boolean onShoot(LivingEntity player, Consumer<Consumer<GenericArrowEntity>> consumer) {
        consumer.accept((Consumer) e -> e.m_7311_(this.time));
        return true;
    }

    @Override
    public void postHurtEntity(GenericArrowEntity genericArrow, LivingEntity target) {
        target.m_7311_(this.time);
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        list.add(LangData.FEATURE_FIRE.get((double) this.time / 20.0));
    }
}