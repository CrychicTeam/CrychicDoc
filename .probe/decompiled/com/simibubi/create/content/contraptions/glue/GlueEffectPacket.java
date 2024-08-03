package com.simibubi.create.content.contraptions.glue;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class GlueEffectPacket extends SimplePacketBase {

    private BlockPos pos;

    private Direction direction;

    private boolean fullBlock;

    public GlueEffectPacket(BlockPos pos, Direction direction, boolean fullBlock) {
        this.pos = pos;
        this.direction = direction;
        this.fullBlock = fullBlock;
    }

    public GlueEffectPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.direction = Direction.from3DDataValue(buffer.readByte());
        this.fullBlock = buffer.readBoolean();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
        buffer.writeByte(this.direction.get3DDataValue());
        buffer.writeBoolean(this.fullBlock);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> this::handleClient));
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public void handleClient() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player.m_20183_().m_123314_(this.pos, 100.0)) {
            SuperGlueItem.spawnParticles(mc.level, this.pos, this.direction, this.fullBlock);
        }
    }
}