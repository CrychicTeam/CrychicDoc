package net.minecraft.util.datafix.fixes;

import com.mojang.datafixers.DataFixUtils;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.serialization.Dynamic;
import java.util.function.Function;
import java.util.stream.Stream;

public class PoiTypeRenameFix extends AbstractPoiSectionFix {

    private final Function<String, String> renamer;

    public PoiTypeRenameFix(Schema schema0, String string1, Function<String, String> functionStringString2) {
        super(schema0, string1);
        this.renamer = functionStringString2;
    }

    @Override
    protected <T> Stream<Dynamic<T>> processRecords(Stream<Dynamic<T>> streamDynamicT0) {
        return streamDynamicT0.map(p_216714_ -> p_216714_.update("type", p_216718_ -> (Dynamic) DataFixUtils.orElse(p_216718_.asString().map(this.renamer).map(p_216718_::createString).result(), p_216718_)));
    }
}