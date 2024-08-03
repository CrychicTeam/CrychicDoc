package fuzs.puzzleslib.api.client.event.v1;

import fuzs.puzzleslib.api.event.v1.core.EventInvoker;
import fuzs.puzzleslib.api.event.v1.core.EventResult;
import java.util.Objects;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.GameType;

public final class RenderGuiElementEvents {

    public static final RenderGuiElementEvents.GuiOverlay VIGNETTE = new RenderGuiElementEvents.GuiOverlay("vignette");

    public static final RenderGuiElementEvents.GuiOverlay SPYGLASS = new RenderGuiElementEvents.GuiOverlay("spyglass");

    public static final RenderGuiElementEvents.GuiOverlay HELMET = new RenderGuiElementEvents.GuiOverlay("helmet");

    public static final RenderGuiElementEvents.GuiOverlay FROSTBITE = new RenderGuiElementEvents.GuiOverlay("frostbite");

    public static final RenderGuiElementEvents.GuiOverlay PORTAL = new RenderGuiElementEvents.GuiOverlay("portal");

    public static final RenderGuiElementEvents.GuiOverlay HOTBAR = new RenderGuiElementEvents.GuiOverlay("hotbar", minecraft -> !minecraft.options.hideGui && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR);

    public static final RenderGuiElementEvents.GuiOverlay CROSSHAIR = new RenderGuiElementEvents.GuiOverlay("crosshair", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay BOSS_EVENT_PROGRESS = new RenderGuiElementEvents.GuiOverlay("boss_event_progress", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay PLAYER_HEALTH = new RenderGuiElementEvents.GuiOverlay("player_health", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay ARMOR_LEVEL = new RenderGuiElementEvents.GuiOverlay("armor_level", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay FOOD_LEVEL = new RenderGuiElementEvents.GuiOverlay("food_level", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay MOUNT_HEALTH = new RenderGuiElementEvents.GuiOverlay("mount_health", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay AIR_LEVEL = new RenderGuiElementEvents.GuiOverlay("air_level", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay JUMP_BAR = new RenderGuiElementEvents.GuiOverlay("jump_bar", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay EXPERIENCE_BAR = new RenderGuiElementEvents.GuiOverlay("experience_bar", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay ITEM_NAME = new RenderGuiElementEvents.GuiOverlay("item_name", minecraft -> !minecraft.options.hideGui && minecraft.gameMode.getPlayerMode() != GameType.SPECTATOR);

    public static final RenderGuiElementEvents.GuiOverlay SLEEP_FADE = new RenderGuiElementEvents.GuiOverlay("sleep_fade");

    public static final RenderGuiElementEvents.GuiOverlay DEBUG_TEXT = new RenderGuiElementEvents.GuiOverlay("debug_text");

    public static final RenderGuiElementEvents.GuiOverlay FPS_GRAPH = new RenderGuiElementEvents.GuiOverlay("fps_graph");

    public static final RenderGuiElementEvents.GuiOverlay POTION_ICONS = new RenderGuiElementEvents.GuiOverlay("potion_icons");

    public static final RenderGuiElementEvents.GuiOverlay RECORD_OVERLAY = new RenderGuiElementEvents.GuiOverlay("record_overlay", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay SUBTITLES = new RenderGuiElementEvents.GuiOverlay("subtitles", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay TITLE_TEXT = new RenderGuiElementEvents.GuiOverlay("title_text", minecraft -> !minecraft.options.hideGui);

    public static final RenderGuiElementEvents.GuiOverlay SCOREBOARD = new RenderGuiElementEvents.GuiOverlay("scoreboard");

    public static final RenderGuiElementEvents.GuiOverlay CHAT_PANEL = new RenderGuiElementEvents.GuiOverlay("chat_panel");

    public static final RenderGuiElementEvents.GuiOverlay PLAYER_LIST = new RenderGuiElementEvents.GuiOverlay("player_list");

    private RenderGuiElementEvents() {
    }

    public static EventInvoker<RenderGuiElementEvents.Before> before(RenderGuiElementEvents.GuiOverlay id) {
        Objects.requireNonNull(id, "id is null");
        return EventInvoker.lookup(RenderGuiElementEvents.Before.class, id);
    }

    public static EventInvoker<RenderGuiElementEvents.After> after(RenderGuiElementEvents.GuiOverlay id) {
        Objects.requireNonNull(id, "id is null");
        return EventInvoker.lookup(RenderGuiElementEvents.After.class, id);
    }

    @FunctionalInterface
    public interface After {

        void onAfterRenderGuiElement(Minecraft var1, GuiGraphics var2, float var3, int var4, int var5);
    }

    @FunctionalInterface
    public interface Before {

        EventResult onBeforeRenderGuiElement(Minecraft var1, GuiGraphics var2, float var3, int var4, int var5);
    }

    public static record GuiOverlay(ResourceLocation id, Predicate<Minecraft> filter) {

        public GuiOverlay(String id) {
            this(new ResourceLocation(id));
        }

        public GuiOverlay(ResourceLocation id) {
            this(id, minecraft -> true);
        }

        public GuiOverlay(String id, Predicate<Minecraft> filter) {
            this(new ResourceLocation(id), filter);
        }

        public GuiOverlay(ResourceLocation id, Predicate<Minecraft> filter) {
            Objects.requireNonNull(id, "id is null");
            Objects.requireNonNull(filter, "filter is null");
            this.id = id;
            this.filter = filter;
        }
    }
}