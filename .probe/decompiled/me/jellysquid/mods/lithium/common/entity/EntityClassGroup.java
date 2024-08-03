package me.jellysquid.mods.lithium.common.entity;

import cpw.mods.modlauncher.api.INameMappingService.Domain;
import it.unimi.dsi.fastutil.objects.Reference2ByteOpenHashMap;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;
import me.jellysquid.mods.lithium.common.reflection.ReflectionUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Shulker;
import net.minecraft.world.entity.vehicle.Minecart;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class EntityClassGroup {

    public static final EntityClassGroup MINECART_BOAT_LIKE_COLLISION;

    private final Predicate<Class<?>> classFitEvaluator;

    private volatile Reference2ByteOpenHashMap<Class<?>> class2GroupContains = new Reference2ByteOpenHashMap();

    public EntityClassGroup(Predicate<Class<?>> classFitEvaluator) {
        Objects.requireNonNull(classFitEvaluator);
        this.classFitEvaluator = classFitEvaluator;
    }

    public void clear() {
        this.class2GroupContains = new Reference2ByteOpenHashMap();
    }

    public boolean contains(Class<?> entityClass) {
        byte contains = this.class2GroupContains.getOrDefault(entityClass, (byte) 2);
        return contains != 2 ? contains == 1 : this.testAndAddClass(entityClass);
    }

    boolean testAndAddClass(Class<?> entityClass) {
        int var7;
        synchronized (this) {
            byte contains = this.class2GroupContains.getOrDefault(entityClass, (byte) 2);
            if (contains != 2) {
                return contains == 1;
            }
            Reference2ByteOpenHashMap<Class<?>> newMap = this.class2GroupContains.clone();
            var7 = this.classFitEvaluator.test(entityClass) ? 1 : 0;
            newMap.put(entityClass, (byte) var7);
            this.class2GroupContains = newMap;
        }
        return var7 == 1;
    }

    static {
        String remapped_collidesWith = ObfuscationReflectionHelper.remapName(Domain.METHOD, "m_7337_");
        MINECART_BOAT_LIKE_COLLISION = new EntityClassGroup(entityClass -> ReflectionUtil.hasMethodOverride(entityClass, Entity.class, true, remapped_collidesWith, Entity.class));
        if (!MINECART_BOAT_LIKE_COLLISION.contains(Minecart.class)) {
            throw new AssertionError();
        } else {
            if (MINECART_BOAT_LIKE_COLLISION.contains(Shulker.class)) {
                Logger.getLogger("Lithium EntityClassGroup").warning("Either Lithium EntityClassGroup is broken or something else gave Shulkers the minecart-like collision behavior.");
            }
            MINECART_BOAT_LIKE_COLLISION.clear();
        }
    }

    public static class NoDragonClassGroup extends EntityClassGroup {

        public static final EntityClassGroup.NoDragonClassGroup BOAT_SHULKER_LIKE_COLLISION;

        public NoDragonClassGroup(Predicate<Class<?>> classFitEvaluator) {
            super(classFitEvaluator);
            if (classFitEvaluator.test(EnderDragon.class)) {
                throw new IllegalArgumentException("EntityClassGroup.NoDragonClassGroup cannot be initialized: Must exclude EnderDragonEntity!");
            }
        }

        static {
            String remapped_isCollidable = ObfuscationReflectionHelper.remapName(Domain.METHOD, "m_5829_");
            BOAT_SHULKER_LIKE_COLLISION = new EntityClassGroup.NoDragonClassGroup(entityClass -> ReflectionUtil.hasMethodOverride(entityClass, Entity.class, true, remapped_isCollidable));
            if (!BOAT_SHULKER_LIKE_COLLISION.contains(Shulker.class)) {
                throw new AssertionError();
            } else {
                BOAT_SHULKER_LIKE_COLLISION.clear();
            }
        }
    }
}