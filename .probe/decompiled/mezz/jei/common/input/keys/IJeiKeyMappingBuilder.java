package mezz.jei.common.input.keys;

public interface IJeiKeyMappingBuilder {

    IJeiKeyMappingBuilder setContext(JeiKeyConflictContext var1);

    IJeiKeyMappingBuilder setModifier(JeiKeyModifier var1);

    IJeiKeyMappingInternal buildMouseLeft();

    IJeiKeyMappingInternal buildMouseRight();

    IJeiKeyMappingInternal buildMouseMiddle();

    IJeiKeyMappingInternal buildKeyboardKey(int var1);

    IJeiKeyMappingInternal buildUnbound();
}