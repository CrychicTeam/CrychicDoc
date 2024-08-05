package journeymap.client.api.display;

public interface Context {

    public static enum MapType implements Context {

        Any,
        Day,
        Night,
        Underground,
        Topo,
        Biome
    }

    public static enum UI implements Context {

        Any, Fullscreen, Minimap, Webmap
    }
}