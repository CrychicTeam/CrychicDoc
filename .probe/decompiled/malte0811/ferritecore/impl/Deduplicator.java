package malte0811.ferritecore.impl;

import com.mojang.datafixers.util.Unit;
import it.unimi.dsi.fastutil.HashCommon;
import it.unimi.dsi.fastutil.objects.ObjectOpenCustomHashSet;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import malte0811.ferritecore.hash.LambdaBasedHash;
import malte0811.ferritecore.mixin.dedupbakedquad.BakedQuadAccess;
import malte0811.ferritecore.util.PredicateHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

public class Deduplicator {

    private static final Map<String, String> VARIANT_IDENTITIES = new ConcurrentHashMap();

    private static final Map<List<Pair<Predicate<BlockState>, BakedModel>>, MultiPartBakedModel> KNOWN_MULTIPART_MODELS = new ConcurrentHashMap();

    private static final Map<List<Predicate<BlockState>>, Predicate<BlockState>> OR_PREDICATE_CACHE = new ConcurrentHashMap();

    private static final Map<List<Predicate<BlockState>>, Predicate<BlockState>> AND_PREDICATE_CACHE = new ConcurrentHashMap();

    private static final ObjectOpenCustomHashSet<int[]> BAKED_QUAD_CACHE = new ObjectOpenCustomHashSet(new LambdaBasedHash(Deduplicator::betterIntArrayHash, Arrays::equals));

    public static String deduplicateVariant(String variant) {
        return (String) VARIANT_IDENTITIES.computeIfAbsent(variant, Function.identity());
    }

    public static MultiPartBakedModel makeMultipartModel(List<Pair<Predicate<BlockState>, BakedModel>> selectors) {
        return (MultiPartBakedModel) KNOWN_MULTIPART_MODELS.computeIfAbsent(selectors, MultiPartBakedModel::new);
    }

    public static Predicate<BlockState> or(List<Predicate<BlockState>> list) {
        return (Predicate<BlockState>) OR_PREDICATE_CACHE.computeIfAbsent(list, PredicateHelper::or);
    }

    public static Predicate<BlockState> and(List<Predicate<BlockState>> list) {
        return (Predicate<BlockState>) AND_PREDICATE_CACHE.computeIfAbsent(list, PredicateHelper::and);
    }

    public static void deduplicate(BakedQuad bq) {
        synchronized (BAKED_QUAD_CACHE) {
            int[] deduped = (int[]) BAKED_QUAD_CACHE.addOrGet(bq.getVertices());
            ((BakedQuadAccess) bq).setVertices(deduped);
        }
    }

    public static void registerReloadListener() {
        ((ReloadableResourceManager) Minecraft.getInstance().getResourceManager()).registerReloadListener(new SimplePreparableReloadListener<Unit>() {

            protected Unit prepare(@NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
                return Unit.INSTANCE;
            }

            protected void apply(@NotNull Unit object, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller profiler) {
                Deduplicator.VARIANT_IDENTITIES.clear();
                Deduplicator.KNOWN_MULTIPART_MODELS.clear();
                Deduplicator.OR_PREDICATE_CACHE.clear();
                Deduplicator.AND_PREDICATE_CACHE.clear();
                Deduplicator.BAKED_QUAD_CACHE.clear();
                Deduplicator.BAKED_QUAD_CACHE.trim();
            }
        });
    }

    private static int betterIntArrayHash(int[] in) {
        int result = 0;
        for (int i : in) {
            result = 31 * result + HashCommon.murmurHash3(i);
        }
        return result;
    }
}