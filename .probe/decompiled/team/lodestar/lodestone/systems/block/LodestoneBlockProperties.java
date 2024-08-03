package team.lodestar.lodestone.systems.block;

import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.ToIntFunction;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;
import team.lodestar.lodestone.handlers.ThrowawayBlockDataHandler;
import team.lodestar.lodestone.systems.datagen.LodestoneDatagenBlockData;

public class LodestoneBlockProperties extends BlockBehaviour.Properties {

    public static LodestoneBlockProperties of() {
        return new LodestoneBlockProperties();
    }

    public static LodestoneBlockProperties copy(BlockBehaviour pBlockBehaviour) {
        LodestoneBlockProperties properties = of();
        properties.f_60888_ = pBlockBehaviour.properties.destroyTime;
        properties.f_60887_ = pBlockBehaviour.properties.explosionResistance;
        properties.f_60884_ = pBlockBehaviour.properties.hasCollision;
        properties.f_60890_ = pBlockBehaviour.properties.isRandomlyTicking;
        properties.f_60886_ = pBlockBehaviour.properties.lightEmission;
        properties.f_283880_ = pBlockBehaviour.properties.mapColor;
        properties.f_60885_ = pBlockBehaviour.properties.soundType;
        properties.f_60891_ = pBlockBehaviour.properties.friction;
        properties.f_60892_ = pBlockBehaviour.properties.speedFactor;
        properties.f_60903_ = pBlockBehaviour.properties.dynamicShape;
        properties.f_60895_ = pBlockBehaviour.properties.canOcclude;
        properties.f_60896_ = pBlockBehaviour.properties.isAir;
        properties.f_60889_ = pBlockBehaviour.properties.requiresCorrectToolForDrops;
        properties.f_60893_ = pBlockBehaviour.properties.jumpFactor;
        properties.f_60894_ = pBlockBehaviour.properties.drops;
        properties.f_278123_ = pBlockBehaviour.properties.ignitedByLava;
        properties.f_279618_ = pBlockBehaviour.properties.forceSolidOn;
        properties.f_278130_ = pBlockBehaviour.properties.pushReaction;
        properties.f_243850_ = pBlockBehaviour.properties.spawnParticlesOnBreak;
        properties.f_279538_ = pBlockBehaviour.properties.instrument;
        properties.f_279630_ = pBlockBehaviour.properties.replaceable;
        properties.f_60897_ = pBlockBehaviour.properties.isValidSpawn;
        properties.f_60898_ = pBlockBehaviour.properties.isRedstoneConductor;
        properties.f_60899_ = pBlockBehaviour.properties.isSuffocating;
        properties.f_60900_ = pBlockBehaviour.properties.isViewBlocking;
        properties.f_60902_ = pBlockBehaviour.properties.emissiveRendering;
        properties.f_244138_ = pBlockBehaviour.properties.requiredFeatures;
        properties.f_271289_ = pBlockBehaviour.properties.offsetFunction;
        properties.f_60901_ = pBlockBehaviour.properties.hasPostProcess;
        return properties;
    }

    public LodestoneBlockProperties addThrowawayData(Function<LodestoneThrowawayBlockData, LodestoneThrowawayBlockData> function) {
        ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.put(this, (LodestoneThrowawayBlockData) function.apply((LodestoneThrowawayBlockData) ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.getOrDefault(this, new LodestoneThrowawayBlockData())));
        return this;
    }

    public LodestoneThrowawayBlockData getThrowawayData() {
        return (LodestoneThrowawayBlockData) ThrowawayBlockDataHandler.THROWAWAY_DATA_CACHE.getOrDefault(this, LodestoneThrowawayBlockData.EMPTY);
    }

    public LodestoneBlockProperties setCutoutRenderType() {
        return this.setRenderType(() -> RenderType::m_110457_);
    }

    public LodestoneBlockProperties setRenderType(Supplier<Supplier<RenderType>> renderType) {
        if (FMLEnvironment.dist.isClient()) {
            this.addThrowawayData(d -> d.setRenderType(renderType));
        }
        return this;
    }

