package me.shedaniel.clothconfig2.gui;

import java.util.Optional;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.api.Tooltip;
import me.shedaniel.math.Point;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;

@OnlyIn(Dist.CLIENT)
public class ClothConfigTabButton extends AbstractButton {

    private final int index;

    private final ClothConfigScreen screen;

    @Nullable
    private final Supplier<Optional<FormattedText[]>> descriptionSupplier;

    public ClothConfigTabButton(ClothConfigScreen screen, int index, int int_1, int int_2, int int_3, int int_4, Component string_1, Supplier<Optional<FormattedText[]>> descriptionSupplier) {
        super(int_1, int_2, int_3, int_4, string_1);
        this.index = index;
        this.screen = screen;
        this.descriptionSupplier = descriptionSupplier;
    }

    public ClothConfigTabButton(ClothConfigScreen screen, int index, int int_1, int int_2, int int_3, int int_4, Component string_1) {
        this(screen, index, int_1, int_2, int_3, int_4, string_1, null);
    }

    @Override
    public void onPress() {
        if (this.index != -1) {
            this.screen.selectedCategoryIndex = this.index;
        }
        this.screen.m_6575_(Minecraft.getInstance(), this.screen.f_96543_, this.screen.f_96544_);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.f_93623_ = this.index != this.screen.selectedCategoryIndex;
        super.m_88315_(graphics, mouseX, mouseY, delta);
        if (this.isMouseOver((double) mouseX, (double) mouseY)) {
            Optional<FormattedText[]> tooltip = this.getDescription();
            if (tooltip.isPresent() && ((FormattedText[]) tooltip.get()).length > 0) {
                this.screen.addTooltip(Tooltip.of(new Point(mouseX, mouseY), (FormattedText[]) tooltip.get()));
            }
        }
    }

    @Override
    protected boolean clicked(double double_1, double double_2) {
        return this.f_93624_ && this.f_93623_ && this.isMouseOver(double_1, double_2);
    }

    @Override
    public boolean isMouseOver(double double_1, double double_2) {
        return this.f_93624_ && double_1 >= (double) this.m_252754_() && double_2 >= (double) this.m_252907_() && double_1 < (double) (this.m_252754_() + this.f_93618_) && double_2 < (double) (this.m_252907_() + this.f_93619_) && double_1 >= 20.0 && double_1 < (double) (this.screen.f_96543_ - 20);
    }

    public Optional<FormattedText[]> getDescription() {
        return this.descriptionSupplier != null ? (Optional) this.descriptionSupplier.get() : Optional.empty();
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }
}