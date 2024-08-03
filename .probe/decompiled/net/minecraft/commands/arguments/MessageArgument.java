package net.minecraft.commands.arguments;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.commands.CommandSigningContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.selector.EntitySelector;
import net.minecraft.commands.arguments.selector.EntitySelectorParser;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.FilteredText;

public class MessageArgument implements SignedArgument<MessageArgument.Message> {

    private static final Collection<String> EXAMPLES = Arrays.asList("Hello world!", "foo", "@e", "Hello @p :)");

    public static MessageArgument message() {
        return new MessageArgument();
    }

    public static Component getMessage(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1) throws CommandSyntaxException {
        MessageArgument.Message $$2 = (MessageArgument.Message) commandContextCommandSourceStack0.getArgument(string1, MessageArgument.Message.class);
        return $$2.resolveComponent((CommandSourceStack) commandContextCommandSourceStack0.getSource());
    }

    public static void resolveChatMessage(CommandContext<CommandSourceStack> commandContextCommandSourceStack0, String string1, Consumer<PlayerChatMessage> consumerPlayerChatMessage2) throws CommandSyntaxException {
        MessageArgument.Message $$3 = (MessageArgument.Message) commandContextCommandSourceStack0.getArgument(string1, MessageArgument.Message.class);
        CommandSourceStack $$4 = (CommandSourceStack) commandContextCommandSourceStack0.getSource();
        Component $$5 = $$3.resolveComponent($$4);
        CommandSigningContext $$6 = $$4.getSigningContext();
        PlayerChatMessage $$7 = $$6.getArgument(string1);
        if ($$7 != null) {
            resolveSignedMessage(consumerPlayerChatMessage2, $$4, $$7.withUnsignedContent($$5));
        } else {
            resolveDisguisedMessage(consumerPlayerChatMessage2, $$4, PlayerChatMessage.system($$3.text).withUnsignedContent($$5));
        }
    }

    private static void resolveSignedMessage(Consumer<PlayerChatMessage> consumerPlayerChatMessage0, CommandSourceStack commandSourceStack1, PlayerChatMessage playerChatMessage2) {
        MinecraftServer $$3 = commandSourceStack1.getServer();
        CompletableFuture<FilteredText> $$4 = filterPlainText(commandSourceStack1, playerChatMessage2);
        CompletableFuture<Component> $$5 = $$3.getChatDecorator().decorate(commandSourceStack1.getPlayer(), playerChatMessage2.decoratedContent());
        commandSourceStack1.getChatMessageChainer().append(p_247979_ -> CompletableFuture.allOf($$4, $$5).thenAcceptAsync(p_247970_ -> {
            PlayerChatMessage $$5x = playerChatMessage2.withUnsignedContent((Component) $$5.join()).filter(((FilteredText) $$4.join()).mask());
            consumerPlayerChatMessage0.accept($$5x);
        }, p_247979_));
    }

    private static void resolveDisguisedMessage(Consumer<PlayerChatMessage> consumerPlayerChatMessage0, CommandSourceStack commandSourceStack1, PlayerChatMessage playerChatMessage2) {
        MinecraftServer $$3 = commandSourceStack1.getServer();
        CompletableFuture<Component> $$4 = $$3.getChatDecorator().decorate(commandSourceStack1.getPlayer(), playerChatMessage2.decoratedContent());
        commandSourceStack1.getChatMessageChainer().append(p_247974_ -> $$4.thenAcceptAsync(p_247965_ -> consumerPlayerChatMessage0.accept(playerChatMessage2.withUnsignedContent(p_247965_)), p_247974_));
    }

    private static CompletableFuture<FilteredText> filterPlainText(CommandSourceStack commandSourceStack0, PlayerChatMessage playerChatMessage1) {
        ServerPlayer $$2 = commandSourceStack0.getPlayer();
        return $$2 != null && playerChatMessage1.hasSignatureFrom($$2.m_20148_()) ? $$2.getTextFilter().processStreamMessage(playerChatMessage1.signedContent()) : CompletableFuture.completedFuture(FilteredText.passThrough(playerChatMessage1.signedContent()));
    }

