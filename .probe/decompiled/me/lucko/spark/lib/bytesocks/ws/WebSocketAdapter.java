package me.lucko.spark.lib.bytesocks.ws;

import me.lucko.spark.lib.bytesocks.ws.drafts.Draft;
import me.lucko.spark.lib.bytesocks.ws.exceptions.InvalidDataException;
import me.lucko.spark.lib.bytesocks.ws.framing.Framedata;
import me.lucko.spark.lib.bytesocks.ws.framing.PingFrame;
import me.lucko.spark.lib.bytesocks.ws.framing.PongFrame;
import me.lucko.spark.lib.bytesocks.ws.handshake.ClientHandshake;
import me.lucko.spark.lib.bytesocks.ws.handshake.HandshakeImpl1Server;
import me.lucko.spark.lib.bytesocks.ws.handshake.ServerHandshake;
import me.lucko.spark.lib.bytesocks.ws.handshake.ServerHandshakeBuilder;

public abstract class WebSocketAdapter implements WebSocketListener {

    private PingFrame pingFrame;

    @Override
    public ServerHandshakeBuilder onWebsocketHandshakeReceivedAsServer(WebSocket conn, Draft draft, ClientHandshake request) throws InvalidDataException {
        return new HandshakeImpl1Server();
    }

    @Override
    public void onWebsocketHandshakeReceivedAsClient(WebSocket conn, ClientHandshake request, ServerHandshake response) throws InvalidDataException {
    }

    @Override
    public void onWebsocketHandshakeSentAsClient(WebSocket conn, ClientHandshake request) throws InvalidDataException {
    }

    @Override
    public void onWebsocketPing(WebSocket conn, Framedata f) {
        conn.sendFrame(new PongFrame((PingFrame) f));
    }

    @Override
    public void onWebsocketPong(WebSocket conn, Framedata f) {
    }

    @Override
    public PingFrame onPreparePing(WebSocket conn) {
        if (this.pingFrame == null) {
            this.pingFrame = new PingFrame();
        }
        return this.pingFrame;
    }
}