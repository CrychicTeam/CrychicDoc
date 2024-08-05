package se.mickelus.tetra.items.modular.impl.bow;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ObjectHolder;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.TetraMod;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.FocusEffect;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.event.ModularLooseProjectilesEvent;
import se.mickelus.tetra.event.ModularProjectileSpawnEvent;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.properties.TetraAttributes;

@ParametersAreNonnullByDefault
public class ModularBowItem extends ModularItem {

    public static final String staveKey = "bow/stave";

    public static final String stringKey = "bow/string";

    public static final String riserKey = "bow/riser";

    public static final String identifier = "modular_bow";

    public static final double velocityFactor = 0.125;

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(1, 21, -11, -3);

    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(-14, 23);

    @ObjectHolder(registryName = "item", value = "tetra:modular_bow")
    public static ModularBowItem instance;

    protected ModuleModel arrowModel0 = new ModuleModel("draw_0", new ResourceLocation("tetra", "item/module/bow/arrow_0"));

    protected ModuleModel arrowModel1 = new ModuleModel("draw_1", new ResourceLocation("tetra", "item/module/bow/arrow_1"));

    protected ModuleModel arrowModel2 = new ModuleModel("draw_2", new ResourceLocation("tetra", "item/module/bow/arrow_2"));

    protected ItemStack vanillaBow;

