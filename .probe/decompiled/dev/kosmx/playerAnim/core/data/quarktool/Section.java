package dev.kosmx.playerAnim.core.data.quarktool;

import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Easing;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class Section implements Playable {

    public boolean isForward = true;

    public final List<Playable> elements = new ArrayList();

    private final boolean isParallel;

    private int line;

    @Nullable
    private Playable moveOperator;

    public Section(QuarkReader animData, int line, List<List<String>> text) throws QuarkParsingError {
        this.line = line;
        this.isParallel = ((String) ((List) text.get(line)).get(1)).equals("parallel");
        while (true) {
            this.line++;
            if (this.line >= text.size()) {
                throw new QuarkParsingError();
            }
            if (((List) text.get(this.line)).size() != 0 && ((String) ((List) text.get(this.line)).get(0)).charAt(0) != '#') {
                String id = (String) ((List) text.get(this.line)).get(0);
                List<String> block = (List<String>) text.get(this.line);
                switch(id) {
                    case "end":
                        return;
                    case "section":
                        Section section = new Section(animData, this.line, text);
                        Playable moveOp = section.getMoveOperator();
                        if (moveOp == null) {
                            this.elements.add(section);
                        } else {
                            this.elements.add(moveOp);
                        }
                        this.line = section.getLine();
                        break;
                    case "move":
                        try {
                            int pos = block.size() - 3;
                            if (block.size() < 4) {
                                throw new QuarkParsingError();
                            }
                            Ease ease;
                            if (block.size() == 5) {
                                ease = Easing.easeFromString((String) block.get(4));
                            } else if (block.size() != 8 && (block.size() != 7 || !((String) block.get(5)).equals("pause"))) {
                                ease = Ease.INOUTQUAD;
                            } else {
                                ease = Easing.easeFromString((String) block.get(4));
                            }
                            Move move = new Move(animData.getPFromStr((String) block.get(1)), Float.parseFloat((String) block.get(3)), (int) ((double) Integer.parseInt((String) block.get(2)) * 0.02), ease);
                            String var13 = (String) block.get(pos);
                            switch(var13) {
                                case "repeat":
                                    this.elements.add(new Repeat(move, (int) ((double) Integer.parseInt((String) block.get(pos + 2)) * 0.02), Integer.parseInt((String) block.get(pos + 1))));
                                    continue;
                                case "yoyo":
                                    this.elements.add(new Yoyo(move, (int) ((double) Integer.parseInt((String) block.get(pos + 2)) * 0.02), Integer.parseInt((String) block.get(pos + 1))));
                                    continue;
                                case "pause":
                                    this.elements.add(new Pauseable(move, (int) ((double) Integer.parseInt((String) block.get(pos + 1)) * 0.02)));
                                    continue;
                                default:
                                    this.elements.add(move);
                                    continue;
                            }
                        } catch (NumberFormatException var18) {
                            throw new QuarkParsingError("While trying to add move, error has happened: " + var18.getMessage(), this.line);
                        }
                    case "repeat":
                        try {
                            this.setMoveOperator(new Repeat(this, (int) ((double) Integer.parseInt((String) block.get(2)) * 0.02), Integer.parseInt((String) block.get(1))));
                            break;
                        } catch (NumberFormatException var17) {
                            throw new QuarkParsingError("While trying to add repeat, error has happened: " + var17.getMessage(), this.line);
                        }
                    case "yoyo":
                        try {
                            this.setMoveOperator(new Yoyo(this, (int) ((double) Integer.parseInt((String) block.get(2)) * 0.02), Integer.parseInt((String) block.get(1))));
                            break;
                        } catch (NumberFormatException var16) {
                            throw new QuarkParsingError("While trying to add yoyo, error has happened: " + var16.getMessage(), this.line);
                        }
                    case "pause":
                        try {
                            this.elements.add(new Pause((int) ((double) Integer.parseInt((String) block.get(1)) * 0.02)));
                        } catch (NumberFormatException var15) {
                            throw new QuarkParsingError("While trying to add yoyo, error has happened: " + var15.getMessage(), this.line);
                        }
                    case "reset":
                        break;
                    default:
                        throw new QuarkParsingError();
                }
            }
        }
    }

    public void setMoveOperator(Playable object) throws QuarkParsingError {
        if (this.moveOperator != null) {
            throw new QuarkParsingError();
        } else {
            this.moveOperator = object;
        }
    }

    public int getLine() {
        return this.line;
    }

    private int playObject(Playable object, int time) throws QuarkParsingError {
        return this.isForward ? object.playForward(time) : object.playBackward(time);
    }

    private int play(int time) throws QuarkParsingError {
        return this.isParallel ? this.playParallel(time) : this.playSequel(time);
    }

    @Nullable
    public Playable getMoveOperator() {
        return this.moveOperator;
    }

    @Override
    public int playForward(int time) throws QuarkParsingError {
        this.isForward = true;
        return this.play(time);
    }

    @Override
    public int playBackward(int time) throws QuarkParsingError {
        this.isForward = false;
        return this.play(time);
    }

    private int playParallel(int time) throws QuarkParsingError {
        int length = time;
        for (Playable object : this.elements) {
            int t;
            if (this.isForward) {
                t = object.playForward(time);
            } else {
                t = object.playBackward(time);
            }
            if (t > length) {
                length = t;
            }
        }
        return length;
    }

    private int playSequel(int time) throws QuarkParsingError {
        int t = time;
        for (int i = this.isForward ? 0 : this.elements.size() - 1; i >= 0; i += this.isForward ? 1 : -1) {
            if (i >= this.elements.size()) {
                return t;
            }
            t = this.playObject((Playable) this.elements.get(i), t);
        }
        return t;
    }
}