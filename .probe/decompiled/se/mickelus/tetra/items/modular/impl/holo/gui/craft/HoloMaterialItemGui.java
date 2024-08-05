package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import java.util.function.Consumer;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiItem;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiItemRolling;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.data.MaterialData;

@ParametersAreNonnullByDefault
public class HoloMaterialItemGui extends GuiClickable {

    protected GuiTexture backdrop;

    protected MaterialData material;

    protected Consumer<MaterialData> onHover;

    protected Consumer<MaterialData> onBlur;

    protected boolean isMuted = false;

    GuiItemRolling icon;

    public HoloMaterialItemGui(int x, int y, MaterialData material, Consumer<MaterialData> onHover, Consumer<MaterialData> onBlur, Consumer<MaterialData> onSelect) {
        super(x, y, 16, 16, () -> onSelect.accept(material));
        this.material = material;
        this.onHover = onHover;
        this.onBlur = onBlur;
        this.backdrop = new GuiTexture(0, 0, 16, 16, 52, 16, GuiTextures.workbench);
        this.addChild(this.backdrop);
        this.icon = new GuiItemRolling(0, 0).setTooltip(false).setCountVisibility(GuiItem.CountMode.never).setItems(material.material.getApplicableItemStacks());
        this.addChild(this.icon);
    }

    public void updateSelection(MaterialData material) {
        this.isMuted = material != null && !this.material.equals(material);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }

    @Override
    protected void onFocus() {
        super.onFocus();
        this.onHover.accept(this.material);
        this.backdrop.setColor(16777164);
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        this.onBlur.accept(this.material);
        this.backdrop.setColor(this.isMuted ? 8355711 : 16777215);
    }
}