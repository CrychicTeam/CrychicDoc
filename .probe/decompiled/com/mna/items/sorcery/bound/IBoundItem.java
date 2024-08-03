package com.mna.items.sorcery.bound;

import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.items.IShowHud;
import com.mna.api.spells.ComponentApplicationResult;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.api.spells.targeting.SpellContext;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import com.mna.api.timing.DelayedEventQueue;
import com.mna.api.timing.TimedDelayedSpellEffect;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.capabilities.playerdata.progression.PlayerProgressionProvider;
import com.mna.spells.SpellCaster;
import com.mna.spells.crafting.SpellRecipe;
import java.util.ArrayList;
import java.util.UUID;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.mutable.MutableBoolean;
import software.bernie.geckolib.animatable.GeoItem;

public interface IBoundItem extends GeoItem, IShowHud {

    UUID BASE_RANGE_UUID = UUID.fromString("84468043-330D-428A-A8A3-688DB03E029F");

    float getPassiveManaDrain(Entity var1);

    default ItemStack createFromSpell(Item outputItem, ItemStack originalItem, ISpellDefinition spell) {
        CompoundTag nbt = new CompoundTag();
        originalItem.save(nbt);
        ItemStack bound = new ItemStack(outputItem);
        bound.getOrCreateTag().put("original_item", nbt);
        bound.setHoverName(originalItem.getHoverName());
        spell.writeToNBT(bound.getOrCreateTag());
        return bound;
    }

    default ItemStack restoreItem(ItemStack stack) {
        return ItemStack.of(stack.getOrCreateTagElement("original_item"));
    }

    default SpellRecipe getRecipe(ItemStack stack) {
        return SpellRecipe.fromNBT(stack.getOrCreateTag());
    }

    default void handleInventoryTick(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof Player player) {
            player.getCapability(PlayerMagicProvider.MAGIC).ifPresent(m -> {
                if (!m.getCastingResource().hasEnoughAbsolute(player, this.getPassiveManaDrain(entityIn))) {
                    int restoreSlot = itemSlot;
                    if (itemSlot == 0 && player.getInventory().offhand.get(0).hashCode() == stack.hashCode()) {
                        restoreSlot = 40;
                    }
                    player.getInventory().setItem(restoreSlot, this.restoreItem(stack));
                } else {
                    m.getCastingResource().consume(player, this.getPassiveManaDrain(entityIn));
                }
            });
        }
    }

    default boolean canCastSpell(ISpellDefinition recipe, LivingEntity attacker, InteractionHand hand) {
        if (!(attacker instanceof Player caster)) {
            return false;
        } else {
            MutableBoolean canCast = new MutableBoolean(true);
            caster.getCapability(PlayerProgressionProvider.PROGRESSION).ifPresent(p -> {
                if (!caster.isCreative()) {
                    if (recipe.getComplexity() > (float) p.getTierMaxComplexity() || recipe.getShape().getPart().getTier(attacker.m_9236_()) > p.getTier()) {
                        canCast.setFalse();
                    }
                    recipe.iterateComponents(c -> {
                        if (((SpellEffect) c.getPart()).getTier(attacker.m_9236_()) > p.getTier()) {
                            canCast.setFalse();
                        }
                    });
                }
            });
            return canCast.booleanValue();
        }
    }

    default boolean affectTarget(ISpellDefinition recipe, LivingEntity attacker, LivingEntity target, InteractionHand hand) {
        MutableBoolean applied = new MutableBoolean(false);
        if (!attacker.m_9236_().isClientSide() && recipe.isValid()) {
            if (!this.canCastSpell(recipe, attacker, hand)) {
                return false;
            }
            SpellContext context = new SpellContext(attacker.m_9236_(), recipe);
            SpellSource source = new SpellSource(attacker, hand);
            ArrayList<SpellEffect> appledEffects = new ArrayList();
            recipe.iterateComponents(c -> {
                int delay = (int) (c.getValue(Attribute.DELAY) * 20.0F);
                boolean appliedComponent = false;
                if (delay > 0) {
                    DelayedEventQueue.pushEvent(attacker.m_9236_(), new TimedDelayedSpellEffect(((SpellEffect) c.getPart()).getRegistryName().toString(), delay, source, new SpellTarget(target), c, context));
                    applied.setTrue();
                    appliedComponent = true;
                } else if (((SpellEffect) c.getPart()).ApplyEffect(source, new SpellTarget(target), c, context) == ComponentApplicationResult.SUCCESS) {
                    applied.setTrue();
                    appledEffects.add((SpellEffect) c.getPart());
                    appliedComponent = true;
                }
                if (appliedComponent && attacker instanceof Player) {
                    SpellCaster.addComponentRoteProgress((Player) attacker, (SpellEffect) c.getPart());
                }
            });
            if (applied.isTrue() && !attacker.m_9236_().isClientSide()) {
                SpellCaster.spawnClientFX(attacker.m_9236_(), attacker.m_20182_(), Vec3.ZERO, source, appledEffects);
            }
            if (attacker instanceof Player && applied.isTrue()) {
                IPlayerProgression progression = (IPlayerProgression) ((Player) attacker).getCapability(PlayerProgressionProvider.PROGRESSION).orElse(null);
                if (progression != null && !recipe.canFactionCraft(progression) && recipe instanceof SpellRecipe) {
                    ((SpellRecipe) recipe).usedByPlayer((Player) attacker);
                }
            }
        }
        return applied.booleanValue();
    }
}