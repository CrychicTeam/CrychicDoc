package snownee.kiwi.customization.shape;

import com.google.common.base.Supplier;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.datafixers.util.Pair;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.Shapes;
import org.jetbrains.annotations.Nullable;
import snownee.kiwi.Kiwi;
import snownee.kiwi.loader.Platform;

public class ShapeStorage {

    private final ImmutableMap<ResourceLocation, ShapeGenerator> shapes;

    private final Map<Pair<ShapeGenerator, Object>, ShapeGenerator> transformed = Maps.newHashMap();

    public ShapeStorage(Map<ResourceLocation, ShapeGenerator> shapes) {
        this.shapes = ImmutableMap.copyOf(shapes);
    }

    @Nullable
    public ShapeGenerator get(ResourceLocation id) {
        return (ShapeGenerator) this.shapes.get(id);
    }

    public static ShapeStorage reload(Supplier<Map<ResourceLocation, UnbakedShape>> shapesSupplier) {
        Map<ResourceLocation, UnbakedShape> shapes = (Map<ResourceLocation, UnbakedShape>) (Platform.isDataGen() ? Maps.newHashMap() : (Map) shapesSupplier.get());
        shapes.put(new ResourceLocation("empty"), new UnbakedShape.Inlined(Shapes.empty()));
        shapes.put(new ResourceLocation("block"), new UnbakedShape.Inlined(Shapes.block()));
        BakingContext.Impl context = new BakingContext.Impl(shapes);
        LinkedHashSet<ShapeRef> refs = Sets.newLinkedHashSet();
        List<ShapeStorage.UnresolvedEntry> unresolved = (List<ShapeStorage.UnresolvedEntry>) shapes.entrySet().stream().map(entryx -> {
            UnbakedShape unbakedShape = (UnbakedShape) entryx.getValue();
            Set<ShapeRef> dependenciesx = (Set<ShapeRef>) collectDependencies(unbakedShape).filter($ -> $ != unbakedShape).filter(ShapeRef.class::isInstance).map(ShapeRef.class::cast).collect(Collectors.toSet());
            if (dependenciesx.isEmpty()) {
                context.bake((ResourceLocation) entryx.getKey(), unbakedShape);
                return null;
            } else {
                return new ShapeStorage.UnresolvedEntry((ResourceLocation) entryx.getKey(), unbakedShape, dependenciesx);
            }
        }).filter(Objects::nonNull).filter(entryx -> {
            boolean success = true;
            for (ShapeRef ref : entryx.dependencies) {
                if (!shapes.containsKey(ref.id())) {
                    Kiwi.LOGGER.error("Shape %s depends on %s, but it's not found".formatted(entryx.key, ref.id()));
                    success = false;
                }
            }
            if (success) {
                refs.addAll(entryx.dependencies);
            }
            return success;
        }).collect(Collectors.toCollection(LinkedList::new));
        while (!unresolved.isEmpty()) {
            boolean changed = false;
            refs.removeIf(ref -> ref.bindValue(context));
            Iterator<ShapeStorage.UnresolvedEntry> iterator = unresolved.iterator();
            while (iterator.hasNext()) {
                ShapeStorage.UnresolvedEntry entry = (ShapeStorage.UnresolvedEntry) iterator.next();
                Set<ShapeRef> dependencies = entry.dependencies;
                if (dependencies.stream().allMatch(ShapeRef::isResolved)) {
                    context.bake(entry.key, entry.unbakedShape);
                    iterator.remove();
                    changed = true;
                }
            }
            if (!changed) {
                Kiwi.LOGGER.error("Failed to resolve shapes: %s".formatted(unresolved.stream().map(entryx -> entryx.key.toString()).collect(Collectors.joining(", "))));
                break;
            }
        }
        return new ShapeStorage(context.byId);
    }

    private static Stream<UnbakedShape> collectDependencies(UnbakedShape shape) {
        return Stream.concat(Stream.of(shape), shape.dependencies().flatMap(ShapeStorage::collectDependencies));
    }

    public void forEach(BiConsumer<? super ResourceLocation, ? super ShapeGenerator> action) {
        this.shapes.forEach(action);
    }

    public ShapeGenerator transform(ShapeGenerator shape, Object key, UnaryOperator<ShapeGenerator> factory) {
        Pair<ShapeGenerator, Object> pair = Pair.of(shape, key);
        if (this.transformed.containsKey(pair)) {
            return (ShapeGenerator) this.transformed.get(pair);
        } else {
            ShapeGenerator result = (ShapeGenerator) factory.apply(shape);
            this.transformed.put(pair, result);
            return result;
        }
    }

    private static record UnresolvedEntry(ResourceLocation key, UnbakedShape unbakedShape, Set<ShapeRef> dependencies) {
    }
}