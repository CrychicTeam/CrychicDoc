package net.minecraft.network.chat;

import com.mojang.authlib.GameProfile;
import java.time.Duration;
import java.util.UUID;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.SignatureValidator;
import net.minecraft.world.entity.player.ProfilePublicKey;

public record RemoteChatSession(UUID f_244448_, ProfilePublicKey f_243855_) {

    private final UUID sessionId;

    private final ProfilePublicKey profilePublicKey;

    public RemoteChatSession(UUID f_244448_, ProfilePublicKey f_243855_) {
        this.sessionId = f_244448_;
        this.profilePublicKey = f_243855_;
    }

    public SignedMessageValidator createMessageValidator() {
        return new SignedMessageValidator.KeyBased(this.profilePublicKey.createSignatureValidator());
    }

    public SignedMessageChain.Decoder createMessageDecoder(UUID p_249107_) {
        return new SignedMessageChain(p_249107_, this.sessionId).decoder(this.profilePublicKey);
    }

    public RemoteChatSession.Data asData() {
        return new RemoteChatSession.Data(this.sessionId, this.profilePublicKey.data());
    }

    public boolean hasExpired() {
        return this.profilePublicKey.data().hasExpired();
    }

    public static record Data(UUID f_244232_, ProfilePublicKey.Data f_243937_) {

        private final UUID sessionId;

        private final ProfilePublicKey.Data profilePublicKey;

        public Data(UUID f_244232_, ProfilePublicKey.Data f_243937_) {
            this.sessionId = f_244232_;
            this.profilePublicKey = f_243937_;
        }

        public static RemoteChatSession.Data read(FriendlyByteBuf p_252181_) {
            return new RemoteChatSession.Data(p_252181_.readUUID(), new ProfilePublicKey.Data(p_252181_));
        }

        public static void write(FriendlyByteBuf p_248910_, RemoteChatSession.Data p_250537_) {
            p_248910_.writeUUID(p_250537_.sessionId);
            p_250537_.profilePublicKey.write(p_248910_);
        }

        public RemoteChatSession validate(GameProfile p_251231_, SignatureValidator p_248970_, Duration p_251179_) throws ProfilePublicKey.ValidationException {
            return new RemoteChatSession(this.sessionId, ProfilePublicKey.createValidated(p_248970_, p_251231_.getId(), this.profilePublicKey, p_251179_));
        }
    }
}