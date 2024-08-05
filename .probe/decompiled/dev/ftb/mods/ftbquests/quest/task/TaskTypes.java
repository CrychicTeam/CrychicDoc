package dev.ftb.mods.ftbquests.quest.task;

import dev.architectury.fluid.FluidStack;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.icon.Icon;
import dev.ftb.mods.ftblibrary.icon.Icons;
import dev.ftb.mods.ftblibrary.util.client.ClientUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluids;

public interface TaskTypes {

    Map<ResourceLocation, TaskType> TYPES = new LinkedHashMap();

    TaskType ITEM = register(new ResourceLocation("ftbquests", "item"), ItemTask::new, () -> Icon.getIcon("minecraft:item/diamond"));

    TaskType CUSTOM = register(new ResourceLocation("ftbquests", "custom"), CustomTask::new, () -> Icons.COLOR_HSB);

    TaskType XP = register(new ResourceLocation("ftbquests", "xp"), XPTask::new, () -> Icon.getIcon("minecraft:item/experience_bottle"));

    TaskType DIMENSION = register(new ResourceLocation("ftbquests", "dimension"), DimensionTask::new, () -> Icon.getIcon("minecraft:block/nether_portal"));

    TaskType STAT = register(new ResourceLocation("ftbquests", "stat"), StatTask::new, () -> Icon.getIcon("minecraft:item/iron_sword"));

    TaskType KILL = register(new ResourceLocation("ftbquests", "kill"), KillTask::new, () -> Icon.getIcon("minecraft:item/diamond_sword"));

    TaskType LOCATION = register(new ResourceLocation("ftbquests", "location"), LocationTask::new, () -> Icon.getIcon("minecraft:item/compass_00"));

    TaskType CHECKMARK = register(new ResourceLocation("ftbquests", "checkmark"), CheckmarkTask::new, () -> Icons.ACCEPT_GRAY);

    TaskType ADVANCEMENT = register(new ResourceLocation("ftbquests", "advancement"), AdvancementTask::new, () -> Icon.getIcon("minecraft:item/wheat"));

    TaskType OBSERVATION = register(new ResourceLocation("ftbquests", "observation"), ObservationTask::new, () -> Icons.ART);

    TaskType BIOME = register(new ResourceLocation("ftbquests", "biome"), BiomeTask::new, () -> Icon.getIcon("minecraft:block/oak_sapling"));

    TaskType STRUCTURE = register(new ResourceLocation("ftbquests", "structure"), StructureTask::new, () -> Icon.getIcon("minecraft:item/filled_map"));

    TaskType STAGE = register(new ResourceLocation("ftbquests", "gamestage"), StageTask::new, () -> Icons.CONTROLLER);

    TaskType FLUID = register(new ResourceLocation("ftbquests", "fluid"), FluidTask::new, () -> Icon.getIcon((String) Optional.ofNullable(ClientUtils.getStillTexture(FluidStack.create(Fluids.WATER, 1000L))).map(ResourceLocation::toString).orElse("missingno")).withTint(Color4I.rgb(8421631)).combineWith(Icon.getIcon(FluidTask.TANK_TEXTURE.toString())));

    static TaskType register(ResourceLocation name, TaskType.Provider provider, Supplier<Icon> iconSupplier) {
        return (TaskType) TYPES.computeIfAbsent(name, id -> new TaskType(id, provider, iconSupplier));
    }

    static void init() {
    }
}