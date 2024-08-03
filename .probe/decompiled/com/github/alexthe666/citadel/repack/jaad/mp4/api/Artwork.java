package com.github.alexthe666.citadel.repack.jaad.mp4.api;

import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta.ITunesMetadataBox;
import java.awt.Image;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

public class Artwork {

    private Artwork.Type type;

    private byte[] data;

    private Image image;

    Artwork(Artwork.Type type, byte[] data) {
        this.type = type;
        this.data = data;
    }

    public Artwork.Type getType() {
        return this.type;
    }

    public byte[] getData() {
        return this.data;
    }

    public Image getImage() throws IOException {
        try {
            if (this.image == null) {
                this.image = ImageIO.read(new ByteArrayInputStream(this.data));
            }
            return this.image;
        } catch (IOException var2) {
            Logger.getLogger("MP4 API").log(Level.SEVERE, "Artwork.getImage failed: {0}", var2.toString());
            throw var2;
        }
    }

    public static enum Type {

        GIF, JPEG, PNG, BMP;

        static Artwork.Type forDataType(ITunesMetadataBox.DataType dataType) {
            return switch(dataType) {
                case GIF ->
                    GIF;
                case JPEG ->
                    JPEG;
                case PNG ->
                    PNG;
                case BMP ->
                    BMP;
                default ->
                    null;
            };
        }
    }
}