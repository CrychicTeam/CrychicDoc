package dev.xkmc.modulargolems.content.capability;

import dev.xkmc.l2library.util.Proxy;
import dev.xkmc.modulargolems.content.entity.mode.GolemMode;
import dev.xkmc.modulargolems.content.entity.mode.GolemModes;
import dev.xkmc.modulargolems.init.data.MGLangData;
import java.util.UUID;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public interface GolemConfigEditor {

    @OnlyIn(Dist.CLIENT)
    static GolemConfigEditor readable(UUID id, int color) {
        return new GolemConfigEditor.Readable(Proxy.getClientWorld(), id, color);
    }

    Level level();

    GolemConfigEntry entry();

    default Component getDisplayName() {
        return this.entry().getDisplayName();
    }

    default GolemMode getDefaultMode() {
        return GolemModes.get(this.entry().defaultMode);
    }

    default void setDefaultMode(GolemMode mode) {
        this.entry().defaultMode = mode.getID();
        this.sync();
    }

    default boolean summonToPosition() {
        return this.entry().summonToPosition;
    }

    default void setSummonToPosition(boolean bool) {
        this.entry().summonToPosition = bool;
        this.sync();
    }

    default boolean locked() {
        return this.entry().locked;
    }

    default void setLocked(boolean lock) {
        this.entry().locked = lock;
        this.sync();
    }

    default void sync() {
        this.entry().sync(this.level());
    }

    default PickupFilterEditor getFilter() {
        return new PickupFilterEditor(this);
    }

    default TargetFilterEditor target() {
        return new TargetFilterEditor(this);
    }

    default SquadEditor getSquad() {
        return new SquadEditor(this);
    }

    default PathEditor getPath() {
        return new PathEditor(this);
    }

    public static record Readable(Level level, UUID id, int color) implements GolemConfigEditor {

        @Override
        public GolemConfigEntry entry() {
            return GolemConfigStorage.get(this.level).getOrCreateStorage(this.id, this.color, MGLangData.LOADING.get());
        }
    }

    public static record Writable(Level level, GolemConfigEntry entry) implements GolemConfigEditor {
    }
}