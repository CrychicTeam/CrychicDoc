package com.mna.api.entities.construct;

import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.items.DynamicItemFilter;
import com.mna.api.spells.base.ISpellDefinition;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public interface IConstruct<T extends AbstractGolem> extends IFluidHandler, IItemHandlerModifiable {

    default AbstractGolem asEntity() {
        return (AbstractGolem) this;
    }

    IConstructConstruction getConstructData();

    IConstructDiagnostics getDiagnostics();

    default void pushDiagnosticMessage(String message, @Nullable ResourceLocation icon, boolean allowDuplicates) {
        this.getDiagnostics().pushDiagnosticMessage(message, icon, allowDuplicates);
    }

    default void pushDiagnosticMessage(Component message, @Nullable ResourceLocation icon, boolean allowDuplicates) {
        this.pushDiagnosticMessage(message.getContents().toString(), icon, allowDuplicates);
    }

    default void pushDiagnosticMessage(Component message, @Nullable ResourceLocation icon) {
        this.pushDiagnosticMessage(message, icon, false);
    }

    default void pushDiagnosticMessage(String message, @Nullable ResourceLocation icon) {
        this.pushDiagnosticMessage(message, icon, false);
    }

    int getCarrySize();

    InteractionHand[] getCarryingHands();

    InteractionHand[] getCarryingHands(Predicate<ItemStack> var1);

    InteractionHand[] getCarryingHands(DynamicItemFilter var1);

    InteractionHand[] getEmptyHands();

    ISpellDefinition[] getCastableSpells();

    Optional<InteractionHand> getHandWithCapability(ConstructCapability var1);

    Optional<LivingEntity> getDualCannonTarget();

    Player getOwner();

    void lookTowards(Vec3 var1, float var2);

    void lookTowards(Vec3 var1);

    boolean setupSpellCast(boolean var1);

    boolean startSpellCast(@Nullable SpellTarget var1);

    boolean tickSpellCast();

    void resetSpellCast();

    boolean isRangedAttacking();

    boolean canManaCannonAttack();

    boolean canFluidSpray();

    boolean canDualSweep();

    boolean expandFluidRange();

    boolean canSpellCast();

    boolean isDefeated();

    boolean isDueling();

    int getAttackDelay();

    void setWatering();

    void setMining(boolean var1);

    void setAdventuring(boolean var1);

    void setHunting(boolean var1);

    boolean isFishing();

    void setFishing(BlockPos var1);

    void setEating(InteractionHand var1);

    void setDefeated(int var1);

    void stopFishing();

    void resetActions();

    int getStoredFluidAmount();

    float getFluidPct();

    float getMana();

    float getMaxMana();

    float getManaPct();

    default List<LivingEntity> getValidAttackTargets() {
        return this.getValidAttackTargets(null);
    }

    List<LivingEntity> getValidAttackTargets(@Nullable LivingEntity var1);

    boolean validateFriendlyTarget(LivingEntity var1);

    boolean performRangedAttack(LivingEntity var1);

    void setHat(ItemStack var1);

    void setBanner(ItemStack var1);

    void setDualSweeping();

    void setHappy(int var1);

    void setAngry(int var1);

    void setUnimpressed(int var1);

    void setConfused(int var1);

    void setConcerned(int var1);

    boolean setCurrentCommand(Player var1, ConstructAITask<?> var2);

    ConstructAITask<?> getCurrentCommand();

    int getIntelligence();

    boolean hasItem(ItemStack var1, boolean var2, boolean var3);

    boolean hasItem(DynamicItemFilter var1);

    boolean hasItem(ResourceLocation var1, int var2);

    void dropAllItems();

    default Tier getBlockHarvestLevel(ConstructCapability capability) {
        ConstructMaterial material = this.getConstructData().getLowestMaterialCooldownMultiplierForCapability(capability);
        return (Tier) (material != null ? material.getEquivalentTier() : Tiers.WOOD);
    }
}