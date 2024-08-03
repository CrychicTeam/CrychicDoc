package snownee.kiwi.customization.builder;

import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableListMultimap.Builder;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.block.Block;
import snownee.kiwi.util.KHolder;
import snownee.kiwi.util.resource.OneTimeLoader;

public class BuilderRules {

    private static ImmutableListMultimap<Block, KHolder<BuilderRule>> byBlock = ImmutableListMultimap.of();

    private static ImmutableMap<ResourceLocation, KHolder<BuilderRule>> byId = ImmutableMap.of();

    public static Collection<KHolder<BuilderRule>> find(Block block) {
        return byBlock.get(block);
    }

    public static int reload(ResourceManager resourceManager, OneTimeLoader.Context context) {
        Map<ResourceLocation, BuilderRule> families = OneTimeLoader.load(resourceManager, "kiwi/builder_rule", BuilderRule.CODEC, context);
        byId = (ImmutableMap<ResourceLocation, KHolder<BuilderRule>>) families.entrySet().stream().map(e -> new KHolder<>((ResourceLocation) e.getKey(), (BuilderRule) e.getValue())).collect(ImmutableMap.toImmutableMap(KHolder::key, Function.identity()));
        Builder<Block, KHolder<BuilderRule>> byBlockBuilder = ImmutableListMultimap.builder();
        UnmodifiableIterator var4 = byId.values().iterator();
        while (var4.hasNext()) {
            KHolder<BuilderRule> holder = (KHolder<BuilderRule>) var4.next();
            holder.value().relatedBlocks().forEach(block -> byBlockBuilder.put(block, holder));
        }
        byBlock = byBlockBuilder.build();
        return byId.size();
    }

    public static BuilderRule get(ResourceLocation id) {
        KHolder<BuilderRule> holder = (KHolder<BuilderRule>) byId.get(id);
        return holder == null ? null : holder.value();
    }

    public static Collection<KHolder<BuilderRule>> all() {
        return byId.values();
    }
}