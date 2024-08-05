package org.embeddedt.embeddium.gui.frame;

import com.google.common.base.Predicates;
import com.google.common.collect.UnmodifiableIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.gui.options.OptionGroup;
import me.jellysquid.mods.sodium.client.gui.options.OptionImpact;
import me.jellysquid.mods.sodium.client.gui.options.OptionPage;
import me.jellysquid.mods.sodium.client.gui.options.control.Control;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.gui.widgets.AbstractWidget;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;
import org.apache.commons.lang3.Validate;
import org.embeddedt.embeddium.client.gui.options.OptionIdentifier;
import org.embeddedt.embeddium.util.PlatformUtil;
import org.jetbrains.annotations.Nullable;

public class OptionPageFrame extends AbstractFrame {

    protected final OptionPage page;

    private long lastTime = 0L;

    private ControlElement<?> lastHoveredElement = null;

    protected final Predicate<Option<?>> optionFilter;

    public OptionPageFrame(Dim2i dim, boolean renderOutline, OptionPage page, Predicate<Option<?>> optionFilter) {
        super(dim, renderOutline);
        this.page = page;
        this.optionFilter = optionFilter;
        this.setupFrame();
        this.buildFrame();
    }

    public static OptionPageFrame.Builder createBuilder() {
        return new OptionPageFrame.Builder();
    }

