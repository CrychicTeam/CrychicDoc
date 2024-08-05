package com.mna.api.spells.base;

import com.mna.api.ManaAndArtificeMod;
import com.mna.api.faction.IFaction;
import com.mna.api.spells.SpellCraftingContext;
import com.mna.api.spells.SpellPartTags;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.attributes.AttributeValuePair;
import com.mna.api.spells.targeting.SpellSource;
import com.mna.api.spells.targeting.SpellTarget;
import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

public interface ISpellComponent {

    ResourceLocation getRegistryName();

    ResourceLocation getGuiIcon();

    boolean isCraftable(SpellCraftingContext var1);

    boolean isUseableByPlayers();

    int requiredXPForRote();

    SpellPartTags getUseTag();

    default String getDescriptionTooltip(Attribute forAttr) {
        return this.getRegistryName().toString() + ".desc." + forAttr.toString().toLowerCase();
    }

    default IFaction getFactionRequirement() {
        return null;
    }

    default boolean canBeOnRandomStaff() {
        return !this.isSilverSpell();
    }

    int getTier(Level var1);

    SpellBlacklistResult canBeCastAt(Level var1, Vec3 var2);

    void onRegistered();

    default boolean magnitudeHealthCheck(SpellSource source, SpellTarget target, int magnitude, int healthPerMagnitude) {
        if (source.getCaster() == target.getEntity()) {
            return true;
        } else if (!target.isLivingEntity()) {
            return false;
        } else {
            int maxHP = healthPerMagnitude * magnitude;
            if (target.getLivingEntity().getMaxHealth() > (float) maxHP) {
                if (source.isPlayerCaster()) {
                    source.getPlayer().m_213846_(Component.translatable("mna:generic.too_powerful"));
                }
                return false;
            } else {
                return true;
            }
        }
    }

    default boolean isTargetFriendlyToCaster(SpellSource source, Entity target) {
        if (target.isAlive() && target instanceof LivingEntity && !source.isPlayerCaster()) {
            boolean targetIsPlayer = target instanceof Player;
            return !targetIsPlayer || !((Player) target).isCreative() && !((Player) target).isSpectator() ? ManaAndArtificeMod.getSummonHelper().isEntityFriendly(target, source.getCaster()) : true;
        } else {
            return false;
        }
    }

    default boolean isSilverSpell() {
        return false;
    }

    default boolean isBaseMna() {
        return this.getRegistryName().getNamespace().equals("mna");
    }

    default String getAddingModName() {
        Optional<? extends ModContainer> otherModContainer = ModList.get().getModContainerById(this.getRegistryName().getNamespace());
        return otherModContainer.isPresent() ? ((ModContainer) otherModContainer.get()).getModInfo().getDisplayName() : "<Unknown Mod>";
    }

    default void initializeConfigs(AttributeValuePair... attributeValuePairs) {
        if (ManaAndArtificeMod.getConfigHelper() != null) {
            if (!ManaAndArtificeMod.getConfigHelper().isPartInitialized(this)) {
                ManaAndArtificeMod.getConfigHelper().initForPart(this, attributeValuePairs);
            }
        }
    }
}