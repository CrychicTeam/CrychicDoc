package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovSaami extends MarkovGenerator {

    public MarkovDictionary markov2;

    public MarkovSaami(int seqlen) {
        this.markov = new MarkovDictionary("saami_bothgenders.txt", seqlen);
    }

    public MarkovSaami() {
        this(3);
    }

    @Override
    public String fetch(int gender) {
        return this.markov.generateWord();
    }
}