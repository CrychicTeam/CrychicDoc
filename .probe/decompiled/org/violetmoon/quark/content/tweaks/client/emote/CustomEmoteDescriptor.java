package org.violetmoon.quark.content.tweaks.client.emote;

import net.minecraft.resources.ResourceLocation;
import org.violetmoon.quark.content.tweaks.module.EmotesModule;

public class CustomEmoteDescriptor extends EmoteDescriptor {

    public CustomEmoteDescriptor(String name, String regName, int index) {
        super(CustomEmote.class, name, regName, index, getSprite(name), new CustomEmoteTemplate(name));
    }

    public static ResourceLocation getSprite(String name) {
        ResourceLocation customRes = new ResourceLocation("quark_custom", name);
        return EmotesModule.Client.resourcePack.hasResource(name) ? customRes : new ResourceLocation("quark", "textures/emote/custom.png");
    }

    @Override
    public String getTranslationKey() {
        return ((CustomEmoteTemplate) this.template).getName();
    }

    @Override
    public String getLocalizedName() {
        return this.getTranslationKey();
    }
}