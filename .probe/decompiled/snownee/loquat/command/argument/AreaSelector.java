package snownee.loquat.command.argument;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.core.AreaManager;
import snownee.loquat.core.area.Area;
import snownee.loquat.core.select.SelectionManager;

public record AreaSelector(int maxResults, Predicate<Area> predicate, MinMaxBounds.Doubles range, UnaryOperator<Vec3> position, @Nullable AABB aabb, BiConsumer<Vec3, List<? extends Area>> order, boolean selectedAreas, @Nullable UUID uuid) {

    public static final BiConsumer<Vec3, List<? extends Area>> ORDER_ARBITRARY = (origin, areas) -> {
    };

    public static final BiConsumer<Vec3, List<? extends Area>> ORDER_NEAREST = (origin, areas) -> areas.sort((a, b) -> Doubles.compare(a.distanceToSqr(origin), b.distanceToSqr(origin)));

    public static final BiConsumer<Vec3, List<? extends Area>> ORDER_FURTHEST = (origin, areas) -> areas.sort((a, b) -> Doubles.compare(b.distanceToSqr(origin), a.distanceToSqr(origin)));

    public static final BiConsumer<Vec3, List<? extends Area>> ORDER_RANDOM = (origin, areas) -> Collections.shuffle(areas);

    private void checkPermissions(CommandSourceStack source) throws CommandSyntaxException {
    }

    public Area findSingleArea(CommandSourceStack source) throws CommandSyntaxException {
        this.checkPermissions(source);
        List<? extends Area> list = this.findAreas(source);
        if (list.isEmpty()) {
            throw AreaArgument.NO_AREAS_FOUND.create();
        } else if (list.size() > 1) {
            throw AreaArgument.ERROR_NOT_SINGLE_AREA.create();
        } else {
            return (Area) list.get(0);
        }
    }

    public List<? extends Area> findAreas(CommandSourceStack source) throws CommandSyntaxException {
        return this.findAreasRaw(source);
    }

    private List<? extends Area> findAreasRaw(CommandSourceStack source) throws CommandSyntaxException {
        this.checkPermissions(source);
        AreaManager areaManager = AreaManager.of(source.getLevel());
        if (this.uuid != null) {
            Area area = areaManager.get(this.uuid);
            return (List<? extends Area>) (area == null ? List.of() : Lists.newArrayList(new Area[] { area }));
        } else {
            Vec3 vec3 = (Vec3) this.position.apply(source.getPosition());
            Predicate<Area> predicate = this.getPredicate(vec3);
            List<Area> list = Lists.newArrayList();
            if (this.selectedAreas) {
                ServerPlayer player = source.getPlayer();
                if (player == null) {
                    SelectionManager selectionManager = null;
                    return List.of();
                }
                SelectionManager selectionManager = SelectionManager.of(player);
                this.addAreas(list, selectionManager.getSelectedAreas(source.getLevel()), predicate);
            } else {
                this.addAreas(list, areaManager.areas().stream(), predicate);
            }
            return this.sortAndLimit(vec3, list);
        }
    }

    private void addAreas(List<Area> list, Stream<Area> areas, Predicate<Area> predicate) {
        int i = this.getResultLimit();
        if (list.size() < i) {
            if (this.aabb != null) {
                areas.filter(predicate).forEach(list::add);
            } else {
                areas.filter(predicate).forEach(list::add);
            }
        }
    }

    private int getResultLimit() {
        return this.order == ORDER_ARBITRARY ? this.maxResults : Integer.MAX_VALUE;
    }

    private Predicate<Area> getPredicate(Vec3 vec3) {
        Predicate<Area> predicate = this.predicate;
        if (this.aabb != null) {
            AABB aabb = this.aabb.move(vec3);
            predicate = predicate.and(area -> area.intersects(aabb));
        }
        if (!this.range.m_55327_()) {
            predicate = predicate.and(area -> this.range.matchesSqr(area.distanceToSqr(vec3)));
        }
        return predicate;
    }

    private <T extends Area> List<T> sortAndLimit(Vec3 vec3, List<T> areas) {
        if (areas.size() > 1) {
            this.order.accept(vec3, areas);
        }
        return areas.subList(0, Math.min(this.maxResults, areas.size()));
    }
}