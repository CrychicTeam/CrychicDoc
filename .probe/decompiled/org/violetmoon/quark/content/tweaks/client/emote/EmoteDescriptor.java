package org.violetmoon.quark.content.tweaks.client.emote;

import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class EmoteDescriptor {

    public static final ResourceLocation TIER_1 = new ResourceLocation("quark", "textures/emote/patreon_t1.png");

    public static final ResourceLocation TIER_2 = new ResourceLocation("quark", "textures/emote/patreon_t2.png");

    public static final ResourceLocation TIER_3 = new ResourceLocation("quark", "textures/emote/patreon_t3.png");

    public static final ResourceLocation TIER_4 = new ResourceLocation("quark", "textures/emote/patreon_t4.png");

    public static final ResourceLocation TIER_GOD = new ResourceLocation("quark", "textures/emote/patreon_t99.png");

    public final Class<? extends EmoteBase> clazz;

    public final int index;

    public final String name;

    public final String regName;

    public final ResourceLocation texture;

    public final EmoteTemplate template;

    private int tier;

    public EmoteDescriptor(Class<? extends EmoteBase> clazz, String name, String regName, int index) {
        this(clazz, name, regName, index, new ResourceLocation("quark", "textures/emote/" + name + ".png"), new EmoteTemplate(name + ".emote"));
    }

    public EmoteDescriptor(Class<? extends EmoteBase> clazz, String name, String regName, int index, ResourceLocation texture, EmoteTemplate template) {
        this.clazz = clazz;
        this.index = index;
        this.name = name;
        this.regName = regName;
        this.texture = texture;
        this.template = template;
        this.tier = template.tier;
    }

    public void updateTier(EmoteTemplate template) {
        this.tier = template.tier;
    }

    public String getTranslationKey() {
        return "quark.emote." + this.name;
    }

    public String getLocalizedName() {
        return I18n.get(this.getTranslationKey());
    }

    public String getRegistryName() {
        return this.regName;
    }

    public int getTier() {
        return this.tier;
    }

    public ResourceLocation getTierTexture() {
        if (this.tier >= 99) {
            return TIER_GOD;
        } else if (this.tier >= 4) {
            return TIER_4;
        } else if (this.tier >= 3) {
            return TIER_3;
        } else if (this.tier >= 2) {
            return TIER_2;
        } else {
            return this.tier >= 1 ? TIER_1 : null;
        }
    }

    public String toString() {
        return this.name;
    }

    public EmoteBase instantiate(Player player, HumanoidModel<?> model, HumanoidModel<?> armorModel, HumanoidModel<?> armorLegModel) {
        try {
            return (EmoteBase) this.clazz.getConstructor(EmoteDescriptor.class, Player.class, HumanoidModel.class, HumanoidModel.class, HumanoidModel.class).newInstance(this, player, model, armorModel, armorLegModel);
        } catch (Exception var6) {
            throw new RuntimeException(var6);
        }
    }
}