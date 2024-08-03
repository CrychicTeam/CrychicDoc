package com.mna.spells.shapes;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.sorcery.targeting.Smite;
import com.mna.factions.Factions;
import com.mna.items.sorcery.ItemStaff;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class ShapeSmite extends ShapeRaytrace {

    public ShapeSmite(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 5.0F, 0.5F, 3.0F), new AttributeValuePair(Attribute.RANGE, 8.0F, 8.0F, 32.0F, 1.0F, 2.0F), new AttributeValuePair(Attribute.HEIGHT, 1.0F, 1.0F, 3.0F, 0.5F, 3.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        List<SpellTarget> tgts = super.Target(source, world, modificationData, recipe);
        SpellTarget tgt = (SpellTarget) tgts.get(0);
        return tgt == SpellTarget.NONE ? tgts : this.targetAfterRaytrace(source, world, modificationData, recipe, tgt);
    }

    @Override
    public List<SpellTarget> TargetNPCCast(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget targetHint) {
        return this.targetAfterRaytrace(source, world, modificationData, recipe, targetHint);
    }

    private List<SpellTarget> targetAfterRaytrace(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe, SpellTarget hint) {
        if (world.isClientSide) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            Vec3 position = null;
            if (hint.isBlock()) {
                BlockPos blockPos = hint.getBlock().relative(hint.getBlockFace(null));
                position = new Vec3((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_() + 0.5, (double) blockPos.m_123343_() + 0.5);
            } else if (hint.isEntity()) {
                position = hint.getEntity().position();
            }
            int count = 0;
            for (BlockPos targetPos = BlockPos.containing(position); count < 15; targetPos = targetPos.above()) {
                if (!world.m_46859_(targetPos)) {
                    count--;
                    break;
                }
                count++;
            }
            position = position.add(0.0, (double) count, 0.0);
            float bonusDamage = 0.0F;
            if (source.isPlayerCaster()) {
                LazyOptional<IPlayerProgression> progression = source.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION);
                if (progression.isPresent() && ((IPlayerProgression) progression.resolve().get()).getAlliedFaction() == Factions.COUNCIL) {
                    ItemStack heldItem = source.getCaster().getItemInHand(source.getHand());
                    if (heldItem.getItem() instanceof ItemStaff) {
                        bonusDamage = (float) ((byte) heldItem.getEnchantmentLevel(Enchantments.POWER_ARROWS)) * 0.1F;
                    }
                }
            }
            CompoundTag recipeData = new CompoundTag();
            recipe.writeToNBT(recipeData);
            Smite projectile = new Smite(world, position, recipeData, source.getCaster());
            projectile.setBonusDamagePctFromHeight(bonusDamage);
            world.m_7967_(projectile);
            return Arrays.asList(new SpellTarget(projectile));
        }
    }

    @Override
    public boolean spawnsTargetEntity() {
        return true;
    }

    @Override
    public float initialComplexity() {
        return 20.0F;
    }

    @Override
    public int requiredXPForRote() {
        return 500;
    }
}