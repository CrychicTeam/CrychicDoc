package io.github.lightman314.lightmanscurrency.client.gui.easy;

import io.github.lightman314.lightmanscurrency.api.misc.IEasyTickable;
import io.github.lightman314.lightmanscurrency.api.misc.client.rendering.EasyGuiGraphics;
import io.github.lightman314.lightmanscurrency.client.gui.easy.interfaces.IEasyScreen;
import io.github.lightman314.lightmanscurrency.client.gui.widget.button.tab.ITab;
import io.github.lightman314.lightmanscurrency.client.util.ScreenArea;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.Font;

public abstract class EasyTab implements ITab, IEasyTickable {

    private final IEasyScreen screen;

    private final List<Object> children = new ArrayList();

    private boolean wasOpen = false;

    public final Font getFont() {
        return this.screen.getFont();
    }

    protected EasyTab(IEasyScreen screen) {
        this.screen = screen;
    }

    public boolean blockInventoryClosing() {
        return false;
    }

    public <T> T addChild(T child) {
        if (!this.children.contains(child)) {
            this.children.add(child);
        }
        this.screen.addChild(child);
        return child;
    }

    public void removeChild(Object child) {
        this.children.remove(child);
        this.screen.removeChild(child);
    }

    public final void onOpen() {
        this.children.clear();
        this.addChild(this);
        this.initialize(this.screen.getArea(), !this.wasOpen);
        this.wasOpen = true;
    }

    protected abstract void initialize(ScreenArea var1, boolean var2);

    public abstract void renderBG(@Nonnull EasyGuiGraphics var1);

    public void renderAfterWidgets(@Nonnull EasyGuiGraphics gui) {
    }

    public final void onClose() {
        for (Object child : new ArrayList(this.children)) {
            this.screen.removeChild(child);
        }
        this.children.clear();
        this.wasOpen = false;
        this.closeAction();
    }

    protected void closeAction() {
    }

    @Override
    public void tick() {
    }
}