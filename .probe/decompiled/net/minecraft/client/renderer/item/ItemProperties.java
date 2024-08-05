package net.minecraft.client.renderer.item;

import com.google.common.collect.Maps;
import java.util.Map;
import javax.annotation.Nullable;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.GlobalPos;
import net.minecraft.core.Holder;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.CompassItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ElytraItem;
import net.minecraft.world.item.FishingRodItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LightBlock;

public class ItemProperties {

    private static final Map<ResourceLocation, ItemPropertyFunction> GENERIC_PROPERTIES = Maps.newHashMap();

    private static final String TAG_CUSTOM_MODEL_DATA = "CustomModelData";

    private static final ResourceLocation DAMAGED = new ResourceLocation("damaged");

    private static final ResourceLocation DAMAGE = new ResourceLocation("damage");

    private static final ClampedItemPropertyFunction PROPERTY_DAMAGED = (p_174660_, p_174661_, p_174662_, p_174663_) -> p_174660_.isDamaged() ? 1.0F : 0.0F;

    private static final ClampedItemPropertyFunction PROPERTY_DAMAGE = (p_174655_, p_174656_, p_174657_, p_174658_) -> Mth.clamp((float) p_174655_.getDamageValue() / (float) p_174655_.getMaxDamage(), 0.0F, 1.0F);

    private static final Map<Item, Map<ResourceLocation, ItemPropertyFunction>> PROPERTIES = Maps.newHashMap();

    private static ClampedItemPropertyFunction registerGeneric(ResourceLocation resourceLocation0, ClampedItemPropertyFunction clampedItemPropertyFunction1) {
        GENERIC_PROPERTIES.put(resourceLocation0, clampedItemPropertyFunction1);
        return clampedItemPropertyFunction1;
    }

    private static void registerCustomModelData(ItemPropertyFunction itemPropertyFunction0) {
        GENERIC_PROPERTIES.put(new ResourceLocation("custom_model_data"), itemPropertyFunction0);
    }

    private static void register(Item item0, ResourceLocation resourceLocation1, ClampedItemPropertyFunction clampedItemPropertyFunction2) {
        ((Map) PROPERTIES.computeIfAbsent(item0, p_117828_ -> Maps.newHashMap())).put(resourceLocation1, clampedItemPropertyFunction2);
    }

    @Nullable
    public static ItemPropertyFunction getProperty(Item item0, ResourceLocation resourceLocation1) {
        if (item0.getMaxDamage() > 0) {
            if (DAMAGE.equals(resourceLocation1)) {
                return PROPERTY_DAMAGE;
            }
            if (DAMAGED.equals(resourceLocation1)) {
                return PROPERTY_DAMAGED;
            }
        }
        ItemPropertyFunction $$2 = (ItemPropertyFunction) GENERIC_PROPERTIES.get(resourceLocation1);
        if ($$2 != null) {
            return $$2;
        } else {
            Map<ResourceLocation, ItemPropertyFunction> $$3 = (Map<ResourceLocation, ItemPropertyFunction>) PROPERTIES.get(item0);
            return $$3 == null ? null : (ItemPropertyFunction) $$3.get(resourceLocation1);
        }
    }

