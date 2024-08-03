package net.minecraftforge.event;

import java.util.function.Consumer;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.event.IModBusEvent;

public class AddPackFindersEvent extends Event implements IModBusEvent {

    private final PackType packType;

    private final Consumer<RepositorySource> sources;

    public AddPackFindersEvent(PackType packType, Consumer<RepositorySource> sources) {
        this.packType = packType;
        this.sources = sources;
    }

    public void addRepositorySource(RepositorySource source) {
        this.sources.accept(source);
    }

    public PackType getPackType() {
        return this.packType;
    }
}