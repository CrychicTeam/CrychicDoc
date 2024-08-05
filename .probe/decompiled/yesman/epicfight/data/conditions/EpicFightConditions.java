package yesman.epicfight.data.conditions;

import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import yesman.epicfight.data.conditions.entity.OffhandCategoryCondition;
import yesman.epicfight.data.conditions.itemstack.TagValueCondition;

public class EpicFightConditions {

    public static final DeferredRegister<Function<CompoundTag, Condition<?>>> CONDITIONS = DeferredRegister.create(new ResourceLocation("epicfight", "conditions"), "epicfight");

    public static final Supplier<IForgeRegistry<Function<CompoundTag, Condition<?>>>> REGISTRY = CONDITIONS.makeRegistry(RegistryBuilder::new);

    public static final RegistryObject<Function<CompoundTag, Condition<?>>> OFFHAND_CATEGORY = CONDITIONS.register(new ResourceLocation("epicfight", "offhand_category").getPath(), () -> OffhandCategoryCondition::new);

    public static final RegistryObject<Function<CompoundTag, Condition<?>>> TAG_VALUE = CONDITIONS.register(new ResourceLocation("epicfight", "tag_value").getPath(), () -> TagValueCondition::new);

    public static <T extends Condition<?>> Function<CompoundTag, T> getConditionOrThrow(ResourceLocation key) {
        if (!((IForgeRegistry) REGISTRY.get()).containsKey(key)) {
            throw new IllegalArgumentException("No condition named " + key);
        } else {
            return (Function<CompoundTag, T>) ((IForgeRegistry) REGISTRY.get()).getValue(key);
        }
    }
}