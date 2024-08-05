package malte0811.ferritecore.ducks;

public interface SmallThreadDetectable {

    byte UNLOCKED = 0;

    byte LOCKED = 1;

    byte CRASHING = 2;

    byte ferritecore$getState();

    void ferritecore$setState(byte var1);
}