package dev.ftb.mods.ftblibrary.sidebar;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.architectury.platform.Platform;
import dev.ftb.mods.ftblibrary.FTBLibraryClient;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.misc.LoadingScreen;
import dev.ftb.mods.ftblibrary.util.ChainedBooleanSupplier;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class SidebarButton implements Comparable<SidebarButton> {

    private static final BooleanSupplier NEI_NOT_LOADED = () -> !Platform.isModLoaded("notenoughitems");

    private final ResourceLocation id;

    private final JsonObject json;

    private final SidebarButtonGroup group;

    private Icon icon = Icon.empty();

    private int x = 0;

    private boolean defaultConfig = true;

    private boolean configValue = true;

    private final List<String> clickEvents = new ArrayList();

    private final List<String> shiftClickEvents = new ArrayList();

    private final boolean loadingScreen;

    private ChainedBooleanSupplier visible = ChainedBooleanSupplier.TRUE;

    private Supplier<String> customTextHandler = null;

    private Consumer<List<String>> tooltipHandler = null;

    public SidebarButton(ResourceLocation id, SidebarButtonGroup group, JsonObject json) {
        this.group = group;
        this.id = id;
        this.json = json;
        if (json.has("icon")) {
            this.icon = Icon.getIcon(json.get("icon"));
        }
        if (this.icon.isEmpty()) {
            this.icon = Icons.ACCEPT_GRAY;
        }
        if (json.has("click")) {
            JsonElement j = json.get("click");
            for (JsonElement e : j.isJsonArray() ? j.getAsJsonArray() : Collections.singleton(j)) {
                if (e.isJsonPrimitive()) {
                    this.clickEvents.add(e.getAsString());
                }
            }
        }
        if (json.has("shift_click")) {
            JsonElement j = json.get("shift_click");
            for (JsonElement ex : j.isJsonArray() ? j.getAsJsonArray() : Collections.singleton(j)) {
                if (ex.isJsonPrimitive()) {
                    this.shiftClickEvents.add(ex.getAsString());
                }
            }
        }
        if (json.has("config")) {
            this.defaultConfig = this.configValue = json.get("config").getAsBoolean();
        }
        if (json.has("x")) {
            this.x = json.get("x").getAsInt();
        }
        if (json.has("requires_op") && json.get("requires_op").getAsBoolean()) {
            this.addVisibilityCondition(ClientUtils.IS_CLIENT_OP);
        }
        if (json.has("hide_with_nei") && json.get("hide_with_nei").getAsBoolean()) {
            this.addVisibilityCondition(NEI_NOT_LOADED);
        }
        if (json.has("required_mods")) {
            LinkedHashSet<String> requiredServerMods = new LinkedHashSet();
            for (JsonElement exx : json.get("required_mods").getAsJsonArray()) {
                requiredServerMods.add(exx.getAsString());
            }
            this.addVisibilityCondition(() -> {
                for (String s : requiredServerMods) {
                    if (!Platform.isModLoaded(s)) {
                        return false;
                    }
                }
                return true;
            });
        }
        this.loadingScreen = json.has("loading_screen") && json.get("loading_screen").getAsBoolean();
    }

    public static SidebarButton copyWithoutGroup(SidebarButton toCopy) {
        return new SidebarButton(toCopy.id, null, toCopy.json);
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public SidebarButtonGroup getGroup() {
        return this.group;
    }

    public JsonObject getJson() {
        return this.json;
    }

    public void addVisibilityCondition(BooleanSupplier supplier) {
        this.visible = this.visible.and(supplier);
    }

    public String getLangKey() {
        return Util.makeDescriptionId("sidebar_button", this.id);
    }

    public String getTooltipLangKey() {
        return this.getLangKey() + ".tooltip";
    }

    public String toString() {
        return this.id.toString();
    }

    public final int hashCode() {
        return this.id.hashCode();
    }

    public final boolean equals(Object o) {
        return o == this || o instanceof SidebarButton && this.id.equals(((SidebarButton) o).id);
    }

    public Icon getIcon() {
        return this.icon;
    }

    public int getX() {
        return this.x;
    }

    public boolean getDefaultConfig() {
        return this.defaultConfig;
    }

    public void onClicked(boolean shift) {
        if (this.loadingScreen) {
            new LoadingScreen(Component.translatable(this.getLangKey())).openGui();
        }
        for (String event : shift && !this.shiftClickEvents.isEmpty() ? this.shiftClickEvents : this.clickEvents) {
            GuiHelper.BLANK_GUI.handleClick(event);
        }
    }

    public boolean isActuallyVisible() {
        return this.configValue && FTBLibraryClient.showButtons != 0 && this.isVisible();
    }

    public boolean isVisible() {
        return this.visible.getAsBoolean();
    }

    public boolean getConfig() {
        return this.configValue;
    }

    public void setConfig(boolean value) {
        this.configValue = value;
    }

    @Nullable
    public Supplier<String> getCustomTextHandler() {
        return this.customTextHandler;
    }

    public void setCustomTextHandler(Supplier<String> text) {
        this.customTextHandler = text;
    }

    @Nullable
    public Consumer<List<String>> getTooltipHandler() {
        return this.tooltipHandler;
    }

    public void setTooltipHandler(Consumer<List<String>> text) {
        this.tooltipHandler = text;
    }

    public int compareTo(SidebarButton button) {
        return this.getX() - button.getX();
    }
}