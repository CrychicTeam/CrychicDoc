package se.mickelus.tetra.data.provider;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;

public class TetraLootTableProvider extends LootTableProvider {

    public TetraLootTableProvider(PackOutput packOutput) {
        super(packOutput, Set.of(), ImmutableList.of());
    }

    public List<LootTableProvider.SubProviderEntry> getTables() {
        return Stream.of(MultiblockSchematicLootProvider.getLootTables()).flatMap(Collection::stream).toList();
    }
}