    public MessageArgument.Message parse(StringReader stringReader0) throws CommandSyntaxException {
        return MessageArgument.Message.parseText(stringReader0, true);
    }

    public Collection<String> getExamples() {
        return EXAMPLES;
    }

    public static class Message {

        final String text;

        private final MessageArgument.Part[] parts;

        public Message(String string0, MessageArgument.Part[] messageArgumentPart1) {
            this.text = string0;
            this.parts = messageArgumentPart1;
        }

        public String getText() {
            return this.text;
        }

        public MessageArgument.Part[] getParts() {
            return this.parts;
        }

        Component resolveComponent(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
            return this.toComponent(commandSourceStack0, commandSourceStack0.hasPermission(2));
        }

        public Component toComponent(CommandSourceStack commandSourceStack0, boolean boolean1) throws CommandSyntaxException {
            if (this.parts.length != 0 && boolean1) {
                MutableComponent $$2 = Component.literal(this.text.substring(0, this.parts[0].getStart()));
                int $$3 = this.parts[0].getStart();
                for (MessageArgument.Part $$4 : this.parts) {
                    Component $$5 = $$4.toComponent(commandSourceStack0);
                    if ($$3 < $$4.getStart()) {
                        $$2.append(this.text.substring($$3, $$4.getStart()));
                    }
                    if ($$5 != null) {
                        $$2.append($$5);
                    }
                    $$3 = $$4.getEnd();
                }
                if ($$3 < this.text.length()) {
                    $$2.append(this.text.substring($$3));
                }
                return $$2;
            } else {
                return Component.literal(this.text);
            }
        }

        public static MessageArgument.Message parseText(StringReader stringReader0, boolean boolean1) throws CommandSyntaxException {
            String $$2 = stringReader0.getString().substring(stringReader0.getCursor(), stringReader0.getTotalLength());
            if (!boolean1) {
                stringReader0.setCursor(stringReader0.getTotalLength());
                return new MessageArgument.Message($$2, new MessageArgument.Part[0]);
            } else {
                List<MessageArgument.Part> $$3 = Lists.newArrayList();
                int $$4 = stringReader0.getCursor();
                while (true) {
                    int $$5;
                    EntitySelector $$7;
                    while (true) {
                        if (!stringReader0.canRead()) {
                            return new MessageArgument.Message($$2, (MessageArgument.Part[]) $$3.toArray(new MessageArgument.Part[0]));
                        }
                        if (stringReader0.peek() == '@') {
                            $$5 = stringReader0.getCursor();
                            try {
                                EntitySelectorParser $$6 = new EntitySelectorParser(stringReader0);
                                $$7 = $$6.parse();
                                break;
                            } catch (CommandSyntaxException var8) {
                                if (var8.getType() != EntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE && var8.getType() != EntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE) {
                                    throw var8;
                                }
                                stringReader0.setCursor($$5 + 1);
                            }
                        } else {
                            stringReader0.skip();
                        }
                    }
                    $$3.add(new MessageArgument.Part($$5 - $$4, stringReader0.getCursor() - $$4, $$7));
                }
            }
        }
    }

    public static class Part {

        private final int start;

        private final int end;

        private final EntitySelector selector;

        public Part(int int0, int int1, EntitySelector entitySelector2) {
            this.start = int0;
            this.end = int1;
            this.selector = entitySelector2;
        }

        public int getStart() {
            return this.start;
        }

        public int getEnd() {
            return this.end;
        }

        public EntitySelector getSelector() {
            return this.selector;
        }

        @Nullable
        public Component toComponent(CommandSourceStack commandSourceStack0) throws CommandSyntaxException {
            return EntitySelector.joinNames(this.selector.findEntities(commandSourceStack0));
        }
    }
}