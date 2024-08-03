package dev.xkmc.l2artifacts.init.data.slot;

import dev.xkmc.l2artifacts.init.data.ArtifactSlotCuriosType;
import dev.xkmc.l2library.serial.config.RecordDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class SlotGen extends RecordDataProvider {

    public SlotGen(DataGenerator generator) {
        super(generator, "Curios Generator");
    }

    @Override
    public void add(BiConsumer<String, Record> map) {
        for (ArtifactSlotCuriosType e : ArtifactSlotCuriosType.values()) {
            e.buildConfig(map::accept);
        }
        map.accept("l2artifacts/curios/entities/l2artifacts", new CurioEntityBuilder(new ArrayList(List.of(new ResourceLocation("player"))), new ArrayList(Stream.of(ArtifactSlotCuriosType.values()).map(ArtifactSlotCuriosType::getIdentifier).toList())));
    }
}