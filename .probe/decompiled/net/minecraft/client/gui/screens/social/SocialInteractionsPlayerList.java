package net.minecraft.client.gui.screens.social;

import com.google.common.base.Strings;
import com.google.common.base.Suppliers;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.multiplayer.chat.ChatLog;
import net.minecraft.client.multiplayer.chat.LoggedChatEvent;
import net.minecraft.client.multiplayer.chat.LoggedChatMessage;

public class SocialInteractionsPlayerList extends ContainerObjectSelectionList<PlayerEntry> {

    private final SocialInteractionsScreen socialInteractionsScreen;

    private final List<PlayerEntry> players = Lists.newArrayList();

    @Nullable
    private String filter;

    public SocialInteractionsPlayerList(SocialInteractionsScreen socialInteractionsScreen0, Minecraft minecraft1, int int2, int int3, int int4, int int5, int int6) {
        super(minecraft1, int2, int3, int4, int5, int6);
        this.socialInteractionsScreen = socialInteractionsScreen0;
        this.m_93488_(false);
        this.m_93496_(false);
    }

    @Override
    protected void enableScissor(GuiGraphics guiGraphics0) {
        guiGraphics0.enableScissor(this.f_93393_, this.f_93390_ + 4, this.f_93392_, this.f_93391_);
    }

    public void updatePlayerList(Collection<UUID> collectionUUID0, double double1, boolean boolean2) {
        Map<UUID, PlayerEntry> $$3 = new HashMap();
        this.addOnlinePlayers(collectionUUID0, $$3);
        this.updatePlayersFromChatLog($$3, boolean2);
        this.updateFiltersAndScroll($$3.values(), double1);
    }

    private void addOnlinePlayers(Collection<UUID> collectionUUID0, Map<UUID, PlayerEntry> mapUUIDPlayerEntry1) {
        ClientPacketListener $$2 = this.f_93386_.player.connection;
        for (UUID $$3 : collectionUUID0) {
            PlayerInfo $$4 = $$2.getPlayerInfo($$3);
            if ($$4 != null) {
                boolean $$5 = $$4.hasVerifiableChat();
                mapUUIDPlayerEntry1.put($$3, new PlayerEntry(this.f_93386_, this.socialInteractionsScreen, $$3, $$4.getProfile().getName(), $$4::m_105337_, $$5));
            }
        }
    }

    private void updatePlayersFromChatLog(Map<UUID, PlayerEntry> mapUUIDPlayerEntry0, boolean boolean1) {
        for (GameProfile $$3 : collectProfilesFromChatLog(this.f_93386_.getReportingContext().chatLog())) {
            PlayerEntry $$4;
            if (boolean1) {
                $$4 = (PlayerEntry) mapUUIDPlayerEntry0.computeIfAbsent($$3.getId(), p_243147_ -> {
                    PlayerEntry $$2 = new PlayerEntry(this.f_93386_, this.socialInteractionsScreen, $$3.getId(), $$3.getName(), Suppliers.memoize(() -> this.f_93386_.getSkinManager().getInsecureSkinLocation($$3)), true);
                    $$2.setRemoved(true);
                    return $$2;
                });
            } else {
                $$4 = (PlayerEntry) mapUUIDPlayerEntry0.get($$3.getId());
                if ($$4 == null) {
                    continue;
                }
            }
            $$4.setHasRecentMessages(true);
        }
    }

    private static Collection<GameProfile> collectProfilesFromChatLog(ChatLog chatLog0) {
        Set<GameProfile> $$1 = new ObjectLinkedOpenHashSet();
        for (int $$2 = chatLog0.end(); $$2 >= chatLog0.start(); $$2--) {
            LoggedChatEvent $$3 = chatLog0.lookup($$2);
            if ($$3 instanceof LoggedChatMessage.Player) {
                LoggedChatMessage.Player $$4 = (LoggedChatMessage.Player) $$3;
                if ($$4.message().hasSignature()) {
                    $$1.add($$4.profile());
                }
            }
        }
        return $$1;
    }

    private void sortPlayerEntries() {
        this.players.sort(Comparator.comparing(p_240744_ -> {
            if (p_240744_.getPlayerId().equals(this.f_93386_.getUser().getProfileId())) {
                return 0;
            } else if (p_240744_.getPlayerId().version() == 2) {
                return 4;
            } else if (this.f_93386_.getReportingContext().hasDraftReportFor(p_240744_.getPlayerId())) {
                return 1;
            } else {
                return p_240744_.hasRecentMessages() ? 2 : 3;
            }
        }).thenComparing(p_240745_ -> {
            if (!p_240745_.getPlayerName().isBlank()) {
                int $$1 = p_240745_.getPlayerName().codePointAt(0);
                if ($$1 == 95 || $$1 >= 97 && $$1 <= 122 || $$1 >= 65 && $$1 <= 90 || $$1 >= 48 && $$1 <= 57) {
                    return 0;
                }
            }
            return 1;
        }).thenComparing(PlayerEntry::m_100600_, String::compareToIgnoreCase));
    }

    private void updateFiltersAndScroll(Collection<PlayerEntry> collectionPlayerEntry0, double double1) {
        this.players.clear();
        this.players.addAll(collectionPlayerEntry0);
        this.sortPlayerEntries();
        this.updateFilteredPlayers();
        this.m_5988_(this.players);
        this.m_93410_(double1);
    }

    private void updateFilteredPlayers() {
        if (this.filter != null) {
            this.players.removeIf(p_100710_ -> !p_100710_.getPlayerName().toLowerCase(Locale.ROOT).contains(this.filter));
            this.m_5988_(this.players);
        }
    }

    public void setFilter(String string0) {
        this.filter = string0;
    }

    public boolean isEmpty() {
        return this.players.isEmpty();
    }

    public void addPlayer(PlayerInfo playerInfo0, SocialInteractionsScreen.Page socialInteractionsScreenPage1) {
        UUID $$2 = playerInfo0.getProfile().getId();
        for (PlayerEntry $$3 : this.players) {
            if ($$3.getPlayerId().equals($$2)) {
                $$3.setRemoved(false);
                return;
            }
        }
        if ((socialInteractionsScreenPage1 == SocialInteractionsScreen.Page.ALL || this.f_93386_.getPlayerSocialManager().shouldHideMessageFrom($$2)) && (Strings.isNullOrEmpty(this.filter) || playerInfo0.getProfile().getName().toLowerCase(Locale.ROOT).contains(this.filter))) {
            boolean $$4 = playerInfo0.hasVerifiableChat();
            PlayerEntry $$5 = new PlayerEntry(this.f_93386_, this.socialInteractionsScreen, playerInfo0.getProfile().getId(), playerInfo0.getProfile().getName(), playerInfo0::m_105337_, $$4);
            this.m_7085_($$5);
            this.players.add($$5);
        }
    }

    public void removePlayer(UUID uUID0) {
        for (PlayerEntry $$1 : this.players) {
            if ($$1.getPlayerId().equals(uUID0)) {
                $$1.setRemoved(true);
                return;
            }
        }
    }
}