package io.github.lightman314.lightmanscurrency.client.gui.widget.easy;

import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import io.github.lightman314.lightmanscurrency.client.util.ScreenPosition;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;

public abstract class EasyButton extends EasyWidget {

    public static final Consumer<EasyButton> NULL_PRESS = button -> {
    };

    private final Consumer<EasyButton> press;

    protected EasyButton(int x, int y, int width, int height, Runnable press) {
        this(x, y, width, height, (Consumer<EasyButton>) (b -> press.run()));
    }

    protected EasyButton(int x, int y, int width, int height, Consumer<EasyButton> press) {
        super(x, y, width, height);
        this.press = press;
    }

    protected EasyButton(ScreenPosition position, int width, int height, Runnable press) {
        this(position, width, height, (Consumer<EasyButton>) (b -> press.run()));
    }

    protected EasyButton(ScreenPosition position, int width, int height, Consumer<EasyButton> press) {
        super(position, width, height);
        this.press = press;
    }

    protected EasyButton(ScreenPosition position, int width, int height, Component title, Runnable press) {
        this(position, width, height, title, (Consumer<EasyButton>) (b -> press.run()));
    }

    protected EasyButton(ScreenPosition position, int width, int height, Component title, Consumer<EasyButton> press) {
        super(position, width, height, title);
        this.press = press;
    }

    protected EasyButton(ScreenArea area, Runnable press) {
        this(area, (Consumer<EasyButton>) (b -> press.run()));
    }

    protected EasyButton(ScreenArea area, Consumer<EasyButton> press) {
        super(area);
        this.press = press;
    }

    protected EasyButton(ScreenArea area, Component title, Runnable press) {
        this(area, title, (Consumer<EasyButton>) (b -> press.run()));
    }

    protected EasyButton(ScreenArea area, Component title, Consumer<EasyButton> press) {
        super(area, title);
        this.press = press;
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0;
    }

    @Override
    public void playDownSound(@Nonnull SoundManager manager) {
        playClick(manager);
    }

    public static void playClick(@Nonnull SoundManager manager) {
        manager.play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        this.onPress();
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (CommonInputs.selected(int0)) {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            this.onPress();
            return true;
        } else {
            return false;
        }
    }

    protected void onPress() {
        this.press.accept(this);
        this.m_93692_(false);
    }
}