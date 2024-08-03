package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.sound.SFX;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.factions.Factions;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.phys.Vec3;

public class ComponentPhantomSound extends SpellEffect {

    public ComponentPhantomSound(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 15.0F, 1.0F, 0.0F), new AttributeValuePair(Attribute.PRECISION, 1.0F, 1.0F, 15.0F, 1.0F, 0.0F));
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (context.countAffectedBlocks(this) <= 0 && context.countAffectedEntities(this) <= 0) {
            if (target.isBlock()) {
                context.addAffectedBlock(this, target.getBlock());
            } else {
                context.addAffectedEntity(this, target.getEntity());
            }
            float volume = 0.1F * modificationData.getValue(Attribute.LESSER_MAGNITUDE);
            SoundEvent sfx = null;
            switch((int) modificationData.getValue(Attribute.PRECISION)) {
                case 1:
                    sfx = SoundEvents.CREEPER_PRIMED;
                    break;
                case 2:
                    sfx = SoundEvents.ENDERMAN_SCREAM;
                    break;
                case 3:
                    sfx = SoundEvents.ZOMBIE_AMBIENT;
                    break;
                case 4:
                    sfx = SoundEvents.VILLAGER_AMBIENT;
                    break;
                case 5:
                    sfx = SoundEvents.VILLAGER_YES;
                    break;
                case 6:
                    sfx = SoundEvents.VILLAGER_NO;
                    break;
                case 7:
                    sfx = SoundEvents.PHANTOM_AMBIENT;
                    break;
                case 8:
                    sfx = SoundEvents.BELL_BLOCK;
                    break;
                case 9:
                    sfx = SoundEvents.WITCH_CELEBRATE;
                    break;
                case 10:
                    sfx = SoundEvents.BLAZE_SHOOT;
                    break;
                case 11:
                    sfx = SoundEvents.ARROW_HIT;
                    break;
                case 12:
                    sfx = SFX.Entity.HulkingZombie.ATTACK;
                    break;
                case 13:
                    sfx = SFX.Entity.Imp.IDLE;
                    break;
                case 14:
                    sfx = SFX.Entity.Construct.HORN;
                    break;
                case 15:
                    sfx = SFX.Entity.Pixie.ATTACK;
            }
            if (sfx != null) {
                Vec3 pos = target.getPosition();
                context.getServerLevel().m_6263_(null, pos.x, pos.y, pos.z, sfx, SoundSource.PLAYERS, 1.0F + volume, 1.0F);
                return ComponentApplicationResult.SUCCESS;
            } else {
                return ComponentApplicationResult.FAIL;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.UNKNOWN;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.FEY;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 100;
    }

    @Override
    public List<Affinity> getValidTinkerAffinities() {
        return Arrays.asList(Affinity.ARCANE, Affinity.ENDER, Affinity.WATER, Affinity.WIND, Affinity.ICE, Affinity.LIGHTNING, Affinity.EARTH);
    }

    @Override
    public boolean isSilverSpell() {
        return true;
    }

    @Override
    public boolean canBeChanneled() {
        return false;
    }
}