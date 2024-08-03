package dev.architectury.event.events.client;

import dev.architectury.event.CompoundEventResult;
import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.hooks.client.screen.ScreenAccess;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ClientGuiEvent {

    Event<ClientGuiEvent.RenderHud> RENDER_HUD = EventFactory.createLoop();

    Event<ClientGuiEvent.DebugText> DEBUG_TEXT_LEFT = EventFactory.createLoop();

    Event<ClientGuiEvent.DebugText> DEBUG_TEXT_RIGHT = EventFactory.createLoop();

    Event<ClientGuiEvent.ScreenInitPre> INIT_PRE = EventFactory.createEventResult();

    Event<ClientGuiEvent.ScreenInitPost> INIT_POST = EventFactory.createLoop();

    Event<ClientGuiEvent.ScreenRenderPre> RENDER_PRE = EventFactory.createEventResult();

    Event<ClientGuiEvent.ScreenRenderPost> RENDER_POST = EventFactory.createLoop();

    Event<ClientGuiEvent.ContainerScreenRenderBackground> RENDER_CONTAINER_BACKGROUND = EventFactory.createLoop();

    Event<ClientGuiEvent.ContainerScreenRenderForeground> RENDER_CONTAINER_FOREGROUND = EventFactory.createLoop();

    Event<ClientGuiEvent.SetScreen> SET_SCREEN = EventFactory.createCompoundEventResult();

    @OnlyIn(Dist.CLIENT)
    public interface ContainerScreenRenderBackground {

        void render(AbstractContainerScreen<?> var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ContainerScreenRenderForeground {

        void render(AbstractContainerScreen<?> var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    @OnlyIn(Dist.CLIENT)
    public interface DebugText {

        void gatherText(List<String> var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface RenderHud {

        void renderHud(GuiGraphics var1, float var2);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ScreenInitPost {

        void init(Screen var1, ScreenAccess var2);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ScreenInitPre {

        EventResult init(Screen var1, ScreenAccess var2);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ScreenRenderPost {

        void render(Screen var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ScreenRenderPre {

        EventResult render(Screen var1, GuiGraphics var2, int var3, int var4, float var5);
    }

    @OnlyIn(Dist.CLIENT)
    public interface SetScreen {

        CompoundEventResult<Screen> modifyScreen(Screen var1);
    }
}