    public void setupFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();
        int y = 0;
        if (!this.page.getGroups().isEmpty()) {
            OptionGroup lastGroup = (OptionGroup) this.page.getGroups().get(this.page.getGroups().size() - 1);
            UnmodifiableIterator var3 = this.page.getGroups().iterator();
            while (var3.hasNext()) {
                OptionGroup group = (OptionGroup) var3.next();
                int visibleOptionCount = (int) group.getOptions().stream().filter(this.optionFilter::test).count();
                y += visibleOptionCount * 18;
                if (visibleOptionCount > 0 && group != lastGroup) {
                    y += 4;
                }
            }
        }
        this.dim = this.dim.withHeight(y);
    }

    @Override
    public void buildFrame() {
        if (this.page != null) {
            this.children.clear();
            this.drawable.clear();
            this.controlElements.clear();
            int y = 0;
            UnmodifiableIterator var2 = this.page.getGroups().iterator();
            while (var2.hasNext()) {
                OptionGroup group = (OptionGroup) var2.next();
                boolean needPadding = false;
                UnmodifiableIterator var5 = group.getOptions().iterator();
                while (var5.hasNext()) {
                    Option<?> option = (Option<?>) var5.next();
                    if (this.optionFilter.test(option)) {
                        Control<?> control = option.getControl();
                        Dim2i dim = new Dim2i(0, y, this.dim.width(), 18).withParentOffset(this.dim);
                        ControlElement<?> element = control.createElement(dim);
                        this.children.add(element);
                        y += 18;
                        needPadding = true;
                    }
                }
                if (needPadding) {
                    y += 4;
                }
            }
            super.buildFrame();
        }
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        ControlElement<?> hoveredElement = (ControlElement<?>) this.controlElements.stream().filter(AbstractWidget::isHovered).findFirst().orElse((ControlElement) this.controlElements.stream().filter(AbstractWidget::m_93696_).findFirst().orElse(null));
        super.render(drawContext, mouseX, mouseY, delta);
        if (hoveredElement != null && this.lastHoveredElement == hoveredElement && (this.dim.containsCursor((double) mouseX, (double) mouseY) && hoveredElement.isHovered() && hoveredElement.isMouseOver((double) mouseX, (double) mouseY) || hoveredElement.m_93696_())) {
            if (this.lastTime == 0L) {
                this.lastTime = System.currentTimeMillis();
            }
            this.renderOptionTooltip(drawContext, hoveredElement);
        } else {
            this.lastTime = 0L;
            this.lastHoveredElement = hoveredElement;
        }
    }

    private static String normalizeModForTooltip(@Nullable String mod) {
        if (mod == null) {
            return null;
        } else {
            byte var2 = -1;
            switch(mod.hashCode()) {
                case 695073197:
                    if (mod.equals("minecraft")) {
                        var2 = 0;
                    }
                default:
                    return switch(var2) {
                        case 0 ->
                            "embeddium";
                        default ->
                            mod;
                    };
            }
        }
    }

    private void renderOptionTooltip(GuiGraphics drawContext, ControlElement<?> element) {
        if (this.lastTime + 500L <= System.currentTimeMillis()) {
            Dim2i dim = element.getDimensions();
            int textPadding = 3;
            int boxPadding = 3;
            int boxWidth = dim.width();
            int boxY = dim.getLimitY();
            int boxX = dim.x();
            Option<?> option = element.getOption();
            List<FormattedCharSequence> tooltip = new ArrayList(Minecraft.getInstance().font.split(option.getTooltip(), boxWidth - textPadding * 2));
            OptionImpact impact = option.getImpact();
            if (impact != null) {
                tooltip.add(Language.getInstance().getVisualOrder(Component.translatable("sodium.options.performance_impact_string", impact.getLocalizedName()).withStyle(ChatFormatting.GRAY)));
            }
            OptionIdentifier<?> id = option.getId();
            if (OptionIdentifier.isPresent(this.page.getId()) && OptionIdentifier.isPresent(id) && !Objects.equals(normalizeModForTooltip(this.page.getId().getModId()), normalizeModForTooltip(id.getModId()))) {
                tooltip.add(Language.getInstance().getVisualOrder(Component.translatable("embeddium.options.added_by_mod_string", Component.literal(PlatformUtil.getModName(id.getModId())).withStyle(ChatFormatting.WHITE)).withStyle(ChatFormatting.GRAY)));
            }
            int boxHeight = tooltip.size() * 12 + boxPadding;
            int boxYLimit = boxY + boxHeight;
            int boxYCutoff = this.dim.getLimitY();
            if (boxYLimit > boxYCutoff) {
                boxY -= boxHeight + dim.height();
            }
            if (boxY < 0) {
                boxY = dim.getLimitY();
            }
            drawContext.pose().pushPose();
            drawContext.pose().translate(0.0F, 0.0F, 90.0F);
            this.drawRect(drawContext, boxX, boxY, boxX + boxWidth, boxY + boxHeight, -536870912);
            this.drawBorder(drawContext, boxX, boxY, boxX + boxWidth, boxY + boxHeight, -3179338);
            for (int i = 0; i < tooltip.size(); i++) {
                drawContext.drawString(Minecraft.getInstance().font, (FormattedCharSequence) tooltip.get(i), boxX + textPadding, boxY + textPadding + i * 12, -1, true);
            }
            drawContext.pose().popPose();
        }
    }

    @Nullable
    @Override
    public ComponentPath nextFocusPath(FocusNavigationEvent navigation) {
        return super.nextFocusPath(navigation);
    }

    public static class Builder {

        private Dim2i dim;

        private boolean renderOutline;

        private OptionPage page;

        private Predicate<Option<?>> optionFilter = Predicates.alwaysTrue();

        public OptionPageFrame.Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public OptionPageFrame.Builder shouldRenderOutline(boolean renderOutline) {
            this.renderOutline = renderOutline;
            return this;
        }

        public OptionPageFrame.Builder setOptionPage(OptionPage page) {
            this.page = page;
            return this;
        }

        public OptionPageFrame.Builder setOptionFilter(Predicate<Option<?>> optionFilter) {
            this.optionFilter = optionFilter;
            return this;
        }

        public OptionPageFrame build() {
            Validate.notNull(this.dim, "Dimension must be specified", new Object[0]);
            Validate.notNull(this.page, "Option Page must be specified", new Object[0]);
            return new OptionPageFrame(this.dim, this.renderOutline, this.page, this.optionFilter);
        }
    }
}