package me.shedaniel.autoconfig.example;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigHolder;
import me.shedaniel.autoconfig.serializer.DummyConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import net.minecraft.world.InteractionResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class ExampleInits {

    public static void exampleCommonInit() {
        ConfigHolder<ExampleConfig> holder = AutoConfig.register(ExampleConfig.class, PartitioningSerializer.wrap(DummyConfigSerializer::new));
        holder.getConfig();
        AutoConfig.getConfigHolder(ExampleConfig.class).getConfig();
        AutoConfig.<ExampleConfig>getConfigHolder(ExampleConfig.class).registerSaveListener((manager, data) -> InteractionResult.SUCCESS);
        AutoConfig.<ExampleConfig>getConfigHolder(ExampleConfig.class).registerLoadListener((manager, newData) -> InteractionResult.SUCCESS);
    }

    @OnlyIn(Dist.CLIENT)
    public static void exampleClientInit() {
        AutoConfig.getGuiRegistry(ExampleConfig.class);
    }
}