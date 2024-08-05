package com.mna.spells.shapes;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.spells.targeting.SpellTargetHelper;
import com.mna.blocks.BlockInit;
import com.mna.blocks.ritual.ChalkRuneBlock;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.entities.sorcery.targeting.SpellSigil;
import com.mna.factions.Factions;
import com.mna.items.sorcery.ItemStaff;
import java.util.Arrays;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

public class ShapeSigil extends ShapeRaytrace {

    public ShapeSigil(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.RADIUS, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.WIDTH, 1.0F, 1.0F, 5.0F, 1.0F, 15.0F), new AttributeValuePair(Attribute.MAGNITUDE, 1.0F, 1.0F, 3.0F, 1.0F, 5.0F), new AttributeValuePair(Attribute.RANGE, 3.0F, 3.0F, 16.0F, 1.0F, 3.0F));
    }

    @Override
    public List<SpellTarget> Target(SpellSource source, Level world, IModifiedSpellPart<Shape> modificationData, ISpellDefinition recipe) {
        if (!source.isPlayerCaster()) {
            return Arrays.asList(SpellTarget.NONE);
        } else {
            float range = modificationData.getValue(Attribute.RANGE);
            HitResult targetResult = SpellTargetHelper.rayTrace(source.getCaster(), world, source.getOrigin(), source.getForward(), true, false, ClipContext.Block.OUTLINE, entity -> entity.isPickable() && entity.isAlive() && entity != source.getCaster(), source.getBoundingBox().inflate((double) range, (double) range, (double) range), (double) range);
            HitResult.Type targetType = targetResult.getType();
            if (targetType == HitResult.Type.MISS) {
                return Arrays.asList(SpellTarget.NONE);
            } else {
                if (targetType == HitResult.Type.ENTITY) {
                    EntityHitResult ehr = (EntityHitResult) targetResult;
                    if (ehr.getEntity() instanceof SpellSigil) {
                        SpellSigil sigil = (SpellSigil) ehr.getEntity();
                        if (sigil.isPermanent()) {
                            sigil.addCharge();
                            return Arrays.asList(new SpellTarget(source.getPlayer()));
                        }
                    }
                }
                boolean permanent = false;
                byte bonus = 0;
                if (source.isPlayerCaster()) {
                    LazyOptional<IPlayerProgression> progression = source.getPlayer().getCapability(PlayerProgressionProvider.PROGRESSION);
                    if (progression.isPresent() && ((IPlayerProgression) progression.resolve().get()).getAlliedFaction() == Factions.COUNCIL) {
                        ItemStack heldItem = source.getCaster().getItemInHand(source.getHand());
                        if (heldItem.getItem() instanceof ItemStaff) {
                            bonus = (byte) heldItem.getEnchantmentLevel(Enchantments.POWER_ARROWS);
                        }
                    }
                }
                Vec3 sigilPos = targetResult.getLocation();
                BlockPos pos = BlockPos.containing(sigilPos);
                if (world.m_45976_(Entity.class, new AABB(pos)).size() != 0) {
                    return Arrays.asList(SpellTarget.NONE);
                } else {
                    if (targetType == HitResult.Type.BLOCK) {
                        BlockHitResult brtr = (BlockHitResult) targetResult;
                        BlockState state = world.getBlockState(brtr.getBlockPos());
                        if (state.m_60734_() == BlockInit.CHALK_RUNE.get() && (Boolean) state.m_61143_(ChalkRuneBlock.METAL)) {
                            permanent = true;
                            world.setBlock(brtr.getBlockPos(), Blocks.AIR.defaultBlockState(), 3);
                        } else if (brtr.getDirection() != Direction.UP) {
                            sigilPos.add((double) brtr.getDirection().getStepX(), (double) brtr.getDirection().getStepY(), (double) brtr.getDirection().getStepZ());
                        }
                    }
                    if (!world.isClientSide) {
                        SpellSigil sigil = new SpellSigil(source.getPlayer(), recipe);
                        sigil.m_6034_((double) ((float) pos.m_123341_() + 0.5F), sigilPos.y() + 0.01, (double) ((float) pos.m_123343_() + 0.5F));
                        if (permanent) {
                            sigil.setPermanent();
                        }
                        sigil.setSize((byte) ((int) modificationData.getValue(Attribute.WIDTH)));
                        sigil.setCastWithBonus(bonus);
                        world.m_7967_(sigil);
                    }
                    return Arrays.asList(new SpellTarget(source.getPlayer()));
                }
            }
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
        return 250;
    }
}