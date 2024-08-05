package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovAztec extends MarkovGenerator {

    public MarkovAztec(int seqlen) {
        this.markov = new MarkovDictionary("aztec_given.txt", seqlen);
    }

    public MarkovAztec() {
        this(3);
    }

    @Override
    public String fetch(int gender) {
        return this.markov.generateWord();
    }
}