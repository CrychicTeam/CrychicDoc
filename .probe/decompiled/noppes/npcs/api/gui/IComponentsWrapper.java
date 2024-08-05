package noppes.npcs.api.gui;

import java.util.List;
import noppes.npcs.api.entity.IEntity;
import noppes.npcs.api.item.IItemStack;

public interface IComponentsWrapper {

    IButton addButton(int var1, String var2, int var3, int var4);

    IButton addButton(int var1, String var2, int var3, int var4, int var5, int var6);

    IButtonList addButtonList(int var1, int var2, int var3, int var4, int var5);

    IButton addTexturedButton(int var1, String var2, int var3, int var4, int var5, int var6, String var7);

    IButton addTexturedButton(int var1, String var2, int var3, int var4, int var5, int var6, String var7, int var8, int var9);

    ILabel addLabel(int var1, String var2, int var3, int var4, int var5, int var6);

    ILabel addLabel(int var1, String var2, int var3, int var4, int var5, int var6, int var7);

    ITextField addTextField(int var1, int var2, int var3, int var4, int var5);

    ITextArea addTextArea(int var1, int var2, int var3, int var4, int var5);

    IScroll addScroll(int var1, int var2, int var3, int var4, int var5, String[] var6);

    ISlider addSlider(int var1, int var2, int var3, int var4, int var5, String var6);

    IEntityDisplay addEntityDisplay(int var1, int var2, int var3, IEntity var4);

    IAssetsSelector addAssetsSelector(int var1, int var2, int var3, int var4, int var5);

    ITexturedRect addTexturedRect(int var1, String var2, int var3, int var4, int var5, int var6);

    ITexturedRect addTexturedRect(int var1, String var2, int var3, int var4, int var5, int var6, int var7, int var8);

    List<ICustomGuiComponent> getComponents();

    ICustomGuiComponent getComponent(int var1);

    void addComponent(ICustomGuiComponent var1);

    void removeComponent(int var1);

    List<IItemSlot> getSlots();

    List<IItemSlot> getPlayerSlots();

    IItemSlot addItemSlot(int var1, int var2);

    IItemSlot addItemSlot(int var1, int var2, IItemStack var3);

    void removeItemSlot(IItemSlot var1);

    @Deprecated
    void showPlayerInventory(int var1, int var2);

    IItemSlot[] showPlayerInventory(int var1, int var2, boolean var3);
}