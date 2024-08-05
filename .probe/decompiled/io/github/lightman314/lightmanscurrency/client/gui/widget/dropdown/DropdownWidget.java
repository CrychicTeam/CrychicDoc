package io.github.lightman314.lightmanscurrency.client.gui.widget.dropdown;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.WidgetAddon;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IMouseListener;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyButton;
import io.github.lightman314.lightmanscurrency.client.gui.widget.easy.EasyWidgetWithChildren;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import io.github.lightman314.lightmanscurrency.util.MathUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

public class DropdownWidget extends EasyWidgetWithChildren implements IMouseListener {

    public static final ResourceLocation GUI_TEXTURE = new ResourceLocation("lightmanscurrency", "textures/gui/dropdown.png");

    public static final int HEIGHT = 12;

    boolean open = false;

    int currentlySelected;

    private final List<Component> options;

    private final Consumer<Integer> onSelect;

    private final Function<Integer, Boolean> optionActive;

    List<DropdownButton> optionButtons = new ArrayList();

    public DropdownWidget(ScreenPosition pos, int width, int selected, Consumer<Integer> onSelect, Component... options) {
        this(pos.x, pos.y, width, selected, onSelect, options);
    }

    public DropdownWidget(int x, int y, int width, int selected, Consumer<Integer> onSelect, Component... options) {
        this(x, y, width, selected, onSelect, index -> true, options);
    }

    public DropdownWidget(ScreenPosition pos, int width, int selected, Consumer<Integer> onSelect, List<Component> options) {
        this(pos.x, pos.y, width, selected, onSelect, options);
    }

    public DropdownWidget(int x, int y, int width, int selected, Consumer<Integer> onSelect, List<Component> options) {
        this(x, y, width, selected, onSelect, index -> true, options);
    }

    public DropdownWidget(ScreenPosition pos, int width, int selected, Consumer<Integer> onSelect, Function<Integer, Boolean> optionActive, Component... options) {
        this(pos.x, pos.y, width, selected, onSelect, optionActive, options);
    }

    public DropdownWidget(int x, int y, int width, int selected, Consumer<Integer> onSelect, Function<Integer, Boolean> optionActive, Component... options) {
        this(x, y, width, selected, onSelect, optionActive, Lists.newArrayList(options));
    }

    public DropdownWidget(ScreenPosition pos, int width, int selected, Consumer<Integer> onSelect, Function<Integer, Boolean> optionActive, List<Component> options) {
        this(pos.x, pos.y, width, selected, onSelect, optionActive, options);
    }

    public DropdownWidget(int x, int y, int width, int selected, Consumer<Integer> onSelect, Function<Integer, Boolean> optionActive, List<Component> options) {
        super(x, y, width, 12);
        this.options = options;
        this.currentlySelected = MathUtil.clamp(selected, 0, this.options.size() - 1);
        this.onSelect = onSelect;
        this.optionActive = optionActive;
    }

    public DropdownWidget withAddons(WidgetAddon... addons) {
        this.withAddonsInternal(addons);
        return this;
    }

    @Override
    public boolean addChildrenBeforeThis() {
        return true;
    }

    @Override
    public void addChildren() {
        this.optionButtons = new ArrayList();
        for (int i = 0; i < this.options.size(); i++) {
            int index = i;
            int yPos = this.m_252907_() + 12 + i * 12;
            DropdownButton button = this.addChild(new DropdownButton(this.m_252754_(), yPos, this.f_93618_, (Component) this.options.get(i), () -> this.OnSelect(index)));
            this.optionButtons.add(button);
            ((DropdownButton) this.optionButtons.get(i)).f_93624_ = this.open;
        }
    }

    @Override
    public void renderTick() {
        if (this.open) {
            for (int i = 0; i < this.optionButtons.size(); i++) {
                ((DropdownButton) this.optionButtons.get(i)).f_93623_ = (Boolean) this.optionActive.apply(i) && i != this.currentlySelected;
            }
        }
    }

    @Override
    public void renderWidget(@Nonnull EasyGuiGraphics gui) {
        int offset = this.f_93622_ ? this.f_93619_ : 0;
        if (!this.f_93623_) {
            gui.setColor(0.5F, 0.5F, 0.5F);
        } else {
            gui.resetColor();
        }
        gui.blit(GUI_TEXTURE, 0, 0, 0, offset, 2, 12);
        int xOffset = 0;
        while (xOffset < this.f_93618_ - 14) {
            int xPart = Math.min(this.f_93618_ - 14 - xOffset, 244);
            gui.blit(GUI_TEXTURE, 2 + xOffset, 0, 2, offset, xPart, 12);
            xOffset += xPart;
        }
        gui.blit(GUI_TEXTURE, this.f_93618_ - 12, 0, 244, offset, 12, 12);
        gui.drawString(this.fitString(gui, ((Component) this.options.get(this.currentlySelected)).getString()), 2, 2, 4210752);
        gui.resetColor();
    }

    @Override
    public boolean onMouseClicked(double mouseX, double mouseY, int click) {
        if (this.f_93623_ && this.f_93624_) {
            if (this.m_93680_(mouseX, mouseY) && this.isValidClickButton(click)) {
                this.playDownSound(Minecraft.getInstance().getSoundManager());
                this.open = !this.open;
                this.optionButtons.forEach(button -> button.f_93624_ = this.open);
                return true;
            }
            if (this.open && !this.isOverChild(mouseX, mouseY)) {
                this.open = false;
                this.optionButtons.forEach(button -> button.f_93624_ = false);
            }
        }
        return false;
    }

    private boolean isOverChild(double mouseX, double mouseY) {
        for (DropdownButton b : this.optionButtons) {
            if (b.m_5953_(mouseX, mouseY)) {
                return true;
            }
        }
        return false;
    }

    private void OnSelect(int index) {
        if (index >= 0 && index < this.optionButtons.size()) {
            this.currentlySelected = index;
            this.onSelect.accept(index);
            this.open = false;
            this.optionButtons.forEach(b -> b.f_93624_ = false);
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput narrator) {
    }

    private String fitString(EasyGuiGraphics gui, String text) {
        if (gui.font.width(text) <= this.f_93618_ - 14) {
            return text;
        } else {
            while (gui.font.width(text + "...") > this.f_93618_ - 14 && !text.isEmpty()) {
                text = text.substring(0, text.length() - 1);
            }
            return text + "...";
        }
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0;
    }

    @Override
    public void playDownSound(@Nonnull SoundManager manager) {
        EasyButton.playClick(manager);
    }
}