package dev.latvian.mods.kubejs.stages;

import java.util.Collection;
import java.util.List;
import net.minecraft.world.entity.player.Player;

public class NoStages extends Stages {

    public static final NoStages NULL_INSTANCE = new NoStages(null);

    private NoStages(Player player) {
        super(player);
    }

    @Override
    public boolean addNoUpdate(String stage) {
        return false;
    }

    @Override
    public boolean removeNoUpdate(String stage) {
        return false;
    }

    @Override
    public Collection<String> getAll() {
        return List.of();
    }

    @Override
    public boolean has(String stage) {
        return false;
    }

    @Override
    public boolean clear() {
        return false;
    }

    @Override
    public void sync() {
    }

    @Override
    public void replace(Collection<String> stages) {
    }
}