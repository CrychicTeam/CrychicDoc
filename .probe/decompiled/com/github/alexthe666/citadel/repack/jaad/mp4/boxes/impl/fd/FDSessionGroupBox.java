package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.fd;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class FDSessionGroupBox extends FullBox {

    private long[][] groupIDs;

    private long[][] hintTrackIDs;

    public FDSessionGroupBox() {
        super("FD Session Group Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        int sessionGroups = (int) in.readBytes(2);
        this.groupIDs = new long[sessionGroups][];
        this.hintTrackIDs = new long[sessionGroups][];
        for (int i = 0; i < sessionGroups; i++) {
            int entryCount = in.read();
            this.groupIDs[i] = new long[entryCount];
            for (int j = 0; j < entryCount; j++) {
                this.groupIDs[i][j] = in.readBytes(4);
            }
            int channelsInSessionGroup = (int) in.readBytes(2);
            this.hintTrackIDs[i] = new long[channelsInSessionGroup];
            for (int var7 = 0; var7 < channelsInSessionGroup; var7++) {
                this.hintTrackIDs[i][var7] = in.readBytes(4);
            }
        }
    }

    public long[][] getGroupIDs() {
        return this.groupIDs;
    }

    public long[][] getHintTrackIDs() {
        return this.hintTrackIDs;
    }
}