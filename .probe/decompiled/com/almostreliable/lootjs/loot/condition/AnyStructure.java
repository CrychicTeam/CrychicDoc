package com.almostreliable.lootjs.loot.condition;

import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class AnyStructure implements IExtendedLootCondition {

    private final List<AnyStructure.StructureLocator> structureLocators;

    private final boolean exact;

    public AnyStructure(List<AnyStructure.StructureLocator> structureLocators, boolean exact) {
        this.structureLocators = structureLocators;
        this.exact = exact;
    }

    public boolean test(LootContext context) {
        Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);
        if (origin != null) {
            BlockPos blockPos = new BlockPos((int) origin.x, (int) origin.y, (int) origin.z);
            StructureManager structureManager = context.getLevel().structureManager();
            Registry<Structure> registry = context.getLevel().m_9598_().registryOrThrow(Registries.STRUCTURE);
            for (AnyStructure.StructureLocator l : this.structureLocators) {
                Structure feature = l.getStructure(registry, context.getLevel(), blockPos);
                if (feature != null) {
                    StructureStart structureAt = this.exact ? structureManager.getStructureWithPieceAt(blockPos, feature) : structureManager.getStructureAt(blockPos, feature);
                    if (structureAt.isValid()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isExact() {
        return this.exact;
    }

    public static class Builder {

        private final List<ResourceKey<Structure>> ids = new ArrayList();

        private final List<TagKey<Structure>> tags = new ArrayList();

        public AnyStructure.Builder add(String idOrTag) {
            if (idOrTag.startsWith("#")) {
                ResourceLocation rl = new ResourceLocation(idOrTag.substring(1));
                this.tags.add(TagKey.create(Registries.STRUCTURE, rl));
            } else {
                ResourceLocation rl = new ResourceLocation(idOrTag);
                this.ids.add(ResourceKey.create(Registries.STRUCTURE, rl));
            }
            return this;
        }

        public AnyStructure build(boolean exact) {
            ArrayList<AnyStructure.StructureLocator> locators = new ArrayList();
            for (ResourceKey<Structure> id : this.ids) {
                locators.add(new AnyStructure.ById(id));
            }
            for (TagKey<Structure> tag : this.tags) {
                locators.add(new AnyStructure.ByTag(tag));
            }
            return new AnyStructure(locators, exact);
        }
    }

    public static record ById(ResourceKey<Structure> id) implements AnyStructure.StructureLocator {

        @Nullable
        @Override
        public Structure getStructure(Registry<Structure> registry, ServerLevel level, BlockPos pos) {
            return registry.get(this.id);
        }
    }

    public static record ByTag(TagKey<Structure> tag) implements AnyStructure.StructureLocator {

        @Nullable
        @Override
        public Structure getStructure(Registry<Structure> registry, ServerLevel level, BlockPos pos) {
            return (Structure) registry.getTag(this.tag).map(h -> level.getChunkSource().getGenerator().findNearestMapStructure(level, h, pos, 1, false)).map(Pair::getSecond).map(Holder::m_203334_).orElse(null);
        }
    }

    public interface StructureLocator {

        @Nullable
        Structure getStructure(Registry<Structure> var1, ServerLevel var2, BlockPos var3);
    }
}