package harmonised.pmmo.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import harmonised.pmmo.features.party.PartyUtils;
import harmonised.pmmo.setup.datagen.LangProvider;
import java.util.List;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.UuidArgument;
import net.minecraft.server.level.ServerPlayer;

public class CmdNodeParty {

    private static final String REQUEST_ID = "requestID";

    public static ArgumentBuilder<CommandSourceStack, ?> register(CommandDispatcher<CommandSourceStack> dispatcher) {
        return ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) ((LiteralArgumentBuilder) Commands.literal("party").then(Commands.literal("create").executes(c -> partyCreate(c)))).then(Commands.literal("leave").executes(c -> partyLeave(c)))).then(Commands.literal("invite").then(Commands.argument("player", EntityArgument.player()).executes(c -> partyInvite(c))))).then(Commands.literal("uninvite").then(Commands.argument("player", EntityArgument.player()).executes(c -> partyUninvite(c))))).then(Commands.literal("list").executes(c -> listParty(c)))).then(Commands.literal("accept").then(Commands.argument("requestID", UuidArgument.uuid()).executes(c -> partyAccept(c))))).then(Commands.literal("decline").then(Commands.argument("requestID", UuidArgument.uuid()).executes(c -> partyDecline(c))))).executes(c -> {
            System.out.println(PartyUtils.isInParty(((CommandSourceStack) c.getSource()).getPlayerOrException()));
            return 0;
        });
    }

    public static int partyCreate(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ((CommandSourceStack) ctx.getSource()).getPlayerOrException();
        if (PartyUtils.isInParty(player)) {
            ((CommandSourceStack) ctx.getSource()).sendFailure(LangProvider.PARTY_ALREADY_IN.asComponent());
            return 1;
        } else {
            PartyUtils.createParty(player);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_CREATED.asComponent(), false);
            return 0;
        }
    }

    public static int partyLeave(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        PartyUtils.removeFromParty(((CommandSourceStack) ctx.getSource()).getPlayerOrException());
        ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_LEFT.asComponent(), false);
        return 0;
    }

    public static int partyInvite(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        if (!PartyUtils.isInParty(((CommandSourceStack) ctx.getSource()).getPlayerOrException())) {
            ((CommandSourceStack) ctx.getSource()).sendFailure(LangProvider.PARTY_NOT_IN.asComponent());
            return 1;
        } else {
            PartyUtils.inviteToParty(((CommandSourceStack) ctx.getSource()).getPlayerOrException(), player);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_INVITE.asComponent(player.m_5446_()), false);
            return 0;
        }
    }

    public static int partyUninvite(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = EntityArgument.getPlayer(ctx, "player");
        PartyUtils.uninviteToParty(((CommandSourceStack) ctx.getSource()).getPlayerOrException(), player);
        return 0;
    }

    public static int listParty(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (!PartyUtils.isInParty(((CommandSourceStack) ctx.getSource()).getPlayerOrException())) {
            ((CommandSourceStack) ctx.getSource()).sendFailure(LangProvider.PARTY_NOT_IN.asComponent());
            return 1;
        } else {
            List<String> memberNames = PartyUtils.getPartyMembers(((CommandSourceStack) ctx.getSource()).getPlayerOrException()).stream().map(s -> s.m_7755_().getString()).toList();
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_MEMBER_TOTAL.asComponent(memberNames.size()), false);
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_MEMBER_LIST.asComponent(memberNames), false);
            return 0;
        }
    }

    public static int partyAccept(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        UUID requestID = UuidArgument.getUuid(ctx, "requestID");
        PartyUtils.acceptInvite(((CommandSourceStack) ctx.getSource()).getPlayerOrException(), requestID);
        return 0;
    }

    public static int partyDecline(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        UUID requestID = UuidArgument.getUuid(ctx, "requestID");
        if (PartyUtils.declineInvite(requestID)) {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_DECLINE.asComponent(), false);
            return 1;
        } else {
            ((CommandSourceStack) ctx.getSource()).sendSuccess(() -> LangProvider.PARTY_NO_INVITES.asComponent(), false);
            return 0;
        }
    }
}