package club.iananderson.seasonhud.client;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyBindings {

    public static final KeyMapping seasonhudOptionsKeyMapping = new KeyMapping("key.seasonhud.seasonhudOptions", KeyConflictContext.IN_GAME, InputConstants.getKey("key.keyboard.h"), "key.categories.seasonhud");
}