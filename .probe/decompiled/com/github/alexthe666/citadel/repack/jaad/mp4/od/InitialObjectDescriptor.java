package com.github.alexthe666.citadel.repack.jaad.mp4.od;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import java.io.IOException;

public class InitialObjectDescriptor extends Descriptor {

    private int objectDescriptorID;

    private boolean urlPresent;

    private boolean includeInlineProfiles;

    private String url;

    private int odProfile;

    private int sceneProfile;

    private int audioProfile;

    private int visualProfile;

    private int graphicsProfile;

    @Override
    void decode(MP4InputStream in) throws IOException {
        int x = (int) in.readBytes(2);
        this.objectDescriptorID = x >> 6 & 1023;
        this.urlPresent = (x >> 5 & 1) == 1;
        this.includeInlineProfiles = (x >> 4 & 1) == 1;
        if (this.urlPresent) {
            this.url = in.readString(this.size - 2);
        } else {
            this.odProfile = in.read();
            this.sceneProfile = in.read();
            this.audioProfile = in.read();
            this.visualProfile = in.read();
            this.graphicsProfile = in.read();
        }
        this.readChildren(in);
    }

    public int getObjectDescriptorID() {
        return this.objectDescriptorID;
    }

    public boolean includesInlineProfiles() {
        return this.includeInlineProfiles;
    }

    public boolean isURLPresent() {
        return this.urlPresent;
    }

    public String getURL() {
        return this.url;
    }

    public boolean areProfilesPresent() {
        return !this.urlPresent;
    }

    public int getODProfile() {
        return this.odProfile;
    }

    public int getSceneProfile() {
        return this.sceneProfile;
    }

    public int getAudioProfile() {
        return this.audioProfile;
    }

    public int getVisualProfile() {
        return this.visualProfile;
    }

    public int getGraphicsProfile() {
        return this.graphicsProfile;
    }
}