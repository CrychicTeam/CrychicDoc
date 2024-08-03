package net.liopyu.animationjs.utils;

import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.ModifierLayer;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.minecraftApi.PlayerAnimationAccess;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lio.playeranimatorapi.API.PlayerAnimAPIClient;
import lio.playeranimatorapi.data.PlayerAnimationData;
import lio.playeranimatorapi.modifier.CommonModifier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;

public class AnimationJSHelperClass {

    public static final Set<String> clientErrorMessagesLogged = new HashSet();

    public static final Set<String> clientWarningMessagesLogged = new HashSet();

    public static final Set<String> serverErrorMessagesLogged = new HashSet();

    public static final Set<String> serverWarningMessagesLogged = new HashSet();

    public static void logServerErrorMessageOnce(String errorMessage) {
        if (!serverErrorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.SERVER.error(errorMessage);
            serverErrorMessagesLogged.add(errorMessage);
        }
    }

    public static void logClientErrorMessageOnce(String errorMessage) {
        if (!clientErrorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.CLIENT.error(errorMessage);
            clientErrorMessagesLogged.add(errorMessage);
        }
    }

    public static void logServerWarningMessageOnce(String errorMessage) {
        if (!serverWarningMessagesLogged.contains(errorMessage)) {
            ConsoleJS.CLIENT.warn(errorMessage);
            serverWarningMessagesLogged.add(errorMessage);
        }
    }

    public static void logClientWarningMessageOnce(String errorMessage) {
        if (!clientWarningMessagesLogged.contains(errorMessage)) {
            ConsoleJS.CLIENT.warn(errorMessage);
            clientWarningMessagesLogged.add(errorMessage);
        }
    }

    public static void logServerErrorMessageOnceCatchable(String errorMessage, Throwable e) {
        if (!serverErrorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.CLIENT.error(errorMessage, e);
            serverErrorMessagesLogged.add(errorMessage);
        }
    }

    public static void logClientErrorMessageOnceCatchable(String errorMessage, Throwable e) {
        if (!clientErrorMessagesLogged.contains(errorMessage)) {
            ConsoleJS.CLIENT.error(errorMessage, e);
            clientErrorMessagesLogged.add(errorMessage);
        }
    }

    public static Object convertObjectToDesired(Object input, String outputType) {
        String var2 = outputType.toLowerCase();
        return switch(var2) {
            case "resourcelocation" ->
                convertToResourceLocation(input);
            case "ease" ->
                easeFromString(input);
            case "modifierlist" ->
                modifierList(input);
            case "integer" ->
                convertToInteger(input);
            case "double" ->
                convertToDouble(input);
            case "float" ->
                convertToFloat(input);
            case "itemstack" ->
                getStack(input);
            default ->
                input;
        };
    }

