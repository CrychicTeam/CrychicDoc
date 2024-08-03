package com.mna.blocks.tileentities;

import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.items.ChargeableItem;
import com.mna.api.particles.ParticleInit;
import com.mna.blocks.runeforging.PedestalBlock;
import com.mna.blocks.tileentities.init.TileEntityInit;
import com.mna.effects.EffectInit;
import com.mna.entities.EntityInit;
import com.mna.entities.constructs.animated.Construct;
import com.mna.entities.sorcery.targeting.SpellSigil;
import com.mna.network.ServerMessageDispatcher;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ManaCrystalTile extends BlockEntity implements GeoBlockEntity {

    private ArrayList<Long> knownPedestalLocations;

    private static final int SCAN_RADIUS = 8;

    private AnimatableInstanceCache animCache = GeckoLibUtil.createInstanceCache(this);

    public ManaCrystalTile(BlockPos pos, BlockState state) {
        super(TileEntityInit.MANA_CRYSTAL.get(), pos, state);
        this.knownPedestalLocations = new ArrayList();
    }

    public static void ServerTick(Level level, BlockPos pos, BlockState state, ManaCrystalTile tile) {
        if (!level.isClientSide()) {
            if (level.getGameTime() % 80L == 0L) {
                tile.addEffectsToPlayers();
                tile.rechargeConstructs();
                tile.scanForPedestals();
            }
            if (level.getGameTime() % 5L == 0L) {
                tile.chargeItems();
            }
            if (level.getGameTime() % 100L == 0L) {
                tile.chargeSigils();
            }
        }
    }

    private void addEffectsToPlayers() {
        double radius = 16.0;
        AABB axisalignedbb = new AABB(this.f_58858_).inflate(radius);
        List<Player> list = this.m_58904_().m_45976_(Player.class, axisalignedbb);
        Vec3 myPos = new Vec3((double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5);
        for (Player playerentity : list) {
            double dist = myPos.distanceTo(playerentity.m_20182_());
            int magnitudeByDist = 4 - (int) Math.floor(dist / 4.0);
            playerentity.m_7292_(new MobEffectInstance(EffectInit.MANA_REGEN.get(), 100, magnitudeByDist, true, false, true));
        }
    }

    private void rechargeConstructs() {
        double radius = 16.0;
        AABB axisalignedbb = new AABB(this.f_58858_).inflate(radius);
        List<Construct> list = this.m_58904_().m_45976_(Construct.class, axisalignedbb);
        Vec3 myPos = new Vec3((double) this.m_58899_().m_123341_() + 0.5, (double) this.m_58899_().m_123342_() + 0.5, (double) this.m_58899_().m_123343_() + 0.5);
        for (Construct construct : list) {
            if (construct.getManaPct() != 1.0F && construct.getConstructData().isCapabilityEnabled(ConstructCapability.STORE_MANA)) {
                double dist = myPos.distanceTo(construct.m_20182_());
                int magnitudeByDist = 4 - (int) Math.floor(dist / 4.0);
                float manaRestored = (float) (magnitudeByDist * 50);
                construct.adjustMana(manaRestored);
                ServerMessageDispatcher.sendParticleSpawn((double) this.f_58858_.m_123341_() + 0.5, (double) (this.f_58858_.m_123342_() + 1), (double) this.f_58858_.m_123343_() + 0.5, construct.m_20185_(), construct.m_20186_() + 0.8, construct.m_20189_(), 0, 32.0F, this.f_58857_.dimension(), ParticleInit.LIGHTNING_BOLT.get());
            }
        }
    }

    private void chargeItems() {
        if (this.knownPedestalLocations.size() != 0) {
            int i = (int) Math.floor(Math.random() * (double) this.knownPedestalLocations.size());
            BlockPos curPos = BlockPos.of((Long) this.knownPedestalLocations.get(i));
            if (this.m_58904_().isLoaded(curPos)) {
                BlockState state = this.m_58904_().getBlockState(curPos);
                if (state.m_60734_() instanceof PedestalBlock) {
                    if (this.f_58857_.getBlockEntity(curPos) instanceof PedestalTile pedestal) {
                        ItemStack stack = pedestal.m_8020_(0);
                        if (stack.getItem() instanceof ChargeableItem) {
                            ChargeableItem item = (ChargeableItem) stack.getItem();
                            if (item.getMana(stack) < item.getMaxMana()) {
                                item.refundMana(pedestal.m_8020_(0), 50.0F, null);
                                ServerMessageDispatcher.sendParticleSpawn((double) this.f_58858_.m_123341_() + 0.5, (double) (this.f_58858_.m_123342_() + 1), (double) this.f_58858_.m_123343_() + 0.5, (double) curPos.m_123341_() + 0.5, (double) curPos.m_123342_() + 0.9, (double) curPos.m_123343_() + 0.5, 0, 32.0F, this.f_58857_.dimension(), ParticleInit.LIGHTNING_BOLT.get());
                            }
                        }
                    }
                } else {
                    this.knownPedestalLocations.remove(i--);
                }
            }
        }
    }

    private void chargeSigils() {
        double radius = 16.0;
        AABB axisalignedbb = new AABB(this.f_58858_).inflate(radius);
        this.m_58904_().getEntities((EntityTypeTest<Entity, SpellSigil>) EntityInit.SPELL_RUNE.get(), axisalignedbb, e -> e instanceof SpellSigil && e.isPermanent()).forEach(sigil -> sigil.addCharge());
    }

    private void scanForPedestals() {
        for (int i = -8; i <= 8; i++) {
            for (int j = -8; j <= 8; j++) {
                for (int k = -8; k <= 8; k++) {
                    BlockPos curPos = this.f_58858_.offset(i, j, k);
                    if (this.m_58904_().isLoaded(curPos)) {
                        BlockState state = this.m_58904_().getBlockState(curPos);
                        if (state.m_60734_() instanceof PedestalBlock) {
                            long posLong = curPos.asLong();
                            if (!this.knownPedestalLocations.contains(posLong)) {
                                this.knownPedestalLocations.add(posLong);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar registrar) {
        registrar.add(new AnimationController<>(this, state -> state.setAndContinue(RawAnimation.begin().thenLoop("animation.mana_crystal_armature.idle"))));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.animCache;
    }
}