package harmonised.pmmo.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import java.util.List;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@FunctionalInterface
@ZenRegister
@Name("mods.pmmo.CTDescriptionFunction")
@Document("mods/PMMO/CTDescriptionFunction")
public interface CTDescriptionFunction {

    @Method
    List<LiteralContents> apply(Player var1, MapData var2);
}