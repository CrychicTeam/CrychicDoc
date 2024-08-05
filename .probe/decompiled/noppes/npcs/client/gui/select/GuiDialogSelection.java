package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.util.HashMap;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiDialogSelection extends GuiBasic implements ICustomScrollListener {

    private HashMap<String, DialogCategory> categoryData = new HashMap();

    private HashMap<String, Dialog> dialogData = new HashMap();

    private GuiCustomScrollNop scrollCategories;

    private GuiCustomScrollNop scrollDialogs;

    private DialogCategory selectedCategory;

    public Dialog selectedDialog;

    private GuiSelectionListener listener;

    public GuiDialogSelection(int dialog) {
        this.drawDefaultBackground = false;
        this.title = "";
        this.setBackground("menubg.png");
        this.imageWidth = 366;
        this.imageHeight = 226;
        this.selectedDialog = (Dialog) DialogController.instance.dialogs.get(dialog);
        if (this.selectedDialog != null) {
            this.selectedCategory = this.selectedDialog.category;
        }
    }

    @Override
    public void init() {
        super.init();
        if (this.wrapper.parent instanceof GuiSelectionListener) {
            this.listener = (GuiSelectionListener) this.wrapper.parent;
        }
        this.addLabel(new GuiLabel(0, "gui.categories", this.guiLeft + 8, this.guiTop + 4));
        this.addLabel(new GuiLabel(1, "dialog.dialogs", this.guiLeft + 175, this.guiTop + 4));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + this.imageWidth - 26, this.guiTop + 4, 20, 20, "X"));
        HashMap<String, DialogCategory> categoryData = new HashMap();
        HashMap<String, Dialog> dialogData = new HashMap();
        for (DialogCategory category : DialogController.instance.categories.values()) {
            categoryData.put(category.title, category);
        }
        this.categoryData = categoryData;
        if (this.selectedCategory != null) {
            for (Dialog dialog : this.selectedCategory.dialogs.values()) {
                dialogData.put(dialog.title, dialog);
            }
        }
        this.dialogData = dialogData;
        if (this.scrollCategories == null) {
            this.scrollCategories = new GuiCustomScrollNop(this, 0);
            this.scrollCategories.setSize(170, 200);
        }
        this.scrollCategories.setList(Lists.newArrayList(categoryData.keySet()));
        if (this.selectedCategory != null) {
            this.scrollCategories.setSelected(this.selectedCategory.title);
        }
        this.scrollCategories.guiLeft = this.guiLeft + 4;
        this.scrollCategories.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollCategories);
        if (this.scrollDialogs == null) {
            this.scrollDialogs = new GuiCustomScrollNop(this, 1);
            this.scrollDialogs.setSize(170, 200);
        }
        this.scrollDialogs.setList(Lists.newArrayList(dialogData.keySet()));
        if (this.selectedDialog != null) {
            this.scrollDialogs.setSelected(this.selectedDialog.title);
        }
        this.scrollDialogs.guiLeft = this.guiLeft + 175;
        this.scrollDialogs.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollDialogs);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        if (guiCustomScroll.id == 0) {
            this.selectedCategory = (DialogCategory) this.categoryData.get(this.scrollCategories.getSelected());
            this.selectedDialog = null;
            this.scrollDialogs.clearSelection();
        }
        if (guiCustomScroll.id == 1) {
            this.selectedDialog = (Dialog) this.dialogData.get(this.scrollDialogs.getSelected());
        }
        this.init();
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
        if (this.selectedDialog != null) {
            if (this.listener != null) {
                this.listener.selected(this.selectedDialog.id, this.selectedDialog.title);
            }
            this.close();
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            if (this.selectedDialog != null) {
                this.scrollDoubleClicked(null, null);
            } else {
                this.close();
            }
        }
    }
}