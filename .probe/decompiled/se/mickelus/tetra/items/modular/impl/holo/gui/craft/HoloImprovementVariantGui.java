package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.GuiAttachment;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiString;
import se.mickelus.mutil.gui.GuiStringOutline;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.OutcomePreview;

@ParametersAreNonnullByDefault
public class HoloImprovementVariantGui extends GuiClickable {

    private final GuiTexture backdrop;

    private final GuiString label;

    private final OutcomePreview preview;

    private final Consumer<OutcomePreview> onVariantHover;

    private final Consumer<OutcomePreview> onVariantBlur;

    private final List<Component> tooltip;

    private boolean isMuted;

    public HoloImprovementVariantGui(int x, int y, String name, int labelStart, OutcomePreview preview, boolean isConnected, Consumer<OutcomePreview> onVariantHover, Consumer<OutcomePreview> onVariantBlur, Consumer<OutcomePreview> onVariantSelect) {
        super(x, y, 19, 11, () -> onVariantSelect.accept(preview));
        this.preview = preview;
        this.onVariantHover = onVariantHover;
        this.onVariantBlur = onVariantBlur;
        String truncatedName = name;
        if (name.length() > labelStart) {
            truncatedName = name.substring(labelStart);
        }
        if (truncatedName.length() > 4) {
            truncatedName = truncatedName.substring(0, 4);
        }
        truncatedName = truncatedName.trim().toLowerCase();
        if (isConnected) {
            this.addChild(new GuiTexture(-2, 0, 11, 11, 17, 0, GuiTextures.holo).setAttachmentAnchor(GuiAttachment.topRight));
        }
        this.backdrop = new GuiTexture(0, 0, 17, 11, 0, 0, GuiTextures.holo);
        this.addChild(this.backdrop);
        this.label = new GuiStringOutline(9, 1, truncatedName);
        this.label.setAttachmentPoint(GuiAttachment.topCenter);
        this.addChild(this.label);
        this.tooltip = ImmutableList.of(Component.literal(name));
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.onVariantHover.accept(this.preview);
        this.backdrop.setColor(16777164);
        this.label.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.onVariantBlur.accept(this.preview);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
        this.label.setColor(this.isMuted ? 8355711 : 16777215);
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }

    public void setMuted(boolean muted) {
        this.isMuted = muted;
        this.backdrop.setColor(muted ? 8355711 : 16777215);
        this.label.setColor(muted ? 8355711 : 16777215);
    }
}