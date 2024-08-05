package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.blocks.BlockInit;
import com.mna.effects.EffectInit;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ComponentGrow extends PotionEffectComponent {

    private static final UUID _ara = UUID.fromString("c10026c1-3b38-401a-8e4a-590cba8037b4");

    public ComponentGrow(ResourceLocation guiIcon) {
        super(guiIcon, EffectInit.ENLARGE, new AttributeValuePair(Attribute.DURATION, 120.0F, 30.0F, 600.0F, 30.0F, 5.0F), new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 3.0F, 1.0F, 9.0F, 1.0F, 5.0F));
        this.addPermanencyReagent(new ItemStack(BlockInit.DESERT_NOVA.get()), new IFaction[0]);
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && source.getCaster() == target.getEntity()) {
            Player player = source.getPlayer();
            if (player.getGameProfile() != null && player.getGameProfile().getId() != null) {
                UUID profileID = player.getGameProfile().getId();
                boolean isAranai = profileID.equals(_ara);
                if (isAranai) {
                    if (player.m_21023_(EffectInit.ENLARGE.get())) {
                        player.m_21195_(EffectInit.ENLARGE.get());
                        return ComponentApplicationResult.SUCCESS;
                    }
                    context.getLevel().playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SFX.Entity.Aranai.GRUMBLE, SoundSource.PLAYERS, 1.0F, 1.0F);
                    modificationData.setValue(Attribute.DURATION, -1.0F);
                }
            }
        }
        return super.ApplyEffect(source, target, modificationData, context);
    }

    @Override
    protected List<SpellReagent> getPermanencyReagents(Player caster, InteractionHand hand) {
        if (caster == null) {
            return null;
        } else {
            UUID profileID = caster.getGameProfile().getId();
            boolean isAranai = profileID.equals(_ara);
            return isAranai ? null : super.getPermanencyReagents(caster, hand);
        }
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ENDER;
    }

    @Override
    public float initialComplexity() {
        return 40.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.WATER, Affinity.WIND, Affinity.ICE);
    }
}