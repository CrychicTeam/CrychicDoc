package vazkii.patchouli.forge.network;

import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.client.book.ClientBookRegistry;

public class ForgeMessageOpenBookGui {

    private final ResourceLocation book;

    @Nullable
    private final ResourceLocation entry;

    private final int page;

    public ForgeMessageOpenBookGui(ResourceLocation book, @Nullable ResourceLocation entry, int page) {
        this.book = book;
        this.entry = entry;
        this.page = page;
    }

    public static ForgeMessageOpenBookGui decode(FriendlyByteBuf buf) {
        ResourceLocation book = buf.readResourceLocation();
        String tmp = buf.readUtf();
        ResourceLocation entry;
        if (tmp.isEmpty()) {
            entry = null;
        } else {
            entry = ResourceLocation.tryParse(tmp);
        }
        int page = buf.readVarInt();
        return new ForgeMessageOpenBookGui(book, entry, page);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeResourceLocation(this.book);
        buf.writeUtf(this.entry == null ? "" : this.entry.toString());
        buf.writeVarInt(this.page);
    }

    public static void send(ServerPlayer player, ResourceLocation book, @Nullable ResourceLocation entry, int page) {
        ForgeNetworkHandler.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new ForgeMessageOpenBookGui(book, entry, page));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> ClientBookRegistry.INSTANCE.displayBookGui(this.book, this.entry, this.page));
        ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
    }
}