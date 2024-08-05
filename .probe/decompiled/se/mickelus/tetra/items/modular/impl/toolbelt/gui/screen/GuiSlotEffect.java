package se.mickelus.tetra.items.modular.impl.toolbelt.gui.screen;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.impl.toolbelt.SlotType;

@ParametersAreNonnullByDefault
public class GuiSlotEffect extends GuiElement {

    private List<Component> tooltip;

    public GuiSlotEffect(int x, int y, SlotType slotType, ItemEffect effect) {
        super(x, y, 8, 8);
        this.tooltip = Collections.singletonList(Component.translatable(String.format("tetra.toolbelt.effect.tooltip.%s.%s", slotType, effect.getKey())));
        if (ItemEffect.quickAccess.equals(effect)) {
            this.addChild(new GuiTexture(0, 0, 8, 8, 0, 64, GuiTextures.toolbelt).setColor(12303291));
        } else if (ItemEffect.cellSocket.equals(effect)) {
            this.addChild(new GuiTexture(0, 0, 8, 8, 8, 64, GuiTextures.toolbelt).setColor(12303291));
        } else {
            this.addChild(new GuiString(0, 0, "?"));
        }
    }

    public static Collection<GuiSlotEffect> getEffectsForSlot(SlotType slotType, Collection<ItemEffect> slotEffects) {
        int offset = 4 - slotEffects.size() * 4;
        AtomicInteger i = new AtomicInteger(0);
        return (Collection<GuiSlotEffect>) slotEffects.stream().map(effect -> new GuiSlotEffect(8 * i.getAndIncrement() + offset, 0, slotType, effect)).collect(Collectors.toList());
    }

    public static Collection<GuiElement> getEffectsForInventory(SlotType slotType, Collection<Collection<ItemEffect>> inventoryEffects) {
        return getEffectsForInventory(slotType, inventoryEffects, Integer.MAX_VALUE);
    }

    public static Collection<GuiElement> getEffectsForInventory(SlotType slotType, Collection<Collection<ItemEffect>> inventoryEffects, int columns) {
        AtomicInteger i = new AtomicInteger(0);
        return (Collection<GuiElement>) inventoryEffects.stream().map(slotEffects -> {
            GuiElement group = new GuiElement(i.get() % columns * 17, -19 - i.getAndIncrement() / columns * 17, 16, 8);
            group.setAttachment(GuiAttachment.bottomLeft);
            getEffectsForSlot(slotType, slotEffects).forEach(group::addChild);
            return group;
        }).collect(Collectors.toList());
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : super.getTooltipLines();
    }
}