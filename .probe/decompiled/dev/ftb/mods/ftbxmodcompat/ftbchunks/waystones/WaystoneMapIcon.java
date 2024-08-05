package dev.ftb.mods.ftbxmodcompat.ftbchunks.waystones;

import dev.ftb.mods.ftbchunks.client.mapicon.StaticMapIcon;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;

public class WaystoneMapIcon extends StaticMapIcon {

    public static final Icon ICON = Icon.getIcon("ftbchunks:textures/waystone.png");

    public static final Icon ICON_GLOBAL = ICON.withTint(Color4I.rgb(15431909));

    public final String name;

    public final boolean global;

    public WaystoneMapIcon(BlockPos pos, String n, boolean g) {
        super(pos);
        this.name = n;
        this.global = g;
        this.icon = this.global ? ICON_GLOBAL : ICON;
    }

    public int getPriority() {
        return 50;
    }

    public void addTooltip(TooltipList list) {
        list.string(this.name);
        if (this.global) {
            list.styledString("Global", ChatFormatting.LIGHT_PURPLE);
        }
        super.addTooltip(list);
    }
}