package journeymap.client.ui.dialog;

import journeymap.client.Constants;
import journeymap.client.task.main.DeleteMapTask;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.JmUI;
import net.minecraft.client.gui.GuiGraphics;

public class DeleteMapConfirmation extends JmUI {

    Button buttonAll;

    Button buttonCurrent;

    Button buttonClose;

    public DeleteMapConfirmation() {
        this(null);
    }

    public DeleteMapConfirmation(JmUI returnDisplay) {
        super(Constants.getString("jm.common.deletemap_dialog"), returnDisplay);
    }

    @Override
    public void init() {
        this.getRenderables().clear();
        super.setRenderBottomBar(true);
        this.m_142416_(this.buttonAll = new Button(Constants.getString("jm.common.deletemap_dialog_all"), b -> {
            DeleteMapTask.queue(true);
            this.closeAndReturn();
        }));
        this.m_142416_(this.buttonCurrent = new Button(Constants.getString("jm.common.deletemap_dialog_this"), b -> {
            DeleteMapTask.queue(false);
            this.closeAndReturn();
        }));
        this.m_142416_(this.buttonClose = new Button(Constants.getString("jm.waypoint.cancel"), b -> this.closeAndReturn()));
        this.buttonAll.setDefaultStyle(false);
        this.buttonCurrent.setDefaultStyle(false);
        this.buttonClose.setDefaultStyle(false);
        this.getRenderables().add(this.buttonAll);
        this.getRenderables().add(this.buttonCurrent);
        this.getRenderables().add(this.buttonClose);
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.init();
        }
        int x = this.f_96543_ / 2;
        int y = this.f_96544_ / 4;
        int vgap = 3;
        graphics.drawCenteredString(this.getFontRenderer(), Constants.getString("jm.common.deletemap_dialog_text"), x, y, 16777215);
        this.buttonAll.centerHorizontalOn(x).setY(y + 18);
        this.buttonCurrent.centerHorizontalOn(x).below(this.buttonAll, 3);
        this.buttonClose.centerHorizontalOn(x).below(this.buttonCurrent, 12);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        switch(keyCode) {
            case 256:
                this.closeAndReturn();
            default:
                return true;
        }
    }
}