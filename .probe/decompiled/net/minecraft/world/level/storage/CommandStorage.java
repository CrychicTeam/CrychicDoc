package net.minecraft.world.level.storage;

import com.google.common.collect.Maps;
import java.util.Map;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.SavedData;

public class CommandStorage {

    private static final String ID_PREFIX = "command_storage_";

    private final Map<String, CommandStorage.Container> namespaces = Maps.newHashMap();

    private final DimensionDataStorage storage;

    public CommandStorage(DimensionDataStorage dimensionDataStorage0) {
        this.storage = dimensionDataStorage0;
    }

    private CommandStorage.Container newStorage(String string0) {
        CommandStorage.Container $$1 = new CommandStorage.Container();
        this.namespaces.put(string0, $$1);
        return $$1;
    }

    public CompoundTag get(ResourceLocation resourceLocation0) {
        String $$1 = resourceLocation0.getNamespace();
        CommandStorage.Container $$2 = this.storage.get(p_164844_ -> this.newStorage($$1).load(p_164844_), createId($$1));
        return $$2 != null ? $$2.get(resourceLocation0.getPath()) : new CompoundTag();
    }

    public void set(ResourceLocation resourceLocation0, CompoundTag compoundTag1) {
        String $$2 = resourceLocation0.getNamespace();
        this.storage.<CommandStorage.Container>computeIfAbsent(p_164839_ -> this.newStorage($$2).load(p_164839_), () -> this.newStorage($$2), createId($$2)).put(resourceLocation0.getPath(), compoundTag1);
    }

    public Stream<ResourceLocation> keys() {
        return this.namespaces.entrySet().stream().flatMap(p_164841_ -> ((CommandStorage.Container) p_164841_.getValue()).getKeys((String) p_164841_.getKey()));
    }

    private static String createId(String string0) {
        return "command_storage_" + string0;
    }

    static class Container extends SavedData {

        private static final String TAG_CONTENTS = "contents";

        private final Map<String, CompoundTag> storage = Maps.newHashMap();

        CommandStorage.Container load(CompoundTag compoundTag0) {
            CompoundTag $$1 = compoundTag0.getCompound("contents");
            for (String $$2 : $$1.getAllKeys()) {
                this.storage.put($$2, $$1.getCompound($$2));
            }
            return this;
        }

        @Override
        public CompoundTag save(CompoundTag compoundTag0) {
            CompoundTag $$1 = new CompoundTag();
            this.storage.forEach((p_78070_, p_78071_) -> $$1.put(p_78070_, p_78071_.copy()));
            compoundTag0.put("contents", $$1);
            return compoundTag0;
        }

        public CompoundTag get(String string0) {
            CompoundTag $$1 = (CompoundTag) this.storage.get(string0);
            return $$1 != null ? $$1 : new CompoundTag();
        }

        public void put(String string0, CompoundTag compoundTag1) {
            if (compoundTag1.isEmpty()) {
                this.storage.remove(string0);
            } else {
                this.storage.put(string0, compoundTag1);
            }
            this.m_77762_();
        }

        public Stream<ResourceLocation> getKeys(String string0) {
            return this.storage.keySet().stream().map(p_78062_ -> new ResourceLocation(string0, p_78062_));
        }
    }
}