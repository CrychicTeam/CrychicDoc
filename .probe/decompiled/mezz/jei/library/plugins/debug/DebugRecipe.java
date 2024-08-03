package mezz.jei.library.plugins.debug;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DebugRecipe {

    private static int NEXT_ID = 0;

    private final Button button = Button.builder(Component.literal("test"), b -> {
    }).bounds(0, 0, 40, 20).build();

    private final ResourceLocation registryName = new ResourceLocation("jei", "debug_recipe_" + NEXT_ID);

    public DebugRecipe() {
        NEXT_ID++;
    }

    public Button getButton() {
        return this.button;
    }

    public boolean checkHover(double mouseX, double mouseY) {
        return this.button.m_5953_(mouseX, mouseY);
    }

    public ResourceLocation getRegistryName() {
        return this.registryName;
    }
}