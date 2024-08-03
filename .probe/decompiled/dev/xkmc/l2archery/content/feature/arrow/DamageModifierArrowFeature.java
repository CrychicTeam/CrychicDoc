package dev.xkmc.l2archery.content.feature.arrow;

import dev.xkmc.l2archery.content.entity.GenericArrowEntity;
import dev.xkmc.l2archery.content.feature.types.OnHitFeature;
import dev.xkmc.l2damagetracker.contents.attack.AttackCache;
import dev.xkmc.l2serial.util.Wrappers;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public record DamageModifierArrowFeature(BiConsumer<GenericArrowEntity, AttackCache> source, Consumer<List<Component>> comp) implements OnHitFeature {

    @Override
    public void onHurtModifier(GenericArrowEntity genericArrow, AttackCache source) {
        this.source.accept(genericArrow, source);
    }

    @Override
    public void addTooltip(List<MutableComponent> list) {
        this.comp.accept((List) Wrappers.cast(list));
    }

    @Override
    public boolean allowDuplicate() {
        return true;
    }
}