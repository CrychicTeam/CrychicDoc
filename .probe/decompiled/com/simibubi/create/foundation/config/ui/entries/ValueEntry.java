package com.simibubi.create.foundation.config.ui.entries;

import com.google.common.base.Predicates;
import com.simibubi.create.foundation.config.ui.ConfigAnnotations;
import com.simibubi.create.foundation.config.ui.ConfigHelper;
import com.simibubi.create.foundation.config.ui.ConfigScreen;
import com.simibubi.create.foundation.config.ui.ConfigScreenList;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.widget.BoxWidget;
import com.simibubi.create.foundation.gui.widget.ElementWidget;
import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import com.simibubi.create.foundation.utility.Pair;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.common.ForgeConfigSpec;

public class ValueEntry<T> extends ConfigScreenList.LabeledEntry {

    protected static final int resetWidth = 28;

    protected ForgeConfigSpec.ConfigValue<T> value;

    protected ForgeConfigSpec.ValueSpec spec;

    protected BoxWidget resetButton;

    protected boolean editable = true;

    public ValueEntry(String label, ForgeConfigSpec.ConfigValue<T> value, ForgeConfigSpec.ValueSpec spec) {
        super(label);
        this.value = value;
        this.spec = spec;
        this.path = String.join(".", value.getPath());
        this.resetButton = new BoxWidget(0, 0, 16, 16).<ElementWidget>showingElement(AllIcons.I_CONFIG_RESET.asStencil()).withCallback(() -> {
            this.setValue((T) spec.getDefault());
            this.onReset();
        });
        this.resetButton.modifyElement(e -> ((DelegatedStencilElement) e).withElementRenderer((DelegatedStencilElement.ElementRenderer) BoxWidget.gradientFactory.apply(this.resetButton)));
        this.listeners.add(this.resetButton);
        List<String> path = value.getPath();
        this.labelTooltip.add(Components.literal(label).withStyle(ChatFormatting.WHITE));
        String comment = spec.getComment();
        if (comment != null && !comment.isEmpty()) {
            List<String> commentLines = new ArrayList(Arrays.asList(comment.split("\n")));
            Pair<String, Map<String, String>> metadata = ConfigHelper.readMetadataFromComment(commentLines);
            if (metadata.getFirst() != null) {
                this.unit = metadata.getFirst();
            }
            if (metadata.getSecond() != null && !metadata.getSecond().isEmpty()) {
                this.annotations.putAll(metadata.getSecond());
            }
            this.labelTooltip.addAll((Collection) commentLines.stream().filter(Predicates.not(s -> s.startsWith("Range"))).filter(s -> !s.equals(".")).map(Components::literal).flatMap(stc -> TooltipHelper.cutTextComponent(stc, TooltipHelper.Palette.ALL_GRAY).stream()).collect(Collectors.toList()));
            if (this.annotations.containsKey(ConfigAnnotations.RequiresRelog.TRUE.getName())) {
                this.labelTooltip.addAll(TooltipHelper.cutStringTextComponent("Changing this value will require a _relog_ to take full effect", TooltipHelper.Palette.GRAY_AND_GOLD));
            }
            if (this.annotations.containsKey(ConfigAnnotations.RequiresRestart.CLIENT.getName())) {
                this.labelTooltip.addAll(TooltipHelper.cutStringTextComponent("Changing this value will require a _restart_ to take full effect", TooltipHelper.Palette.GRAY_AND_RED));
            }
            this.labelTooltip.add(Components.literal(ConfigScreen.modID + ":" + (String) path.get(path.size() - 1)).withStyle(ChatFormatting.DARK_GRAY));
        }
    }

    @Override
    protected void setEditable(boolean b) {
        this.editable = b;
        this.resetButton.f_93623_ = this.editable && !this.isCurrentValueDefault();
        this.resetButton.animateGradientFromState();
    }

    @Override
    public void tick() {
        super.tick();
        this.resetButton.tick();
    }

    @Override
    public void render(GuiGraphics graphics, int index, int y, int x, int width, int height, int mouseX, int mouseY, boolean p_230432_9_, float partialTicks) {
        super.render(graphics, index, y, x, width, height, mouseX, mouseY, p_230432_9_, partialTicks);
        this.resetButton.m_252865_(x + width - 28 + 6);
        this.resetButton.m_253211_(y + 10);
        this.resetButton.m_88315_(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected int getLabelWidth(int totalWidth) {
        return (int) ((float) totalWidth * 0.4F) + 30;
    }

    public void setValue(@Nonnull T value) {
        ConfigHelper.setValue(this.path, this.value, value, this.annotations);
        this.onValueChange(value);
    }

    @Nonnull
    public T getValue() {
        return ConfigHelper.getValue(this.path, this.value);
    }

    protected boolean isCurrentValueDefault() {
        return this.spec.getDefault().equals(this.getValue());
    }

    public void onReset() {
        this.onValueChange(this.getValue());
    }

    public void onValueChange() {
        this.onValueChange(this.getValue());
    }

    public void onValueChange(T newValue) {
        this.resetButton.f_93623_ = this.editable && !this.isCurrentValueDefault();
        this.resetButton.animateGradientFromState();
    }

    protected void bumpCog() {
        this.bumpCog(10.0F);
    }

    protected void bumpCog(float force) {
        if (this.f_93521_ != null && this.f_93521_ instanceof ConfigScreenList) {
            ((ConfigScreenList) this.f_93521_).bumpCog(force);
        }
    }
}