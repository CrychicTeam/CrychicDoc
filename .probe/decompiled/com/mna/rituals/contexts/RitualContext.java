package com.mna.rituals.contexts;

import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IRitualRecipe;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.entities.rituals.Ritual;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RitualContext implements IRitualContext {

    private Player caster;

    private Ritual ritual;

    public RitualContext(Player caster, Ritual ritual) {
        this.caster = caster;
        this.ritual = ritual;
    }

    @Override
    public Player getCaster() {
        return this.caster;
    }

    @Override
    public IRitualRecipe getRecipe() {
        return this.ritual.getCurrentRitual();
    }

    @Override
    public NonNullList<RitualBlockPos> getAllPositions() {
        return this.ritual.getRitualData((byte) 0);
    }

    @Override
    public NonNullList<RitualBlockPos> getIndexedPositions() {
        return this.ritual.getRitualData((byte) 1);
    }

    @Override
    public BlockPos getCenter() {
        return this.ritual.m_20183_();
    }

    @Override
    public List<ItemStack> getCollectedReagents() {
        return this.ritual.getCollectedReagents();
    }

    @Override
    public List<ItemStack> getCollectedReagents(Predicate<ItemStack> filter) {
        return (List<ItemStack>) this.ritual.getCollectedReagents().stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public List<ResourceLocation> getCollectedPatterns() {
        return this.ritual.getCollectedPatterns();
    }

    @Override
    public List<ResourceLocation> getCollectedPatterns(Predicate<ResourceLocation> filter) {
        return (List<ResourceLocation>) this.ritual.getCollectedPatterns().stream().filter(filter).collect(Collectors.toList());
    }

    @Override
    public Level getLevel() {
        return this.ritual.m_9236_();
    }

    @Override
    public void replaceReagents(ResourceLocation key, NonNullList<ResourceLocation> replacements) {
        ManaAndArtifice.LOGGER.warn("RitualContext replaceReagents called outside replace reagents stage.");
    }

    @Override
    public void replaceReagents(ResourceLocation key, ResourceLocation replacement) {
        ManaAndArtifice.LOGGER.warn("RitualContext replaceReagents called outside replace reagents stage.");
    }

    @Override
    public void replacePatterns(NonNullList<ResourceLocation> replacements) {
        ManaAndArtifice.LOGGER.warn("RitualContext replacePatterns called outside replace reagents stage.");
    }

    @Override
    public void appendPatterns(NonNullList<ResourceLocation> append) {
        ManaAndArtifice.LOGGER.warn("RitualContext replacePatterns called outside replace reagents stage.");
    }
}