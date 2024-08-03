package noppes.npcs.api.gui;

import noppes.npcs.api.function.gui.GuiComponentClicked;

public interface IScroll extends ICustomGuiComponent {

    String[] getList();

    IScroll setList(String[] var1);

    @Deprecated
    int getDefaultSelection();

    @Deprecated
    IScroll setDefaultSelection(int var1);

    int[] getSelection();

    IScroll setSelection(int... var1);

    String[] getSelectionList();

    IScroll setSelectionList(String... var1);

    boolean isMultiSelect();

    IScroll setMultiSelect(boolean var1);

    IScroll setOnClick(GuiComponentClicked<IScroll> var1);

    IScroll setOnDoubleClick(GuiComponentClicked<IScroll> var1);

    boolean getHasSearch();

    IScroll setHasSearch(boolean var1);
}