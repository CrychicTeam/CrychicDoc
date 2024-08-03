package dev.ftb.mods.ftblibrary.config;

import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.Panel;
import dev.ftb.mods.ftblibrary.ui.SimpleTextButton;
import dev.ftb.mods.ftblibrary.ui.Widget;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.ui.misc.ButtonListBaseScreen;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import org.jetbrains.annotations.Nullable;

public class EnumConfig<E> extends ConfigWithVariants<E> {

    public final NameMap<E> nameMap;

    public EnumConfig(NameMap<E> nm) {
        this.nameMap = nm;
        this.defaultValue = this.nameMap.defaultValue;
        this.value = this.nameMap.defaultValue;
    }

    @Override
    public Component getStringForGUI(E v) {
        return this.nameMap.getDisplayName(v);
    }

    @Override
    public Color4I getColor(E v) {
        Color4I col = this.nameMap.getColor(v);
        return col.isEmpty() ? Tristate.DEFAULT.color : col;
    }

    @Override
    public void addInfo(TooltipList list) {
        super.addInfo(list);
        if (this.nameMap.size() > 0) {
            list.blankLine();
            for (int i = 0; i < this.nameMap.size(); i++) {
                if (i >= 10) {
                    list.add(Component.literal("... " + (this.nameMap.size() - i) + " more ...").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
                    break;
                }
                E v = this.nameMap.get(i);
                boolean e = this.isEqual(v, this.value);
                MutableComponent c = Component.literal(e ? "+ " : "- ");
                c.withStyle(e ? ChatFormatting.AQUA : ChatFormatting.DARK_GRAY);
                c.append(this.nameMap.getDisplayName(v));
                list.add(c);
            }
        }
    }

    @Override
    public void onClicked(Widget clickedWidget, MouseButton button, ConfigCallback callback) {
        if (this.nameMap.values.size() <= 16 && !BaseScreen.isCtrlKeyDown()) {
            super.onClicked(clickedWidget, button, callback);
        } else {
            var gui = new ButtonListBaseScreen() {

                @Override
                public void addButtons(Panel panel) {
                    for (final E v : EnumConfig.this.nameMap) {
                        panel.add(new SimpleTextButton(panel, EnumConfig.this.nameMap.getDisplayName(v), EnumConfig.this.nameMap.getIcon(v)) {

                            @Override
                            public void onClicked(MouseButton button) {
                                this.playClickSound();
                                boolean changed = EnumConfig.this.setCurrentValue(v);
                                callback.save(changed);
                            }
                        });
                    }
                }
            };
            gui.setHasSearchBox(true);
            gui.openGui();
        }
    }

    @Override
    public E getIteration(E currentValue, boolean next) {
        return next ? this.nameMap.getNext(currentValue) : this.nameMap.getPrevious(currentValue);
    }

    @Override
    public Icon getIcon(@Nullable E v) {
        if (v != null) {
            Icon icon = this.nameMap.getIcon(v);
            if (!icon.isEmpty()) {
                return icon;
            }
        }
        return super.getIcon(v);
    }
}