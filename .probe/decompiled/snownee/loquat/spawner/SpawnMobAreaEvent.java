package snownee.loquat.spawner;

import com.google.common.base.Preconditions;
import com.google.common.math.LongMath;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import org.apache.commons.compress.utils.Lists;
import snownee.loquat.AreaEventTypes;
import snownee.loquat.LoquatConfig;
import snownee.loquat.core.AreaEvent;
import snownee.loquat.core.area.Area;
import snownee.loquat.util.LoquatUtil;
import snownee.lychee.core.Job;
import snownee.lychee.core.LycheeContext;
import snownee.lychee.core.post.Delay;

public class SpawnMobAreaEvent extends AreaEvent {

    private final ResourceLocation spawnerId;

    private final Spawner spawner;

    private final Difficulty difficulty;

    private final ResourceLocation difficultyId;

    private int lastWave;

    private final List<ActiveWave> waves = Lists.newArrayList();

    private boolean hasTimeout;

    private long timeoutInTicks;

    public SpawnMobAreaEvent(Area area, Spawner spawner, ResourceLocation spawnerId, Difficulty difficulty, ResourceLocation difficultyId) {
        super(area);
        Preconditions.checkNotNull(spawner, "Spawner %s not found", spawnerId);
        this.spawnerId = spawnerId;
        this.spawner = spawner;
        this.difficultyId = difficultyId;
        this.difficulty = difficulty;
    }

    @Override
    public void tick(ServerLevel world) {
        if (this.lastWave >= this.spawner.waves.length && this.waves.isEmpty()) {
            this.isFinished = true;
            if (LoquatConfig.debug) {
                LoquatUtil.runCommandSilently(world, "scoreboard objectives remove LoquatSpawner");
            }
        } else {
            if (this.waves.isEmpty() || this.hasTimeout && world.m_46467_() >= this.timeoutInTicks) {
                this.newWave(world);
            }
            this.waves.removeIf(wave -> wave.tick(world));
            if (LoquatConfig.debug) {
                LoquatUtil.runCommandSilently(world, "scoreboard players reset * LoquatSpawner");
                int mobs = this.waves.stream().mapToInt(ActiveWave::getRemainMobs).sum();
                LoquatUtil.runCommandSilently(world, "scoreboard players set mobs:%d LoquatSpawner 1".formatted(mobs));
                int timeout = this.hasTimeout ? (int) (this.timeoutInTicks - world.m_46467_()) / 20 : -1;
                LoquatUtil.runCommandSilently(world, "scoreboard players set timeout:%ds LoquatSpawner 2".formatted(timeout));
            }
        }
    }

    public void newWave(ServerLevel world) {
        Preconditions.checkArgument(this.lastWave < this.spawner.waves.length, "No more wave");
        LycheeContext.Builder<LycheeContext> builder = new LycheeContext.Builder<>(world);
        builder.withParameter(LootContextParams.ORIGIN, this.area.getOrigin());
        LycheeContext ctx = builder.create(LycheeCompat.LOOT_CONTEXT_PARAM_SET);
        ActiveWave activeWave = new ActiveWave(this, this.lastWave++, ctx, this.difficulty.getLevel(world));
        Spawner.Wave wave = activeWave.getWave();
        if (wave.contextual.checkConditions(activeWave, ctx, 1) > 0) {
            if (LoquatConfig.debug) {
                if (this.lastWave == 1) {
                    LoquatUtil.runCommandSilently(world, "scoreboard objectives add LoquatSpawner dummy");
                    LoquatUtil.runCommandSilently(world, "scoreboard objectives setdisplay sidebar LoquatSpawner");
                }
                LoquatUtil.runCommandSilently(world, "scoreboard objectives modify LoquatSpawner displayname \"Wave: %d/%d\"".formatted(this.lastWave, this.spawner.waves.length));
            }
            this.waves.add(activeWave);
            this.hasTimeout = wave.timeout > 0;
            if (this.hasTimeout) {
                this.timeoutInTicks = LongMath.checkedAdd(world.m_46467_(), (long) wave.timeout * 20L);
            }
            activeWave.onStart();
            activeWave.applyPostActions(ctx, 1);
            if (wave.wait > 0.0F) {
                ctx.runtime.jobs.push(new Job(new Delay(wave.wait), 1));
            }
            ctx.runtime.run(activeWave, ctx);
        }
    }

    @Override
    public AreaEvent.Type<?> getType() {
        return AreaEventTypes.SPAWN_MOBS;
    }

    public ResourceLocation getSpawnerId() {
        return this.spawnerId;
    }

    public Spawner getSpawner() {
        return this.spawner;
    }

    public static class Type extends AreaEvent.Type<SpawnMobAreaEvent> {

        public SpawnMobAreaEvent deserialize(Area area, CompoundTag data) {
            ResourceLocation spawnerId = new ResourceLocation(data.getString("Spawner"));
            ResourceLocation difficultyId = new ResourceLocation(data.getString("Difficulty"));
            SpawnMobAreaEvent event = new SpawnMobAreaEvent(area, LycheeCompat.SPAWNERS.get(spawnerId), spawnerId, LycheeCompat.DIFFICULTIES.get(difficultyId), difficultyId);
            event.lastWave = data.getInt("LastWave");
            event.hasTimeout = data.getBoolean("HasTimeout");
            if (event.hasTimeout) {
                event.timeoutInTicks = data.getLong("TimeoutInTicks");
            }
            return event;
        }

        public CompoundTag serialize(CompoundTag data, SpawnMobAreaEvent event) {
            data.putString("Spawner", event.spawnerId.toString());
            data.putString("Difficulty", event.difficultyId.toString());
            data.putInt("LastWave", event.lastWave);
            data.putBoolean("HasTimeout", event.hasTimeout);
            if (event.hasTimeout) {
                data.putLong("TimeoutInTicks", event.timeoutInTicks);
            }
            return data;
        }
    }
}