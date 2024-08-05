package io.github.apace100.origins.badge;

import io.github.apace100.calio.data.SerializableData.Instance;
import io.github.apace100.origins.util.PowerKeyManager;
import io.github.edwinmindcraft.apoli.api.power.configuration.ConfiguredPower;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public record KeybindBadge(ResourceLocation spriteId, String text) implements Badge {

    public KeybindBadge(Instance instance) {
        this(instance.getId("sprite"), (String) instance.get("text"));
    }

    @Override
    public boolean hasTooltip() {
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static void addLines(List<ClientTooltipComponent> tooltips, Component text, Font textRenderer, int widthLimit) {
        if (textRenderer.width(text) > widthLimit) {
            for (FormattedCharSequence orderedText : textRenderer.split(text, widthLimit)) {
                tooltips.add(new ClientTextTooltip(orderedText));
            }
        } else {
            tooltips.add(new ClientTextTooltip(text.getVisualOrderText()));
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public List<ClientTooltipComponent> getTooltipComponents(ConfiguredPower<?, ?> powerType, int widthLimit, float time, Font textRenderer) {
        List<ClientTooltipComponent> tooltips = new LinkedList();
        Component keyText = Component.literal("[").append((Component) KeyMapping.createNameSupplier(PowerKeyManager.getKeyIdentifier(powerType.getRegistryName())).get()).append(Component.literal("]"));
        addLines(tooltips, Component.translatable(this.text, keyText), textRenderer, widthLimit);
        return tooltips;
    }

    @Override
    public Instance toData(Instance instance) {
        instance.set("sprite", this.spriteId);
        instance.set("text", this.text);
        return instance;
    }

    @Override
    public BadgeFactory getBadgeFactory() {
        return BadgeFactories.KEYBIND.get();
    }
}