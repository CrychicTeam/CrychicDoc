package net.zanckor.questapi.api.file.dialog.abstractdialog;

import java.io.IOException;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.zanckor.questapi.api.file.dialog.codec.Conversation;

public abstract class AbstractDialogRequirement {

    public abstract boolean handler(Player var1, Conversation var2, int var3, Entity var4) throws IOException;

    public abstract boolean handler(Player var1, Conversation var2, int var3, String var4) throws IOException;

    public abstract boolean handler(Player var1, Conversation var2, int var3, Item var4) throws IOException;
}