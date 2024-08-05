package dev.ftb.mods.ftblibrary.integration;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Lifecycle;
import dev.ftb.mods.ftblibrary.config.ui.ResourceSearchMode;
import dev.ftb.mods.ftblibrary.config.ui.SelectItemStackScreen;
import dev.ftb.mods.ftblibrary.config.ui.SelectableResource;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.ItemIcon;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButton;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonCreatedEvent;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonGroup;
import dev.ftb.mods.ftblibrary.sidebar.SidebarButtonManager;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.favorites.FavoriteEntry;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType;
import me.shedaniel.rei.api.client.favorites.FavoriteEntryType.Registry;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Tooltip;
import me.shedaniel.rei.api.client.gui.widgets.TooltipContext;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.type.VanillaEntryTypes;
import me.shedaniel.rei.api.common.util.CollectionUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

public class REIIntegration implements REIClientPlugin {

    public static final ResourceLocation ID = new ResourceLocation("ftblibrary", "sidebar_button");

    private static final ResourceSearchMode<ItemStack> REI_ITEMS = new ResourceSearchMode<ItemStack>() {

        @Override
        public Icon getIcon() {
            return ItemIcon.getItemIcon(Items.GLOW_BERRIES);
        }

        @Override
        public MutableComponent getDisplayName() {
            return Component.translatable("ftblibrary.select_item.list_mode.rei");
        }

        @Override
        public Collection<? extends SelectableResource<ItemStack>> getAllResources() {
            return CollectionUtils.filterAndMap(EntryRegistry.getInstance().getPreFilteredList(), stack -> stack.getType().equals(VanillaEntryTypes.ITEM), stack -> (SelectableResource) stack.castValue());
        }
    };

    public void registerFavorites(Registry registry) {
        registry.register(ID, REIIntegration.SidebarButtonType.INSTANCE);
        for (SidebarButtonGroup group : SidebarButtonManager.INSTANCE.getGroups()) {
            List<REIIntegration.SidebarButtonEntry> buttons = CollectionUtils.map(group.getButtons(), REIIntegration.SidebarButtonEntry::new);
            if (!buttons.isEmpty()) {
                registry.getOrCrateSection(Component.translatable(group.getLangKey())).add(group.isPinned(), (FavoriteEntry[]) buttons.toArray(new REIIntegration.SidebarButtonEntry[0]));
            }
        }
    }

    private static SidebarButton createSidebarButton(ResourceLocation id, SidebarButtonGroup g, JsonObject json) {
        SidebarButton b = new SidebarButton(id, g, json);
        SidebarButtonCreatedEvent.EVENT.invoker().accept(new SidebarButtonCreatedEvent(b));
        return b;
    }

    static {
        SelectItemStackScreen.KNOWN_MODES.prependMode(REI_ITEMS);
    }

    private static class SidebarButtonEntry extends FavoriteEntry {

        private final SidebarButton button;

        public SidebarButtonEntry(SidebarButton button) {
            this.button = button;
        }

        public boolean isInvalid() {
            for (SidebarButtonGroup group : SidebarButtonManager.INSTANCE.getGroups()) {
                for (SidebarButton groupButton : group.getButtons()) {
                    if (groupButton.getId().equals(this.button.getId()) && groupButton.isActuallyVisible()) {
                        return false;
                    }
                }
            }
            return true;
        }

        public Renderer getRenderer(boolean showcase) {
            return new Renderer() {

                public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {
                    GuiHelper.setupDrawing();
                    SidebarButtonEntry.this.button.getIcon().draw(graphics, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
                    if (SidebarButtonEntry.this.button.getCustomTextHandler() != null) {
                        String text = (String) SidebarButtonEntry.this.button.getCustomTextHandler().get();
                        Font font = Minecraft.getInstance().font;
                        if (!text.isEmpty()) {
                            int width = font.width(text);
                            Color4I.LIGHT_RED.draw(graphics, bounds.getX() + bounds.getWidth() - width, bounds.getY() - 1, width + 1, 9);
                            graphics.drawString(font, text, bounds.getX() + bounds.getWidth() - width + 1, bounds.getY(), -1);
                        }
                    }
                }

                @Nullable
                public Tooltip getTooltip(TooltipContext context) {
                    List<String> list = new ArrayList();
                    list.add(I18n.get(SidebarButtonEntry.this.button.getLangKey()));
                    if (SidebarButtonEntry.this.button.getTooltipHandler() != null) {
                        SidebarButtonEntry.this.button.getTooltipHandler().accept(list);
                    }
                    return Tooltip.create(context.getPoint(), CollectionUtils.map(list, Component::m_237113_));
                }
            };
        }

        public boolean doAction(int button) {
            this.button.onClicked(Screen.hasShiftDown());
            return true;
        }

        public long hashIgnoreAmount() {
            return (long) this.button.getId().hashCode();
        }

        public FavoriteEntry copy() {
            return new REIIntegration.SidebarButtonEntry(REIIntegration.createSidebarButton(this.button.getId(), null, this.button.getJson()));
        }

        public ResourceLocation getType() {
            return REIIntegration.ID;
        }

        public boolean isSame(FavoriteEntry other) {
            return other instanceof REIIntegration.SidebarButtonEntry entry ? entry.button.getId().equals(this.button.getId()) : false;
        }
    }

    private static enum SidebarButtonType implements FavoriteEntryType<REIIntegration.SidebarButtonEntry> {

        INSTANCE;

        public CompoundTag save(REIIntegration.SidebarButtonEntry entry, CompoundTag tag) {
            tag.putString("id", entry.button.getId().toString());
            tag.putString("json", new Gson().toJson(entry.button.getJson()));
            return tag;
        }

        public DataResult<REIIntegration.SidebarButtonEntry> read(CompoundTag object) {
            ResourceLocation id = new ResourceLocation(object.getString("id"));
            JsonObject json = (JsonObject) JsonParser.parseString(object.getString("json"));
            return DataResult.success(new REIIntegration.SidebarButtonEntry(REIIntegration.createSidebarButton(id, null, json)), Lifecycle.stable());
        }

        public DataResult<REIIntegration.SidebarButtonEntry> fromArgs(Object... args) {
            if (args.length == 0) {
                return DataResult.error(() -> "Cannot create SidebarButtonEntry from empty args!");
            } else if (args[0] instanceof ResourceLocation id) {
                return !(args[1] instanceof SidebarButton) && !(args[1] instanceof JsonObject) ? DataResult.error(() -> "Creation of SidebarButtonEntry from args expected SidebarButton or JsonObject as the second argument!") : DataResult.success(new REIIntegration.SidebarButtonEntry(args[1] instanceof SidebarButton button ? button : REIIntegration.createSidebarButton(id, null, (JsonObject) args[1])), Lifecycle.stable());
            } else {
                return DataResult.error(() -> "Creation of SidebarButtonEntry from args expected ResourceLocation as the first argument!");
            }
        }
    }
}