package net.mehvahdjukaar.amendments.common.network;

import net.mehvahdjukaar.moonlight.api.platform.network.ChannelHandler;
import net.mehvahdjukaar.moonlight.api.platform.network.NetworkDir;

public class ModNetwork {

    public static final ChannelHandler CHANNEL = ChannelHandler.builder("amendments").register(NetworkDir.PLAY_TO_SERVER, SyncLecternBookMessage.class, SyncLecternBookMessage::new).build();

    public static void init() {
    }
}