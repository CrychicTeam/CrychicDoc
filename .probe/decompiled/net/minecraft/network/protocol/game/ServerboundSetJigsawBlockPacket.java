package net.minecraft.network.protocol.game;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.JigsawBlockEntity;

public class ServerboundSetJigsawBlockPacket implements Packet<ServerGamePacketListener> {

    private final BlockPos pos;

    private final ResourceLocation name;

    private final ResourceLocation target;

    private final ResourceLocation pool;

    private final String finalState;

    private final JigsawBlockEntity.JointType joint;

    public ServerboundSetJigsawBlockPacket(BlockPos blockPos0, ResourceLocation resourceLocation1, ResourceLocation resourceLocation2, ResourceLocation resourceLocation3, String string4, JigsawBlockEntity.JointType jigsawBlockEntityJointType5) {
        this.pos = blockPos0;
        this.name = resourceLocation1;
        this.target = resourceLocation2;
        this.pool = resourceLocation3;
        this.finalState = string4;
        this.joint = jigsawBlockEntityJointType5;
    }

    public ServerboundSetJigsawBlockPacket(FriendlyByteBuf friendlyByteBuf0) {
        this.pos = friendlyByteBuf0.readBlockPos();
        this.name = friendlyByteBuf0.readResourceLocation();
        this.target = friendlyByteBuf0.readResourceLocation();
        this.pool = friendlyByteBuf0.readResourceLocation();
        this.finalState = friendlyByteBuf0.readUtf();
        this.joint = (JigsawBlockEntity.JointType) JigsawBlockEntity.JointType.byName(friendlyByteBuf0.readUtf()).orElse(JigsawBlockEntity.JointType.ALIGNED);
    }

    @Override
    public void write(FriendlyByteBuf friendlyByteBuf0) {
        friendlyByteBuf0.writeBlockPos(this.pos);
        friendlyByteBuf0.writeResourceLocation(this.name);
        friendlyByteBuf0.writeResourceLocation(this.target);
        friendlyByteBuf0.writeResourceLocation(this.pool);
        friendlyByteBuf0.writeUtf(this.finalState);
        friendlyByteBuf0.writeUtf(this.joint.getSerializedName());
    }

    public void handle(ServerGamePacketListener serverGamePacketListener0) {
        serverGamePacketListener0.handleSetJigsawBlock(this);
    }

    public BlockPos getPos() {
        return this.pos;
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public ResourceLocation getTarget() {
        return this.target;
    }

    public ResourceLocation getPool() {
        return this.pool;
    }

    public String getFinalState() {
        return this.finalState;
    }

    public JigsawBlockEntity.JointType getJoint() {
        return this.joint;
    }
}