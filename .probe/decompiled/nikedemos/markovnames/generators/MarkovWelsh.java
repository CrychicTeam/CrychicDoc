package nikedemos.markovnames.generators;

import nikedemos.markovnames.MarkovDictionary;

public class MarkovWelsh extends MarkovGenerator {

    public MarkovDictionary markov2;

    public MarkovWelsh(int seqlen) {
        this.markov = new MarkovDictionary("welsh_male.txt", seqlen);
        this.markov2 = new MarkovDictionary("welsh_female.txt", seqlen);
    }

    public MarkovWelsh() {
        this(3);
    }

    @Override
    public String fetch(int gender) {
        if (gender == 0) {
            gender = MarkovDictionary.rng.nextBoolean() ? 1 : 2;
        }
        String seq1;
        if (gender == 2) {
            seq1 = this.markov2.generateWord();
        } else {
            seq1 = this.markov.generateWord();
        }
        return seq1;
    }
}