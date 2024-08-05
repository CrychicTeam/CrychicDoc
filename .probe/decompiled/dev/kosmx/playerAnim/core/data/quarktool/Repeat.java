package dev.kosmx.playerAnim.core.data.quarktool;

public class Repeat implements Playable {

    protected final Playable playable;

    protected final int delay;

    protected int count;

    public Repeat(Playable parent, int delay, int count) throws QuarkParsingError {
        this.playable = parent;
        if (count >= 0 && count <= 128) {
            this.count = count;
            this.delay = delay;
        } else {
            throw new QuarkParsingError();
        }
    }

    @Override
    public int playForward(int time) throws QuarkParsingError {
        for (int i = 0; i <= this.count; i++) {
            time = this.playable.playForward(time);
            time += this.delay;
        }
        return time;
    }

    @Override
    public int playBackward(int time) throws QuarkParsingError {
        throw new QuarkParsingError();
    }
}