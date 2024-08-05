package com.simibubi.create.infrastructure.command;

import com.simibubi.create.AllSpecialTextures;
import com.simibubi.create.CreateClient;
import com.simibubi.create.foundation.networking.SimplePacketBase;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class HighlightPacket extends SimplePacketBase {

    private final BlockPos pos;

    public HighlightPacket(BlockPos pos) {
        this.pos = pos;
    }

    public HighlightPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.pos);
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> performHighlight(this.pos)));
        return true;
    }

    @OnlyIn(Dist.CLIENT)
    public static void performHighlight(BlockPos pos) {
        if (Minecraft.getInstance().level != null && Minecraft.getInstance().level.m_46749_(pos)) {
            CreateClient.OUTLINER.showAABB("highlightCommand", Shapes.block().bounds().move(pos), 200).lineWidth(0.03125F).colored(15658734).withFaceTexture(AllSpecialTextures.SELECTION);
        }
    }
}