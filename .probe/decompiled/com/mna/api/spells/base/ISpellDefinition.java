package com.mna.api.spells.base;

import com.mna.api.affinity.Affinity;
import com.mna.api.capabilities.IPlayerProgression;
import com.mna.api.faction.IFaction;
import com.mna.api.particles.MAParticleType;
import com.mna.api.spells.SpellCastingResult;
import com.mna.api.spells.SpellReagent;
import com.mna.api.spells.parts.Modifier;
import com.mna.api.spells.parts.Shape;
import com.mna.api.spells.parts.SpellEffect;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ISpellDefinition {

    ISpellDefinition EMPTY = new ISpellDefinition() {

        @Override
        public void writeToNBT(CompoundTag nbt) {
        }

        @Override
        public void setParticleColorOverride(int color) {
        }

        @Override
        public void setOverrideAffinity(Affinity affinity) {
        }

        @Override
        public void setManaCost(float manaCost) {
        }

        @Override
        public void iterateComponents(Consumer<IModifiedSpellPart<SpellEffect>> consumer) {
        }

        @Override
        public boolean isValid() {
            return false;
        }

        @Override
        public boolean isMysterious() {
            return false;
        }

        @Override
        public boolean isHarmful() {
            return false;
        }

        @Override
        public boolean isChanneled() {
            return false;
        }

        @Override
        public int getTier(Level world) {
            return 0;
        }

        @Override
        public IModifiedSpellPart<Shape> getShape() {
            return null;
        }

        @Override
        public List<SpellReagent> getReagents(Player caster, InteractionHand hand, SpellCastingResult castResult) {
            return new ArrayList();
        }

        @Override
        public int getParticleColorOverride() {
            return 0;
        }

        @Override
        public List<Modifier> getModifiers() {
            return new ArrayList();
        }

        @Override
        public Modifier getModifier(int index) {
            return null;
        }

        @Override
        public float getManaCost() {
            return 0.0F;
        }

        @Override
        public Affinity getHighestAffinity() {
            return null;
        }

        @Override
        public List<IModifiedSpellPart<SpellEffect>> getComponents() {
            return new ArrayList();
        }

        @Override
        public IModifiedSpellPart<SpellEffect> getComponent(int index) {
            return null;
        }

        @Override
        public float getComplexity() {
            return 0.0F;
        }

        @Override
        public HashMap<Affinity, Float> getAffinity() {
            return new HashMap();
        }

        @Override
        public int findComponent(SpellEffect component) {
            return 0;
        }

        @Override
        public int countModifiers() {
            return 0;
        }

        @Override
        public int countComponents() {
            return 0;
        }

        @Override
        public MAParticleType colorParticle(MAParticleType particle, Entity living) {
            return particle;
        }

        @Override
        public void clearComponents() {
        }

        @Override
        public int getCooldown(@Nullable LivingEntity caster) {
            return 0;
        }

        @Override
        public boolean canFactionCraft(IPlayerProgression progression) {
            return false;
        }

        @Override
        public void addItemTooltip(ItemStack stack, Level worldIn, List<Component> tooltip, Player player) {
        }

        @Override
        public boolean isFactionSpell(IFaction faction) {
            return false;
        }

        @Override
        public boolean containsPart(ResourceLocation partID) {
            return false;
        }

        @Override
        public boolean isChargedSpell(ItemStack stack) {
            return false;
        }
    };

    @Nullable
    IModifiedSpellPart<Shape> getShape();

    @Nullable
    IModifiedSpellPart<SpellEffect> getComponent(int var1);

    List<IModifiedSpellPart<SpellEffect>> getComponents();

    void iterateComponents(Consumer<IModifiedSpellPart<SpellEffect>> var1);

    int countComponents();

    void clearComponents();

    boolean isMysterious();

    int findComponent(SpellEffect var1);

    @Nullable
    Modifier getModifier(int var1);

    List<Modifier> getModifiers();

    int countModifiers();

    HashMap<Affinity, Float> getAffinity();

    Affinity getHighestAffinity();

    void setOverrideAffinity(Affinity var1);

    int getTier(@Nonnull Level var1);

    boolean isValid();

    float getComplexity();

    float getManaCost();

    boolean isChanneled();

    boolean isHarmful();

    int getCooldown(@Nullable LivingEntity var1);

    int getParticleColorOverride();

    void setParticleColorOverride(int var1);

    MAParticleType colorParticle(MAParticleType var1, Entity var2);

    boolean canFactionCraft(IPlayerProgression var1);

    List<SpellReagent> getReagents(@Nullable Player var1, @Nullable InteractionHand var2, @Nullable SpellCastingResult var3);

    void setManaCost(float var1);

    void writeToNBT(CompoundTag var1);

    void addItemTooltip(ItemStack var1, Level var2, List<Component> var3, Player var4);

    boolean isFactionSpell(IFaction var1);

    boolean containsPart(ResourceLocation var1);

    boolean isChargedSpell(ItemStack var1);

    default boolean isSame(ISpellDefinition other, boolean checkShape, boolean checkComponents, boolean checkModifiers) {
        if (checkShape && !this.getShape().isSame(other.getShape())) {
            return false;
        } else {
            if (checkComponents) {
                if (this.countComponents() != other.countComponents()) {
                    return false;
                }
                for (int i = 0; i < this.countComponents(); i++) {
                    IModifiedSpellPart<SpellEffect> mine = this.getComponent(i);
                    IModifiedSpellPart<SpellEffect> theirs = other.getComponent(i);
                    if (!mine.isSame(theirs)) {
                        return false;
                    }
                }
            }
            if (checkModifiers) {
                if (this.countModifiers() != other.countModifiers()) {
                    return false;
                }
                for (int ix = 0; ix < this.countModifiers(); ix++) {
                    if (this.getModifier(ix) != other.getModifier(ix)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
}