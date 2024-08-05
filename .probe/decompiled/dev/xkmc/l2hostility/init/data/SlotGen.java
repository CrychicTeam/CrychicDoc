package dev.xkmc.l2hostility.init.data;

import dev.xkmc.l2hostility.compat.data.CataclysmData;
import dev.xkmc.l2library.compat.curios.CurioEntityBuilder;
import dev.xkmc.l2library.compat.curios.CurioSlotBuilder;
import dev.xkmc.l2library.compat.curios.SlotCondition;
import dev.xkmc.l2library.serial.config.RecordDataProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;

public class SlotGen extends RecordDataProvider {

    public SlotGen(DataGenerator generator) {
        super(generator, "Curios Generator");
    }

    @Override
    public void add(BiConsumer<String, Record> map) {
        map.accept("l2hostility/curios/slots/hostility_curse", new CurioSlotBuilder(-1300, new ResourceLocation("l2hostility", "slot/empty_hostility_slot").toString()));
        map.accept("l2hostility/curios/entities/l2hostility_entity", new CurioEntityBuilder(new ArrayList(List.of(new ResourceLocation("player"))), new ArrayList(List.of("head", "charm", "ring", "hands", "hostility_curse")), SlotCondition.of()));
        if (ModList.get().isLoaded("cataclysm")) {
            CataclysmData.genSlot(map);
        }
    }
}