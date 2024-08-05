package net.minecraft.network.protocol.game;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.ChatVisiblity;

public record ServerboundClientInformationPacket(String f_133863_, int f_133864_, ChatVisiblity f_133865_, boolean f_133866_, int f_133867_, HumanoidArm f_133868_, boolean f_179550_, boolean f_195812_) implements Packet<ServerGamePacketListener> {

    private final String language;

    private final int viewDistance;

    private final ChatVisiblity chatVisibility;

    private final boolean chatColors;

    private final int modelCustomisation;

    private final HumanoidArm mainHand;

    private final boolean textFilteringEnabled;

    private final boolean allowsListing;

    public static final int MAX_LANGUAGE_LENGTH = 16;

    public ServerboundClientInformationPacket(FriendlyByteBuf p_179560_) {
        this(p_179560_.readUtf(16), p_179560_.readByte(), p_179560_.readEnum(ChatVisiblity.class), p_179560_.readBoolean(), p_179560_.readUnsignedByte(), p_179560_.readEnum(HumanoidArm.class), p_179560_.readBoolean(), p_179560_.readBoolean());
    }

    public ServerboundClientInformationPacket(String f_133863_, int f_133864_, ChatVisiblity f_133865_, boolean f_133866_, int f_133867_, HumanoidArm f_133868_, boolean f_179550_, boolean f_195812_) {
        this.language = f_133863_;
        this.viewDistance = f_133864_;
        this.chatVisibility = f_133865_;
        this.chatColors = f_133866_;
        this.modelCustomisation = f_133867_;
        this.mainHand = f_133868_;
        this.textFilteringEnabled = f_179550_;
        this.allowsListing = f_195812_;
    }

    @Override
    public void write(FriendlyByteBuf p_133884_) {
        p_133884_.writeUtf(this.language);
        p_133884_.writeByte(this.viewDistance);
        p_133884_.writeEnum(this.chatVisibility);
        p_133884_.writeBoolean(this.chatColors);
        p_133884_.writeByte(this.modelCustomisation);
        p_133884_.writeEnum(this.mainHand);
        p_133884_.writeBoolean(this.textFilteringEnabled);
        p_133884_.writeBoolean(this.allowsListing);
    }

    public void handle(ServerGamePacketListener p_133882_) {
        p_133882_.handleClientInformation(this);
    }
}