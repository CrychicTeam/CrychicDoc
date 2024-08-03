package dev.kosmx.playerAnim.core.data.quarktool;

import dev.kosmx.playerAnim.core.util.Ease;

public class Reset implements Playable {

    private Playable[] parts;

    public Reset(QuarkReader reader, String all, int len) throws QuarkParsingError {
        if (all.equals("all")) {
            this.parts = new Playable[18];
            this.addParts(0, reader.head, len);
            this.addParts(3, reader.rightArm, len);
            this.addParts(6, reader.rightLeg, len);
            this.addParts(9, reader.leftArm, len);
            this.addParts(12, reader.leftLeg, len);
            this.addParts(15, reader.torso, len);
        } else {
            this.parts = new Playable[3];
            this.addParts(0, reader.getBPFromStr(all.split("_")), len);
        }
    }

    private void addParts(int i, PartMap part, int len) {
        this.parts[i] = new Move(part.x, 0.0F, len, Ease.INOUTQUAD);
        this.parts[i + 1] = new Move(part.y, 0.0F, len, Ease.INOUTQUAD);
        this.parts[i + 2] = new Move(part.z, 0.0F, len, Ease.INOUTQUAD);
    }

    @Override
    public int playForward(int time) throws QuarkParsingError {
        return 0;
    }

    @Override
    public int playBackward(int time) throws QuarkParsingError {
        return 0;
    }
}