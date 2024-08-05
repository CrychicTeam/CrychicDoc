package noppes.npcs.api.gui;

import noppes.npcs.api.function.gui.GuiComponentClicked;
import noppes.npcs.api.function.gui.GuiComponentUpdate;

public interface IAssetsSelector extends ICustomGuiComponent {

    String getSelected();

    IAssetsSelector setSelected(String var1);

    String getRoot();

    IAssetsSelector setRoot(String var1);

    String getFileType();

    IAssetsSelector setFileType(String var1);

    IAssetsSelector setOnChange(GuiComponentUpdate<IAssetsSelector> var1);

    IAssetsSelector setOnPress(GuiComponentClicked<IAssetsSelector> var1);
}