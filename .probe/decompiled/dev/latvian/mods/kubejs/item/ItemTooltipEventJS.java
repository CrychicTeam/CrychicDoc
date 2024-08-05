package dev.latvian.mods.kubejs.item;

import dev.latvian.mods.kubejs.bindings.TextWrapper;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.ListJS;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

@Info("Invoked when registering handlers for item tooltips.\n\n`text` can be a component or a list of components.\n")
public class ItemTooltipEventJS extends EventJS {

    private final Map<Item, List<ItemTooltipEventJS.StaticTooltipHandler>> map;

    public ItemTooltipEventJS(Map<Item, List<ItemTooltipEventJS.StaticTooltipHandler>> m) {
        this.map = m;
    }

    @Info("Adds text to all items matching the ingredient.")
    public void add(Ingredient item, Object text) {
        if (item.kjs$isWildcard()) {
            this.addToAll(text);
        } else {
            ItemTooltipEventJS.StaticTooltipHandlerFromLines l = new ItemTooltipEventJS.StaticTooltipHandlerFromLines(text);
            if (!l.lines.isEmpty()) {
                for (Item i : item.kjs$getItemTypes()) {
                    if (i != Items.AIR) {
                        ((List) this.map.computeIfAbsent(i, k -> new ArrayList())).add(l);
                    }
                }
            }
        }
    }

    @Info("Adds text to all items.")
    public void addToAll(Object text) {
        ItemTooltipEventJS.StaticTooltipHandlerFromLines l = new ItemTooltipEventJS.StaticTooltipHandlerFromLines(text);
        if (!l.lines.isEmpty()) {
            ((List) this.map.computeIfAbsent(Items.AIR, k -> new ArrayList())).add(l);
        }
    }

    @Info("Adds a dynamic tooltip handler to all items matching the ingredient.")
    public void addAdvanced(Ingredient item, ItemTooltipEventJS.StaticTooltipHandlerFromJS handler) {
        if (item.kjs$isWildcard()) {
            this.addAdvancedToAll(handler);
        } else {
            ItemTooltipEventJS.StaticTooltipHandlerFromJSWrapper l = new ItemTooltipEventJS.StaticTooltipHandlerFromJSWrapper(handler);
            for (Item i : item.kjs$getItemTypes()) {
                if (i != Items.AIR) {
                    ((List) this.map.computeIfAbsent(i, k -> new ArrayList())).add(l);
                }
            }
        }
    }

    @Info("Adds a dynamic tooltip handler to all items.")
    public void addAdvancedToAll(ItemTooltipEventJS.StaticTooltipHandlerFromJS handler) {
        ((List) this.map.computeIfAbsent(Items.AIR, k -> new ArrayList())).add(new ItemTooltipEventJS.StaticTooltipHandlerFromJSWrapper(handler));
    }

    @Info("Is shift key pressed.")
    public boolean isShift() {
        return Screen.hasShiftDown();
    }

    @Info("Is control key pressed.")
    public boolean isCtrl() {
        return Screen.hasControlDown();
    }

    @Info("Is alt key pressed.")
    public boolean isAlt() {
        return Screen.hasAltDown();
    }

    @FunctionalInterface
    public interface StaticTooltipHandler {

        void tooltip(ItemStack var1, boolean var2, List<Component> var3);
    }

    @FunctionalInterface
    public interface StaticTooltipHandlerFromJS {

        void accept(ItemStack var1, boolean var2, List<Object> var3);
    }

    public static class StaticTooltipHandlerFromJSWrapper implements ItemTooltipEventJS.StaticTooltipHandler {

        private final ItemTooltipEventJS.StaticTooltipHandlerFromJS handler;

        public StaticTooltipHandlerFromJSWrapper(ItemTooltipEventJS.StaticTooltipHandlerFromJS h) {
            this.handler = h;
        }

        @Override
        public void tooltip(ItemStack stack, boolean advanced, List<Component> components) {
            if (!stack.isEmpty()) {
                List<Object> text = new ArrayList(components);
                try {
                    this.handler.accept(stack, advanced, text);
                } catch (Exception var7) {
                    ConsoleJS.CLIENT.error("Error while gathering tooltip for " + stack, var7);
                }
                components.clear();
                for (Object o : text) {
                    components.add(TextWrapper.of(o));
                }
            }
        }
    }

    public static class StaticTooltipHandlerFromLines implements ItemTooltipEventJS.StaticTooltipHandler {

        public final List<Component> lines;

        public StaticTooltipHandlerFromLines(List<Component> l) {
            this.lines = l;
        }

        public StaticTooltipHandlerFromLines(Object o) {
            this.lines = new ArrayList();
            for (Object o1 : ListJS.orSelf(o)) {
                this.lines.add(TextWrapper.of(o1));
            }
        }

        @Override
        public void tooltip(ItemStack stack, boolean advanced, List<Component> components) {
            if (!stack.isEmpty()) {
                components.addAll(this.lines);
            }
        }
    }
}