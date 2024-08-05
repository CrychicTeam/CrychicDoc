package net.minecraft.commands.arguments;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.MessageSignature;
import net.minecraft.network.chat.SignableCommand;

public record ArgumentSignatures(List<ArgumentSignatures.Entry> f_240908_) {

    private final List<ArgumentSignatures.Entry> entries;

    public static final ArgumentSignatures EMPTY = new ArgumentSignatures(List.of());

    private static final int MAX_ARGUMENT_COUNT = 8;

    private static final int MAX_ARGUMENT_NAME_LENGTH = 16;

    public ArgumentSignatures(FriendlyByteBuf p_231052_) {
        this(p_231052_.readCollection(FriendlyByteBuf.limitValue(ArrayList::new, 8), ArgumentSignatures.Entry::new));
    }

    public ArgumentSignatures(List<ArgumentSignatures.Entry> f_240908_) {
        this.entries = f_240908_;
    }

    @Nullable
    public MessageSignature get(String p_241493_) {
        for (ArgumentSignatures.Entry $$1 : this.entries) {
            if ($$1.name.equals(p_241493_)) {
                return $$1.signature;
            }
        }
        return null;
    }

    public void write(FriendlyByteBuf p_231062_) {
        p_231062_.writeCollection(this.entries, (p_241214_, p_241215_) -> p_241215_.write(p_241214_));
    }

    public static ArgumentSignatures signCommand(SignableCommand<?> p_251621_, ArgumentSignatures.Signer p_248653_) {
        List<ArgumentSignatures.Entry> $$2 = p_251621_.arguments().stream().map(p_247962_ -> {
            MessageSignature $$2x = p_248653_.sign(p_247962_.value());
            return $$2x != null ? new ArgumentSignatures.Entry(p_247962_.name(), $$2x) : null;
        }).filter(Objects::nonNull).toList();
        return new ArgumentSignatures($$2);
    }

    public static record Entry(String f_240910_, MessageSignature f_240879_) {

        private final String name;

        private final MessageSignature signature;

        public Entry(FriendlyByteBuf p_241305_) {
            this(p_241305_.readUtf(16), MessageSignature.read(p_241305_));
        }

        public Entry(String f_240910_, MessageSignature f_240879_) {
            this.name = f_240910_;
            this.signature = f_240879_;
        }

        public void write(FriendlyByteBuf p_241403_) {
            p_241403_.writeUtf(this.name, 16);
            MessageSignature.write(p_241403_, this.signature);
        }
    }

    @FunctionalInterface
    public interface Signer {

        @Nullable
        MessageSignature sign(String var1);
    }
}