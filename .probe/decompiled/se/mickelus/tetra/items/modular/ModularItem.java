package se.mickelus.tetra.items.modular;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Multimap;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.ConfigHandler;
import se.mickelus.tetra.data.DataManager;
import se.mickelus.tetra.event.ModularItemDamageEvent;
import se.mickelus.tetra.items.TetraItem;
import se.mickelus.tetra.module.data.EffectData;
import se.mickelus.tetra.module.data.ItemProperties;
import se.mickelus.tetra.module.data.SynergyData;
import se.mickelus.tetra.module.data.ToolData;
import se.mickelus.tetra.properties.IToolProvider;

public abstract class ModularItem extends TetraItem implements IModularItem, IToolProvider {

    public static final UUID attackDamageModifier = Item.BASE_ATTACK_DAMAGE_UUID;

    public static final UUID attackSpeedModifier = Item.BASE_ATTACK_SPEED_UUID;

    private static final Logger logger = LogManager.getLogger();

    private final Cache<String, Multimap<Attribute, AttributeModifier>> attributeCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    private final Cache<String, ToolData> toolCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    private final Cache<String, EffectData> effectCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    private final Cache<String, ItemProperties> propertyCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    protected int honeBase = 450;

    protected int honeIntegrityMultiplier = 200;

    protected boolean canHone = true;

    protected String[] majorModuleKeys;

    protected String[] minorModuleKeys;

    protected String[] requiredModules = new String[0];

    protected int baseDurability = 0;

    protected int baseIntegrity = 0;

    protected SynergyData[] synergies = new SynergyData[0];

    public ModularItem(Item.Properties properties) {
        super(properties);
        DataManager.instance.moduleData.onReload(this::clearCaches);
    }

    @Override
    public void clearCaches() {
        logger.debug("Clearing item data caches for {}...", this.toString());
        this.attributeCache.invalidateAll();
        this.toolCache.invalidateAll();
        this.effectCache.invalidateAll();
        this.propertyCache.invalidateAll();
    }

    @Override
    public String[] getMajorModuleKeys(ItemStack itemStack) {
        return this.majorModuleKeys;
    }

    @Override
    public String[] getMinorModuleKeys(ItemStack itemStack) {
        return this.minorModuleKeys;
    }

    @Override
    public String[] getRequiredModules(ItemStack itemStack) {
        return this.requiredModules;
    }

    @Override
    public int getHoneBase(ItemStack itemStack) {
        return this.honeBase;
    }

    @Override
    public int getHoneIntegrityMultiplier(ItemStack itemStack) {
        return this.honeIntegrityMultiplier;
    }

    @Override
    public boolean canGainHoneProgress(ItemStack itemStack) {
        return this.canHone;
    }

    @Override
    public Cache<String, Multimap<Attribute, AttributeModifier>> getAttributeModifierCache() {
        return this.attributeCache;
    }

    @Override
    public Cache<String, EffectData> getEffectDataCache() {
        return this.effectCache;
    }

    @Override
    public Cache<String, ItemProperties> getPropertyCache() {
        return this.propertyCache;
    }

    public Cache<String, ToolData> getToolDataCache() {
        return this.toolCache;
    }

    @Override
    public Item getItem() {
        return this;
    }

    @Override
    public boolean canProvideTools(ItemStack itemStack) {
        return !this.isBroken(itemStack);
    }

    @Override
    public ToolData getToolData(ItemStack itemStack) {
        try {
            return (ToolData) this.getToolDataCache().get(this.getDataCacheKey(itemStack), () -> (ToolData) Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new));
        } catch (ExecutionException var3) {
            var3.printStackTrace();
            return (ToolData) Optional.ofNullable(this.getToolDataRaw(itemStack)).orElseGet(ToolData::new);
        }
    }

    protected ToolData getToolDataRaw(ItemStack itemStack) {
        logger.debug("Gathering tool data for {} ({})", this.getName(itemStack).getString(), this.getDataCacheKey(itemStack));
        return (ToolData) Stream.concat(this.getAllModules(itemStack).stream().map(module -> module.getToolData(itemStack)), Arrays.stream(this.getSynergyData(itemStack)).map(synergy -> synergy.tools)).filter(Objects::nonNull).reduce(null, ToolData::merge);
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(this.getItemName(stack));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level world, List<Component> tooltip, TooltipFlag flag) {
        tooltip.addAll(this.getTooltip(stack, world, flag));
    }

    @NotNull
    @Override
    public Rarity getRarity(@NotNull ItemStack itemStack) {
        return (Rarity) Optional.ofNullable(this.getPropertiesCached(itemStack)).map(props -> props.rarity).orElse(super.m_41460_(itemStack));
    }

    public int getMaxDamage(ItemStack itemStack) {
        return (Integer) Optional.of(this.getPropertiesCached(itemStack)).map(properties -> (float) (properties.durability + this.baseDurability) * properties.durabilityMultiplier).map(Math::round).orElse(0);
    }

    public void setDamage(ItemStack itemStack, int damage) {
        super.setDamage(itemStack, Math.min(itemStack.getMaxDamage() - 1, damage));
    }

    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        ModularItemDamageEvent event = new ModularItemDamageEvent(entity, stack, amount);
        MinecraftForge.EVENT_BUS.post(event);
        amount = event.getAmount();
        return Math.min(stack.getMaxDamage() - stack.getDamageValue() - 1, amount);
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        return Math.round(13.0F - (float) itemStack.getDamageValue() * 13.0F / (float) this.getMaxDamage(itemStack));
    }

    @Override
    public int getBarColor(ItemStack itemStack) {
        float maxDamage = (float) this.getMaxDamage(itemStack);
        float f = Math.max(0.0F, (maxDamage - (float) itemStack.getDamageValue()) / maxDamage);
        return Mth.hsvToRgb(f / 3.0F, 1.0F, 1.0F);
    }

    @Override
    public void onCraftedBy(ItemStack itemStack, Level world, Player player) {
        IModularItem.updateIdentifier(itemStack);
    }

    @Override
    public boolean isFoil(@Nonnull ItemStack itemStack) {
        return ConfigHandler.enableGlint.get() ? super.m_5812_(itemStack) : false;
    }

    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return slotChanged;
    }

    @Override
    public SynergyData[] getAllSynergyData(ItemStack itemStack) {
        return this.synergies;
    }

    public boolean isBookEnchantable(ItemStack itemStack, ItemStack bookStack) {
        return false;
    }

    public boolean canApplyAtEnchantingTable(ItemStack itemStack, Enchantment enchantment) {
        return this.acceptsEnchantment(itemStack, enchantment, true);
    }

    public int getEnchantmentValue(ItemStack itemStack) {
        return this.getEnchantability(itemStack);
    }
}