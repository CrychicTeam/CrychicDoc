package dev.architectury.event.events.client;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import dev.architectury.impl.TooltipAdditionalContextsImpl;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.NonExtendable;

@OnlyIn(Dist.CLIENT)
public interface ClientTooltipEvent {

    Event<ClientTooltipEvent.Item> ITEM = EventFactory.createLoop();

    Event<ClientTooltipEvent.Render> RENDER_PRE = EventFactory.createEventResult();

    Event<ClientTooltipEvent.RenderModifyPosition> RENDER_MODIFY_POSITION = EventFactory.createLoop();

    Event<ClientTooltipEvent.RenderModifyColor> RENDER_MODIFY_COLOR = EventFactory.createLoop();

    static ClientTooltipEvent.AdditionalContexts additionalContexts() {
        return TooltipAdditionalContextsImpl.get();
    }

    @NonExtendable
    public interface AdditionalContexts {

        @Nullable
        ItemStack getItem();

        void setItem(@Nullable ItemStack var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface ColorContext {

        int getBackgroundColor();

        void setBackgroundColor(int var1);

        int getOutlineGradientTopColor();

        void setOutlineGradientTopColor(int var1);

        int getOutlineGradientBottomColor();

        void setOutlineGradientBottomColor(int var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface Item {

        void append(ItemStack var1, List<Component> var2, TooltipFlag var3);
    }

    @OnlyIn(Dist.CLIENT)
    public interface PositionContext {

        int getTooltipX();

        void setTooltipX(int var1);

        int getTooltipY();

        void setTooltipY(int var1);
    }

    @OnlyIn(Dist.CLIENT)
    public interface Render {

        EventResult renderTooltip(GuiGraphics var1, List<? extends ClientTooltipComponent> var2, int var3, int var4);
    }

    @OnlyIn(Dist.CLIENT)
    public interface RenderModifyColor {

        void renderTooltip(GuiGraphics var1, int var2, int var3, ClientTooltipEvent.ColorContext var4);
    }

    @OnlyIn(Dist.CLIENT)
    public interface RenderModifyPosition {

        void renderTooltip(GuiGraphics var1, ClientTooltipEvent.PositionContext var2);
    }
}