    public static ItemStack getStack(Object input) {
        if (input instanceof String) {
            return ((Item) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue(new ResourceLocation((String) input)))).getDefaultInstance();
        } else if (input instanceof ResourceLocation) {
            return ((Item) Objects.requireNonNull(ForgeRegistries.ITEMS.getValue((ResourceLocation) input))).getDefaultInstance();
        } else if (input instanceof ItemStack) {
            return (ItemStack) input;
        } else {
            return input instanceof Item item ? item.getDefaultInstance() : null;
        }
    }

    public static Integer convertToInteger(Object input) {
        if (input instanceof Integer) {
            return (Integer) input;
        } else {
            return !(input instanceof Double) && !(input instanceof Float) ? null : ((Number) input).intValue();
        }
    }

    public static Double convertToDouble(Object input) {
        if (input instanceof Double) {
            return (Double) input;
        } else {
            return !(input instanceof Integer) && !(input instanceof Float) ? null : ((Number) input).doubleValue();
        }
    }

    public static Float convertToFloat(Object input) {
        if (input instanceof Float) {
            return (Float) input;
        } else {
            return !(input instanceof Integer) && !(input instanceof Double) ? null : ((Number) input).floatValue();
        }
    }

    public static Ease easeFromString(Object functionName) {
        if (functionName instanceof String s) {
            String var3 = s.toUpperCase();
            return switch(var3) {
                case "LINEAR" ->
                    Ease.LINEAR;
                case "CONSTANT" ->
                    Ease.CONSTANT;
                case "INSINE" ->
                    Ease.INSINE;
                case "OUTSINE" ->
                    Ease.OUTSINE;
                case "INOUTSINE" ->
                    Ease.INOUTSINE;
                case "INCUBIC" ->
                    Ease.INCUBIC;
                case "OUTCUBIC" ->
                    Ease.OUTCUBIC;
                case "INOUTCUBIC" ->
                    Ease.INOUTCUBIC;
                case "INQUAD" ->
                    Ease.INQUAD;
                case "OUTQUAD" ->
                    Ease.OUTQUAD;
                case "INOUTQUAD" ->
                    Ease.INOUTQUAD;
                case "INQUART" ->
                    Ease.INQUART;
                case "OUTQUART" ->
                    Ease.OUTQUART;
                case "INOUTQUART" ->
                    Ease.INOUTQUART;
                case "INQUINT" ->
                    Ease.INQUINT;
                case "OUTQUINT" ->
                    Ease.OUTQUINT;
                case "INOUTQUINT" ->
                    Ease.INOUTQUINT;
                case "INEXPO" ->
                    Ease.INEXPO;
                case "OUTEXPO" ->
                    Ease.OUTEXPO;
                case "INOUTEXPO" ->
                    Ease.INOUTEXPO;
                case "INCIRC" ->
                    Ease.INCIRC;
                case "OUTCIRC" ->
                    Ease.OUTCIRC;
                case "INOUTCIRC" ->
                    Ease.INOUTCIRC;
                case "INBACK" ->
                    Ease.INBACK;
                case "OUTBACK" ->
                    Ease.OUTBACK;
                case "INOUTBACK" ->
                    Ease.INOUTBACK;
                case "INELASTIC" ->
                    Ease.INELASTIC;
                case "OUTELASTIC" ->
                    Ease.OUTELASTIC;
                case "INOUTELASTIC" ->
                    Ease.INOUTELASTIC;
                case "INBOUNCE" ->
                    Ease.INBOUNCE;
                case "OUTBOUNCE" ->
                    Ease.OUTBOUNCE;
                case "INOUTBOUNCE" ->
                    Ease.INOUTBOUNCE;
                default ->
                    {
                        ConsoleJS.SERVER.error("[AnimationJS]: Unknown Easing type, defaulting to \"LINEAR\"");
                        ???;
                    }
            };
        } else {
            return functionName instanceof Ease ? (Ease) functionName : null;
        }
    }

    public static List<CommonModifier> modifierList(Object input) {
        if (input instanceof List<?> array) {
            List<CommonModifier> list = new ArrayList();
            for (Object obj : array) {
                if (obj instanceof String string) {
                    list.add(new CommonModifier(new ResourceLocation(string), null));
                }
            }
            return list;
        } else {
            return null;
        }
    }

    private static ResourceLocation convertToResourceLocation(Object input) {
        if (input instanceof ResourceLocation) {
            return (ResourceLocation) input;
        } else {
            return input instanceof String ? new ResourceLocation((String) input) : null;
        }
    }

    public static AbstractClientPlayer getClientPlayerByUUID(UUID playerUUID) {
        if (FMLEnvironment.dist.isDedicatedServer()) {
            return null;
        } else {
            Minecraft minecraft = Minecraft.getInstance();
            if (minecraft != null && minecraft.level != null) {
                ClientLevel world = minecraft.level;
                for (AbstractClientPlayer player : world.players()) {
                    if (player.m_20148_().equals(playerUUID)) {
                        return player;
                    }
                }
                return null;
            } else {
                return null;
            }
        }
    }

    public static Player getPlayerByUUID(UUID playerUUID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getPlayerList().getPlayer(playerUUID);
    }

    public static ServerPlayer getServerPlayerByUUID(UUID playerUUID) {
        MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
        return server == null ? null : server.getPlayerList().getPlayer(playerUUID);
    }

    public static ModifierLayer<IAnimation> getanimation(AbstractClientPlayer player) {
        return (ModifierLayer<IAnimation>) PlayerAnimationAccess.getPlayerAssociatedData(player).get(new ResourceLocation("liosplayeranimatorapi", "factory"));
    }

    public static void playClientAnimation(AbstractClientPlayer player, PlayerAnimationData data) {
        PlayerAnimAPIClient.playPlayerAnim(player, data);
    }

    public static void playClientAnimation(AbstractClientPlayer player, ResourceLocation rl) {
        PlayerAnimAPIClient.playPlayerAnim(player, rl);
    }

    public static boolean isClientPlayer(Object player) {
        return player instanceof AbstractClientPlayer;
    }
}