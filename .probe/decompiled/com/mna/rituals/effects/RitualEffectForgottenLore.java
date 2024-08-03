package com.mna.rituals.effects;

import com.mna.api.rituals.IRitualContext;
import com.mna.api.sound.SFX;
import com.mna.items.ItemInit;
import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class RitualEffectForgottenLore extends RitualEffectCreateEssence {

    public RitualEffectForgottenLore(ResourceLocation ritualName) {
        super(ritualName);
    }

    @Override
    public Component canRitualStart(IRitualContext context) {
        return this.getBookshelfLocations(context).size() < 10 ? Component.translatable("ritual.mna.forgotten_lore.no_bookshelves") : null;
    }

    private ArrayList<BlockPos> getBookshelfLocations(IRitualContext context) {
        int searchDist = 4;
        int lb = context.getRecipe().getLowerBound();
        ArrayList<BlockPos> searched = new ArrayList();
        ArrayList<BlockPos> found = new ArrayList();
        for (int i = -lb - 1; i <= lb + 1; i++) {
            BlockPos a = context.getCenter().offset(-searchDist, 0, i);
            BlockPos b = context.getCenter().offset(searchDist, 0, i);
            BlockPos c = context.getCenter().offset(i, 0, -searchDist);
            BlockPos d = context.getCenter().offset(i, 0, searchDist);
            if (!searched.contains(a)) {
                searched.add(a);
            }
            if (!searched.contains(b)) {
                searched.add(b);
            }
            if (!searched.contains(c)) {
                searched.add(c);
            }
            if (!searched.contains(d)) {
                searched.add(d);
            }
        }
        for (BlockPos pos : searched) {
            BlockState state = context.getLevel().getBlockState(pos);
            if (state.getEnchantPowerBonus(context.getLevel(), pos) > 0.0F) {
                found.add(pos);
            }
        }
        return found;
    }

    @Override
    public SoundEvent getLoopSound(IRitualContext context) {
        return SFX.Loops.ARCANE;
    }

    @Override
    public boolean spawnRitualParticles(IRitualContext context) {
        ArrayList<BlockPos> shelves = this.getBookshelfLocations(context);
        if (shelves.size() == 0) {
            return false;
        } else {
            BlockPos end = context.getCenter().above();
            BlockPos start = (BlockPos) shelves.get((int) (Math.random() * (double) shelves.size()));
            context.getLevel().addParticle(ParticleTypes.ENCHANT, (double) end.m_123341_() + 0.5, (double) end.m_123342_() + 0.5, (double) end.m_123343_() + 0.5, (double) (-(end.m_123341_() - start.m_123341_())), -0.5, (double) (-(end.m_123343_() - start.m_123343_())));
            return true;
        }
    }

    @Override
    public ItemStack getOutputStack() {
        return new ItemStack(ItemInit.MOTE_ARCANE.get());
    }
}