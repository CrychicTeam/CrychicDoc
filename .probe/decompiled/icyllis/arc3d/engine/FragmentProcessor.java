package icyllis.arc3d.engine;

import javax.annotation.concurrent.Immutable;

@Immutable
public abstract class FragmentProcessor extends Processor {

    protected FragmentProcessor(int classID) {
        super(classID);
    }
}