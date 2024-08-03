package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record SpriteBadge(ResourceLocation spriteId) implements Badge {

    public SpriteBadge(Instance instance) {
        this(instance.getId("sprite"));
    }

    @Override
    public boolean hasTooltip() {
        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ClientTooltipComponent> getTooltipComponents(ConfiguredPower<?, ?> powerType, int widthLimit, float time, Font textRenderer) {
        return new ArrayList();
    }

    @Override
    public Instance toData(Instance instance) {
        instance.set("sprite", this.spriteId);
        return instance;
    }

    @Override
    public BadgeFactory getBadgeFactory() {
        return BadgeFactories.SPRITE.get();
    }
}