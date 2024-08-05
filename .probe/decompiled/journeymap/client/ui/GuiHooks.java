package journeymap.client.ui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.data.ModelData;

public class GuiHooks {

    public static void pushGuiLayer(Screen screen) {
        ForgeHooksClient.pushGuiLayer(Minecraft.getInstance(), screen);
    }

    public static void popGuiLayer() {
        ForgeHooksClient.popGuiLayer(Minecraft.getInstance());
    }

    public static float getGuiFarPlane() {
        return ForgeHooksClient.getGuiFarPlane();
    }

    public static ModelData getModelData(BlockPos blockPos) {
        ModelData data = ModelData.EMPTY;
        if (blockPos != null) {
            data = Minecraft.getInstance().level.getModelDataManager().getAt(blockPos);
            if (data == null) {
                data = ModelData.EMPTY;
            }
        }
        return data;
    }
}