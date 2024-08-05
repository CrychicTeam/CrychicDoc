package harmonised.pmmo.compat.crafttweaker;

import com.blamejared.crafttweaker.api.annotation.ZenRegister;
import com.blamejared.crafttweaker_annotations.annotations.BracketEnum;
import com.blamejared.crafttweaker_annotations.annotations.NativeTypeRegistration;
import harmonised.pmmo.api.enums.EventType;
import harmonised.pmmo.api.enums.ModifierDataType;
import harmonised.pmmo.api.enums.ObjectType;
import harmonised.pmmo.api.enums.ReqType;
import java.util.Locale;
import org.openzen.zencode.java.ZenCodeType.Getter;
import org.openzen.zencode.java.ZenCodeType.Method;

@ZenRegister
public class ExtendedEnums {

    @NativeTypeRegistration(value = EventType.class, zenCodeName = "mods.pmmo.EventType")
    @BracketEnum("pmmo:eventtype")
    @ZenRegister
    public static class ExtendedEventType {

        @Method
        @Getter("commandString")
        public static String getCommandString(EventType internal) {
            return "<constant:pmmo:eventtype:" + internal.name().toLowerCase(Locale.ROOT) + ">";
        }
    }

    @NativeTypeRegistration(value = ModifierDataType.class, zenCodeName = "mods.pmmo.ModifierDataType")
    @BracketEnum("pmmo:modifierdatatype")
    @ZenRegister
    public static class ExtendedModifierDataType {

        @Method
        @Getter("commandString")
        public static String getCommandString(ModifierDataType internal) {
            return "<constant:pmmo:modifierdatatype:" + internal.name().toLowerCase(Locale.ROOT) + ">";
        }
    }

    @NativeTypeRegistration(value = ObjectType.class, zenCodeName = "mods.pmmo.ObjectType")
    @BracketEnum("pmmo:objecttype")
    @ZenRegister
    public static class ExtendedObjectType {

        @Method
        @Getter("commandString")
        public static String getCommandString(ObjectType internal) {
            return "<constant:pmmo:objecttype:" + internal.name().toLowerCase(Locale.ROOT) + ">";
        }
    }

    @NativeTypeRegistration(value = ReqType.class, zenCodeName = "mods.pmmo.ReqType")
    @BracketEnum("pmmo:reqtype")
    @ZenRegister
    public static class ExtendedReqType {

        @Method
        @Getter("commandString")
        public static String getCommandString(ReqType internal) {
            return "<constant:pmmo:reqtype:" + internal.name().toLowerCase(Locale.ROOT) + ">";
        }
    }
}