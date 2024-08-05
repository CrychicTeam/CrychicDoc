package com.simibubi.create.content.equipment.symmetryWand;

import com.simibubi.create.foundation.networking.SimplePacketBase;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class SymmetryEffectPacket extends SimplePacketBase {

    private BlockPos mirror;

    private List<BlockPos> positions;

    public SymmetryEffectPacket(BlockPos mirror, List<BlockPos> positions) {
        this.mirror = mirror;
        this.positions = positions;
    }

    public SymmetryEffectPacket(FriendlyByteBuf buffer) {
        this.mirror = buffer.readBlockPos();
        int amt = buffer.readInt();
        this.positions = new ArrayList(amt);
        for (int i = 0; i < amt; i++) {
            this.positions.add(buffer.readBlockPos());
        }
    }

    @Override
    public void write(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.mirror);
        buffer.writeInt(this.positions.size());
        for (BlockPos blockPos : this.positions) {
            buffer.writeBlockPos(blockPos);
        }
    }

    @Override
    public boolean handle(NetworkEvent.Context context) {
        context.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
            if (!(Minecraft.getInstance().player.m_20182_().distanceTo(Vec3.atLowerCornerOf(this.mirror)) > 100.0)) {
                for (BlockPos to : this.positions) {
                    SymmetryHandler.drawEffect(this.mirror, to);
                }
            }
        }));
        return true;
    }
}