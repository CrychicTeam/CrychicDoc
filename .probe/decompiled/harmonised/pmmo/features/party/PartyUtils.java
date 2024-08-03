package harmonised.pmmo.features.party;

import harmonised.pmmo.config.Config;
import harmonised.pmmo.setup.datagen.LangProvider;
import harmonised.pmmo.util.MsLoggy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

public class PartyUtils {

    private static final Map<UUID, Integer> playerToPartyMap = new HashMap();

    private static final Map<UUID, PartyUtils.Invite> invites = new HashMap();

    public static List<ServerPlayer> getPartyMembersInRange(ServerPlayer player) {
        List<ServerPlayer> inRange = new ArrayList();
        for (ServerPlayer member : getPartyMembers(player)) {
            if (player.m_20182_().distanceTo(member.m_20182_()) <= (double) Config.PARTY_RANGE.get().intValue()) {
                inRange.add(member);
            }
        }
        return inRange;
    }

    public static List<ServerPlayer> getPartyMembers(ServerPlayer player) {
        int partyID = (Integer) playerToPartyMap.getOrDefault(player.m_20148_(), -1);
        if (partyID == -1) {
            return List.of(player);
        } else {
            List<ServerPlayer> outList = new ArrayList();
            outList.add(player);
            for (Entry<UUID, Integer> member : playerToPartyMap.entrySet()) {
                if (!((UUID) member.getKey()).equals(player.m_20148_()) && (Integer) member.getValue() == partyID) {
                    ServerPlayer memberPlayer = player.m_20194_().getPlayerList().getPlayer((UUID) member.getKey());
                    if (memberPlayer != null) {
                        outList.add(memberPlayer);
                    }
                }
            }
            return outList;
        }
    }

    public static void inviteToParty(Player member, Player invitee) {
        UUID requestID = UUID.randomUUID();
        Style acceptStyle = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pmmo party accept " + requestID.toString())).withBold(true).withColor(ChatFormatting.GREEN).withUnderlined(true);
        MutableComponent accept = LangProvider.PARTY_ACCEPT.asComponent().withStyle(acceptStyle);
        Style declineStyle = Style.EMPTY.withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/pmmo party decline " + requestID.toString())).withBold(true).withColor(ChatFormatting.RED).withUnderlined(true);
        MutableComponent decline = LangProvider.PARTY_DECLINE_INVITE.asComponent().withStyle(declineStyle);
        invitee.m_213846_(LangProvider.PARTY_PLAYER_INVITED.asComponent(member.getDisplayName(), accept, decline));
        invites.put(requestID, new PartyUtils.Invite((Integer) playerToPartyMap.get(member.m_20148_()), invitee.m_20148_()));
    }

    public static void uninviteToParty(Player member, Player invitee) {
        int memberPartyID = (Integer) playerToPartyMap.getOrDefault(member.m_20148_(), -1);
        if (memberPartyID == -1) {
            member.m_213846_(LangProvider.PARTY_NOT_IN.asComponent());
        } else {
            UUID inviteToRemove = null;
            for (Entry<UUID, PartyUtils.Invite> invite : invites.entrySet()) {
                PartyUtils.Invite i = (PartyUtils.Invite) invite.getValue();
                if (i.partyID() == memberPartyID && i.player().equals(invitee.m_20148_())) {
                    inviteToRemove = (UUID) invite.getKey();
                    break;
                }
            }
            if (inviteToRemove != null) {
                invites.remove(inviteToRemove);
                member.m_213846_(LangProvider.PARTY_RESCIND_INVITE.asComponent(invitee.getDisplayName()));
            }
        }
    }

    public static void acceptInvite(Player invitee, UUID requestID) {
        if (invites.get(requestID) == null) {
            invitee.m_213846_(LangProvider.PARTY_NO_INVITES.asComponent());
        }
        PartyUtils.Invite invite = (PartyUtils.Invite) invites.get(requestID);
        if (invite.player().equals(invitee.m_20148_())) {
            playerToPartyMap.put(invitee.m_20148_(), invite.partyID());
            invites.remove(requestID);
            invitee.m_213846_(LangProvider.PARTY_JOINED.asComponent());
        }
    }

    public static boolean declineInvite(UUID requestID) {
        return invites.remove(requestID) != null;
    }

    public static void removeFromParty(Player player) {
        playerToPartyMap.remove(player.m_20148_());
    }

    public static void createParty(Player player) {
        playerToPartyMap.put(player.m_20148_(), getFreePartyID());
        MsLoggy.DEBUG.log(MsLoggy.LOG_CODE.FEATURE, MsLoggy.mapToString(playerToPartyMap));
    }

    public static boolean isInParty(Player player) {
        return MsLoggy.DEBUG.logAndReturn(playerToPartyMap.containsKey(player.m_20148_()), MsLoggy.LOG_CODE.FEATURE, "Is In Party: {}");
    }

    private static int getFreePartyID() {
        for (int id = 0; id < Integer.MAX_VALUE; id++) {
            if (!playerToPartyMap.values().contains(id)) {
                return id;
            }
        }
        return -1;
    }

    private static record Invite(int partyID, UUID player) {
    }
}