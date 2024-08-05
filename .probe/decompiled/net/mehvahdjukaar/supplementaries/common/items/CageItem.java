package net.mehvahdjukaar.supplementaries.common.items;

import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.client.ICustomItemRendererProvider;
import net.mehvahdjukaar.moonlight.api.client.ItemStackRenderer;
import net.mehvahdjukaar.supplementaries.client.renderers.items.CageItemRenderer;
import net.mehvahdjukaar.supplementaries.configs.CommonConfigs;
import net.mehvahdjukaar.supplementaries.reg.ModTags;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class CageItem extends AbstractMobContainerItem implements ICustomItemRendererProvider {

    public CageItem(Block block, Item.Properties properties) {
        super(block, properties, 0.875F, 1.0F, false);
    }

    @Override
    public void playCatchSound(Player player) {
        player.m_9236_().playSound(null, player.m_20183_(), SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 1.0F, 0.7F);
    }

    @Override
    public void playFailSound(Player player) {
    }

    @Override
    public void playReleaseSound(Level world, Vec3 v) {
        world.playSound(null, v.x(), v.y(), v.z(), SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1.0F, 0.05F);
    }

    @Override
    public boolean canItemCatch(Entity e) {
        if ((Boolean) CommonConfigs.Functional.CAGE_AUTO_DETECT.get() && this.canFitEntity(e)) {
            return true;
        } else {
            EntityType<?> type;
            boolean var10000;
            label36: {
                type = e.getType();
                if (e instanceof LivingEntity le && le.isBaby()) {
                    var10000 = true;
                    break label36;
                }
                var10000 = false;
            }
            boolean isBaby = var10000;
            return (Boolean) CommonConfigs.Functional.CAGE_ALL_BABIES.get() && isBaby || type.is(ModTags.CAGE_CATCHABLE) || type.is(ModTags.CAGE_BABY_CATCHABLE) && isBaby;
        }
    }

    @Override
    public Supplier<ItemStackRenderer> getRendererFactory() {
        return CageItemRenderer::new;
    }
}