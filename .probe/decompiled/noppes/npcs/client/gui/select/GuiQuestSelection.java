package noppes.npcs.client.gui.select;

import com.google.common.collect.Lists;
import java.util.HashMap;
import noppes.npcs.controllers.QuestController;
import noppes.npcs.controllers.data.Quest;
import noppes.npcs.controllers.data.QuestCategory;
import noppes.npcs.shared.client.gui.components.GuiBasic;
import noppes.npcs.shared.client.gui.components.GuiButtonNop;
import noppes.npcs.shared.client.gui.components.GuiCustomScrollNop;
import noppes.npcs.shared.client.gui.components.GuiLabel;
import noppes.npcs.shared.client.gui.listeners.GuiSelectionListener;
import noppes.npcs.shared.client.gui.listeners.ICustomScrollListener;

public class GuiQuestSelection extends GuiBasic implements ICustomScrollListener {

    private HashMap<String, QuestCategory> categoryData = new HashMap();

    private HashMap<String, Quest> questData = new HashMap();

    private GuiCustomScrollNop scrollCategories;

    private GuiCustomScrollNop scrollQuests;

    private QuestCategory selectedCategory;

    public Quest selectedQuest;

    private GuiSelectionListener listener;

    public GuiQuestSelection(int quest) {
        this.drawDefaultBackground = false;
        this.title = "";
        this.setBackground("menubg.png");
        this.imageWidth = 366;
        this.imageHeight = 226;
        this.selectedQuest = (Quest) QuestController.instance.quests.get(quest);
        if (this.selectedQuest != null) {
            this.selectedCategory = this.selectedQuest.category;
        }
    }

    @Override
    public void init() {
        super.init();
        if (this.wrapper.parent instanceof GuiSelectionListener) {
            this.listener = (GuiSelectionListener) this.wrapper.parent;
        }
        this.addLabel(new GuiLabel(0, "gui.categories", this.guiLeft + 8, this.guiTop + 4));
        this.addLabel(new GuiLabel(1, "quest.quests", this.guiLeft + 175, this.guiTop + 4));
        this.addButton(new GuiButtonNop(this, 2, this.guiLeft + this.imageWidth - 26, this.guiTop + 4, 20, 20, "X"));
        HashMap<String, QuestCategory> categoryData = new HashMap();
        HashMap<String, Quest> questData = new HashMap();
        for (QuestCategory category : QuestController.instance.categories.values()) {
            categoryData.put(category.title, category);
        }
        this.categoryData = categoryData;
        if (this.selectedCategory != null) {
            for (Quest quest : this.selectedCategory.quests.values()) {
                questData.put(quest.title, quest);
            }
        }
        this.questData = questData;
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
        if (this.scrollQuests == null) {
            this.scrollQuests = new GuiCustomScrollNop(this, 1);
            this.scrollQuests.setSize(170, 200);
        }
        this.scrollQuests.setList(Lists.newArrayList(questData.keySet()));
        if (this.selectedQuest != null) {
            this.scrollQuests.setSelected(this.selectedQuest.title);
        }
        this.scrollQuests.guiLeft = this.guiLeft + 175;
        this.scrollQuests.guiTop = this.guiTop + 14;
        this.addScroll(this.scrollQuests);
    }

    @Override
    public void scrollClicked(double i, double j, int k, GuiCustomScrollNop guiCustomScroll) {
        if (guiCustomScroll.id == 0) {
            this.selectedCategory = (QuestCategory) this.categoryData.get(this.scrollCategories.getSelected());
            this.selectedQuest = null;
            this.scrollQuests.clearSelection();
        }
        if (guiCustomScroll.id == 1) {
            this.selectedQuest = (Quest) this.questData.get(this.scrollQuests.getSelected());
        }
        this.init();
    }

    @Override
    public void scrollDoubleClicked(String selection, GuiCustomScrollNop scroll) {
        if (this.selectedQuest != null) {
            if (this.listener != null) {
                this.listener.selected(this.selectedQuest.id, this.selectedQuest.title);
            }
            this.close();
        }
    }

    @Override
    public void buttonEvent(GuiButtonNop guibutton) {
        int id = guibutton.id;
        if (id == 2) {
            if (this.selectedQuest != null) {
                this.scrollDoubleClicked(null, null);
            } else {
                this.close();
            }
        }
    }
}