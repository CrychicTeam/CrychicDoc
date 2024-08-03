package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.Tooltips;
import se.mickelus.tetra.gui.GuiTextures;

@ParametersAreNonnullByDefault
public class HoloMaterialFeatureGui extends GuiElement {

    protected GuiTexture backdrop;

    protected GuiString label;

    protected GuiString value;

    List<Component> tooltip;

    List<Component> extendedTooltip;

    public HoloMaterialFeatureGui(int x, int y, String key, boolean current, boolean preview) {
        super(x, y, 29, 29);
        if (I18n.exists("tetra.material.feature." + key + ".tooltip_extended")) {
            this.tooltip = ImmutableList.of(Component.translatable("tetra.material.feature." + key + ".tooltip"), Component.literal(" "), Tooltips.expand);
            this.extendedTooltip = ImmutableList.of(Component.translatable("tetra.material.feature." + key + ".tooltip"), Component.literal(" "), Tooltips.expand, Component.translatable("tetra.material.feature." + key + ".tooltip_extended").withStyle(ChatFormatting.GRAY));
        } else {
            this.tooltip = ImmutableList.of(Component.translatable("tetra.material.feature." + key + ".tooltip"));
        }
        this.backdrop = new GuiTexture(0, 0, 29, 29, 97, 0, GuiTextures.workbench);
        this.backdrop.setColor(2236962);
        this.addChild(this.backdrop);
        this.value = new GuiStringOutline(0, 8, I18n.get("tetra.material.feature." + key));
        this.value.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.value);
        if (current != preview) {
            this.value.setColor(preview ? 11206570 : 16755370);
        }
        this.label = new GuiStringOutline(0, -3, I18n.get("tetra.holo.craft.materials.feature"));
        this.label.setColor(8355711);
        this.label.setAttachment(GuiAttachment.bottomCenter);
        this.addChild(this.label);
    }

    @Override
    public List<Component> getTooltipLines() {
        if (this.hasFocus()) {
            return Screen.hasShiftDown() && this.extendedTooltip != null ? this.extendedTooltip : this.tooltip;
        } else {
            return null;
        }
    }
}