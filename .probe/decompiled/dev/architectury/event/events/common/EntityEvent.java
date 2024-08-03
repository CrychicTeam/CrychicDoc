package dev.architectury.event.events.common;

import dev.architectury.event.Event;
import dev.architectury.event.EventFactory;
import dev.architectury.event.EventResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BaseSpawner;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.Nullable;

public interface EntityEvent {

    Event<EntityEvent.LivingDeath> LIVING_DEATH = EventFactory.createEventResult();

    Event<EntityEvent.LivingHurt> LIVING_HURT = EventFactory.createEventResult();

    Event<EntityEvent.LivingCheckSpawn> LIVING_CHECK_SPAWN = EventFactory.createEventResult();

    Event<EntityEvent.Add> ADD = EventFactory.createEventResult();

    Event<EntityEvent.EnterSection> ENTER_SECTION = EventFactory.createLoop();

    Event<EntityEvent.AnimalTame> ANIMAL_TAME = EventFactory.createEventResult();

    public interface Add {

        EventResult add(Entity var1, Level var2);
    }

    public interface AnimalTame {

        EventResult tame(Animal var1, Player var2);
    }

    public interface EnterSection {

        void enterSection(Entity var1, int var2, int var3, int var4, int var5, int var6, int var7);
    }

    public interface LivingCheckSpawn {

        EventResult canSpawn(LivingEntity var1, LevelAccessor var2, double var3, double var5, double var7, MobSpawnType var9, @Nullable BaseSpawner var10);
    }

    public interface LivingDeath {

        EventResult die(LivingEntity var1, DamageSource var2);
    }

    public interface LivingHurt {

        EventResult hurt(LivingEntity var1, DamageSource var2, float var3);
    }
}