    public LodestoneBlockProperties addDatagenData(Function<LodestoneDatagenBlockData, LodestoneDatagenBlockData> function) {
        if (DatagenModLoader.isRunningDataGen()) {
            ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.put(this, (LodestoneDatagenBlockData) function.apply((LodestoneDatagenBlockData) ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.getOrDefault(this, new LodestoneDatagenBlockData())));
        }
        return this;
    }

    public LodestoneDatagenBlockData getDatagenData() {
        return (LodestoneDatagenBlockData) ThrowawayBlockDataHandler.DATAGEN_DATA_CACHE.getOrDefault(this, LodestoneDatagenBlockData.EMPTY);
    }

    public LodestoneBlockProperties addTag(TagKey<Block> tag) {
        this.addDatagenData(d -> d.addTag(tag));
        return this;
    }

    @SafeVarargs
    public final LodestoneBlockProperties addTags(TagKey<Block>... tags) {
        this.addDatagenData(d -> d.addTags(tags));
        return this;
    }

    public LodestoneBlockProperties hasInheritedLoot() {
        this.addDatagenData(LodestoneDatagenBlockData::hasInheritedLoot);
        return this;
    }

    public LodestoneBlockProperties needsPickaxe() {
        this.addDatagenData(LodestoneDatagenBlockData::needsPickaxe);
        return this;
    }

    public LodestoneBlockProperties needsAxe() {
        this.addDatagenData(LodestoneDatagenBlockData::needsAxe);
        return this;
    }

    public LodestoneBlockProperties needsShovel() {
        this.addDatagenData(LodestoneDatagenBlockData::needsShovel);
        return this;
    }

    public LodestoneBlockProperties needsHoe() {
        this.addDatagenData(LodestoneDatagenBlockData::needsHoe);
        return this;
    }

    public LodestoneBlockProperties needsStone() {
        this.addDatagenData(LodestoneDatagenBlockData::needsStone);
        return this;
    }

    public LodestoneBlockProperties needsIron() {
        this.addDatagenData(LodestoneDatagenBlockData::needsIron);
        return this;
    }

    public LodestoneBlockProperties needsDiamond() {
        this.addDatagenData(LodestoneDatagenBlockData::needsDiamond);
        return this;
    }

    @NotNull
    public LodestoneBlockProperties noCollission() {
        return (LodestoneBlockProperties) super.noCollission();
    }

    @NotNull
    public LodestoneBlockProperties noOcclusion() {
        return (LodestoneBlockProperties) super.noOcclusion();
    }

    @NotNull
    public LodestoneBlockProperties friction(float friction) {
        return (LodestoneBlockProperties) super.friction(friction);
    }

    @NotNull
    public LodestoneBlockProperties speedFactor(float factor) {
        return (LodestoneBlockProperties) super.speedFactor(factor);
    }

    @NotNull
    public LodestoneBlockProperties jumpFactor(float factor) {
        return (LodestoneBlockProperties) super.jumpFactor(factor);
    }

    @NotNull
    public LodestoneBlockProperties sound(@NotNull SoundType type) {
        return (LodestoneBlockProperties) super.sound(type);
    }

    @NotNull
    public LodestoneBlockProperties lightLevel(@NotNull ToIntFunction<BlockState> lightMap) {
        return (LodestoneBlockProperties) super.lightLevel(lightMap);
    }

    @NotNull
    public LodestoneBlockProperties strength(float destroyTime, float explosionResistance) {
        return (LodestoneBlockProperties) super.strength(destroyTime, explosionResistance);
    }

    @NotNull
    public LodestoneBlockProperties instabreak() {
        return (LodestoneBlockProperties) super.instabreak();
    }

    @NotNull
    public LodestoneBlockProperties strength(float strength) {
        return (LodestoneBlockProperties) super.strength(strength);
    }

    @NotNull
    public LodestoneBlockProperties randomTicks() {
        return (LodestoneBlockProperties) super.randomTicks();
    }

    @NotNull
    public LodestoneBlockProperties dynamicShape() {
        return (LodestoneBlockProperties) super.dynamicShape();
    }

    @NotNull
    public LodestoneBlockProperties noLootTable() {
        return (LodestoneBlockProperties) super.noLootTable();
    }

