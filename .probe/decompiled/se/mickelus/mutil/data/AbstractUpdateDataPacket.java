package se.mickelus.mutil.data;

import com.google.gson.JsonElement;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import se.mickelus.mutil.network.AbstractPacket;

@ParametersAreNonnullByDefault
public abstract class AbstractUpdateDataPacket extends AbstractPacket {

    protected String directory;

    protected Map<ResourceLocation, String> data;

    public AbstractUpdateDataPacket() {
    }

    public AbstractUpdateDataPacket(String directory, Map<ResourceLocation, JsonElement> data) {
        this.directory = directory;
        this.data = (Map<ResourceLocation, String>) data.entrySet().stream().collect(Collectors.toMap(Entry::getKey, entry -> ((JsonElement) entry.getValue()).toString()));
    }

    @Override
    public void toBytes(FriendlyByteBuf buffer) {
        buffer.writeUtf(this.directory);
        buffer.writeInt(this.data.size());
        this.data.forEach((resourceLocation, data) -> {
            buffer.writeResourceLocation(resourceLocation);
            buffer.writeUtf(data);
        });
    }

    @Override
    public void fromBytes(FriendlyByteBuf buffer) {
        this.directory = buffer.readUtf();
        int count = buffer.readInt();
        this.data = new HashMap();
        for (int i = 0; i < count; i++) {
            this.data.put(buffer.readResourceLocation(), buffer.readUtf());
        }
    }

    @Override
    public abstract void handle(Player var1);
}