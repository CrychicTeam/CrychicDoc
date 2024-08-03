package net.zanckor.questapi.api.screen;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;

public abstract class AbstractDialog extends Screen {

    protected AbstractDialog(Component component) {
        super(component);
    }

    public abstract Screen modifyScreen(int var1, String var2, int var3, HashMap<Integer, List<Integer>> var4, HashMap<Integer, List<String>> var5, UUID var6, String var7, Item var8, NpcType var9);
}