package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.data.MutableFloat;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;

@FunctionalInterface
public interface ComputeCameraAnglesCallback {

    EventInvoker<ComputeCameraAnglesCallback> EVENT = EventInvoker.lookup(ComputeCameraAnglesCallback.class);

    void onComputeCameraAngles(GameRenderer var1, Camera var2, float var3, MutableFloat var4, MutableFloat var5, MutableFloat var6);
}