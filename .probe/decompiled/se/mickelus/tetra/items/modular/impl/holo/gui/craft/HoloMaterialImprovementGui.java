package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.items.modular.IModularItem;

@ParametersAreNonnullByDefault
public class HoloMaterialImprovementGui extends GuiElement {

    protected GuiTexture backdrop;

    protected GuiString label;

    protected GuiString value;

    List<Component> tooltip;

    public HoloMaterialImprovementGui(int x, int y, String key, boolean current, boolean preview) {
        super(x, y, 29, 29);
        String improvementName = IModularItem.getImprovementName(key, 0);
        this.tooltip = ImmutableList.of(Component.translatable("tetra.holo.craft.materials.stat_modifier.tooltip", improvementName), Component.literal(IModularItem.getImprovementDescription(key)).withStyle(ChatFormatting.DARK_GRAY));
        this.backdrop = new GuiTexture(0, 0, 29, 29, 97, 0, GuiTextures.workbench);
        this.backdrop.setColor(2236962);
        this.addChild(this.backdrop);
        this.value = new GuiStringOutline(0, 8, improvementName);
        this.value.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.value);
        if (current != preview) {
            this.value.setColor(preview ? 11206570 : 16755370);
        }
        this.label = new GuiStringOutline(0, -3, I18n.get("tetra.holo.craft.materials.stat_modifier"));
        this.label.setColor(8355711);
        this.label.setAttachment(GuiAttachment.bottomCenter);
        this.addChild(this.label);
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }
}