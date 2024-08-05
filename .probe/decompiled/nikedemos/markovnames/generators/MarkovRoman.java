package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovRoman extends MarkovGenerator {

    public MarkovDictionary markov2;

    public MarkovDictionary markov3;

    public MarkovRoman(int seqlen) {
        this.markov = new MarkovDictionary("roman_praenomina.txt", seqlen);
        this.markov2 = new MarkovDictionary("roman_nomina.txt", seqlen);
        this.markov3 = new MarkovDictionary("roman_cognomina.txt", seqlen);
    }

    public MarkovRoman() {
        this(3);
    }

    @Override
    public String feminize(String element, boolean flag) {
        if (element.endsWith("us")) {
            element = element.substring(0, element.length() - 2) + "a";
        } else if (element.endsWith("o")) {
            element = element.substring(0, element.length() - 2) + "a";
        }
        return element;
    }

    @Override
    public String fetch(int gender) {
        String seq1 = this.markov.generateWord();
        String seq2 = this.markov2.generateWord();
        String seq3 = this.markov3.generateWord();
        if (gender == 0) {
            gender = MarkovDictionary.rng.nextBoolean() ? 1 : 2;
        }
        if (gender == 2) {
            seq1 = this.feminize(seq1, false);
            seq2 = this.feminize(seq2, false);
            seq3 = this.feminize(seq3, true);
        }
        return seq1 + " " + seq2 + " " + seq3;
    }
}