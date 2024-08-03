package com.github.alexthe666.citadel.repack.jaad.mp4.boxes.impl.meta;

import com.github.alexthe666.citadel.repack.jaad.mp4.MP4InputStream;
import com.github.alexthe666.citadel.repack.jaad.mp4.boxes.FullBox;
import java.io.IOException;

public class RequirementBox extends FullBox {

    private String requirement;

    public RequirementBox() {
        super("Requirement Box");
    }

    @Override
    public void decode(MP4InputStream in) throws IOException {
        super.decode(in);
        this.requirement = in.readString((int) this.getLeft(in));
    }

    public String getRequirement() {
        return this.requirement;
    }
}