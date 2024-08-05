package net.minecraft.network.chat;

import net.minecraft.server.level.ServerPlayer;

public interface OutgoingChatMessage {

    Component content();

    void sendToPlayer(ServerPlayer var1, boolean var2, ChatType.Bound var3);

    static OutgoingChatMessage create(PlayerChatMessage playerChatMessage0) {
        return (OutgoingChatMessage) (playerChatMessage0.isSystem() ? new OutgoingChatMessage.Disguised(playerChatMessage0.decoratedContent()) : new OutgoingChatMessage.Player(playerChatMessage0));
    }

    public static record Disguised(Component f_244003_) implements OutgoingChatMessage {

        private final Component content;

        public Disguised(Component f_244003_) {
            this.content = f_244003_;
        }

        @Override
        public void sendToPlayer(ServerPlayer p_249237_, boolean p_249574_, ChatType.Bound p_250880_) {
            p_249237_.connection.sendDisguisedChatMessage(this.content, p_250880_);
        }
    }

    public static record Player(PlayerChatMessage f_243697_) implements OutgoingChatMessage {

        private final PlayerChatMessage message;

        public Player(PlayerChatMessage f_243697_) {
            this.message = f_243697_;
        }

        @Override
        public Component content() {
            return this.message.decoratedContent();
        }

        @Override
        public void sendToPlayer(ServerPlayer p_249642_, boolean p_251123_, ChatType.Bound p_251482_) {
            PlayerChatMessage $$3 = this.message.filter(p_251123_);
            if (!$$3.isFullyFiltered()) {
                p_249642_.connection.sendPlayerChatMessage($$3, p_251482_);
            }
        }
    }
}