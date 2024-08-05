package com.mna.rituals.contexts;

import com.mna.ManaAndArtifice;
import com.mna.api.recipes.IRitualRecipe;
import com.mna.api.rituals.IRitualContext;
import com.mna.api.rituals.RitualBlockPos;
import com.mna.entities.rituals.Ritual;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class RitualCheckContext implements IRitualContext {

    private Player caster;

    private ServerLevel world;

    private IRitualRecipe recipe;

    private Ritual ritual;

    private BlockPos center;

    public RitualCheckContext(Player caster, ServerLevel world, IRitualRecipe matched, BlockPos center, @Nullable Ritual ritual) {
        this.caster = caster;
        this.world = world;
        this.recipe = matched;
        this.ritual = ritual;
        this.center = center;
    }

    @Override
    public Player getCaster() {
        return this.caster;
    }

    @Override
    public IRitualRecipe getRecipe() {
        return this.recipe;
    }

    @Override
    public NonNullList<RitualBlockPos> getAllPositions() {
        return this.ritual == null ? NonNullList.create() : this.ritual.getRitualData((byte) 0);
    }

    @Override
    public NonNullList<RitualBlockPos> getIndexedPositions() {
        return this.ritual == null ? NonNullList.create() : this.ritual.getRitualData((byte) 0);
    }

    @Override
    public BlockPos getCenter() {
        return this.center;
    }

    @Override
    public List<ItemStack> getCollectedReagents() {
        ManaAndArtifice.LOGGER.warn("RitualContext getCollectedReagents called during check reagents stage.");
        return new ArrayList();
    }

    @Override
    public List<ItemStack> getCollectedReagents(Predicate<ItemStack> filter) {
        ManaAndArtifice.LOGGER.warn("RitualContext getCollectedReagents called during check reagents stage.");
        return new ArrayList();
    }

    @Override
    public List<ResourceLocation> getCollectedPatterns() {
        ManaAndArtifice.LOGGER.warn("RitualContext getCollectedPatterns called during check reagents stage.");
        return new ArrayList();
    }

    @Override
    public List<ResourceLocation> getCollectedPatterns(Predicate<ResourceLocation> filter) {
        ManaAndArtifice.LOGGER.warn("RitualContext getCollectedPatterns called during check reagents stage.");
        return new ArrayList();
    }

    public ServerLevel getLevel() {
        return this.world;
    }

    @Override
    public void replaceReagents(ResourceLocation key, NonNullList<ResourceLocation> replacements) {
        ManaAndArtifice.LOGGER.warn("RitualContext replaceReagents called during check reagents stage.  This will do nothing!  Use the proper modifyReagents function.");
    }

    @Override
    public void replaceReagents(ResourceLocation key, ResourceLocation replacement) {
        ManaAndArtifice.LOGGER.warn("RitualContext replaceReagents called during check reagents stage.  This will do nothing!  Use the proper modifyReagents function.");
    }

    @Override
    public void replacePatterns(NonNullList<ResourceLocation> replacements) {
        ManaAndArtifice.LOGGER.warn("RitualContext replacePatterns called during check reagents phase.  This will do nothing!  Use the proper modifyReagents function.");
    }

    @Override
    public void appendPatterns(NonNullList<ResourceLocation> append) {
        ManaAndArtifice.LOGGER.warn("RitualContext appendPatterns called during check reagents phase.  This will do nothing!  Use the proper modifyReagents function.");
    }
}