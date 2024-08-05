package se.mickelus.tetra.items.modular.impl.crossbow;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.ChatFormatting;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.FireworkRocketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.registries.ObjectHolder;
import org.jetbrains.annotations.NotNull;
import se.mickelus.mutil.network.PacketHandler;
import se.mickelus.mutil.util.CastOptional;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.blocks.forged.chthonic.ChthonicExtractorBlock;
import se.mickelus.tetra.blocks.forged.chthonic.ExtractorProjectileEntity;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.effect.ItemEffect;
import se.mickelus.tetra.gui.GuiModuleOffsets;
import se.mickelus.tetra.items.modular.ModularItem;
import se.mickelus.tetra.module.ItemModule;
import se.mickelus.tetra.module.SchematicRegistry;
import se.mickelus.tetra.module.data.ModuleModel;
import se.mickelus.tetra.module.schematic.RepairSchematic;
import se.mickelus.tetra.properties.AttributeHelper;
import se.mickelus.tetra.properties.TetraAttributes;

@ParametersAreNonnullByDefault
public class ModularCrossbowItem extends ModularItem {

    public static final String staveKey = "crossbow/stave";

    public static final String stockKey = "crossbow/stock";

    public static final String stringKey = "crossbow/string";

    public static final String attachmentAKey = "crossbow/attachment_0";

    public static final String attachmentBKey = "crossbow/attachment_1";

    public static final String identifier = "modular_crossbow";

    public static final double velocityFactor = 0.125;

    private static final GuiModuleOffsets majorOffsets = new GuiModuleOffsets(-13, 0, -13, 18);

    private static final GuiModuleOffsets minorOffsets = new GuiModuleOffsets(4, -1, 13, 12, 4, 25);

    @ObjectHolder(registryName = "item", value = "tetra:modular_crossbow")
    public static ModularCrossbowItem instance;

    public static double multishotDefaultSpread = 10.0;

    protected ModuleModel arrowModel = new ModuleModel("item", new ResourceLocation("tetra", "item/module/crossbow/arrow"));

    protected ModuleModel extractorModel = new ModuleModel("item", new ResourceLocation("tetra", "item/module/crossbow/extractor"));

    protected ModuleModel fireworkModel = new ModuleModel("item", new ResourceLocation("tetra", "item/module/crossbow/firework"));

    protected ItemStack shootableDummy;

    private boolean isLoadingStart = false;

    private boolean isLoadingMiddle = false;

    public ModularCrossbowItem(@NotNull Item shootableDummy) {
        super(new Item.Properties().stacksTo(1).fireResistant());
        this.majorModuleKeys = new String[] { "crossbow/stave", "crossbow/stock" };
        this.minorModuleKeys = new String[] { "crossbow/attachment_0", "crossbow/string", "crossbow/attachment_1" };
        this.requiredModules = new String[] { "crossbow/string", "crossbow/stock", "crossbow/stave" };
        this.shootableDummy = new ItemStack(shootableDummy);
        this.updateConfig(ConfigHandler.honeCrossbowBase.get(), ConfigHandler.honeCrossbowIntegrityMultiplier.get());
        SchematicRegistry.instance.registerSchematic(new RepairSchematic(this, "modular_crossbow"));
    }

    public static float getProjectileVelocity(double strength, float velocityBonus) {
        float velocity = (float) Math.max(1.0, 1.0 + (strength - 6.0) * 0.125);
        return velocity + velocity * velocityBonus;
    }

    @Override
    public void commonInit(PacketHandler packetHandler) {
        DataManager.instance.synergyData.onReload(() -> this.synergies = DataManager.instance.getSynergyData("crossbow/"));
    }

    public void updateConfig(int honeBase, int honeIntegrityMultiplier) {
        this.honeBase = honeBase;
        this.honeIntegrityMultiplier = honeIntegrityMultiplier;
    }

