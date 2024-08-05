package dev.ftb.mods.ftbxmodcompat.ftbteams.kubejs;

import dev.ftb.mods.ftbteams.api.event.PlayerJoinedPartyTeamEvent;
import dev.ftb.mods.ftbteams.api.event.PlayerLeftPartyTeamEvent;
import dev.ftb.mods.ftbteams.api.event.TeamEvent;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;

public class FTBTeamsKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        TeamEvent.PLAYER_JOINED_PARTY.register(FTBTeamsKubeJSPlugin::onPlayerJoinedParty);
        TeamEvent.PLAYER_LEFT_PARTY.register(FTBTeamsKubeJSPlugin::onPlayerLeftParty);
    }

    @Override
    public void registerEvents() {
        FTBTeamsKubeJSEvents.EVENT_GROUP.register();
    }

    private static void onPlayerJoinedParty(PlayerJoinedPartyTeamEvent event) {
        FTBTeamsKubeJSEvents.PLAYER_JOINED_PARTY.post(ScriptType.SERVER, new PlayerTeamEventJS(event.getPlayer(), event.getTeam(), event.getPreviousTeam()));
    }

    private static void onPlayerLeftParty(PlayerLeftPartyTeamEvent event) {
        FTBTeamsKubeJSEvents.PLAYER_LEFT_PARTY.post(ScriptType.SERVER, new PlayerTeamEventJS(event.getPlayer(), event.getPlayerTeam(), event.getTeam()));
    }
}