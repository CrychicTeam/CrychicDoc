package se.mickelus.tetra.items.modular.impl.holo.gui.craft;

import com.google.common.collect.ImmutableList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import se.mickelus.mutil.gui.GuiElement;
import se.mickelus.mutil.gui.GuiTexture;
import se.mickelus.tetra.gui.GuiTextures;
import se.mickelus.tetra.module.schematic.OutcomePreview;
import se.mickelus.tetra.module.schematic.UpgradeSchematic;

@ParametersAreNonnullByDefault
public class HoloDescription extends GuiElement {

    private final List<Component> emptyTooltip;

    private final GuiTexture icon = new GuiTexture(0, 0, 9, 9, 128, 32, GuiTextures.workbench);

    private List<Component> tooltip;

    public HoloDescription(int x, int y) {
        super(x, y, 9, 9);
        this.addChild(this.icon);
        this.emptyTooltip = Collections.singletonList(Component.translatable("tetra.holo.craft.empty_description"));
    }

    public void update(OutcomePreview[] previews) {
        this.tooltip = (List<Component>) Arrays.stream(previews).map(preview -> "tetra.module." + preview.moduleKey + ".description").filter(I18n::m_118936_).map(Component::m_237115_).findFirst().map(component -> component).map(ImmutableList::of).orElse(null);
    }

    public void update(UpgradeSchematic schematic, ItemStack itemStack) {
        this.tooltip = ImmutableList.of(Component.literal(schematic.getDescription(itemStack)));
    }

    @Override
    public List<Component> getTooltipLines() {
        return this.hasFocus() ? this.tooltip : null;
    }
}