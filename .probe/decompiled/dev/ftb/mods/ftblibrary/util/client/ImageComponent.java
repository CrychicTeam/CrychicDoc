package dev.ftb.mods.ftblibrary.util.client;

import dev.ftb.mods.ftblibrary.icon.Icon;
import net.minecraft.network.chat.ComponentContents;

public class ImageComponent implements ComponentContents {

    public Icon image = Icon.empty();

    public int width = 100;

    public int height = 100;

    public int align = 1;

    public boolean fit = false;

    public String toString() {
        StringBuilder sb = new StringBuilder("{image:");
        sb.append(this.image);
        sb.append(" width:").append(this.width);
        sb.append(" height:").append(this.height);
        sb.append(" align:").append(this.align);
        if (this.fit) {
            sb.append(" fit:true");
        }
        sb.append('}');
        return sb.toString();
    }
}