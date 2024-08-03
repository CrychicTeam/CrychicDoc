package net.minecraft.commands;

import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.network.chat.PlayerChatMessage;

public interface CommandSigningContext {

    CommandSigningContext ANONYMOUS = new CommandSigningContext() {

        @Nullable
        @Override
        public PlayerChatMessage getArgument(String p_242898_) {
            return null;
        }
    };

    @Nullable
    PlayerChatMessage getArgument(String var1);

    public static record SignedArguments(Map<String, PlayerChatMessage> f_242498_) implements CommandSigningContext {

        private final Map<String, PlayerChatMessage> arguments;

        public SignedArguments(Map<String, PlayerChatMessage> f_242498_) {
            this.arguments = f_242498_;
        }

        @Nullable
        @Override
        public PlayerChatMessage getArgument(String p_242852_) {
            return (PlayerChatMessage) this.arguments.get(p_242852_);
        }
    }
}