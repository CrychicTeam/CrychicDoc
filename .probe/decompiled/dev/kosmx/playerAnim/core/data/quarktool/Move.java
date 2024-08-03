package dev.kosmx.playerAnim.core.data.quarktool;

import dev.kosmx.playerAnim.core.util.Ease;

public class Move implements Playable {

    private final Ease ease;

    private final float value;

    private float valueBefore;

    private float valueAfter;

    private boolean isInitialized = false;

    private PartMap.PartValue part;

    private final int length;

    public Move(PartMap.PartValue part, float value, int length, Ease ease) {
        this.ease = ease;
        this.length = length;
        this.value = value;
        this.part = part;
    }

    @Override
    public int playForward(int time) throws QuarkParsingError {
        if (!this.isInitialized) {
            this.isInitialized = true;
            this.valueBefore = this.part.getValue();
            this.valueAfter = this.value;
            this.part.addValue(time, time + this.length, this.valueAfter, this.ease);
        } else {
            this.part.hold();
            this.part.addValue(time, this.valueBefore, this.ease);
            this.part.addValue(time + this.length, this.valueAfter, Ease.CONSTANT);
        }
        return time + this.length;
    }

    @Override
    public int playBackward(int time) throws QuarkParsingError {
        if (!this.isInitialized) {
            throw new QuarkParsingError();
        } else {
            this.part.hold();
            this.part.addValue(time, this.valueAfter, InverseEase.inverse(this.ease));
            this.part.addValue(time + this.length, this.valueBefore, Ease.CONSTANT);
            return time + this.length;
        }
    }
}