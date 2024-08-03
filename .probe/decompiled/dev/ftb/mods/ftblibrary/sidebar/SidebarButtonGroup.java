package dev.ftb.mods.ftblibrary.sidebar;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;

public class SidebarButtonGroup implements Comparable<SidebarButtonGroup> {

    private final ResourceLocation id;

    private final int y;

    private final boolean isPinned;

    private final List<SidebarButton> buttons;

    @Deprecated(forRemoval = true)
    public SidebarButtonGroup(ResourceLocation id, int y) {
        this(id, y, true);
    }

    public SidebarButtonGroup(ResourceLocation id, int y, boolean isPinned) {
        this.id = id;
        this.y = y;
        this.isPinned = isPinned;
        this.buttons = new ArrayList();
    }

    public ResourceLocation getId() {
        return this.id;
    }

    public String getLangKey() {
        return Util.makeDescriptionId("sidebar_group", this.id);
    }

    public boolean isPinned() {
        return this.isPinned;
    }

    public int getY() {
        return this.y;
    }

    public List<SidebarButton> getButtons() {
        return this.buttons;
    }

    public int compareTo(SidebarButtonGroup group) {
        return this.getY() - group.getY();
    }
}