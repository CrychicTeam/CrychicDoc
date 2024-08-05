package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovGenerator {

    private static final MarkovGenerator[] GENERATOR = new MarkovGenerator[10];

    public MarkovDictionary markov;

    public String name;

    public String symbol;

    public MarkovGenerator(int seqlen) {
    }

    public MarkovGenerator() {
        this(3);
    }

    public String fetch(int gender) {
        return this.stylize(this.markov.generateWord());
    }

    public String fetch() {
        return this.fetch(0);
    }

    public static String fetch(int dictionary, int gender) {
        return GENERATOR[dictionary].fetch(gender);
    }

    public String stylize(String str) {
        return str;
    }

    public String feminize(String element, boolean flag) {
        return element;
    }

    public static void load() {
        GENERATOR[0] = new MarkovRoman(3);
        GENERATOR[1] = new MarkovJapanese(4);
        GENERATOR[2] = new MarkovSlavic(3);
        GENERATOR[3] = new MarkovWelsh(3);
        GENERATOR[4] = new MarkovSaami(3);
        GENERATOR[5] = new MarkovOldNorse(4);
        GENERATOR[6] = new MarkovAncientGreek(3);
        GENERATOR[7] = new MarkovAztec(3);
        GENERATOR[8] = new MarkovCustomNPCsClassic(3);
        GENERATOR[9] = new MarkovSpanish(3);
    }
}