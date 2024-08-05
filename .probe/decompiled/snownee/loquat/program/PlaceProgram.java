package snownee.loquat.program;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import snownee.loquat.Loquat;
import snownee.loquat.LoquatRegistries;
import snownee.loquat.core.area.Area;
import snownee.loquat.util.LoquatDataLoader;

@FunctionalInterface
public interface PlaceProgram {

    LoquatDataLoader<PlaceProgram> LOADER = new LoquatDataLoader<>(Loquat.id("place_program"), "loquat_place_programs", PlaceProgram::parse);

    @Nullable
    static PlaceProgram parse(JsonElement json) {
        JsonObject jsonObject = json.getAsJsonObject();
        ResourceLocation type = ResourceLocation.tryParse(GsonHelper.getAsString(jsonObject, "type"));
        return (PlaceProgram) LoquatRegistries.PLACE_PROGRAM.getOptional(type).map($ -> $.create(jsonObject)).orElse(null);
    }

    boolean place(Level var1, Area var2);

    public abstract static class Type<T extends PlaceProgram> {

        public abstract T create(JsonObject var1);
    }
}