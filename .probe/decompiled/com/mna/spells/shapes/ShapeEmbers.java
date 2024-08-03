package com.mna.spells.shapes;

import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.boss.CouncilWarden;
import com.mna.entities.sorcery.targeting.SpellEmber;
import com.mna.factions.Factions;
import com.mna.tools.SummonUtils;
import java.util.Arrays;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.mutable.MutableFloat;
import org.apache.commons.lang3.mutable.MutableInt;

public class ShapeEmbers extends Shape {

    public ShapeEmbers(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 10.0F), new AttributeValuePair(Attribute.RANGE, 16.0F, 2.0F, 32.0F, 2.0F, 1.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (!world.isClientSide) {
            int amount = (int) modificationData.getValue(Attribute.MAGNITUDE);
            MutableFloat homing = new MutableFloat(0.0F);
            if (source.isPlayerCaster()) {
                ItemStack stack = source.getCaster().getItemInHand(source.getHand());
                source.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(m -> {
                    if (m.getAlliedFaction() == Factions.COUNCIL) {
                        homing.setValue((float) stack.getEnchantmentLevel(Enchantments.POWER_ARROWS) * 0.2F);
                    }
                });
            } else if (source.getCaster() instanceof CouncilWarden) {
                homing.setValue(0.5F);
            }
            for (int i = 0; i < amount; i++) {
                SpellEmber projectile = new SpellEmber(world, source.getCaster(), recipe, 0);
                projectile.setHomingStrength(homing.getValue());
                projectile.setTargetDistance((byte) ((int) modificationData.getValue(Attribute.RANGE)));
                world.m_7967_(projectile);
                SummonUtils.clampTrackedEntities(source.getCaster(), "mna:ember_ids", SummonUtils::getMaxEmbers, (e, p) -> e instanceof SpellEmber);
                MutableInt count = new MutableInt(0);
                SummonUtils.iterateTrackedEntities(source.getCaster(), "mna:ember_ids", entityID -> {
                    Entity entity = world.getEntity(entityID);
                    if (entity != null && entity instanceof SpellEmber) {
                        int adjustedAngle = 120 * count.getAndIncrement();
                        if (count.getValue() > 3) {
                            adjustedAngle -= 60;
                        }
                        ((SpellEmber) entity).setAngle(adjustedAngle);
                    }
                });
            }
        }
        return Arrays.asList(new SpellTarget(source.getCaster()));
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public boolean isChanneled() {
        return false;
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