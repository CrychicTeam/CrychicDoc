package mezz.jei.common.input.keys;

public abstract class AbstractJeiKeyMappingBuilder implements IJeiKeyMappingBuilder {

    protected abstract IJeiKeyMappingInternal buildMouse(int var1);

    @Override
    public final IJeiKeyMappingInternal buildMouseLeft() {
        return this.buildMouse(0);
    }

    @Override
    public final IJeiKeyMappingInternal buildMouseRight() {
        return this.buildMouse(1);
    }

    @Override
    public final IJeiKeyMappingInternal buildMouseMiddle() {
        return this.buildMouse(2);
    }

    @Override
    public final IJeiKeyMappingInternal buildUnbound() {
        return this.buildKeyboardKey(-1);
    }
}