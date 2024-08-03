package dev.ftb.mods.ftbquests.client.gui;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.ContextMenuItem;
import dev.ftb.mods.ftbquests.client.gui.quests.QuestScreen;
import dev.ftb.mods.ftbquests.quest.Movable;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.network.chat.Component;

public class ContextMenuBuilder {

    private final QuestObjectBase object;

    private final QuestScreen screen;

    private Movable deletionFocus = null;

    private final List<ContextMenuItem> atTop = new ArrayList();

    private final List<ContextMenuItem> atBottom = new ArrayList();

    private ContextMenuBuilder(QuestObjectBase object, QuestScreen screen) {
        this.object = object;
        this.screen = screen;
    }

    public static ContextMenuBuilder create(QuestObjectBase object, QuestScreen screen) {
        return new ContextMenuBuilder(object, screen);
    }

    public ContextMenuBuilder withDeletionFocus(Movable m) {
        this.deletionFocus = m;
        return this;
    }

    public ContextMenuBuilder insertAtTop(Collection<ContextMenuItem> toAdd) {
        this.atTop.addAll(toAdd);
        return this;
    }

    public ContextMenuBuilder insertAtBottom(Collection<ContextMenuItem> toAdd) {
        this.atBottom.addAll(toAdd);
        return this;
    }

    public void openContextMenu(BaseScreen gui) {
        gui.openContextMenu(this.build(gui));
    }

    public List<ContextMenuItem> build(BaseScreen gui) {
        List<ContextMenuItem> res = new ArrayList();
        String titleStr = this.object.getTitle().getString();
        String closeQuote = "\"";
        if (titleStr.contains("\n")) {
            titleStr = titleStr.split("\n")[0];
            closeQuote = "\" ...";
        }
        res.add(new ContextMenuItem(Component.literal("\"").append(titleStr).append(closeQuote), Color4I.empty(), null).setCloseMenu(false));
        res.add(ContextMenuItem.SEPARATOR);
        res.addAll(this.atTop);
        this.screen.addObjectMenuItems(res, gui, this.object, this.deletionFocus);
        res.addAll(this.atBottom);
        return List.copyOf(res);
    }
}