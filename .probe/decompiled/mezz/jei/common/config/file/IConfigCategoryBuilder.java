package mezz.jei.common.config.file;

import java.util.List;
import java.util.function.Supplier;
import mezz.jei.api.runtime.config.IJeiConfigValueSerializer;

public interface IConfigCategoryBuilder {

    Supplier<Boolean> addBoolean(String var1, boolean var2, String var3);

    Supplier<Integer> addInteger(String var1, int var2, int var3, int var4, String var5);

    <T extends Enum<T>> Supplier<T> addEnum(String var1, T var2, String var3);

    <T> Supplier<List<T>> addList(String var1, List<T> var2, IJeiConfigValueSerializer<List<T>> var3, String var4);
}