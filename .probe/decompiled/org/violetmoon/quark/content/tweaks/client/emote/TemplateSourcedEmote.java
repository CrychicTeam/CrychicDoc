package org.violetmoon.quark.content.tweaks.client.emote;

import aurelienribon.tweenengine.Timeline;
import cpw.mods.modlauncher.Launcher;
import cpw.mods.modlauncher.api.IEnvironment.Keys;
import cpw.mods.modlauncher.api.TypesafeMap.Key;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.player.Player;
import org.violetmoon.quark.base.Quark;

public class TemplateSourcedEmote extends EmoteBase {

    private static final boolean DEOBF = ((String) Launcher.INSTANCE.environment().getProperty((Key) Keys.NAMING.get()).orElse("")).equals("mcp");

    public TemplateSourcedEmote(EmoteDescriptor desc, Player player, HumanoidModel<?> model, HumanoidModel<?> armorModel, HumanoidModel<?> armorLegsModel) {
        super(desc, player, model, armorModel, armorLegsModel);
        if (this.shouldLoadTimelineOnLaunch()) {
            Quark.LOG.debug("Loading emote " + desc.getTranslationKey());
            desc.template.readAndMakeTimeline(desc, player, model);
        }
    }

    public boolean shouldLoadTimelineOnLaunch() {
        return DEOBF;
    }

    @Override
    public Timeline getTimeline(Player player, HumanoidModel<?> model) {
        return this.desc.template.getTimeline(this.desc, player, model);
    }

    @Override
    public boolean usesBodyPart(int part) {
        return this.desc.template.usesBodyPart(part);
    }
}