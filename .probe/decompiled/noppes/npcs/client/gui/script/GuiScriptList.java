package noppes.npcs.client.gui.script;

import java.util.ArrayList;
import java.util.List;
import noppes.npcs.controllers.ScriptContainer;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;

public class GuiScriptList extends GuiBasic {

    private GuiCustomScrollNop scroll1;

    private GuiCustomScrollNop scroll2;

    private ScriptContainer container;

    private List<String> scripts;

    public GuiScriptList(List<String> scripts, ScriptContainer container) {
        this.container = container;
        this.setBackground("menubg.png");
        this.imageWidth = 346;
        this.imageHeight = 216;
        if (scripts == null) {
            scripts = new ArrayList();
        }
        this.scripts = scripts;
    }

    @Override
    public void init() {
        super.init();
        if (this.scroll1 == null) {
            this.scroll1 = new GuiCustomScrollNop(this, 0);
            this.scroll1.setSize(140, 180);
        }
        this.scroll1.guiLeft = this.guiLeft + 4;
        this.scroll1.guiTop = this.guiTop + 14;
        this.addScroll(this.scroll1);
        this.addLabel(new GuiLabel(1, "script.availableScripts", this.guiLeft + 4, this.guiTop + 4));
        if (this.scroll2 == null) {
            this.scroll2 = new GuiCustomScrollNop(this, 1);
            this.scroll2.setSize(140, 180);
        }
        this.scroll2.guiLeft = this.guiLeft + 200;
        this.scroll2.guiTop = this.guiTop + 14;
        this.addScroll(this.scroll2);
        this.addLabel(new GuiLabel(2, "script.loadedScripts", this.guiLeft + 200, this.guiTop + 4));
        List<String> temp = new ArrayList(this.scripts);
        temp.removeAll(this.container.scripts);
        this.scroll1.setList(temp);
        this.scroll2.setList(this.container.scripts);
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 145, this.guiTop + 40, 55, 20, ">"));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 145, this.guiTop + 62, 55, 20, "<"));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 145, this.guiTop + 90, 55, 20, ">>"));
        this.addButton(new GuiButtonNop(this, 4, this.guiLeft + 145, this.guiTop + 112, 55, 20, "<<"));
        this.addButton(new GuiButtonNop(this, 66, this.guiLeft + 260, this.guiTop + 194, 60, 20, "gui.done"));
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1 && this.scroll1.hasSelected()) {
            this.container.scripts.add(this.scroll1.getSelected());
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 2 && this.scroll2.hasSelected()) {
            this.container.scripts.remove(this.scroll2.getSelected());
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 3) {
            this.container.scripts.clear();
            for (String script : this.scripts) {
                this.container.scripts.add(script);
            }
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 4) {
            this.container.scripts.clear();
            this.scroll1.clearSelection();
            this.scroll2.clearSelection();
            this.init();
        }
        if (guibutton.id == 66) {
            this.close();
        }
    }

    @Override
    public void save() {
    }
}