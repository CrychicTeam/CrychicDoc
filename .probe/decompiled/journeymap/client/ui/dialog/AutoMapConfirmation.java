package journeymap.client.ui.dialog;

import journeymap.client.Constants;
import journeymap.client.JourneymapClient;
import journeymap.client.properties.ClientCategory;
import journeymap.client.task.main.IMainThreadTask;
import journeymap.client.task.multi.MapRegionTask;
import journeymap.client.ui.UIManager;
import journeymap.client.ui.component.Button;
import journeymap.client.ui.component.ButtonList;
import journeymap.client.ui.component.JmUI;
import journeymap.client.ui.fullscreen.Fullscreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class AutoMapConfirmation extends JmUI {

    Button buttonOptions;

    Button buttonAll;

    Button buttonMissing;

    Button buttonClose;

    public AutoMapConfirmation() {
        this(null);
    }

    public AutoMapConfirmation(JmUI returnDisplay) {
        super(Constants.getString("jm.common.automap_dialog"), returnDisplay);
    }

    @Override
    public void init() {
        this.getRenderables().clear();
        super.setRenderBottomBar(true);
        this.buttonOptions = (Button) this.m_142416_(new Button(Constants.getString("jm.common.options_button"), button -> UIManager.INSTANCE.openOptionsManager(this, ClientCategory.Cartography)));
        this.buttonAll = (Button) this.m_142416_(new Button(Constants.getString("jm.common.automap_dialog_all"), button -> this.initAutoMapping(true, Boolean.TRUE)));
        this.buttonMissing = (Button) this.m_142416_(new Button(Constants.getString("jm.common.automap_dialog_missing"), button -> this.initAutoMapping(true, Boolean.FALSE)));
        this.buttonClose = (Button) this.m_142416_(new Button(Constants.getString("jm.common.close"), button -> this.closeAndReturn()));
        this.buttonOptions.setDefaultStyle(false);
        this.buttonAll.setDefaultStyle(false);
        this.buttonMissing.setDefaultStyle(false);
        this.buttonClose.setDefaultStyle(false);
        this.getRenderables().add(this.buttonOptions);
        this.getRenderables().add(this.buttonAll);
        this.getRenderables().add(this.buttonMissing);
        this.getRenderables().add(this.buttonClose);
    }

    @Override
    protected void layoutButtons(GuiGraphics graphics) {
        if (this.getRenderables().isEmpty()) {
            this.init();
        }
        int x = this.f_96543_ / 2;
        int lineHeight = 9 + 3;
        int y = 35 + lineHeight * 2;
        graphics.drawCenteredString(this.getFontRenderer(), Constants.getString("jm.common.automap_dialog_summary_1"), x, y, 16777215);
        y += lineHeight;
        graphics.drawCenteredString(this.getFontRenderer(), Constants.getString("jm.common.automap_dialog_summary_2"), x, y, 16777215);
        y += lineHeight * 2;
        this.buttonOptions.centerHorizontalOn(x).centerVerticalOn(y);
        y += lineHeight * 3;
        graphics.drawCenteredString(this.getFontRenderer(), Constants.getString("jm.common.automap_dialog_text"), x, y, 16776960);
        y += lineHeight * 2;
        ButtonList buttons = new ButtonList(this.buttonAll, this.buttonMissing);
        buttons.equalizeWidths(this.f_96547_, 4, 200);
        buttons.layoutCenteredHorizontal(x, y, true, 4);
        this.buttonClose.centerHorizontalOn(x).below(this.buttonMissing, lineHeight);
    }

    protected void initAutoMapping(final boolean enable, final Object arg) {
        MapRegionTask.MAP_TYPE = Fullscreen.state().getMapType();
        JourneymapClient.getInstance().queueMainThreadTask(new IMainThreadTask() {

            @Override
            public IMainThreadTask perform(Minecraft mc, JourneymapClient jm) {
                JourneymapClient.getInstance().toggleTask(MapRegionTask.Manager.class, enable, arg);
                return null;
            }

            @Override
            public String getName() {
                return "Automap";
            }
        });
        this.closeAndReturn();
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