package dev.latvian.mods.kubejs.client;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientPlayerEvent;
import dev.architectury.event.events.client.ClientTooltipEvent;
import dev.architectury.hooks.client.screen.ScreenAccess;
import dev.architectury.hooks.fluid.FluidBucketHooks;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.bindings.event.ClientEvents;
import dev.latvian.mods.kubejs.bindings.event.ItemEvents;
import dev.latvian.mods.kubejs.client.painter.Painter;
import dev.latvian.mods.kubejs.core.ImageButtonKJS;
import dev.latvian.mods.kubejs.item.ItemTooltipEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.Tags;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.recipebook.RecipeUpdateListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class KubeJSClientEventHandler {

    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");

    public static Map<Item, List<ItemTooltipEventJS.StaticTooltipHandler>> staticItemTooltips = null;

    private final Map<ResourceLocation, TagInstance> tempTagNames = new LinkedHashMap();

    public void init() {
        ClientGuiEvent.DEBUG_TEXT_LEFT.register(this::debugInfoLeft);
        ClientGuiEvent.DEBUG_TEXT_RIGHT.register(this::debugInfoRight);
        ClientTooltipEvent.ITEM.register(this::itemTooltip);
        ClientPlayerEvent.CLIENT_PLAYER_JOIN.register(this::loggedIn);
        ClientPlayerEvent.CLIENT_PLAYER_QUIT.register(this::loggedOut);
        ClientPlayerEvent.CLIENT_PLAYER_RESPAWN.register(this::respawn);
        ClientGuiEvent.RENDER_HUD.register(Painter.INSTANCE::inGameScreenDraw);
        ClientGuiEvent.RENDER_POST.register(Painter.INSTANCE::guiScreenDraw);
        ClientGuiEvent.INIT_PRE.register(this::guiPreInit);
        ClientGuiEvent.INIT_POST.register(this::guiPostInit);
    }

    private void debugInfoLeft(List<String> lines) {
        if (Minecraft.getInstance().player != null && ClientEvents.DEBUG_LEFT.hasListeners()) {
            ClientEvents.DEBUG_LEFT.post(ScriptType.CLIENT, new DebugInfoEventJS(lines));
        }
    }

    private void debugInfoRight(List<String> lines) {
        if (Minecraft.getInstance().player != null && ClientEvents.DEBUG_RIGHT.hasListeners()) {
            ClientEvents.DEBUG_RIGHT.post(ScriptType.CLIENT, new DebugInfoEventJS(lines));
        }
    }

    private void itemTooltip(ItemStack stack, List<Component> lines, TooltipFlag flag) {
        if (!stack.isEmpty()) {
            boolean advanced = flag.isAdvanced();
            if (advanced && ClientProperties.get().getShowTagNames() && Screen.hasShiftDown()) {
                Consumer<TagKey<?>> addToTempTags = tag -> ((TagInstance) this.tempTagNames.computeIfAbsent(tag.location(), TagInstance::new)).registries.add(tag.registry());
                Tags.byItemStack(stack).forEach(addToTempTags);
                if (stack.getItem() instanceof BlockItem item) {
                    Tags.byBlock(item.getBlock()).forEach(addToTempTags);
                }
                if (stack.getItem() instanceof BucketItem bucket) {
                    Fluid fluid = FluidBucketHooks.getFluid(bucket);
                    if (fluid != Fluids.EMPTY) {
                        Tags.byFluid(fluid).forEach(addToTempTags);
                    }
                }
                if (stack.getItem() instanceof SpawnEggItem item) {
                    Tags.byEntityType(item.getType(stack.getTag())).forEach(addToTempTags);
                }
                for (TagInstance instance : this.tempTagNames.values()) {
                    lines.add(instance.toText());
                }
                this.tempTagNames.clear();
            }
            if (staticItemTooltips == null) {
                staticItemTooltips = new HashMap();
                ItemEvents.TOOLTIP.post(ScriptType.CLIENT, new ItemTooltipEventJS(staticItemTooltips));
            }
            try {
                for (ItemTooltipEventJS.StaticTooltipHandler handler : (List) staticItemTooltips.getOrDefault(Items.AIR, List.of())) {
                    handler.tooltip(stack, advanced, lines);
                }
            } catch (Exception var9) {
                ConsoleJS.CLIENT.error("Error while gathering tooltip for " + stack, var9);
            }
            try {
                for (ItemTooltipEventJS.StaticTooltipHandler handler : (List) staticItemTooltips.getOrDefault(stack.getItem(), List.of())) {
                    handler.tooltip(stack, advanced, lines);
                }
            } catch (Exception var8) {
                ConsoleJS.CLIENT.error("Error while gathering tooltip for " + stack, var8);
            }
        }
    }

    private void loggedIn(LocalPlayer player) {
        ClientEvents.LOGGED_IN.post(ScriptType.CLIENT, new ClientEventJS());
    }

    private void loggedOut(LocalPlayer player) {
        ClientEvents.LOGGED_OUT.post(ScriptType.CLIENT, new ClientEventJS());
        Painter.INSTANCE.clear();
    }

    private void respawn(LocalPlayer oldPlayer, LocalPlayer newPlayer) {
    }

    @Nullable
    public static Screen setScreen(Screen screen) {
        return (Screen) (screen instanceof TitleScreen && !ConsoleJS.STARTUP.errors.isEmpty() && CommonProperties.get().startupErrorGUI ? new KubeJSErrorScreen(screen, ConsoleJS.STARTUP) : screen);
    }

    private EventResult guiPreInit(Screen screen, ScreenAccess screenAccess) {
        return EventResult.pass();
    }

    private void guiPostInit(Screen screen, ScreenAccess access) {
        if (ClientProperties.get().getDisableRecipeBook() && screen instanceof RecipeUpdateListener) {
            Iterator<? extends GuiEventListener> iterator = screen.children().iterator();
            while (iterator.hasNext()) {
                GuiEventListener listener = (GuiEventListener) iterator.next();
                if (listener instanceof AbstractWidget && listener instanceof ImageButtonKJS buttonKJS && RECIPE_BUTTON_TEXTURE.equals(buttonKJS.kjs$getButtonTexture())) {
                    access.getRenderables().remove(listener);
                    access.getNarratables().remove(listener);
                    iterator.remove();
                    return;
                }
            }
        }
    }
}