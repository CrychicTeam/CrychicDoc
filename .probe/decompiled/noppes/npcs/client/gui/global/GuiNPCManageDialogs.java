package noppes.npcs.client.gui.global;

import com.google.common.collect.Lists;
import java.util.HashMap;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiEditText;
import noppes.npcs.client.gui.util.GuiNPCInterface2;
import noppes.npcs.controllers.DialogController;
import noppes.npcs.controllers.data.Dialog;
import noppes.npcs.controllers.data.DialogCategory;
import noppes.npcs.entity.EntityNPCInterface;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.server.SPacketDialogCategoryRemove;
import noppes.npcs.packets.server.SPacketDialogCategorySave;
import noppes.npcs.packets.server.SPacketDialogRemove;
import noppes.npcs.packets.server.SPacketDialogSave;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.components.GuiTextFieldNop;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiNPCManageDialogs extends GuiNPCInterface2 implements ICustomScrollListener {

    private HashMap<String, DialogCategory> categoryData = new HashMap();

    private HashMap<String, Dialog> dialogData = new HashMap();

    private GuiCustomScrollNop scrollCategories;

    private GuiCustomScrollNop scrollDialogs;

    public static Screen Instance;

    private DialogCategory selectedCategory;

    private Dialog selectedDialog;

    public GuiNPCManageDialogs(EntityNPCInterface npc) {
        super(npc);
        Instance = this;
    }

    @Override
    public void init() {
        super.init();
        this.addLabel(new GuiLabel(0, "gui.categories", this.guiLeft + 8, this.guiTop + 4));
        this.addLabel(new GuiLabel(1, "dialog.dialogs", this.guiLeft + 175, this.guiTop + 4));
        this.addLabel(new GuiLabel(3, "dialog.dialogs", this.guiLeft + 356, this.guiTop + 8));
        this.addButton(new GuiButtonNop(this, 13, this.guiLeft + 356, this.guiTop + 18, 58, 20, "selectServer.edit", this.selectedDialog != null));
        this.addButton(new GuiButtonNop(this, 12, this.guiLeft + 356, this.guiTop + 41, 58, 20, "gui.remove", this.selectedDialog != null));
        this.addButton(new GuiButtonNop(this, 11, this.guiLeft + 356, this.guiTop + 64, 58, 20, "gui.add", this.selectedCategory != null));
        this.addLabel(new GuiLabel(2, "gui.categories", this.guiLeft + 356, this.guiTop + 110));
        this.addButton(new GuiButtonNop(this, 3, this.guiLeft + 356, this.guiTop + 120, 58, 20, "selectServer.edit", this.selectedCategory != null));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + 356, this.guiTop + 143, 58, 20, "gui.remove", this.selectedCategory != null));
        this.addButton(new GuiButtonNop(this, 1, this.guiLeft + 356, this.guiTop + 166, 58, 20, "gui.add"));
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
        this.scrollCategories.guiLeft = this.guiLeft + 4;
        this.scrollCategories.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollCategories);
        if (this.scrollDialogs == null) {
            this.scrollDialogs = new GuiCustomScrollNop(this, 1);
            this.scrollDialogs.setSize(170, 200);
        }
        this.scrollDialogs.setList(Lists.newArrayList(dialogData.keySet()));
        this.scrollDialogs.guiLeft = this.guiLeft + 175;
        this.scrollDialogs.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollDialogs);
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        if (guibutton.id == 1) {
            this.setSubGui(new SubGuiEditText(1, I18n.get("gui.new")));
        }
        if (guibutton.id == 2) {
            ConfirmScreen guiyesno = new ConfirmScreen(bo -> {
                if (bo) {
                    Packets.sendServer(new SPacketDialogCategoryRemove(this.selectedCategory.id));
                }
                NoppesUtil.openGUI(this.player, this);
            }, Component.translatable(this.selectedCategory.title), Component.translatable("gui.deleteMessage"));
            this.setScreen(guiyesno);
        }
        if (guibutton.id == 3) {
            this.setSubGui(new SubGuiEditText(3, this.selectedCategory.title));
        }
        if (guibutton.id == 11) {
            this.setSubGui(new SubGuiEditText(11, I18n.get("gui.new")));
        }
        if (guibutton.id == 12) {
            ConfirmScreen guiyesno = new ConfirmScreen(bo -> {
                if (bo) {
                    Packets.sendServer(new SPacketDialogRemove(this.selectedDialog.id));
                }
                NoppesUtil.openGUI(this.player, this);
            }, Component.translatable(this.selectedDialog.title), Component.translatable("gui.deleteMessage"));
            this.setScreen(guiyesno);
        }
        if (guibutton.id == 13) {
            this.setSubGui(new GuiDialogEdit(this.selectedDialog));
        }
    }

    @Override
    public void subGuiClosed(Screen subgui) {
        if (!(subgui instanceof SubGuiEditText) || !((SubGuiEditText) subgui).cancelled) {
            if (subgui instanceof SubGuiEditText dialogEdit) {
                if (dialogEdit.id == 1) {
                    DialogCategory category = new DialogCategory();
                    category.title = dialogEdit.text;
                    while (DialogController.instance.containsCategoryName(category)) {
                        category.title = category.title + "_";
                    }
                    Packets.sendServer(new SPacketDialogCategorySave(category.writeNBT(new CompoundTag())));
                }
                if (dialogEdit.id == 3) {
                    this.selectedCategory.title = dialogEdit.text;
                    while (DialogController.instance.containsCategoryName(this.selectedCategory)) {
                        this.selectedCategory.title = this.selectedCategory.title + "_";
                    }
                    Packets.sendServer(new SPacketDialogCategorySave(this.selectedCategory.writeNBT(new CompoundTag())));
                }
                if (dialogEdit.id == 11) {
                    Dialog dialog = new Dialog(this.selectedCategory);
                    dialog.title = dialogEdit.text;
                    while (DialogController.instance.containsDialogName(this.selectedCategory, dialog)) {
                        dialog.title = dialog.title + "_";
                    }
                    Packets.sendServer(new SPacketDialogSave(this.selectedCategory.id, dialog.save(new CompoundTag())));
                }
            }
            if (subgui instanceof GuiDialogEdit) {
                this.init();
            }
        }
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
        if (this.selectedDialog != null && scroll.id == 1) {
            this.setSubGui(new GuiDialogEdit(this.selectedDialog));
        }
    }

    @Override
    public void close() {
        super.close();
    }

    @Override
    public void save() {
        GuiTextFieldNop.unfocus();
    }
}