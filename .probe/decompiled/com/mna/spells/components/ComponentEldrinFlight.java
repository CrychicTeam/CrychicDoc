package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.WellspringNode;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.worlddata.WorldMagicProvider;
import com.mna.effects.EffectInit;
import com.mna.entities.utility.EldrinFlight;
import com.mna.factions.Factions;
import com.mna.network.ServerMessageDispatcher;
import java.util.HashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableObject;

public class ComponentEldrinFlight extends SpellEffect {

    public ComponentEldrinFlight(ResourceLocation guiIcon) {
        super(guiIcon);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (source.isPlayerCaster() && target.isEntity() && target.getEntity() instanceof Player) {
            if (source.getPlayer() != target.getEntity() && !source.getPlayer().m_7307_(target.getEntity())) {
                return ComponentApplicationResult.FAIL;
            }
            int dist = GeneralConfigValues.WellspringDistance;
            if (!source.getPlayer().m_21023_(EffectInit.CHOOSING_WELLSPRING.get())) {
                source.getPlayer().m_7292_(new MobEffectInstance(EffectInit.CHOOSING_WELLSPRING.get(), 200));
                ServerMessageDispatcher.sendWellspringSyncMessage(context.getServerLevel(), (ServerPlayer) source.getPlayer(), (int) ((double) dist * 1.5));
                return ComponentApplicationResult.FAIL;
            }
            source.getPlayer().m_21195_(EffectInit.CHOOSING_WELLSPRING.get());
            Vec3 start = new Vec3(source.getOrigin().x(), 0.0, source.getOrigin().z());
            MutableBoolean success = new MutableBoolean(false);
            context.getServerLevel().getCapability(WorldMagicProvider.MAGIC).ifPresent(m -> {
                int halfDist = dist / 2;
                HashMap<BlockPos, WellspringNode> nodes = m.getWellspringRegistry().getNearbyNodes(source.getPlayer().m_20183_(), 50, dist + halfDist);
                if (nodes.size() == 0) {
                    source.getPlayer().m_213846_(Component.translatable("mna:components/eldrin_flight.none-nearby"));
                } else {
                    MutableObject<Vec3> selectedWellspring = new MutableObject();
                    MutableFloat closestAngle = new MutableFloat(Math.PI);
                    Vec3 playerLook = Vec3.directionFromRotation(new Vec2(0.0F, source.getPlayer().f_20885_));
                    double startLength = playerLook.length();
                    nodes.forEach((pos, node) -> {
                        Vec3 nodePos = new Vec3((double) pos.m_123341_() + 0.5, node.hasForcedYLevel() ? (double) (node.getYLevel() - 3) : -1.0, (double) pos.m_123343_() + 0.5);
                        Vec3 nodeDir = nodePos.subtract(start);
                        double dot = playerLook.dot(nodeDir);
                        double aLenTimesBLen = startLength * nodeDir.length();
                        double theta = Math.acos(dot / aLenTimesBLen);
                        if (theta < (double) closestAngle.getValue().floatValue()) {
                            closestAngle.setValue(theta);
                            selectedWellspring.setValue(nodePos);
                        }
                    });
                    target.getLivingEntity().addEffect(new MobEffectInstance(EffectInit.ELDRIN_FLIGHT.get(), 600));
                    Vec3 end = (Vec3) selectedWellspring.getValue();
                    EldrinFlight travel = new EldrinFlight(context.getServerLevel(), (Player) target.getEntity(), target.getEntity().position(), end);
                    context.getServerLevel().addFreshEntity(travel);
                    target.getLivingEntity().getPersistentData().putInt("eldrin_flight_entity_id", travel.m_19879_());
                    success.setTrue();
                }
            });
            if (success.getValue()) {
                return ComponentApplicationResult.SUCCESS;
            }
        }
        return ComponentApplicationResult.FAIL;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public IFaction getFactionRequirement() {
        return Factions.COUNCIL;
    }

    @Override
    public float initialComplexity() {
        return 55.0F;
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.SELF;
    }
}