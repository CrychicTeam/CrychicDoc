package net.minecraft.network.chat;

import javax.annotation.Nullable;
import net.minecraft.util.SignatureValidator;

@FunctionalInterface
public interface SignedMessageValidator {

    SignedMessageValidator ACCEPT_UNSIGNED = p_252109_ -> !p_252109_.hasSignature();

    SignedMessageValidator REJECT_ALL = p_251793_ -> false;

    boolean updateAndValidate(PlayerChatMessage var1);

    public static class KeyBased implements SignedMessageValidator {

        private final SignatureValidator validator;

        @Nullable
        private PlayerChatMessage lastMessage;

        private boolean isChainValid = true;

        public KeyBased(SignatureValidator signatureValidator0) {
            this.validator = signatureValidator0;
        }

        private boolean validateChain(PlayerChatMessage playerChatMessage0) {
            return playerChatMessage0.equals(this.lastMessage) ? true : this.lastMessage == null || playerChatMessage0.link().isDescendantOf(this.lastMessage.link());
        }

        @Override
        public boolean updateAndValidate(PlayerChatMessage playerChatMessage0) {
            this.isChainValid = this.isChainValid && playerChatMessage0.verify(this.validator) && this.validateChain(playerChatMessage0);
            if (!this.isChainValid) {
                return false;
            } else {
                this.lastMessage = playerChatMessage0;
                return true;
            }
        }
    }
}