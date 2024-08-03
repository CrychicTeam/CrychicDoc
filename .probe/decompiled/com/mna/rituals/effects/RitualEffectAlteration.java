package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualEffect;
import com.mna.api.spells.ICanContainSpell;
import com.mna.api.spells.parts.Modifier;
import com.mna.blocks.tileentities.ChalkRuneTile;
import com.mna.entities.utility.PresentItem;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemModifierBook;
import com.mna.items.sorcery.ItemSpell;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.spells.crafting.SpellRecipe;
import java.util.Optional;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.apache.commons.lang3.mutable.MutableObject;

public class RitualEffectAlteration extends RitualEffect {

    public RitualEffectAlteration(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public boolean applyStartCheckInCreative() {
        return true;
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        MutableObject<MutableComponent> output = new MutableObject();
        context.getAllPositions().stream().forEach(pos -> {
            BlockEntity te = context.getLevel().getBlockEntity(pos.getBlockPos());
            if (te != null && te instanceof ChalkRuneTile tecr) {
                ItemStack reagentStack = tecr.m_8020_(0);
                if (reagentStack.getItem() instanceof ItemSpell && !(reagentStack.getItem() instanceof ItemSpellBook)) {
                    if (((ItemSpell) reagentStack.getItem()).isTranscribedSpell(reagentStack)) {
                        output.setValue(Component.translatable("ritual.mna.spell_modification.transcribed"));
                    } else {
                        SpellRecipe recipe = SpellRecipe.fromNBT(reagentStack.getTag());
                        if (recipe.countModifiers() >= 3) {
                            output.setValue(Component.translatable("ritual.mna.spell_modification.at-maximum"));
                        }
                    }
                } else if (reagentStack.getItem() == ItemInit.MODIFIER_BOOK.get()) {
                    Optional<Modifier> selectedModifier = ItemModifierBook.getModifier(reagentStack);
                    if (!selectedModifier.isPresent()) {
                        output.setValue(Component.translatable("ritual.mna.spell_modification.not-selected"));
                    }
                }
            }
        });
        return (Component) output.getValue();
    }

    @Override
    protected boolean applyRitualEffect(IRitualContext context) {
        ItemStack book = (ItemStack) context.getCollectedReagents().get(9);
        ItemStack spell = (ItemStack) context.getCollectedReagents().get(8);
        if (!book.isEmpty() && !spell.isEmpty() && book.getItem() == ItemInit.MODIFIER_BOOK.get() && spell.getItem() instanceof ICanContainSpell) {
            Optional<Modifier> m = ItemModifierBook.getModifier(book);
            SpellRecipe recipe = SpellRecipe.fromNBT(spell.getOrCreateTag());
            boolean didAdd = false;
            if (m.isPresent()) {
                didAdd = recipe.addModifier((Modifier) m.get());
            }
            ItemStack outputStack = spell.copy();
            recipe.writeToNBT(outputStack.getOrCreateTag());
            PresentItem epi = new PresentItem(context.getLevel(), (double) context.getCenter().m_123341_() + 0.5, (double) (context.getCenter().m_123342_() + 1), (double) context.getCenter().m_123343_() + 0.5, outputStack);
            context.getLevel().m_7967_(epi);
            return didAdd;
        } else {
            return false;
        }
    }

    @Override
    protected int getApplicationTicks(IRitualContext context) {
        return 0;
    }

    @Override
    protected boolean modifyRitualReagentsAndPatterns(ItemStack dataStack, IRitualContext context) {
        if (dataStack.getItem() != ItemInit.MODIFIER_BOOK.get()) {
            return false;
        } else {
            MutableBoolean successfullyReplaced = new MutableBoolean(false);
            ItemModifierBook.getModifier(dataStack).ifPresent(m -> ItemModifierBook.getRecipe(m, context.getLevel()).ifPresent(r -> {
                context.replaceReagents(new ResourceLocation("mna:dynamic-modifier-1"), NonNullList.of(new ResourceLocation(""), r.getRequiredItems()));
                context.replacePatterns(NonNullList.of(new ResourceLocation(""), r.getRequiredPatterns()));
                successfullyReplaced.setTrue();
            }));
            return successfullyReplaced.booleanValue();
        }
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return super.getLoopSound(context);
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        return super.spawnRitualParticles(context);
    }
}