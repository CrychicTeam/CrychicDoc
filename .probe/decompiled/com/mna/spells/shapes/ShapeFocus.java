package com.mna.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.spells.targeting.SpellTargetHelper;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.entities.sorcery.targeting.SpellFocus;
import com.mna.network.ServerMessageDispatcher;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ShapeFocus extends Shape {

    public ShapeFocus(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.DURATION, 3.0F, 1.0F, 10.0F, 0.5F, 10.0F), new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F), new AttributeValuePair(Attribute.DEPTH, 4.0F, 1.0F, 16.0F, 1.0F, 10.0F), new AttributeValuePair(Attribute.RANGE, 8.0F, 1.0F, 16.0F, 1.0F, 3.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (!source.isPlayerCaster()) {
            return new ArrayList();
        } else {
            source.getPlayer().getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                float distance = modificationData.getValue(Attribute.DEPTH);
                m.setFocusDistance(distance);
                if (!world.isClientSide) {
                    ServerMessageDispatcher.sendPlayerFocusDistanceChange((ServerPlayer) source.getPlayer(), distance, modificationData.getValue(Attribute.RANGE));
                }
                if (!world.isClientSide) {
                    HitResult targetResult = SpellTargetHelper.rayTrace(source.getCaster(), world, source.getOrigin(), source.getForward(), true, false, ClipContext.Block.OUTLINE, entity -> entity.isPickable() && entity.isAlive() && entity != source.getCaster(), source.getBoundingBox().inflate((double) distance, (double) distance, (double) distance), (double) distance);
                    Vec3 position = targetResult.getType() != HitResult.Type.MISS ? targetResult.getLocation() : source.getCaster().m_146892_().add(source.getCaster().m_20156_().scale((double) distance));
                    SpellFocus projectile = new SpellFocus(source.getCaster(), recipe, world, position);
                    world.m_7967_(projectile);
                }
            });
            return Arrays.asList(new SpellTarget(source.getCaster()));
        }
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public boolean isChanneled() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 45.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 12000;
    }
}