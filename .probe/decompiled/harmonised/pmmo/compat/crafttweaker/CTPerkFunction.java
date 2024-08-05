package harmonised.pmmo.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker.api.data.MapData;
import com.blamejared.crafttweaker_annotations.annotations.Document;
import net.minecraft.world.entity.player.Player;
import org.openzen.zencode.java.ZenCodeType.Method;
import org.openzen.zencode.java.ZenCodeType.Name;

@FunctionalInterface
@ZenRegister
@Name("mods.pmmo.CTPerkFunction")
@Document("mods/PMMO/CTPerkFunction")
public interface CTPerkFunction {

    @Method
    MapData apply(Player var1, MapData var2);
}