package net.minecraft.network.chat;

import com.mojang.logging.LogUtils;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.util.SignatureValidator;
import net.minecraft.util.Signer;
import net.minecraft.world.entity.player.ProfilePublicKey;
import org.slf4j.Logger;

public class SignedMessageChain {

    private static final Logger LOGGER = LogUtils.getLogger();

    @Nullable
    private SignedMessageLink nextLink;

    public SignedMessageChain(UUID uUID0, UUID uUID1) {
        this.nextLink = SignedMessageLink.root(uUID0, uUID1);
    }

    public SignedMessageChain.Encoder encoder(Signer signer0) {
        return p_248067_ -> {
            SignedMessageLink $$2 = this.advanceLink();
            return $$2 == null ? null : new MessageSignature(signer0.sign(p_248065_ -> PlayerChatMessage.updateSignature(p_248065_, $$2, p_248067_)));
        };
    }

    public SignedMessageChain.Decoder decoder(ProfilePublicKey profilePublicKey0) {
        SignatureValidator $$1 = profilePublicKey0.createSignatureValidator();
        return (p_248061_, p_248062_) -> {
            SignedMessageLink $$4 = this.advanceLink();
            if ($$4 == null) {
                throw new SignedMessageChain.DecodeException(Component.translatable("chat.disabled.chain_broken"), false);
            } else if (profilePublicKey0.data().hasExpired()) {
                throw new SignedMessageChain.DecodeException(Component.translatable("chat.disabled.expiredProfileKey"), false);
            } else {
                PlayerChatMessage $$5 = new PlayerChatMessage($$4, p_248061_, p_248062_, null, FilterMask.PASS_THROUGH);
                if (!$$5.verify($$1)) {
                    throw new SignedMessageChain.DecodeException(Component.translatable("multiplayer.disconnect.unsigned_chat"), true);
                } else {
                    if ($$5.hasExpiredServer(Instant.now())) {
                        LOGGER.warn("Received expired chat: '{}'. Is the client/server system time unsynchronized?", p_248062_.content());
                    }
                    return $$5;
                }
            }
        };
    }

    @Nullable
    private SignedMessageLink advanceLink() {
        SignedMessageLink $$0 = this.nextLink;
        if ($$0 != null) {
            this.nextLink = $$0.advance();
        }
        return $$0;
    }

    public static class DecodeException extends ThrowingComponent {

        private final boolean shouldDisconnect;

        public DecodeException(Component component0, boolean boolean1) {
            super(component0);
            this.shouldDisconnect = boolean1;
        }

        public boolean shouldDisconnect() {
            return this.shouldDisconnect;
        }
    }

    @FunctionalInterface
    public interface Decoder {

        SignedMessageChain.Decoder REJECT_ALL = (p_253466_, p_253467_) -> {
            throw new SignedMessageChain.DecodeException(Component.translatable("chat.disabled.missingProfileKey"), false);
        };

        static SignedMessageChain.Decoder unsigned(UUID uUID0) {
            return (p_248069_, p_248070_) -> PlayerChatMessage.unsigned(uUID0, p_248070_.content());
        }

        PlayerChatMessage unpack(@Nullable MessageSignature var1, SignedMessageBody var2) throws SignedMessageChain.DecodeException;
    }

    @FunctionalInterface
    public interface Encoder {

        SignedMessageChain.Encoder UNSIGNED = p_250548_ -> null;

        @Nullable
        MessageSignature pack(SignedMessageBody var1);
    }
}