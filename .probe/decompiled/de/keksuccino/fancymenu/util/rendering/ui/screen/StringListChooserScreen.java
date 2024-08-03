package de.keksuccino.fancymenu.util.rendering.ui.screen;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class StringListChooserScreen extends CellScreen {

    protected Consumer<String> callback;

    protected List<String> list;

    public StringListChooserScreen(@NotNull Component title, @NotNull List<String> stringList, @NotNull Consumer<String> callback) {
        super(title);
        this.list = stringList;
        this.callback = callback;
    }

    @Override
    protected void initCells() {
        for (String s : this.list) {
            this.addCell(new StringListChooserScreen.StringCell(s)).setSelectable(true);
        }
        this.addSpacerCell(20);
    }

    @Override
    public boolean allowDone() {
        return this.getSelectedCell() != null;
    }

    @Override
    protected void onCancel() {
        this.callback.accept(null);
    }

    @Override
    protected void onDone() {
        if (this.getSelectedCell() instanceof StringListChooserScreen.StringCell s) {
            this.callback.accept(s.string);
        }
    }

    public class StringCell extends CellScreen.LabelCell {

        public String string;

        public StringCell(@NotNull String string) {
            super(Component.literal(string));
            this.string = string;
        }
    }
}