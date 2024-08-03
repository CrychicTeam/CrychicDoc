package io.redspace.ironsspellbooks.item;

import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.config.ServerConfigs;
import io.redspace.ironsspellbooks.particle.BlastwaveParticleOptions;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EnergizedCoreItem extends Item {

    public EnergizedCoreItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResult useOn(UseOnContext pContext) {
        Level level = pContext.getLevel();
        BlockPos blockPos = pContext.getClickedPos();
        if (pContext.getPlayer() != null && level.getBlockState(blockPos).m_60713_(Blocks.LIGHTNING_ROD)) {
            if (level.isThundering()) {
                if (!level.isClientSide && level.setBlockAndUpdate(blockPos, Blocks.AIR.defaultBlockState())) {
                    ItemStack itemstack = pContext.getItemInHand();
                    if (!pContext.getPlayer().getAbilities().instabuild) {
                        itemstack.shrink(1);
                    }
                    pContext.getPlayer().m_21008_(pContext.getHand(), itemstack);
                    Vec3 center = new Vec3((double) blockPos.m_123341_() + 0.5, (double) blockPos.m_123342_(), (double) blockPos.m_123343_() + 0.5);
                    this.doLightningBolt(level, center);
                    Level.ExplosionInteraction blockinteraction = ServerConfigs.SPELL_GREIFING.get() ? Level.ExplosionInteraction.TNT : Level.ExplosionInteraction.NONE;
                    MagicManager.spawnParticles(level, ParticleHelper.ELECTRIC_SPARKS, center.x, center.y + 0.5, center.z, 50, 0.1F, 0.1F, 0.1F, 0.6, false);
                    MagicManager.spawnParticles(level, new BlastwaveParticleOptions(new Vector3f(0.7F, 1.0F, 1.0F), 6.0F), center.x, center.y + 0.15, center.z, 1, 0.0, 0.0, 0.0, 0.0, true);
                    level.explode(null, level.damageSources().lightningBolt(), null, center.x, center.y, center.z, 3.0F, true, blockinteraction);
                    level.playSound(null, blockPos, SoundEvents.TRIDENT_THUNDER, SoundSource.PLAYERS, 2.0F, 0.6F);
                    ItemEntity itementity = new ItemEntity(level, center.x, center.y + 1.0, center.z, new ItemStack(ItemRegistry.LIGHTNING_ROD_STAFF.get()));
                    itementity.m_146915_(true);
                    level.m_7967_(itementity);
                }
            } else if (level.isClientSide) {
                pContext.getPlayer().displayClientMessage(Component.translatable("item.irons_spellbooks.energized_core.failure").withStyle(ChatFormatting.AQUA), true);
            }
            return InteractionResult.sidedSuccess(level.isClientSide);
        } else {
            return super.useOn(pContext);
        }
    }

    private void doLightningBolt(Level level, Vec3 pos) {
        LightningBolt lightningBolt = EntityType.LIGHTNING_BOLT.create(level);
        lightningBolt.setVisualOnly(true);
        lightningBolt.setDamage(0.0F);
        lightningBolt.m_146884_(pos);
        level.m_7967_(lightningBolt);
    }
}