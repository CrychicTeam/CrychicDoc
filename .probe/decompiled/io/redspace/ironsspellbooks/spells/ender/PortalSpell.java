package io.redspace.ironsspellbooks.spells.ender;

import io.redspace.ironsspellbooks.api.config.DefaultConfig;
import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.registry.SchoolRegistry;
import io.redspace.ironsspellbooks.api.spells.AbstractSpell;
import io.redspace.ironsspellbooks.api.spells.AutoSpellConfig;
import io.redspace.ironsspellbooks.api.spells.CastSource;
import io.redspace.ironsspellbooks.api.spells.CastType;
import io.redspace.ironsspellbooks.api.spells.ICastDataSerializable;
import io.redspace.ironsspellbooks.api.spells.SpellRarity;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.PortalManager;
import io.redspace.ironsspellbooks.capabilities.magic.RecastInstance;
import io.redspace.ironsspellbooks.capabilities.magic.RecastResult;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalData;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalEntity;
import io.redspace.ironsspellbooks.entity.spells.portal.PortalPos;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@AutoSpellConfig
public class PortalSpell extends AbstractSpell {

    public static final int PORTAL_RECAST_COUNT = 2;

    private final ResourceLocation spellId = new ResourceLocation("irons_spellbooks", "portal");

    private final DefaultConfig defaultConfig = new DefaultConfig().setMinRarity(SpellRarity.UNCOMMON).setSchoolResource(SchoolRegistry.ENDER_RESOURCE).setMaxLevel(3).setCooldownSeconds(180.0).build();

    public PortalSpell() {
        this.baseSpellPower = 300;
        this.spellPowerPerLevel = 120;
        this.baseManaCost = 200;
        this.manaCostPerLevel = 10;
        this.castTime = 0;
    }

    @Override
    public DefaultConfig getDefaultConfig() {
        return this.defaultConfig;
    }

    @Override
    public CastType getCastType() {
        return CastType.INSTANT;
    }

    @Override
    public ResourceLocation getSpellResource() {
        return this.spellId;
    }

    @Override
    public ICastDataSerializable getEmptyCastData() {
        return new PortalData();
    }

    @Override
    public int getRecastCount(int spellLevel, @Nullable LivingEntity entity) {
        return 2;
    }

    @Override
    public void onCast(Level level, int spellLevel, LivingEntity entity, CastSource castSource, MagicData playerMagicData) {
        if (entity instanceof Player player && level instanceof ServerLevel serverLevel) {
            Vec3 blockHitResult = Utils.getTargetBlock(level, entity, ClipContext.Fluid.NONE, (double) this.getCastDistance(spellLevel, entity)).m_82450_().subtract(entity.m_20156_().normalize().multiply(0.25, 0.0, 0.25));
            Vec3 portalLocation = level.m_45547_(new ClipContext(blockHitResult, blockHitResult.add(0.0, (double) (-entity.m_20206_() - 1.0F), 0.0), ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).m_82450_().add(0.0, 0.076, 0.0);
            float portalRotation = 90.0F + Utils.getAngle(portalLocation.x, portalLocation.z, entity.m_20185_(), entity.m_20189_()) * (180.0F / (float) Math.PI);
            if (playerMagicData.getPlayerRecasts().hasRecastForSpell(this.getSpellId())) {
                PortalData portalData = (PortalData) playerMagicData.getPlayerRecasts().getRecastInstance(this.getSpellId()).getCastData();
                if (portalData.globalPos1 != null & portalData.portalEntityId1 != null) {
                    portalData.globalPos2 = PortalPos.of(player.f_19853_.dimension(), portalLocation, portalRotation);
                    portalData.setPortalDuration(this.getPortalDuration(spellLevel, player));
                    PortalEntity secondPortalEntity = this.setupPortalEntity(serverLevel, portalData, player, portalLocation, portalRotation);
                    secondPortalEntity.setPortalConnected();
                    portalData.portalEntityId2 = secondPortalEntity.m_20148_();
                    PortalManager.INSTANCE.addPortalData(portalData.portalEntityId1, portalData);
                    PortalManager.INSTANCE.addPortalData(portalData.portalEntityId2, portalData);
                    ServerLevel firstPortalLevel = serverLevel.getServer().getLevel(portalData.globalPos1.dimension());
                    if (firstPortalLevel != null) {
                        PortalEntity firstPortalEntity = (PortalEntity) firstPortalLevel.getEntity(portalData.portalEntityId1);
                        if (firstPortalEntity != null) {
                            firstPortalEntity.setPortalConnected();
                            firstPortalEntity.setTicksToLive(portalData.ticksToLive);
                        }
                    }
                }
            } else {
                PortalData portalData = new PortalData();
                portalData.setPortalDuration(this.getRecastDuration(spellLevel, player) + 10);
                PortalEntity portalEntity = this.setupPortalEntity(level, portalData, player, portalLocation, portalRotation);
                portalData.globalPos1 = PortalPos.of(player.f_19853_.dimension(), portalLocation, portalRotation);
                portalData.portalEntityId1 = portalEntity.m_20148_();
                playerMagicData.getPlayerRecasts().addRecast(new RecastInstance(this.getSpellId(), spellLevel, 2, this.getRecastDuration(spellLevel, player), castSource, portalData), playerMagicData);
            }
        }
        super.onCast(level, spellLevel, entity, castSource, playerMagicData);
    }

    private PortalEntity setupPortalEntity(Level level, PortalData portalData, Player owner, Vec3 spawnPos, float rotation) {
        PortalEntity portalEntity = new PortalEntity(level, portalData);
        portalEntity.setOwnerUUID(owner.m_20148_());
        portalEntity.m_20219_(spawnPos);
        portalEntity.m_146922_(rotation);
        level.m_7967_(portalEntity);
        return portalEntity;
    }

    @Override
    public void onRecastFinished(ServerPlayer serverPlayer, RecastInstance recastInstance, RecastResult recastResult, ICastDataSerializable castDataSerializable) {
        if (recastResult != RecastResult.USED_ALL_RECASTS && castDataSerializable instanceof PortalData portalData && portalData.portalEntityId1 != null && portalData.globalPos1 != null) {
            MinecraftServer server = serverPlayer.m_20194_();
            if (server != null) {
                ServerLevel level = server.getLevel(portalData.globalPos1.dimension());
                if (level != null) {
                    Entity portal1 = level.getEntity(portalData.portalEntityId1);
                    if (portal1 != null) {
                        portal1.discard();
                    } else {
                        PortalManager.INSTANCE.removePortalData(portalData.portalEntityId1);
                    }
                }
            }
        }
        super.onRecastFinished(serverPlayer, recastInstance, recastResult, castDataSerializable);
    }

    public int getRecastDuration(int spellLevel, LivingEntity caster) {
        return 2400;
    }

    public int getPortalDuration(int spellLevel, LivingEntity caster) {
        return (int) (this.getSpellPower(spellLevel, caster) * 20.0F);
    }

    private float getCastDistance(int spellLevel, LivingEntity sourceEntity) {
        return 48.0F;
    }

    @Override
    public List<MutableComponent> getUniqueInfo(int spellLevel, LivingEntity caster) {
        return List.of(Component.translatable("ui.irons_spellbooks.cast_range", Utils.stringTruncation((double) this.getCastDistance(spellLevel, caster), 1)), Component.translatable("ui.irons_spellbooks.portal_duration", Utils.timeFromTicks((float) this.getPortalDuration(spellLevel, caster), 2)));
    }
}