    @Override
    public void clientInit() {
        super.clientInit();
        MinecraftForge.EVENT_BUS.register(new CrossbowOverlay(Minecraft.getInstance()));
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        List<ItemStack> list = this.getProjectiles(stack);
        if (this.isLoaded(stack) && !list.isEmpty()) {
            ItemStack itemstack = (ItemStack) list.get(0);
            tooltip.add(Component.translatable("item.minecraft.crossbow.projectile").append(" ").append(itemstack.getDisplayName()));
            if (flagIn.isAdvanced() && itemstack.getItem() == Items.FIREWORK_ROCKET) {
                List<Component> list1 = Lists.newArrayList();
                Items.FIREWORK_ROCKET.appendHoverText(itemstack, worldIn, list1, flagIn);
                if (!list1.isEmpty()) {
                    for (int i = 0; i < list1.size(); i++) {
                        list1.set(i, Component.literal("  ").append((Component) list1.get(i)).withStyle(ChatFormatting.GRAY));
                    }
                    tooltip.addAll(list1);
                }
            }
        }
        super.appendHoverText(stack, worldIn, tooltip, flagIn);
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal(" "));
            tooltip.add(Component.translatable("item.tetra.crossbow.wip").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal(" "));
        }
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
    public void onUseTick(Level world, LivingEntity entity, ItemStack itemStack, int count) {
        if (!world.isClientSide) {
            int drawDuration = this.getReloadDuration(itemStack);
            float f = this.getProgress(itemStack, entity);
            if (f < 0.2F) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
            }
            if (f >= 0.2F && !this.isLoadingStart) {
                this.isLoadingStart = true;
                world.playSound(null, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), this.getSoundEvent((float) drawDuration), SoundSource.PLAYERS, 0.5F, 1.0F);
            }
            if (f >= 0.5F && drawDuration <= 28 && !this.isLoadingMiddle) {
                this.isLoadingMiddle = true;
                if (drawDuration > 21) {
                    world.playSound(null, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), SoundEvents.CROSSBOW_LOADING_MIDDLE, SoundSource.PLAYERS, 0.5F, 1.0F);
                }
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        ItemStack itemstack = player.m_21120_(hand);
        if (this.isLoaded(itemstack)) {
            this.fireProjectiles(itemstack, world, player);
            this.setLoaded(itemstack, false);
            return InteractionResultHolder.consume(itemstack);
        } else if (!this.findAmmo(player).isEmpty()) {
            if (!this.isLoaded(itemstack)) {
                this.isLoadingStart = false;
                this.isLoadingMiddle = false;
                player.m_6672_(hand);
            }
            return InteractionResultHolder.consume(itemstack);
        } else {
            return InteractionResultHolder.fail(itemstack);
        }
    }

    @Override
    public void releaseUsing(ItemStack itemStack, Level world, LivingEntity entity, int timeLeft) {
        float progress = this.getProgress(itemStack, entity);
        if (progress >= 1.0F && !this.isLoaded(itemStack)) {
            boolean gotLoaded = this.reload(entity, itemStack);
            if (gotLoaded) {
                this.setLoaded(itemStack, true);
                SoundSource soundcategory = entity instanceof Player ? SoundSource.PLAYERS : SoundSource.HOSTILE;
                world.playSound(null, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), SoundEvents.CROSSBOW_LOADING_END, soundcategory, 1.0F, 1.0F / (world.random.nextFloat() * 0.5F + 1.0F) + 0.2F);
            }
        }
    }

    protected void fireProjectiles(ItemStack itemStack, Level world, LivingEntity entity) {
        if (entity instanceof Player player && !world.isClientSide) {
            ItemStack advancementCopy = itemStack.copy();
            int multishotEnchantLevel = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MULTISHOT, itemStack) * 3;
            int count = Math.max(this.getEffectLevel(itemStack, ItemEffect.multishot) + multishotEnchantLevel, 1);
            List<ItemStack> list = this.takeProjectiles(itemStack, 1);
            if (!list.isEmpty()) {
                double spread = (double) this.getEffectEfficiency(itemStack, ItemEffect.multishot);
                if (spread == 0.0 && multishotEnchantLevel > 0) {
                    spread = multishotDefaultSpread;
                }
                for (int i = 0; i < count; i++) {
                    ItemStack ammoStack = (ItemStack) list.get(0);
                    double yaw = (double) player.m_146908_() - spread * (double) (count - 1) / 2.0 + spread * (double) i;
                    boolean isDupe = player.getAbilities().instabuild || count > 1 && i != count / 2;
                    this.fireProjectile(world, itemStack, ammoStack, player, yaw, isDupe);
                }
                itemStack.hurtAndBreak(1, player, p -> p.m_21190_(p.m_7655_()));
                this.applyUsageEffects(entity, itemStack, 1.0);
                world.playSound(null, player.m_20185_(), player.m_20186_(), player.m_20189_(), SoundEvents.CROSSBOW_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
                if (player instanceof ServerPlayer) {
                    CriteriaTriggers.SHOT_CROSSBOW.trigger((ServerPlayer) player, advancementCopy);
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
            }
        }
    }

    public int getReloadDuration(ItemStack itemStack) {
        return Math.max((int) (20.0 * (this.getAttributeValue(itemStack, TetraAttributes.drawSpeed.get()) - (double) EnchantmentHelper.getItemEnchantmentLevel(Enchantments.QUICK_CHARGE, itemStack) * 0.2)), 1);
    }

    public float getProgress(ItemStack itemStack, @Nullable LivingEntity entity) {
        return (Float) Optional.ofNullable(entity).filter(e -> e.getUseItemRemainingTicks() > 0).filter(e -> itemStack.equals(e.getUseItem())).map(e -> (float) (this.getUseDuration(itemStack) - e.getUseItemRemainingTicks()) * 1.0F / (float) this.getReloadDuration(itemStack)).orElse(0.0F);
    }

    private ItemStack findAmmo(LivingEntity entity) {
        return entity.getProjectile(this.shootableDummy);
    }

    protected void fireProjectile(Level world, ItemStack crossbowStack, ItemStack ammoStack, Player player, double yaw, boolean isDupe) {
        double strength = this.getAttributeValue(crossbowStack, TetraAttributes.drawStrength.get());
        float velocityBonus = (float) this.getEffectLevel(crossbowStack, ItemEffect.velocity) / 100.0F;
        float projectileVelocity = getProjectileVelocity(strength, velocityBonus);
        if (ChthonicExtractorBlock.item.equals(ammoStack.getItem()) || ChthonicExtractorBlock.usedItem.equals(ammoStack.getItem())) {
            ExtractorProjectileEntity projectileEntity = new ExtractorProjectileEntity(world, player, ammoStack);
            if (isDupe) {
                projectileEntity.f_36705_ = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            projectileEntity.m_37251_(player, player.m_146909_(), (float) yaw, 0.0F, projectileVelocity, 1.0F);
            world.m_7967_(projectileEntity);
        } else if (ammoStack.getItem() instanceof FireworkRocketItem) {
            FireworkRocketEntity projectile = new FireworkRocketEntity(world, ammoStack, player, player.m_20185_(), player.m_20188_() - 0.15, player.m_20189_(), true);
            projectile.m_37251_(player, player.m_146909_(), (float) yaw, 0.0F, projectileVelocity * 1.6F, 1.0F);
            world.m_7967_(projectile);
        } else {
            ArrowItem ammoItem = (ArrowItem) CastOptional.cast(ammoStack.getItem(), ArrowItem.class).orElse((ArrowItem) Items.ARROW);
            AbstractArrow projectile = ammoItem.createArrow(world, ammoStack, player);
            projectile.setSoundEvent(SoundEvents.CROSSBOW_HIT);
            projectile.setShotFromCrossbow(true);
            projectile.setCritArrow(true);
            projectile.setBaseDamage(projectile.getBaseDamage() - 2.0 + strength / 3.0);
            if (projectileVelocity > 1.0F) {
                projectile.setBaseDamage(projectile.getBaseDamage() / (double) projectileVelocity);
            }
            int piercingLevel = this.getEffectLevel(crossbowStack, ItemEffect.piercing) + EnchantmentHelper.getItemEnchantmentLevel(Enchantments.PIERCING, crossbowStack);
            if (piercingLevel > 0) {
                projectile.setPierceLevel((byte) piercingLevel);
            }
            if (isDupe) {
                projectile.pickup = AbstractArrow.Pickup.CREATIVE_ONLY;
            }
            projectile.m_37251_(player, player.m_146909_(), (float) yaw, 0.0F, projectileVelocity * 3.15F, 1.0F);
            world.m_7967_(projectile);
        }
    }

    public boolean isLoaded(ItemStack stack) {
        CompoundTag compoundnbt = stack.getTag();
        return compoundnbt != null && compoundnbt.getBoolean("Charged");
    }

    public void setLoaded(ItemStack stack, boolean chargedIn) {
        CompoundTag compoundnbt = stack.getOrCreateTag();
        compoundnbt.putBoolean("Charged", chargedIn);
    }

    private ListTag getProjectilesNBT(ItemStack itemStack) {
        return itemStack.hasTag() ? this.getProjectilesNBT(itemStack.getTag()) : new ListTag();
    }

    private ListTag getProjectilesNBT(CompoundTag nbt) {
        return nbt.contains("ChargedProjectiles", 9) ? nbt.getList("ChargedProjectiles", 10) : new ListTag();
    }

    private void writeProjectile(ItemStack crossbowStack, ItemStack projectileStack) {
        CompoundTag crossbowTag = crossbowStack.getOrCreateTag();
        ListTag list = this.getProjectilesNBT(crossbowTag);
        CompoundTag projectileTag = new CompoundTag();
        projectileStack.save(projectileTag);
        list.add(projectileTag);
        crossbowTag.put("ChargedProjectiles", list);
    }

    private ItemStack getFirstProjectile(ItemStack itemStack) {
        ListTag projectiles = this.getProjectilesNBT(itemStack);
        return projectiles.size() > 0 ? ItemStack.of(projectiles.getCompound(0)) : ItemStack.EMPTY;
    }

    private List<ItemStack> getProjectiles(ItemStack itemStack) {
        List<ItemStack> result = Lists.newArrayList();
        ListTag projectileTags = this.getProjectilesNBT(itemStack);
        for (int i = 0; i < projectileTags.size(); i++) {
            CompoundTag stackNbt = projectileTags.getCompound(i);
            result.add(ItemStack.of(stackNbt));
        }
        return result;
    }

    private List<ItemStack> takeProjectiles(ItemStack itemStack, int count) {
        ListTag nbtList = this.getProjectilesNBT(itemStack);
        int size = Math.min(nbtList.size(), count);
        List<ItemStack> result = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            CompoundTag stackNbt = nbtList.getCompound(0);
            nbtList.remove(0);
            result.add(ItemStack.of(stackNbt));
        }
        return result;
    }

    public boolean hasProjectiles(ItemStack stack, Item ammoItem) {
        return this.getProjectiles(stack).stream().anyMatch(s -> s.getItem() == ammoItem);
    }

    private SoundEvent getSoundEvent(float velocity) {
        if (velocity < 7.0F) {
            return SoundEvents.CROSSBOW_QUICK_CHARGE_3;
        } else if (velocity < 15.0F) {
            return SoundEvents.CROSSBOW_QUICK_CHARGE_2;
        } else {
            return velocity < 22.0F ? SoundEvents.CROSSBOW_QUICK_CHARGE_1 : SoundEvents.CROSSBOW_LOADING_START;
        }
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 37000;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.CROSSBOW;
    }

    @Override
    public boolean useOnRelease(ItemStack stack) {
        return true;
    }

    @Override
    public boolean canBeDepleted() {
        return true;
    }

    private String getDrawVariant(ItemStack itemStack, @Nullable LivingEntity entity) {
        float progress = this.getProgress(itemStack, entity);
        if (this.isLoaded(itemStack)) {
            return "loaded";
        } else if (progress == 0.0F) {
            return "item";
        } else if ((double) progress < 0.58) {
            return "draw_0";
        } else {
            return progress < 1.0F ? "draw_1" : "draw_2";
        }
    }

    private String getProjectileVariant(ItemStack itemStack) {
        ItemStack projectileStack = this.getFirstProjectile(itemStack);
        if (projectileStack.getItem() instanceof FireworkRocketItem) {
            return "p1";
        } else {
            return !ChthonicExtractorBlock.item.equals(projectileStack.getItem()) && !ChthonicExtractorBlock.usedItem.equals(projectileStack.getItem()) ? "p0" : "p2";
        }
    }

    private ModuleModel getProjectileModel(ItemStack itemStack) {
        ItemStack projectileStack = this.getFirstProjectile(itemStack);
        if (projectileStack.getItem() instanceof FireworkRocketItem) {
            return this.fireworkModel;
        } else {
            return !ChthonicExtractorBlock.item.equals(projectileStack.getItem()) && !ChthonicExtractorBlock.usedItem.equals(projectileStack.getItem()) ? this.arrowModel : this.extractorModel;
        }
    }

    @Override
    public String getModelCacheKey(ItemStack itemStack, LivingEntity entity) {
        return super.getModelCacheKey(itemStack, entity) + ":" + this.getDrawVariant(itemStack, entity) + this.getProjectileVariant(itemStack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public ImmutableList<ModuleModel> getModels(ItemStack itemStack, @Nullable LivingEntity entity) {
        String modelType = this.getDrawVariant(itemStack, entity);
        ImmutableList<ModuleModel> models = (ImmutableList<ModuleModel>) this.getAllModules(itemStack).stream().sorted(Comparator.comparing(ItemModule::getRenderLayer)).flatMap(itemModule -> Arrays.stream(itemModule.getModels(itemStack))).filter(Objects::nonNull).sorted(Comparator.comparing(ModuleModel::getRenderLayer)).filter(model -> model.type.equals(modelType) || model.type.equals("static")).collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
        return this.isLoaded(itemStack) ? ImmutableList.builder().addAll(models).add(this.getProjectileModel(itemStack)).build() : models;
    }

    private boolean reload(LivingEntity entity, ItemStack crossbowStack) {
        int count = Math.max(this.getEffectLevel(crossbowStack, ItemEffect.ammoCapacity), 1);
        boolean infinite = (Boolean) CastOptional.cast(entity, Player.class).map(player -> player.getAbilities().instabuild).orElse(false);
        ItemStack ammoStack = ItemStack.EMPTY;
        for (int i = 0; i < count; i++) {
            if (ammoStack.isEmpty()) {
                ammoStack = this.findAmmo(entity);
            }
            if (ammoStack.isEmpty() && infinite) {
                ammoStack = new ItemStack(Items.ARROW);
            }
            if (!this.loadProjectiles(entity, crossbowStack, ammoStack, infinite && ammoStack.getItem() instanceof ArrowItem)) {
                return i > 0;
            }
        }
        return true;
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

    private boolean loadProjectiles(LivingEntity entity, ItemStack crossbowStack, ItemStack ammoStack, boolean infiniteAmmo) {
        if (ammoStack.isEmpty()) {
            return false;
        } else {
            ItemStack itemstack;
            if (!infiniteAmmo) {
                itemstack = ammoStack.split(1);
                if (ammoStack.isEmpty() && entity instanceof Player player) {
                    player.getInventory().removeItem(ammoStack);
                }
            } else {
                itemstack = ammoStack.copy();
            }
            this.writeProjectile(crossbowStack, itemstack);
            return true;
        }
    }
}