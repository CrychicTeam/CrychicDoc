package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;

public final class GameRenderEvents {

    public static final EventInvoker<GameRenderEvents.Before> BEFORE = EventInvoker.lookup(GameRenderEvents.Before.class);

    public static final EventInvoker<GameRenderEvents.After> AFTER = EventInvoker.lookup(GameRenderEvents.After.class);

    private GameRenderEvents() {
    }

    @FunctionalInterface
    public interface After {

        void onAfterGameRender(Minecraft var1, GameRenderer var2, float var3);
    }

    @FunctionalInterface
    public interface Before {

        void onBeforeGameRender(Minecraft var1, GameRenderer var2, float var3);
    }
}