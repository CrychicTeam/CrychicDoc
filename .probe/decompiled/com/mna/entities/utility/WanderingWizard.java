package com.mna.entities.utility;

import com.mna.api.entities.ai.CastSpellAtTargetGoal;
import com.mna.api.entities.ai.CastSpellOnSelfGoal;
import com.mna.api.events.WanderingWizardSelectingTradesEvent;
import com.mna.api.sound.SFX;
import com.mna.api.spells.attributes.Attribute;
import com.mna.api.spells.collections.Components;
import com.mna.api.spells.collections.Shapes;
import com.mna.api.tools.MATags;
import com.mna.entities.EntityInit;
import com.mna.entities.IAnimPacketSync;
import com.mna.entities.ai.PlayIdleAnimationGoal;
import com.mna.gui.containers.entity.ContainerWanderingWizard;
import com.mna.items.ItemInit;
import com.mna.items.manaweaving.ItemManaweavingPattern;
import com.mna.items.ritual.ItemThaumaturgicLink;
import com.mna.network.ServerMessageDispatcher;
import com.mna.recipes.manaweaving.ManaweavingPattern;
import com.mna.recipes.manaweaving.ManaweavingPatternSerializer;
import com.mna.spells.crafting.SpellRecipe;
import com.mna.tools.BiomeUtils;
import com.mna.tools.StructureUtils;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.InteractGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.LookAtTradingPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.goal.TradeWithPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkHooks;
import org.apache.commons.lang3.mutable.MutableInt;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.keyframe.event.CustomInstructionKeyframeEvent;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class WanderingWizard extends WanderingTrader implements GeoEntity, AnimationController.CustomKeyframeHandler<WanderingWizard>, IAnimPacketSync<WanderingWizard> {

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    private boolean plantedStaff = false;

    private boolean renderScroll = false;

    private int castTimer = 0;

    private int idleCooldown = this.getIdleCooldown();

    private WanderingWizard.State state = WanderingWizard.State.IDLE;

    private String idleAnim = "";

    private ItemStack staffStack;

    public WanderingWizard(EntityType<? extends WanderingTrader> type, Level worldIn) {
        super(type, worldIn);
        this.m_21557_(true);
        this.staffStack = MATags.getRandomItemFrom(MATags.Items.STAVES);
    }

    public WanderingWizard(Level worldIn) {
        this(EntityInit.WANDERING_WIZARD.get(), worldIn);
    }

    private ItemStack createInvisibilitySpell() {
        ItemStack invisSpell = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe invis = new SpellRecipe();
        invis.setShape(Shapes.SELF);
        invis.addComponent(Components.INVISIBILITY);
        invis.changeComponentAttributeValue(0, Attribute.DURATION, 120.0F);
        invis.writeToNBT(invisSpell.getOrCreateTag());
        return invisSpell;
    }

    private ItemStack createRandomAttackSpell() {
        ItemStack attackSpell = new ItemStack(ItemInit.SPELL.get());
        SpellRecipe boom = new SpellRecipe();
        boom.setShape(Shapes.BOLT);
        if (Math.random() < 0.5) {
            boom.addComponent(Components.FROST_DAMAGE);
            boom.changeComponentAttributeValue(0, Attribute.DAMAGE, 10.0F);
            boom.addComponent(Components.SLOW);
            boom.changeComponentAttributeValue(1, Attribute.MAGNITUDE, 2.0F);
            boom.changeComponentAttributeValue(1, Attribute.DURATION, 5.0F);
        } else {
            boom.addComponent(Components.MAGIC_DAMAGE);
            boom.changeComponentAttributeValue(0, Attribute.DAMAGE, 10.0F);
            boom.addComponent(Components.FLING);
            boom.changeComponentAttributeValue(1, Attribute.SPEED, 2.0F);
        }
        boom.writeToNBT(attackSpell.getOrCreateTag());
        return attackSpell;
    }

    private static boolean shouldCastInvisibility(WanderingWizard entity) {
        return entity.m_9236_().isNight() && !entity.m_20145_();
    }

    private static void onSelfCastStart(WanderingWizard entity) {
        entity.setState(WanderingWizard.State.CAST_SELF);
    }

    private static void onCastEnd(WanderingWizard entity) {
        entity.setState(WanderingWizard.State.IDLE);
        entity.castTimer = 0;
    }

    private static void onTargetCastStart(WanderingWizard entity) {
        entity.setState(WanderingWizard.State.CAST_TARGET);
    }

    private static boolean onTargetCasting(WanderingWizard entity) {
        entity.setState(WanderingWizard.State.CAST_TARGET);
        if (entity.castTimer++ < 5) {
            return false;
        } else {
            entity.setState(WanderingWizard.State.IDLE);
            return true;
        }
    }

    private static boolean canPlayIdleAnim(WanderingWizard entity) {
        return entity.state() == WanderingWizard.State.IDLE && entity.idleCooldown == 0;
    }

    private static void onIdleAnimStart(WanderingWizard entity, String anim) {
        entity.idleAnim = anim;
        entity.setState(WanderingWizard.State.IDLE_ANIM);
    }

    private static void onIdleAnimStop(WanderingWizard entity) {
        entity.idleAnim = "";
        entity.idleCooldown = entity.getIdleCooldown();
        entity.plantedStaff = false;
        entity.renderScroll = false;
        entity.setState(WanderingWizard.State.IDLE);
    }

    private int getIdleCooldown() {
        return 400 + (int) (800.0 * Math.random());
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SFX.Entity.WanderingWizard.HIT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SFX.Entity.WanderingWizard.DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SFX.Entity.WanderingWizard.IDLE;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SFX.Entity.WanderingWizard.YES;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean pGetYesSound) {
        return pGetYesSound ? SFX.Entity.WanderingWizard.YES : SFX.Entity.WanderingWizard.NO;
    }

    @Override
    public void tick() {
        super.m_8119_();
        if (this.f_19797_ > 30) {
            this.m_21557_(false);
        }
        if (this.idleCooldown > 0) {
            this.idleCooldown--;
        }
        if (this.m_9236_().isDay() && this.m_21023_(MobEffects.INVISIBILITY)) {
            this.m_21195_(MobEffects.INVISIBILITY);
        }
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        return this.f_19797_ < 30 ? false : super.m_6469_(source, amount);
    }

    @Override
    public InteractionResult interactAt(Player player, Vec3 vec, InteractionHand hand) {
        return super.m_7111_(player, vec, hand);
    }

    @Override
    public void openTradingScreen(Player player, Component displayName, int level) {
        int bufferCount = 10;
        NetworkHooks.openScreen((ServerPlayer) player, new SimpleMenuProvider((id, playerInventory, player2) -> new ContainerWanderingWizard(id, playerInventory, this), displayName));
        if (player.containerMenu != null) {
            MerchantOffers merchantoffers = this.m_6616_();
            if (!merchantoffers.isEmpty()) {
                MutableInt count = new MutableInt(0);
                MutableInt totalCount = new MutableInt(0);
                MerchantOffers packetOffers = new MerchantOffers();
                merchantoffers.forEach(o -> {
                    packetOffers.add(o);
                    count.increment();
                    totalCount.increment();
                    boolean isFinal = totalCount.getValue() >= merchantoffers.size();
                    if (count.getValue() >= 10 || isFinal) {
                        ServerMessageDispatcher.sendWanderingWizardContainerMessage(((ServerPlayer) player).containerCounter, packetOffers, level, this.m_7809_(), this.m_7826_(), this.m_7862_(), isFinal, (ServerPlayer) player);
                        count.setValue(0);
                        packetOffers.clear();
                    }
                });
            }
        }
    }

    public static AttributeSupplier.Builder getGlobalAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 20.0).add(Attributes.ATTACK_DAMAGE, 5.0);
    }

    @Override
    protected void addOffersFromItemListings(MerchantOffers givenMerchantOffers, VillagerTrades.ItemListing[] newTrades, int maxNumbers) {
        if (!this.m_9236_().isClientSide()) {
            givenMerchantOffers.clear();
            ServerLevel serverLevel = (ServerLevel) this.m_9236_();
            ItemStack costItem = new ItemStack(ItemInit.VINTEUM_INGOT.get());
            for (ManaweavingPattern pattern : ManaweavingPatternSerializer.ALL_RECIPES.values()) {
                ItemStack stack = new ItemStack(ItemInit.RECIPE_SCRAP_MANAWEAVING_PATTERN.get());
                ItemManaweavingPattern.setRecipe(stack, pattern);
                MerchantOffer offer = new MerchantOffer(costItem.copy(), stack, 5, 0, 1.0F);
                givenMerchantOffers.add(offer);
            }
            StructureUtils.getAllStructures(serverLevel).forEach(structureFeature -> {
                if (!structureFeature.is(MATags.Structures.BROKER_STRUCTURES)) {
                    ItemStack stackx = new ItemStack(ItemInit.THAUMATURGIC_LINK.get());
                    ItemThaumaturgicLink.setStructureKey(serverLevel, stackx, structureFeature);
                    MerchantOffer offerx = new MerchantOffer(costItem.copy(), stackx, 1, 0, 1.0F);
                    givenMerchantOffers.add(offerx);
                }
            });
            BiomeUtils.getAllBiomes(serverLevel).forEach(biome -> {
                if (!biome.is(MATags.Biomes.BROKER_BIOMES)) {
                    ItemStack stackx = new ItemStack(ItemInit.THAUMATURGIC_LINK.get());
                    ItemThaumaturgicLink.setBiomeKey(serverLevel, stackx, biome);
                    MerchantOffer offerx = new MerchantOffer(costItem.copy(), stackx, 1, 0, 1.0F);
                    givenMerchantOffers.add(offerx);
                }
            });
            givenMerchantOffers.add(new MerchantOffer(costItem.copy(), new ItemStack(Items.MAP), 1, 0, 1.0F));
            givenMerchantOffers.add(new MerchantOffer(costItem.copy(), new ItemStack(Items.COMPASS), 1, 0, 1.0F));
            MinecraftForge.EVENT_BUS.post(new WanderingWizardSelectingTradesEvent(this, givenMerchantOffers, newTrades, maxNumbers));
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public CompoundTag getPacketData() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("state", this.state().ordinal());
        tag.putString("idleAnim", this.idleAnim);
        tag.put("staff", this.staffStack.save(new CompoundTag()));
        return tag;
    }

    @Override
    public void handlePacketData(CompoundTag nbt) {
        this.setState(WanderingWizard.State.values()[nbt.getInt("state")]);
        this.idleAnim = nbt.getString("idleAnim");
        this.staffStack = ItemStack.of(nbt.getCompound("staff"));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> {
            switch(this.state) {
                case CAST_SELF:
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.WanderingWizard.cast_self"));
                case CAST_TARGET:
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.WanderingWizard.cast_target"));
                case IDLE_ANIM:
                    return state.setAndContinue(RawAnimation.begin().thenPlayAndHold(this.idleAnim));
                case IDLE:
                    if (this.m_20184_().add(0.0, -this.m_20184_().y, 0.0).length() > 0.02F) {
                        return state.setAndContinue(RawAnimation.begin().thenLoop("animation.WanderingWizard.walk"));
                    }
                    return state.setAndContinue(RawAnimation.begin().thenLoop("animation.WanderingWizard.idle"));
                default:
                    return PlayState.CONTINUE;
            }
        }));
    }

    public boolean renderPlantedStaff() {
        return this.plantedStaff;
    }

    public boolean renderScroll() {
        return this.renderScroll;
    }

    public ItemStack staff() {
        return this.staffStack;
    }

    @Override
    public void handle(CustomInstructionKeyframeEvent<WanderingWizard> event) {
        String var2 = event.getKeyframeData().getInstructions();
        switch(var2) {
            case "plant_staff;":
                this.plantedStaff = true;
                break;
            case "grab_scroll;":
                this.renderScroll = true;
                break;
            case "unplant_staff;":
                this.plantedStaff = false;
                break;
            case "ungrab_scroll;":
                this.renderScroll = false;
        }
    }

    @Override
    protected void registerGoals() {
        this.f_21345_.addGoal(0, new FloatGoal(this));
        this.f_21345_.addGoal(0, new CastSpellOnSelfGoal<>(this, this.createInvisibilitySpell(), WanderingWizard::shouldCastInvisibility, WanderingWizard::onSelfCastStart, WanderingWizard::onCastEnd, 34));
        this.f_21345_.addGoal(1, new TradeWithPlayerGoal(this));
        this.f_21345_.addGoal(1, new LookAtTradingPlayerGoal(this));
        this.f_21345_.addGoal(4, new MoveTowardsRestrictionGoal(this, 0.35));
        this.f_21345_.addGoal(6, new CastSpellAtTargetGoal<>(this, this.createRandomAttackSpell(), 0.35, 40, 10.0F).setAttackCooldown(60).setStopCallback(WanderingWizard::onCastEnd).setStartCallback(WanderingWizard::onTargetCastStart).setResetCallback(WanderingWizard::onCastEnd).setPrecastCallback(WanderingWizard::onTargetCasting));
        this.f_21345_.addGoal(8, new WaterAvoidingRandomStrollGoal(this, 0.35));
        this.f_21345_.addGoal(9, new InteractGoal(this, Player.class, 3.0F, 1.0F));
        this.f_21345_.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.f_21345_.addGoal(11, new PlayIdleAnimationGoal<>(this, WanderingWizard::canPlayIdleAnim, WanderingWizard::onIdleAnimStart, WanderingWizard::onIdleAnimStop, new Pair("animation.WanderingWizard.read_scroll", 187)));
        this.f_21346_.addGoal(2, new HurtByTargetGoal(this));
        this.f_21346_.addGoal(3, new NearestAttackableTargetGoal(this, Mob.class, 5, false, false, p_234199_0_ -> p_234199_0_ instanceof Enemy && !(p_234199_0_ instanceof Creeper)));
    }

    public WanderingWizard.State state() {
        return this.state;
    }

    public void setState(WanderingWizard.State state) {
        this.state = state;
        if (!this.m_9236_().isClientSide()) {
            ServerMessageDispatcher.sendEntityStateMessage(this);
        }
    }

    static enum State {

        IDLE, IDLE_ANIM, CAST_SELF, CAST_TARGET
    }
}