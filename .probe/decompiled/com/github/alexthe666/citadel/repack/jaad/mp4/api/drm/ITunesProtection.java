package com.github.alexthe666.citadel.repack.jaad.mp4.api.drm;

import com.github.alexthe666.citadel.repack.jaad.mp4.api.Protection;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Box;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.drm.FairPlayDataBox;

public class ITunesProtection extends Protection {

    private final String userID;

    private final String userName;

    private final String userKey;

    private final byte[] privateKey;

    private final byte[] initializationVector;

    public ITunesProtection(Box sinf) {
        super(sinf);
        Box schi = sinf.getChild(1935894633L);
        this.userID = new String(((FairPlayDataBox) schi.getChild(1970496882L)).getData());
        byte[] b = ((FairPlayDataBox) schi.getChild(1851878757L)).getData();
        int i = 0;
        while (b[i] != 0) {
            i++;
        }
        this.userName = new String(b, 0, i - 1);
        this.userKey = new String(((FairPlayDataBox) schi.getChild(1801812256L)).getData());
        this.privateKey = ((FairPlayDataBox) schi.getChild(1886546294L)).getData();
        this.initializationVector = ((FairPlayDataBox) schi.getChild(1769367926L)).getData();
    }

    @Override
    public Protection.Scheme getScheme() {
        return Protection.Scheme.ITUNES_FAIR_PLAY;
    }

    public String getUserID() {
        return this.userID;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getUserKey() {
        return this.userKey;
    }

    public byte[] getPrivateKey() {
        return this.privateKey;
    }

    public byte[] getInitializationVector() {
        return this.initializationVector;
    }
}