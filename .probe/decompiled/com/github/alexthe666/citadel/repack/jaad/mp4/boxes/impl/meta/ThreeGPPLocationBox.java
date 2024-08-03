package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class ThreeGPPLocationBox extends ThreeGPPMetadataBox {

    private int role;

    private double longitude;

    private double latitude;

    private double altitude;

    private String placeName;

    private String astronomicalBody;

    private String additionalNotes;

    public ThreeGPPLocationBox() {
        super("3GPP Location Information Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        this.decodeCommon(in);
        this.placeName = in.readUTFString((int) this.getLeft(in));
        this.role = in.read();
        this.longitude = in.readFixedPoint(16, 16);
        this.latitude = in.readFixedPoint(16, 16);
        this.altitude = in.readFixedPoint(16, 16);
        this.astronomicalBody = in.readUTFString((int) this.getLeft(in));
        this.additionalNotes = in.readUTFString((int) this.getLeft(in));
    }

    public String getPlaceName() {
        return this.placeName;
    }

    public int getRole() {
        return this.role;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public double getLatitude() {
        return this.latitude;
    }

    public double getAltitude() {
        return this.altitude;
    }

    public String getAstronomicalBody() {
        return this.astronomicalBody;
    }

    public String getAdditionalNotes() {
        return this.additionalNotes;
    }
}