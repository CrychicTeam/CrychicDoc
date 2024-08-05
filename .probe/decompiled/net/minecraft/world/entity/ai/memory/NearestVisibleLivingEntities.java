package net.minecraft.world.entity.ai.memory;

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.objects.Object2BooleanOpenHashMap;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.sensing.Sensor;

public class NearestVisibleLivingEntities {

    private static final NearestVisibleLivingEntities EMPTY = new NearestVisibleLivingEntities();

    private final List<LivingEntity> nearbyEntities;

    private final Predicate<LivingEntity> lineOfSightTest;

    private NearestVisibleLivingEntities() {
        this.nearbyEntities = List.of();
        this.lineOfSightTest = p_186122_ -> false;
    }

    public NearestVisibleLivingEntities(LivingEntity livingEntity0, List<LivingEntity> listLivingEntity1) {
        this.nearbyEntities = listLivingEntity1;
        Object2BooleanOpenHashMap<LivingEntity> $$2 = new Object2BooleanOpenHashMap(listLivingEntity1.size());
        Predicate<LivingEntity> $$3 = p_186111_ -> Sensor.isEntityTargetable(livingEntity0, p_186111_);
        this.lineOfSightTest = p_186115_ -> $$2.computeIfAbsent(p_186115_, $$3);
    }

    public static NearestVisibleLivingEntities empty() {
        return EMPTY;
    }

    public Optional<LivingEntity> findClosest(Predicate<LivingEntity> predicateLivingEntity0) {
        for (LivingEntity $$1 : this.nearbyEntities) {
            if (predicateLivingEntity0.test($$1) && this.lineOfSightTest.test($$1)) {
                return Optional.of($$1);
            }
        }
        return Optional.empty();
    }

    public Iterable<LivingEntity> findAll(Predicate<LivingEntity> predicateLivingEntity0) {
        return Iterables.filter(this.nearbyEntities, p_186127_ -> predicateLivingEntity0.test(p_186127_) && this.lineOfSightTest.test(p_186127_));
    }

    public Stream<LivingEntity> find(Predicate<LivingEntity> predicateLivingEntity0) {
        return this.nearbyEntities.stream().filter(p_186120_ -> predicateLivingEntity0.test(p_186120_) && this.lineOfSightTest.test(p_186120_));
    }

    public boolean contains(LivingEntity livingEntity0) {
        return this.nearbyEntities.contains(livingEntity0) && this.lineOfSightTest.test(livingEntity0);
    }

    public boolean contains(Predicate<LivingEntity> predicateLivingEntity0) {
        for (LivingEntity $$1 : this.nearbyEntities) {
            if (predicateLivingEntity0.test($$1) && this.lineOfSightTest.test($$1)) {
                return true;
            }
        }
        return false;
    }
}