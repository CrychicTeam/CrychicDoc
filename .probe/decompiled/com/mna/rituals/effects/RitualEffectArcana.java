package com.mna.rituals.effects;

import com.mna.advancements.CustomAdvancementTriggers;
import com.mna.api.config.GeneralConfigValues;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.sound.SFX;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.parts.SpellEffect;
import com.mna.capabilities.playerdata.rote.PlayerRoteSpellsProvider;
import com.mna.entities.utility.PresentItem;
import com.mna.events.EventDispatcher;
import com.mna.items.ItemInit;
import com.mna.spells.crafting.SpellRecipe;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;

public class RitualEffectArcana extends RitualEffect {

    public RitualEffectArcana(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Nullable
    private SpellRecipe getRecipe(IRitualContext context) {
        ItemStack recipeItem = ItemStack.EMPTY;
        for (ItemStack stack : context.getCollectedReagents()) {
            if (SpellRecipe.isReagentContainer(stack)) {
                recipeItem = stack;
                break;
            }
        }
        if (recipeItem == null) {
            return null;
        } else {
            SpellRecipe recipe = SpellRecipe.fromNBT(recipeItem.getOrCreateTag());
            return recipe.isValid() ? recipe : null;
        }
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        SpellRecipe recipe = this.getRecipe(context);
        if (recipe != null && context.getCaster() != null) {
            SpellCraftingContext spc = new SpellCraftingContext(context.getCaster());
            return recipe.getShape().getPart().isCraftable(spc) && recipe.getModifiers().stream().allMatch(m -> m.isCraftable(spc)) && recipe.getComponents().stream().allMatch(c -> ((SpellEffect) c.getPart()).isCraftable(spc)) ? null : Component.translatable("mna:rituals/arcana.start_failed");
        } else {
            return null;
        }
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        SpellRecipe recipe = this.getRecipe(context);
        if (recipe == null) {
            return false;
        } else {
            ItemStack spell = new ItemStack(ItemInit.SPELL.get());
            recipe.setMysterious(false);
            recipe.writeToNBT(spell.getOrCreateTag());
            context.getLevel().playSound(null, (double) context.getCenter().m_123341_(), (double) context.getCenter().m_123342_(), (double) context.getCenter().m_123343_(), SFX.Event.Player.SPELL_CREATED, SoundSource.PLAYERS, 1.0F, 1.0F);
            if (context.getCaster() instanceof ServerPlayer) {
                CustomAdvancementTriggers.CRAFT_SPELL.trigger((ServerPlayer) context.getCaster(), recipe);
            }
            PresentItem item = new PresentItem(context.getLevel(), (double) ((float) context.getCenter().above().m_123341_() + 0.5F), (double) context.getCenter().above().m_123342_(), (double) ((float) context.getCenter().above().m_123343_() + 0.5F), spell);
            context.getLevel().m_7967_(item);
            if (recipe.isValid()) {
                EventDispatcher.DispatchSpellCrafted(recipe, context.getCaster());
            }
            if (GeneralConfigValues.LiteMode) {
                context.getCaster().getCapability(PlayerRoteSpellsProvider.ROTE).ifPresent(r -> {
                    if (recipe.getShape() != null) {
                        r.addRoteXP(null, recipe.getShape().getPart(), (float) recipe.getShape().getPart().requiredXPForRote());
                    }
                    recipe.iterateComponents(comp -> {
                        if (comp != null) {
                            r.addRoteXP(null, comp.getPart(), (float) ((SpellEffect) comp.getPart()).requiredXPForRote());
                        }
                    });
                    recipe.getModifiers().forEach(m -> r.addRoteXP(null, m, (float) m.requiredXPForRote()));
                });
            }
            return true;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }

    @Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack dataStack, IRitualContext context) {
        if (!SpellRecipe.isReagentContainer(dataStack)) {
            return false;
        } else {
            context.replaceReagents(new ResourceLocation("mna:dynamic-shape"), SpellRecipe.getShapeReagents(dataStack));
            context.replaceReagents(new ResourceLocation("mna:dynamic-component"), SpellRecipe.getComponentReagents(dataStack));
            context.replaceReagents(new ResourceLocation("mna:dynamic-modifier-1"), SpellRecipe.getModifierReagents(dataStack, 0));
            context.replaceReagents(new ResourceLocation("mna:dynamic-modifier-2"), SpellRecipe.getModifierReagents(dataStack, 1));
            context.replaceReagents(new ResourceLocation("mna:dynamic-modifier-3"), SpellRecipe.getModifierReagents(dataStack, 2));
            context.replacePatterns(SpellRecipe.getPatterns(dataStack));
            return true;
        }
    }
}