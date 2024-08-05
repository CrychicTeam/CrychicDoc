package dev.kosmx.playerAnim.core.data.quarktool;

import dev.kosmx.playerAnim.core.data.AnimationFormat;
import dev.kosmx.playerAnim.core.data.KeyframeAnimation;
import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public class QuarkReader {

    private final KeyframeAnimation.AnimationBuilder emote = new KeyframeAnimation.AnimationBuilder(AnimationFormat.QUARK);

    private boolean isSuccess = false;

    private String name;

    final PartMap head = new PartMap(this.emote.head);

    final PartMap torso = new PartMap(this.emote.body);

    final PartMap rightLeg = new PartMap(this.emote.rightLeg);

    final PartMap leftLeg = new PartMap(this.emote.leftLeg);

    final PartMap rightArm = new PartMap(this.emote.rightArm);

    final PartMap leftArm = new PartMap(this.emote.leftArm);

    private Playable animation;

    public void deserialize(BufferedReader reader, String name) throws QuarkParsingError {
        this.name = name;
        List<List<String>> strings = new ArrayList();
        Stream<String> stream = reader.lines();
        stream.forEach(s -> strings.add(read(s.replaceAll("\t", ""))));
        int i = 0;
        while (i < strings.size()) {
            if (((List) strings.get(i)).size() != 0 && ((String) ((List) strings.get(i)).get(0)).charAt(0) != '#') {
                i = this.getMethod((List<String>) strings.get(i), i, strings);
            } else {
                i++;
            }
        }
        if (this.animation == null) {
            throw new QuarkParsingError();
        } else {
            int length = this.animation.playForward(0);
            this.isSuccess = true;
            this.emote.endTick = length;
        }
    }

    public KeyframeAnimation getEmote() {
        return this.isSuccess ? this.emote.setName("{\"color\":\"white\",\"text\":\"" + this.name + "\"}").setDescription("{\"color\":\"gray\",\"text\":\"Imported from quark\"}").build() : null;
    }

    public static List<String> read(String s) {
        int i = 0;
        while (i < s.length() && s.charAt(i) == ' ') {
            i++;
        }
        s = new StringBuffer(s).replace(0, i, "").toString();
        List<String> list = new ArrayList(Arrays.asList(s.split(" ")));
        list.removeIf(s1 -> s1.equals(""));
        return list;
    }

    public int getMethod(List<String> str, int i, List<List<String>> strings) throws QuarkParsingError {
        if (((String) str.get(0)).equals("name")) {
            this.name = (String) str.get(1);
        } else if (((String) str.get(0)).equals("animation")) {
            Section anim = new Section(this, i, strings);
            if (anim.getMoveOperator() == null) {
                this.animation = anim;
            } else {
                this.animation = anim.getMoveOperator();
            }
            return anim.getLine();
        }
        return i + 1;
    }

    public PartMap getBPFromStr(String[] inf) throws QuarkParsingError {
        if (inf.length == 2) {
            if (inf[0].equals("body")) {
                return this.torso;
            } else if (inf[0].equals("head")) {
                return this.head;
            } else {
                throw new QuarkParsingError();
            }
        } else if (inf.length == 3) {
            if (inf[0].equals("right")) {
                if (inf[1].equals("arm")) {
                    return this.rightArm;
                } else if (inf[1].equals("leg")) {
                    return this.rightLeg;
                } else {
                    throw new QuarkParsingError();
                }
            } else if (inf[0].equals("left")) {
                if (inf[1].equals("arm")) {
                    return this.leftArm;
                } else if (inf[1].equals("leg")) {
                    return this.leftLeg;
                } else {
                    throw new QuarkParsingError();
                }
            } else {
                throw new QuarkParsingError();
            }
        } else {
            throw new QuarkParsingError();
        }
    }

    public PartMap.PartValue getPFromStr(String str) throws QuarkParsingError {
        String[] inf = str.split("_");
        return this.getPFromStrHelper(inf[inf.length - 1], this.getBPFromStr(inf));
    }

    private PartMap.PartValue getPFromStrHelper(String string, PartMap part) throws QuarkParsingError {
        if (string.equals("x")) {
            return part.x;
        } else if (string.equals("y")) {
            return part.y;
        } else if (string.equals("z")) {
            return part.z;
        } else {
            throw new QuarkParsingError();
        }
    }
}