package vazkii.patchouli.api;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.level.Level;

public interface IComponentProcessor {

    void setup(Level var1, IVariableProvider var2);

    IVariable process(Level var1, String var2);

    default void refresh(Screen parent, int left, int top) {
    }

    default boolean allowRender(String group) {
        return true;
    }
}