    static {
        registerGeneric(new ResourceLocation("lefthanded"), (p_174650_, p_174651_, p_174652_, p_174653_) -> p_174652_ != null && p_174652_.getMainArm() != HumanoidArm.RIGHT ? 1.0F : 0.0F);
        registerGeneric(new ResourceLocation("cooldown"), (p_174645_, p_174646_, p_174647_, p_174648_) -> p_174647_ instanceof Player ? ((Player) p_174647_).getCooldowns().getCooldownPercent(p_174645_.getItem(), 0.0F) : 0.0F);
        ClampedItemPropertyFunction $$0 = (p_289244_, p_289245_, p_289246_, p_289247_) -> {
            if (!p_289244_.is(ItemTags.TRIMMABLE_ARMOR)) {
                return Float.NEGATIVE_INFINITY;
            } else {
                return p_289245_ == null ? 0.0F : (Float) ArmorTrim.getTrim(p_289245_.m_9598_(), p_289244_).map(ArmorTrim::m_266210_).map(Holder::m_203334_).map(TrimMaterial::f_265933_).orElse(0.0F);
            }
        };
        registerGeneric(ItemModelGenerators.TRIM_TYPE_PREDICATE_ID, $$0);
        registerCustomModelData((p_174640_, p_174641_, p_174642_, p_174643_) -> p_174640_.hasTag() ? (float) p_174640_.getTag().getInt("CustomModelData") : 0.0F);
        register(Items.BOW, new ResourceLocation("pull"), (p_174635_, p_174636_, p_174637_, p_174638_) -> {
            if (p_174637_ == null) {
                return 0.0F;
            } else {
                return p_174637_.getUseItem() != p_174635_ ? 0.0F : (float) (p_174635_.getUseDuration() - p_174637_.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        register(Items.BRUSH, new ResourceLocation("brushing"), (p_272332_, p_272333_, p_272334_, p_272335_) -> p_272334_ != null && p_272334_.getUseItem() == p_272332_ ? (float) (p_272334_.getUseItemRemainingTicks() % 10) / 10.0F : 0.0F);
        register(Items.BOW, new ResourceLocation("pulling"), (p_174630_, p_174631_, p_174632_, p_174633_) -> p_174632_ != null && p_174632_.isUsingItem() && p_174632_.getUseItem() == p_174630_ ? 1.0F : 0.0F);
        register(Items.BUNDLE, new ResourceLocation("filled"), (p_174625_, p_174626_, p_174627_, p_174628_) -> BundleItem.getFullnessDisplay(p_174625_));
        register(Items.CLOCK, new ResourceLocation("time"), new ClampedItemPropertyFunction() {

            private double rotation;

            private double rota;

            private long lastUpdateTick;

            @Override
            public float unclampedCall(ItemStack p_174665_, @Nullable ClientLevel p_174666_, @Nullable LivingEntity p_174667_, int p_174668_) {
                Entity $$4 = (Entity) (p_174667_ != null ? p_174667_ : p_174665_.getEntityRepresentation());
                if ($$4 == null) {
                    return 0.0F;
                } else {
                    if (p_174666_ == null && $$4.level() instanceof ClientLevel) {
                        p_174666_ = (ClientLevel) $$4.level();
                    }
                    if (p_174666_ == null) {
                        return 0.0F;
                    } else {
                        double $$5;
                        if (p_174666_.m_6042_().natural()) {
                            $$5 = (double) p_174666_.m_46942_(1.0F);
                        } else {
                            $$5 = Math.random();
                        }
                        $$5 = this.wobble(p_174666_, $$5);
                        return (float) $$5;
                    }
                }
            }

            private double wobble(Level p_117904_, double p_117905_) {
                if (p_117904_.getGameTime() != this.lastUpdateTick) {
                    this.lastUpdateTick = p_117904_.getGameTime();
                    double $$2 = p_117905_ - this.rotation;
                    $$2 = Mth.positiveModulo($$2 + 0.5, 1.0) - 0.5;
                    this.rota += $$2 * 0.1;
                    this.rota *= 0.9;
                    this.rotation = Mth.positiveModulo(this.rotation + this.rota, 1.0);
                }
                return this.rotation;
            }
        });
        register(Items.COMPASS, new ResourceLocation("angle"), new CompassItemPropertyFunction((p_234992_, p_234993_, p_234994_) -> CompassItem.isLodestoneCompass(p_234993_) ? CompassItem.getLodestonePosition(p_234993_.getOrCreateTag()) : CompassItem.getSpawnPosition(p_234992_)));
        register(Items.RECOVERY_COMPASS, new ResourceLocation("angle"), new CompassItemPropertyFunction((p_234983_, p_234984_, p_234985_) -> p_234985_ instanceof Player $$3 ? (GlobalPos) $$3.getLastDeathLocation().orElse(null) : null));
        register(Items.CROSSBOW, new ResourceLocation("pull"), (p_174610_, p_174611_, p_174612_, p_174613_) -> {
            if (p_174612_ == null) {
                return 0.0F;
            } else {
                return CrossbowItem.isCharged(p_174610_) ? 0.0F : (float) (p_174610_.getUseDuration() - p_174612_.getUseItemRemainingTicks()) / (float) CrossbowItem.getChargeDuration(p_174610_);
            }
        });
        register(Items.CROSSBOW, new ResourceLocation("pulling"), (p_174605_, p_174606_, p_174607_, p_174608_) -> p_174607_ != null && p_174607_.isUsingItem() && p_174607_.getUseItem() == p_174605_ && !CrossbowItem.isCharged(p_174605_) ? 1.0F : 0.0F);
        register(Items.CROSSBOW, new ResourceLocation("charged"), (p_275891_, p_275892_, p_275893_, p_275894_) -> CrossbowItem.isCharged(p_275891_) ? 1.0F : 0.0F);
        register(Items.CROSSBOW, new ResourceLocation("firework"), (p_275887_, p_275888_, p_275889_, p_275890_) -> CrossbowItem.isCharged(p_275887_) && CrossbowItem.containsChargedProjectile(p_275887_, Items.FIREWORK_ROCKET) ? 1.0F : 0.0F);
        register(Items.ELYTRA, new ResourceLocation("broken"), (p_174590_, p_174591_, p_174592_, p_174593_) -> ElytraItem.isFlyEnabled(p_174590_) ? 0.0F : 1.0F);
        register(Items.FISHING_ROD, new ResourceLocation("cast"), (p_174585_, p_174586_, p_174587_, p_174588_) -> {
            if (p_174587_ == null) {
                return 0.0F;
            } else {
                boolean $$4 = p_174587_.getMainHandItem() == p_174585_;
                boolean $$5 = p_174587_.getOffhandItem() == p_174585_;
                if (p_174587_.getMainHandItem().getItem() instanceof FishingRodItem) {
                    $$5 = false;
                }
                return ($$4 || $$5) && p_174587_ instanceof Player && ((Player) p_174587_).fishing != null ? 1.0F : 0.0F;
            }
        });
        register(Items.SHIELD, new ResourceLocation("blocking"), (p_174575_, p_174576_, p_174577_, p_174578_) -> p_174577_ != null && p_174577_.isUsingItem() && p_174577_.getUseItem() == p_174575_ ? 1.0F : 0.0F);
        register(Items.TRIDENT, new ResourceLocation("throwing"), (p_234996_, p_234997_, p_234998_, p_234999_) -> p_234998_ != null && p_234998_.isUsingItem() && p_234998_.getUseItem() == p_234996_ ? 1.0F : 0.0F);
        register(Items.LIGHT, new ResourceLocation("level"), (p_234987_, p_234988_, p_234989_, p_234990_) -> {
            CompoundTag $$4 = p_234987_.getTagElement("BlockStateTag");
            try {
                if ($$4 != null) {
                    Tag $$5 = $$4.get(LightBlock.LEVEL.m_61708_());
                    if ($$5 != null) {
                        return (float) Integer.parseInt($$5.getAsString()) / 16.0F;
                    }
                }
            } catch (NumberFormatException var6) {
            }
            return 1.0F;
        });
        register(Items.GOAT_HORN, new ResourceLocation("tooting"), (p_234978_, p_234979_, p_234980_, p_234981_) -> p_234980_ != null && p_234980_.isUsingItem() && p_234980_.getUseItem() == p_234978_ ? 1.0F : 0.0F);
    }
}