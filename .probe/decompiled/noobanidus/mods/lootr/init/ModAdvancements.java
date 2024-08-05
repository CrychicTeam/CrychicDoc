package noobanidus.mods.lootr.init;

import java.util.UUID;
import net.minecraft.resources.ResourceLocation;
import noobanidus.mods.lootr.advancement.GenericTrigger;

public class ModAdvancements {

    public static final ResourceLocation CHEST_LOCATION = new ResourceLocation("lootr", "chest_opened");

    public static final ResourceLocation BARREL_LOCATION = new ResourceLocation("lootr", "barrel_opened");

    public static final ResourceLocation CART_LOCATION = new ResourceLocation("lootr", "cart_opened");

    public static final ResourceLocation SHULKER_LOCATION = new ResourceLocation("lootr", "shulker_opened");

    public static final ResourceLocation ADVANCEMENT_LOCATION = new ResourceLocation("lootr", "advancement");

    public static final ResourceLocation SCORE_LOCATION = new ResourceLocation("lootr", "score");

    public static GenericTrigger<UUID> CHEST_PREDICATE = null;

    public static GenericTrigger<UUID> BARREL_PREDICATE = null;

    public static GenericTrigger<UUID> CART_PREDICATE = null;

    public static GenericTrigger<UUID> SHULKER_PREDICATE = null;

    public static GenericTrigger<Void> SCORE_PREDICATE = null;

    public static GenericTrigger<ResourceLocation> ADVANCEMENT_PREDICATE = null;

    public static void load() {
    }
}