    public ModularBowItem() {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.majorModuleKeys = new String[] { "bow/string", "bow/stave" };
        this.minorModuleKeys = new String[] { "bow/riser" };
        this.requiredModules = new String[] { "bow/string", "bow/stave" };
        this.vanillaBow = new ItemStack(Items.BOW);
        this.updateConfig(ConfigHandler.honeBowBase.get(), ConfigHandler.honeBowIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_bow"));
    }

    public static float getArrowVelocity(int charge, double strength, float velocityBonus, boolean suspend) {
        float velocity = (float) charge / 20.0F;
        velocity = (velocity * velocity + velocity * 2.0F) / 3.0F;
        if (velocity > 1.0F) {
            velocity = 1.0F;
        }
        velocity *= (float) Math.max(1.0, 1.0 + (strength - 6.0) * 0.125);
        if (suspend && charge >= 20) {
            velocity *= 2.0F;
        } else {
            velocity += velocity * velocityBonus;
        }
        return velocity;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("bow/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public void clientInit() {
        super.clientInit();
        MinecraftForge.EVENT_BUS.register(new RangedFOVTransformer());
    }

    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack itemStack) {
        if (this.isBroken(itemStack)) {
            return AttributeHelper.emptyMap;
        } else if (slot == EquipmentSlot.MAINHAND) {
            return this.getAttributeModifiersCached(itemStack);
        } else {
            return slot == EquipmentSlot.OFFHAND ? (Multimap) this.getAttributeModifiersCached(itemStack).entries().stream().filter(entry -> !((Attribute) entry.getKey()).equals(Attributes.ATTACK_DAMAGE) && !((Attribute) entry.getKey()).equals(Attributes.ATTACK_DAMAGE)).collect(Multimaps.toMultimap(Entry::getKey, Entry::getValue, ArrayListMultimap::create)) : AttributeHelper.emptyMap;
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entity, int timeLeft) {
        if (this.getEffectLevel(itemStack, ItemEffect.overbowed) > 0 && timeLeft <= 0) {
            entity.stopUsingItem();
            CastOptional.cast(entity, Player.class).ifPresent(player -> player.getCooldowns().addCooldown(this, 10));
        } else {
            this.fireArrow(itemStack, world, entity, timeLeft);
        }
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level world, LivingEntity entity) {
        if (this.getEffectLevel(itemStack, ItemEffect.overbowed) > 0) {
            entity.stopUsingItem();
            CastOptional.cast(entity, Player.class).ifPresent(player -> player.getCooldowns().addCooldown(this, 10));
        }
        return super.m_5922_(itemStack, world, entity);
    }

    @Override
    public void onUseTick(Level level, LivingEntity entity, ItemStack itemStack, int count) {
        if (this.getEffectLevel(itemStack, ItemEffect.releaseLatch) > 0 && this.getProgress(itemStack, entity) >= 1.0F) {
            entity.releaseUsingItem();
        }
    }

    protected void fireArrow(ItemStack itemStack, Level world, LivingEntity entity, int timeLeft) {
        if (entity instanceof Player player) {
            ItemStack ammoStack = player.getProjectile(this.vanillaBow);
            boolean playerInfinite = this.isInfinite(player, itemStack, ammoStack);
            int drawProgress = Math.round(this.getProgress(itemStack, entity) * 20.0F);
            drawProgress = ForgeEventFactory.onArrowLoose(itemStack, world, player, drawProgress, !ammoStack.isEmpty() || playerInfinite);
            if (drawProgress < 0) {
                return;
            }
            if (!ammoStack.isEmpty() || playerInfinite) {
                if (ammoStack.isEmpty()) {
                    ammoStack = new ItemStack(Items.ARROW);
                }
                double strength = this.getAttributeValue(itemStack, TetraAttributes.drawStrength.get());
                float velocityBonus = (float) this.getEffectLevel(itemStack, ItemEffect.velocity) / 100.0F;
                int suspendLevel = this.getEffectLevel(itemStack, ItemEffect.suspend);
                ArrowItem ammoItem = (ArrowItem) CastOptional.cast(ammoStack.getItem(), ArrowItem.class).orElse((ArrowItem) Items.ARROW);
                boolean infiniteAmmo = player.getAbilities().instabuild || ammoItem.isInfinite(ammoStack, itemStack, player);
                ModularLooseProjectilesEvent looseProjectilesEvent = new ModularLooseProjectilesEvent(itemStack, ammoStack, player, world, drawProgress, this.getAttributeValue(itemStack, TetraAttributes.drawStrength.get()), suspendLevel > 0, getArrowVelocity(drawProgress, strength, (float) this.getEffectLevel(itemStack, ItemEffect.velocity) / 100.0F, suspendLevel > 0), (double) this.getEffectEfficiency(itemStack, ItemEffect.multishot), Math.max(0.0F, 100.0F - this.getEffectEfficiency(itemStack, ItemEffect.spread) - FocusEffect.getSpreadReduction(player, itemStack)), player.getAbilities().instabuild || ammoItem.isInfinite(ammoStack, itemStack, player), Mth.clamp(this.getEffectLevel(itemStack, ItemEffect.multishot), 1, infiniteAmmo ? 64 : ammoStack.getCount()), (double) player.m_146909_(), (double) player.m_146908_());
                MinecraftForge.EVENT_BUS.post(looseProjectilesEvent);
                ammoStack = looseProjectilesEvent.getAmmoStack();
                ImmutableList<Function<AbstractArrow, AbstractArrow>> projectileRemappers = looseProjectilesEvent.getProjectileRemappers();
                strength = looseProjectilesEvent.getStrength();
                boolean hasSuspend = looseProjectilesEvent.isHasSuspend();
                float projectileVelocity = looseProjectilesEvent.getProjectileVelocity();
                double multishotSpread = looseProjectilesEvent.getMultishotSpread();
                float accuracy = looseProjectilesEvent.getAccuracy();
                infiniteAmmo = looseProjectilesEvent.isInfiniteAmmo();
                int count = looseProjectilesEvent.getCount();
                double basePitch = looseProjectilesEvent.getBasePitch();
                double baseYaw = looseProjectilesEvent.getBaseYaw();
                if (projectileVelocity > 0.1F) {
                    if (!world.isClientSide) {
                        int powerLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.POWER_ARROWS, itemStack);
                        int punchLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PUNCH_ARROWS, itemStack);
                        int flameLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.FLAMING_ARROWS, itemStack);
                        int piercingLevel = this.getEffectLevel(itemStack, ItemEffect.piercing) + EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, itemStack);
                        for (int i = 0; i < count; i++) {
                            double yaw = baseYaw - multishotSpread * (double) (count - 1) / 2.0 + multishotSpread * (double) i;
                            fireProjectile(itemStack, world, (ArrowItem) ammoStack.getItem(), ammoStack, projectileRemappers, player, (float) basePitch, (float) yaw, projectileVelocity, accuracy, drawProgress, strength, powerLevel, punchLevel, flameLevel, piercingLevel, hasSuspend, infiniteAmmo);
                        }
                        this.applyDamage(1, itemStack, player);
                        this.applyNegativeUsageEffects(entity, itemStack, 1.0);
                        if (drawProgress > 15) {
                            this.applyPositiveUsageEffects(entity, itemStack, 1.0);
                        }
                    }
                    float pitchBase = projectileVelocity;
                    if (velocityBonus > 0.0F) {
                        pitchBase = projectileVelocity - projectileVelocity * velocityBonus;
                    } else if (hasSuspend) {
                        pitchBase = projectileVelocity / 2.0F;
                    }
                    world.playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 0.8F + projectileVelocity * 0.2F, 1.9F + world.random.nextFloat() * 0.2F - pitchBase * 0.8F);
                    if (!infiniteAmmo && !player.getAbilities().instabuild) {
                        ammoStack.shrink(count);
                        if (ammoStack.isEmpty()) {
                            player.getInventory().removeItem(ammoStack);
                        }
                    }
                    FocusEffect.onFireArrow(player, itemStack);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public static void fireProjectile(ItemStack itemStack, Level world, ArrowItem ammoItem, ItemStack ammoStack, ImmutableList<Function<AbstractArrow, AbstractArrow>> projectileRemappers, Player player, float basePitch, float yaw, float projectileVelocity, float accuracy, int drawProgress, double strength, int powerLevel, int punchLevel, int flameLevel, int piercingLevel, boolean hasSuspend, boolean infiniteAmmo) {
        AbstractArrow projectile = ammoItem.createArrow(world, ammoStack, player);
        UnmodifiableIterator event = projectileRemappers.iterator();
        while (event.hasNext()) {
            Function<AbstractArrow, AbstractArrow> remapper = (Function<AbstractArrow, AbstractArrow>) event.next();
            projectile = (AbstractArrow) remapper.apply(projectile);
        }
        projectile.m_37251_(player, basePitch, yaw, 0.0F, projectileVelocity * 3.0F, accuracy);
        if (drawProgress >= 20) {
            projectile.setCritArrow(true);
        }
        projectile.setBaseDamage(projectile.getBaseDamage() - 2.0 + strength / 3.0);
        if (powerLevel > 0) {
            projectile.setBaseDamage(projectile.getBaseDamage() + (double) powerLevel * 0.5 + 0.5);
        }
        if (projectileVelocity > 1.0F) {
            projectile.setBaseDamage(projectile.getBaseDamage() / (double) projectileVelocity);
        }
        if (punchLevel > 0) {
            projectile.setKnockback(punchLevel);
        }
        if (flameLevel > 0) {
            projectile.m_20254_(100);
        }
        if (piercingLevel > 0) {
            projectile.setPierceLevel((byte) piercingLevel);
        }
        if (hasSuspend && drawProgress >= 20) {
            projectile.m_20242_(true);
        }
        if (infiniteAmmo || player.getAbilities().instabuild && (ammoStack.getItem() == Items.SPECTRAL_ARROW || ammoStack.getItem() == Items.TIPPED_ARROW)) {
            projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
        }
        if (hasSuspend && drawProgress >= 20) {
            Vec3 projDir = projectile.m_20184_().normalize();
            Vec3 projPos = projectile.m_20182_();
            for (int j = 0; j < 4; j++) {
                Vec3 pos = projPos.add(projDir.scale((double) (2 + j * 2)));
                ((ServerLevel) world).sendParticles(ParticleTypes.END_ROD, pos.x(), pos.y(), pos.z(), 1, 0.0, 0.0, 0.0, 0.01);
            }
        }
        world.m_7967_(projectile);
        ModularProjectileSpawnEvent eventx = new ModularProjectileSpawnEvent(itemStack, ammoStack, player, projectile, world, drawProgress);
        MinecraftForge.EVENT_BUS.post(eventx);
        if (projectileVelocity * 3.0F > 4.0F) {
            TetraMod.packetHandler.sendToAllPlayersNear(new ProjectileMotionPacket(projectile), projectile.m_20183_(), 512.0, world.dimension());
        }
    }

    private boolean isInfinite(Player player, ItemStack bowStack, ItemStack ammoStack) {
        return player.getAbilities().instabuild || ammoStack.isEmpty() && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) > 0 || (Boolean) CastOptional.cast(ammoStack.getItem(), ArrowItem.class).map(item -> item.isInfinite(ammoStack, bowStack, player)).orElse(false);
    }

