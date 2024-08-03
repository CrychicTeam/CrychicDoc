package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.config.ui.EditConfigListScreen;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class ListConfig<E, CV extends ConfigValue<E>> extends ConfigValue<List<E>> {

    public static final Component EMPTY_LIST = Component.literal("[]");

    public static final Component NON_EMPTY_LIST = Component.literal("[...]");

    public static final Color4I COLOR = Color4I.rgb(16755273);

    private final CV type;

    public ListConfig(CV t) {
        this.type = t;
    }

    public CV getType() {
        return this.type;
    }

    public List<E> copy(List<E> v) {
        List<E> list = new ArrayList(v.size());
        for (E value : v) {
            list.add(this.type.copy(value));
        }
        return list;
    }

    public Color4I getColor(List<E> v) {
        return COLOR;
    }

    @Override
    public void addInfo(TooltipList l) {
        if (!this.getValue().isEmpty()) {
            l.add(info("List"));
            List<E> val = this.getValue();
            for (int i = 0; i < val.size(); i++) {
                if (i >= 10) {
                    l.add(Component.literal("... " + (val.size() - i) + " more ...").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    break;
                }
                E value = (E) val.get(i);
                l.add(this.type.getStringForGUI(value));
            }
            if (!this.getDefaultValue().isEmpty()) {
                l.blankLine();
            }
        }
        if (!this.getDefaultValue().isEmpty()) {
            l.add(info("Default"));
            for (E value : this.getDefaultValue()) {
                l.add(this.type.getStringForGUI(value));
            }
        }
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        new EditConfigListScreen<>(this, callback).openGui();
    }

    public Component getStringForGUI(List<E> v) {
        return v == null ? NULL_TEXT : (v.isEmpty() ? EMPTY_LIST : this.formatListSize(v));
    }

    private Component formatListSize(List<E> v) {
        MutableComponent main = v.size() == 1 ? Component.translatable("ftblibrary.gui.listSize1") : Component.translatable("ftblibrary.gui.listSize", v.size());
        return Component.literal("[ ").append(main.withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)).append(" ]");
    }
}