package net.minecraft.client.renderer.texture;

import java.util.Collection;
import java.util.Locale;

public class StitcherException extends RuntimeException {

    private final Collection<Stitcher.Entry> allSprites;

    public StitcherException(Stitcher.Entry stitcherEntry0, Collection<Stitcher.Entry> collectionStitcherEntry1) {
        super(String.format(Locale.ROOT, "Unable to fit: %s - size: %dx%d - Maybe try a lower resolution resourcepack?", stitcherEntry0.name(), stitcherEntry0.width(), stitcherEntry0.height()));
        this.allSprites = collectionStitcherEntry1;
    }

    public Collection<Stitcher.Entry> getAllSprites() {
        return this.allSprites;
    }
}