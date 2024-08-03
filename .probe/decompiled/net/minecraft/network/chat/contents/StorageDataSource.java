package net.minecraft.network.chat.contents;

import java.util.stream.Stream;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public record StorageDataSource(ResourceLocation f_237484_) implements DataSource {

    private final ResourceLocation id;

    public StorageDataSource(ResourceLocation f_237484_) {
        this.id = f_237484_;
    }

    @Override
    public Stream<CompoundTag> getData(CommandSourceStack p_237491_) {
        CompoundTag $$1 = p_237491_.getServer().getCommandStorage().get(this.id);
        return Stream.of($$1);
    }

    public String toString() {
        return "storage=" + this.id;
    }
}