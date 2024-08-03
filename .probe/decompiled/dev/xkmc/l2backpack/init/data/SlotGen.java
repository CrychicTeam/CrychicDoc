package dev.xkmc.l2backpack.init.data;

import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.RecordDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;

public class SlotGen extends RecordDataProvider {

    public SlotGen(DataGenerator generator) {
        super(generator, "Curios Generator");
    }

    @Override
    public void add(BiConsumer<String, Record> map) {
        map.accept("l2backpack/curios/entities/l2backpack_entity", new CurioEntityBuilder(new ArrayList(List.of(new ResourceLocation("player"))), new ArrayList(List.of("back")), SlotCondition.of()));
    }
}