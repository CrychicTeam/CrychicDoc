package com.github.alexthe666.alexsmobs.message;

import com.github.alexthe666.alexsmobs.AlexsMobs;
import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.entity.EntityMungus;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.chunk.PalettedContainer;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

public class MessageMungusBiomeChange {

    public int mungusID;

    public int posX;

    public int posZ;

    public String biomeOption;

    public MessageMungusBiomeChange(int mungusID, int posX, int posY, String biomeOption) {
        this.mungusID = mungusID;
        this.posX = posX;
        this.posZ = posY;
        this.biomeOption = biomeOption;
    }

    public MessageMungusBiomeChange() {
    }

    public static MessageMungusBiomeChange read(FriendlyByteBuf buf) {
        return new MessageMungusBiomeChange(buf.readInt(), buf.readInt(), buf.readInt(), buf.readUtf());
    }

    public static void write(MessageMungusBiomeChange message, FriendlyByteBuf buf) {
        buf.writeInt(message.mungusID);
        buf.writeInt(message.posX);
        buf.writeInt(message.posZ);
        buf.writeUtf(message.biomeOption);
    }

    public static class Handler {

        public static void handle(MessageMungusBiomeChange message, Supplier<NetworkEvent.Context> context) {
            ((NetworkEvent.Context) context.get()).setPacketHandled(true);
            ((NetworkEvent.Context) context.get()).enqueueWork(() -> {
                Player player = ((NetworkEvent.Context) context.get()).getSender();
                if (((NetworkEvent.Context) context.get()).getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                    player = AlexsMobs.PROXY.getClientSidePlayer();
                }
                if (player != null && player.m_9236_() != null) {
                    Entity entity = player.m_9236_().getEntity(message.mungusID);
                    Registry<Biome> registry = player.m_9236_().registryAccess().registryOrThrow(Registries.BIOME);
                    Biome biome = registry.get(new ResourceLocation(message.biomeOption));
                    ResourceKey<Biome> resourceKey = (ResourceKey<Biome>) registry.getResourceKey(biome).orElse(null);
                    Holder<Biome> holder = (Holder<Biome>) registry.getHolder(resourceKey).orElse(null);
                    if (AMConfig.mungusBiomeTransformationType == 2 && entity instanceof EntityMungus && entity.distanceToSqr((double) message.posX, entity.getY(), (double) message.posZ) < 1000.0 && biome != null) {
                        LevelChunk chunk = player.m_9236_().getChunkAt(new BlockPos(message.posX, 0, message.posZ));
                        int i = QuartPos.fromBlock(chunk.m_141937_());
                        int k = i + QuartPos.fromBlock(chunk.m_141928_()) - 1;
                        int l = Mth.clamp(QuartPos.fromBlock((int) entity.getY()), i, k);
                        int j = chunk.m_151564_(QuartPos.toBlock(l));
                        LevelChunkSection section = chunk.m_183278_(j);
                        if (section != null) {
                            PalettedContainer<Holder<Biome>> container = section.getBiomes().recreate();
                            for (int biomeX = 0; biomeX < 4; biomeX++) {
                                for (int biomeY = 0; biomeY < 4; biomeY++) {
                                    for (int biomeZ = 0; biomeZ < 4; biomeZ++) {
                                        container.getAndSetUnchecked(biomeX, biomeY, biomeZ, holder);
                                    }
                                }
                            }
                            section.biomes = container;
                        }
                        AlexsMobs.PROXY.updateBiomeVisuals(message.posX, message.posZ);
                    }
                }
            });
        }
    }
}