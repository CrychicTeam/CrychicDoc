package net.minecraftforge.event.level;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

public class LevelEvent extends Event {

    private final LevelAccessor level;

    public LevelEvent(LevelAccessor level) {
        this.level = level;
    }

    public LevelAccessor getLevel() {
        return this.level;
    }

    @Cancelable
    public static class CreateSpawnPosition extends LevelEvent {

        private final ServerLevelData settings;

        public CreateSpawnPosition(LevelAccessor level, ServerLevelData settings) {
            super(level);
            this.settings = settings;
        }

        public ServerLevelData getSettings() {
            return this.settings;
        }
    }

    public static class Load extends LevelEvent {

        public Load(LevelAccessor level) {
            super(level);
        }
    }

    @Cancelable
    public static class PotentialSpawns extends LevelEvent {

        private final MobCategory mobcategory;

        private final BlockPos pos;

        private final List<MobSpawnSettings.SpawnerData> list;

        private final List<MobSpawnSettings.SpawnerData> view;

        public PotentialSpawns(LevelAccessor level, MobCategory category, BlockPos pos, WeightedRandomList<MobSpawnSettings.SpawnerData> oldList) {
            super(level);
            this.pos = pos;
            this.mobcategory = category;
            if (!oldList.isEmpty()) {
                this.list = new ArrayList(oldList.unwrap());
            } else {
                this.list = new ArrayList();
            }
            this.view = Collections.unmodifiableList(this.list);
        }

        public MobCategory getMobCategory() {
            return this.mobcategory;
        }

        public BlockPos getPos() {
            return this.pos;
        }

        public List<MobSpawnSettings.SpawnerData> getSpawnerDataList() {
            return this.view;
        }

        public void addSpawnerData(MobSpawnSettings.SpawnerData data) {
            this.list.add(data);
        }

        public boolean removeSpawnerData(MobSpawnSettings.SpawnerData data) {
            return this.list.remove(data);
        }
    }

    public static class Save extends LevelEvent {

        public Save(LevelAccessor level) {
            super(level);
        }
    }

    public static class Unload extends LevelEvent {

        public Unload(LevelAccessor level) {
            super(level);
        }
    }
}