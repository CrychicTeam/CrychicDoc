package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.Utils;
import java.io.IOException;

public class RatingBox extends FullBox {

    private String languageCode;

    private String rating;

    public RatingBox() {
        super("Rating Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        if (this.parent.getType() == 1969517665L) {
            super.decode(in);
            long entity = in.readBytes(4);
            long criteria = in.readBytes(4);
            this.languageCode = Utils.getLanguageCode(in.readBytes(2));
            byte[] b = in.readTerminated((int) this.getLeft(in), 0);
            this.rating = new String(b, "UTF-8");
        } else {
            this.readChildren(in);
        }
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public String getRating() {
        return this.rating;
    }
}