package snownee.kiwi.datagen;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import snownee.kiwi.KiwiModules;
import snownee.kiwi.ModuleInfo;

public abstract class KiwiBlockLoot extends FabricBlockLootTableProvider {

    protected final ResourceLocation moduleId;

    private final List<Block> knownBlocks;

    private final Map<Class<?>, Function<Block, LootTable.Builder>> handlers = Maps.newIdentityHashMap();

    private final Set<Block> added = Sets.newHashSet();

    private Function<Block, LootTable.Builder> defaultHandler;

    protected KiwiBlockLoot(ResourceLocation moduleId, FabricDataOutput dataOutput) {
        super(dataOutput);
        this.moduleId = moduleId;
        ModuleInfo info = KiwiModules.get(moduleId);
        Objects.requireNonNull(info);
        this.knownBlocks = info.getRegistries(BuiltInRegistries.BLOCK);
    }

    protected <T extends Block> void handle(Class<T> clazz, Function<T, LootTable.Builder> handler) {
        this.handlers.put(clazz, handler);
    }

    protected void handleDefault(Function<Block, LootTable.Builder> handler) {
        this.defaultHandler = handler;
    }

    public void generate() {
        this.addTables();
        for (Block block : this.knownBlocks) {
            if (!this.added.contains(block)) {
                this.added.add(block);
                Function<Block, LootTable.Builder> handler = (Function<Block, LootTable.Builder>) this.handlers.get(block.getClass());
                if (handler == null) {
                    handler = this.defaultHandler;
                }
                if (handler != null) {
                    LootTable.Builder builder = (LootTable.Builder) handler.apply(block);
                    if (builder != null) {
                        this.add(block, builder);
                    }
                }
            }
        }
    }

    protected abstract void addTables();

    public void add(Block block, LootTable.Builder builder) {
        super.m_247577_(block, builder);
        this.added.add(block);
    }

    public List<Block> getKnownBlocks() {
        return this.knownBlocks;
    }

    public String getName() {
        return super.m_6055_() + " - " + this.moduleId;
    }
}