package se.mickelus.tetra.interactions;

public enum PerformSide {

    client(true, false), server(false, true), both(true, true);

    private boolean runClient;

    private boolean runServer;

    private PerformSide(boolean runClient, boolean runServer) {
        this.runClient = runClient;
        this.runServer = runServer;
    }

    public boolean runClient() {
        return this.runClient;
    }

    public boolean runServer() {
        return this.runServer;
    }
}