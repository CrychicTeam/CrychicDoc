package dev.xkmc.modulargolems.compat.materials.create.modifier;

import dev.xkmc.modulargolems.content.core.StatFilterType;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.modifier.base.GolemModifier;
import dev.xkmc.modulargolems.init.data.MGConfig;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;

public class CoatingModifier extends GolemModifier {

    public CoatingModifier() {
        super(StatFilterType.MASS, 5);
    }

    @Override
    public List<MutableComponent> getDetail(int v) {
        double reduce = (double) v * MGConfig.COMMON.coating.get();
        return List.of(Component.translatable(this.getDescriptionId() + ".desc", reduce).withStyle(ChatFormatting.GREEN));
    }

    @Override
    public void onDamaged(AbstractGolemEntity<?, ?> entity, LivingDamageEvent event, int level) {
        event.setAmount((float) Math.max(0.0, (double) event.getAmount() - (double) level * MGConfig.COMMON.coating.get()));
    }
}