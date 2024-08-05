package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Function;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.gui.stats.getter.ILabelGetter;
import se.mickelus.tetra.module.data.MaterialData;

@ParametersAreNonnullByDefault
public class HoloMaterialStatGui extends GuiElement {

    protected GuiTexture backdrop;

    protected GuiString label;

    protected GuiString value;

    protected ILabelGetter valueFormatter;

    protected Function<MaterialData, Float> getter;

    List<Component> tooltip;

    public HoloMaterialStatGui(int x, int y, String key, ILabelGetter valueFormatter, Function<MaterialData, Float> getter) {
        super(x, y, 29, 29);
        this.valueFormatter = valueFormatter;
        this.getter = getter;
        this.tooltip = ImmutableList.of(Component.translatable("tetra.holo.craft.materials.stat." + key + ".tooltip"));
        this.backdrop = new GuiTexture(0, 0, 29, 29, 97, 0, GuiTextures.workbench);
        this.backdrop.setColor(2236962);
        this.addChild(this.backdrop);
        this.value = new GuiStringOutline(1, 8, "");
        this.value.setAttachment(GuiAttachment.topCenter);
        this.addChild(this.value);
        this.label = new GuiStringOutline(0, -3, I18n.get("tetra.holo.craft.materials.stat." + key + ".short"));
        this.label.setColor(8355711);
        this.label.setAttachment(GuiAttachment.bottomCenter);
        this.addChild(this.label);
    }

    public void update(MaterialData current, MaterialData preview) {
        this.update((double) ((Float) this.getter.apply(current)).floatValue(), (double) ((Float) this.getter.apply(preview)).floatValue());
    }

    protected void update(double current, double preview) {
        this.value.setColor(current == 0.0 && preview == 0.0 ? 4210752 : 16777215);
        this.label.setColor(current == 0.0 && preview == 0.0 ? 2236962 : 8355711);
        this.value.setString(this.valueFormatter.getLabelMerged(current, preview));
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }
}