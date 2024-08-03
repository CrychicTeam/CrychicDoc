package com.mna.spells.components;

import com.mna.api.affinity.Affinity;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.base.IModifiedSpellPart;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.entities.constructs.animated.Construct;
import com.mna.items.sorcery.ItemEntityCrystal;
import com.mna.spells.SpellCaster;
import com.mna.tools.InventoryUtilities;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ComponentCrystallize extends SpellEffect {

    public ComponentCrystallize(ResourceLocation guiIcon) {
        super(guiIcon, new AttributeValuePair(Attribute.LESSER_MAGNITUDE, 1.0F, 1.0F, 4.0F, 1.0F, 10.0F));
        this.addReagent(new ItemStack(Items.DIAMOND), false, true, false, new IFaction[0]);
    }

    @Override
    public int requiredXPForRote() {
        return 200;
    }

    @Override
    public ComponentApplicationResult ApplyEffect(SpellSource source, SpellTarget target, IModifiedSpellPart<SpellEffect> modificationData, SpellContext context) {
        if (!source.isPlayerCaster()) {
            return ComponentApplicationResult.FAIL;
        } else if (target.isLivingEntity() && !(target.getLivingEntity() instanceof Player) && target.getLivingEntity().canChangeDimensions() && target.getLivingEntity().isAlive()) {
            boolean isConstructAndIsMine = target.getLivingEntity() instanceof Construct && ((Construct) target.getLivingEntity()).getOwner() == source.getPlayer();
            int magnitude = (int) modificationData.getValue(Attribute.LESSER_MAGNITUDE);
            if (!isConstructAndIsMine && !this.magnitudeHealthCheck(source, target, magnitude, 20)) {
                return ComponentApplicationResult.FAIL;
            } else if (!SpellCaster.consumeReagents(source.getPlayer(), source.getHand(), this.getRequiredReagents(source.getPlayer(), source.getHand()))) {
                return ComponentApplicationResult.FAIL;
            } else {
                if (!context.getLevel().isClientSide()) {
                    Pair<Boolean, Boolean> captureRedirect = InventoryUtilities.getCaptureAndRedirect(source.getPlayer());
                    ItemStack stack = ItemEntityCrystal.storeEntity(target.getEntity());
                    target.getEntity().remove(Entity.RemovalReason.DISCARDED);
                    if ((Boolean) captureRedirect.getFirst()) {
                        List<ItemStack> drops = new ArrayList();
                        drops.add(stack);
                        InventoryUtilities.redirectCaptureOrDrop(source.getPlayer(), context.getServerLevel(), drops, (Boolean) captureRedirect.getSecond());
                    } else {
                        ItemEntity item = new ItemEntity(context.getLevel(), target.getPosition().x, target.getPosition().y, target.getPosition().z, stack);
                        context.getLevel().m_7967_(item);
                    }
                }
                return ComponentApplicationResult.SUCCESS;
            }
        } else {
            return ComponentApplicationResult.FAIL;
        }
    }

    @Override
    public boolean targetsBlocks() {
        return false;
    }

    @Override
    public Affinity getAffinity() {
        return Affinity.ARCANE;
    }

    @Override
    public float initialComplexity() {
        return 10.0F;
    }

    @Override
    public boolean targetsEntities() {
        return true;
    }

    @Override
    public SpellPartTags getUseTag() {
        return SpellPartTags.UTILITY;
    }
}