    @NotNull
    public LodestoneBlockProperties dropsLike(@NotNull Block block) {
        if (DatagenModLoader.isRunningDataGen()) {
            this.getDatagenData().hasInheritedLootTable = true;
        }
        return (LodestoneBlockProperties) super.dropsLike(block);
    }

    @NotNull
    public LodestoneBlockProperties lootFrom(@NotNull Supplier<? extends Block> blockIn) {
        this.hasInheritedLoot();
        return (LodestoneBlockProperties) super.lootFrom(blockIn);
    }

    @NotNull
    public LodestoneBlockProperties air() {
        return (LodestoneBlockProperties) super.air();
    }

    @NotNull
    public LodestoneBlockProperties isValidSpawn(@NotNull BlockBehaviour.StateArgumentPredicate<EntityType<?>> predicate) {
        return (LodestoneBlockProperties) super.isValidSpawn(predicate);
    }

    @NotNull
    public LodestoneBlockProperties isRedstoneConductor(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isRedstoneConductor(predicate);
    }

    @NotNull
    public LodestoneBlockProperties isSuffocating(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isSuffocating(predicate);
    }

    @NotNull
    public LodestoneBlockProperties isViewBlocking(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.isViewBlocking(predicate);
    }

    @NotNull
    public LodestoneBlockProperties hasPostProcess(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.hasPostProcess(predicate);
    }

    @NotNull
    public LodestoneBlockProperties emissiveRendering(@NotNull BlockBehaviour.StatePredicate predicate) {
        return (LodestoneBlockProperties) super.emissiveRendering(predicate);
    }

    @NotNull
    public LodestoneBlockProperties requiresCorrectToolForDrops() {
        return (LodestoneBlockProperties) super.requiresCorrectToolForDrops();
    }

    @NotNull
    public LodestoneBlockProperties mapColor(@NotNull Function<BlockState, MapColor> functionBlockStateMapColor0) {
        return (LodestoneBlockProperties) super.mapColor(functionBlockStateMapColor0);
    }

    @NotNull
    public LodestoneBlockProperties mapColor(@NotNull DyeColor dyeColor0) {
        return (LodestoneBlockProperties) super.mapColor(dyeColor0);
    }

    @NotNull
    public LodestoneBlockProperties mapColor(@NotNull MapColor mapColor0) {
        return (LodestoneBlockProperties) super.mapColor(mapColor0);
    }

    @NotNull
    public LodestoneBlockProperties destroyTime(float destroyTime) {
        return (LodestoneBlockProperties) super.destroyTime(destroyTime);
    }

    @NotNull
    public LodestoneBlockProperties explosionResistance(float explosionResistance) {
        return (LodestoneBlockProperties) super.explosionResistance(explosionResistance);
    }

    @NotNull
    public LodestoneBlockProperties ignitedByLava() {
        return (LodestoneBlockProperties) super.ignitedByLava();
    }

    @NotNull
    public LodestoneBlockProperties liquid() {
        return (LodestoneBlockProperties) super.liquid();
    }

    @NotNull
    public LodestoneBlockProperties forceSolidOn() {
        return (LodestoneBlockProperties) super.forceSolidOn();
    }

    @NotNull
    public LodestoneBlockProperties pushReaction(@NotNull PushReaction pushReaction0) {
        return (LodestoneBlockProperties) super.pushReaction(pushReaction0);
    }

    @NotNull
    public LodestoneBlockProperties offsetType(@NotNull BlockBehaviour.OffsetType pOffsetType) {
        return (LodestoneBlockProperties) super.offsetType(pOffsetType);
    }

    @NotNull
    public LodestoneBlockProperties noParticlesOnBreak() {
        return (LodestoneBlockProperties) super.noParticlesOnBreak();
    }

    @NotNull
    public LodestoneBlockProperties requiredFeatures(@NotNull FeatureFlag... pRequiredFeatures) {
        return (LodestoneBlockProperties) super.requiredFeatures(pRequiredFeatures);
    }

    @NotNull
    public LodestoneBlockProperties instrument(@NotNull NoteBlockInstrument noteBlockInstrument0) {
        return (LodestoneBlockProperties) super.instrument(noteBlockInstrument0);
    }

    @NotNull
    public LodestoneBlockProperties replaceable() {
        return (LodestoneBlockProperties) super.replaceable();
    }
}