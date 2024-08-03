package dev.ftb.mods.ftbquests.util;

import dev.ftb.mods.ftblibrary.config.ConfigCallback;
import dev.ftb.mods.ftblibrary.config.ConfigValue;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import dev.ftb.mods.ftbquests.client.gui.SelectQuestObjectScreen;
import dev.ftb.mods.ftbquests.quest.QuestObjectBase;
import java.util.function.Predicate;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ConfigQuestObject<T extends QuestObjectBase> extends ConfigValue<T> {

    public final Predicate<QuestObjectBase> predicate;

    public ConfigQuestObject(Predicate<QuestObjectBase> t) {
        this.predicate = t;
    }

    public Component getStringForGUI(@Nullable QuestObjectBase value) {
        return (Component) (value == null ? Component.empty() : value.getTitle());
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.getCanEdit()) {
            new SelectQuestObjectScreen<>(this, callback).openGui();
        }
    }

    @Override
    public void addInfo(TooltipList list) {
        if (this.value != null) {
            list.add(info("ID", this.value));
        }
    }
}