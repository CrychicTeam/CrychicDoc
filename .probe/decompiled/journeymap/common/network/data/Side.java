package journeymap.common.network.data;

public enum Side {

    CLIENT, SERVER;

    public Side opposite() {
        return CLIENT.equals(this) ? SERVER : CLIENT;
    }
}