    public int getDrawDuration(ItemStack itemStack) {
        return Math.max((int) (20.0 * (this.getAttributeValue(itemStack, TetraAttributes.drawSpeed.get()) - (double) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack) * 0.2)), 1);
    }

    public float getProgress(ItemStack itemStack, @Nullable LivingEntity entity) {
        return (Float) Optional.ofNullable(entity).filter(e -> e.getUseItemRemainingTicks() > 0).filter(e -> itemStack.equals(e.getUseItem())).map(e -> (float) (this.getUseDuration(itemStack) - e.getUseItemRemainingTicks()) * 1.0F / (float) this.getDrawDuration(itemStack)).orElse(0.0F);
    }

    public float getOverbowProgress(ItemStack itemStack, @Nullable LivingEntity entity) {
        int overbowedLevel = this.getEffectLevel(itemStack, ItemEffect.overbowed);
        return overbowedLevel > 0 ? (Float) Optional.ofNullable(entity).filter(e -> itemStack.equals(e.getUseItem())).map(LivingEntity::m_21212_).map(useCount -> 1.0F - (float) useCount.intValue() / ((float) overbowedLevel * 2.0F)).map(progress -> Mth.clamp(progress, 0.0F, 1.0F)).orElse(0.0F) : 0.0F;
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        int overbowedLevel = this.getEffectLevel(itemStack, ItemEffect.overbowed);
        return overbowedLevel > 0 ? overbowedLevel * 2 + this.getDrawDuration(itemStack) : 37000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack bowStack = player.m_21120_(hand);
        boolean hasAmmo = !player.getProjectile(this.vanillaBow).isEmpty();
        if (this.isBroken(bowStack)) {
            return InteractionResultHolder.pass(bowStack);
        } else {
            InteractionResultHolder<ItemStack> ret = ForgeEventFactory.onArrowNock(bowStack, world, player, hand, hasAmmo);
            if (ret != null) {
                return ret;
            } else if (!hasAmmo && !player.getAbilities().instabuild && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bowStack) <= 0) {
                return InteractionResultHolder.fail(bowStack);
            } else {
                player.m_6672_(hand);
                return InteractionResultHolder.consume(bowStack);
            }
        }
    }

    private String getDrawVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        float progress = this.getProgress(itemStack, entity);
        if (progress == 0.0F) {
            return "item";
        } else if ((double) progress < 0.65) {
            return "draw_0";
        } else {
            return (double) progress < 0.9 ? "draw_1" : "draw_2";
        }
    }

    private ModuleModel getArrowModel(String drawVariant) {
        switch(drawVariant) {
            case "draw_0":
                return this.arrowModel0;
            case "draw_1":
                return this.arrowModel1;
            case "draw_2":
                return this.arrowModel2;
            default:
                return this.arrowModel0;
        }
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        return super.getModelCacheKey(itemStack, entity) + ":" + this.getDrawVariant(itemStack, entity);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ImmutableList<ModuleModel> getModels(ItemStack itemStack, @Nullable LivingEntity entity) {
        String modelType = this.getDrawVariant(itemStack, entity);
        ImmutableList<ModuleModel> models = (ImmutableList<ModuleModel>) this.getAllModules(itemStack).stream().sorted(Comparator.comparing(ItemModule::getRenderLayer)).flatMap(itemModule -> Arrays.stream(itemModule.getModels(itemStack))).filter(Objects::nonNull).sorted(Comparator.comparing(ModuleModel::getRenderLayer)).filter(model -> model.type.equals(modelType) || model.type.equals("static")).collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
        return !modelType.equals("item") ? ImmutableList.builder().addAll(models).add(this.getArrowModel(modelType)).build() : models;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMajorGuiOffsets(ItemStack itemStack) {
        return majorOffsets;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public GuiModuleOffsets getMinorGuiOffsets(ItemStack itemStack) {
        return minorOffsets;
    }
}