package icyllis.modernui.transition;

public class AutoTransition extends TransitionSet {

    public AutoTransition() {
        this.setOrdering(1);
        this.addTransition(new Fade(2)).addTransition(new Fade(1));
    }
}