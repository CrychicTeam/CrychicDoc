package dev.xkmc.l2complements.init.registrate;

import com.tterrag.registrate.util.entry.EntityEntry;
import dev.xkmc.l2complements.content.entity.ISizedItemEntity;
import dev.xkmc.l2complements.content.entity.SpecialSpriteRenderer;
import dev.xkmc.l2complements.content.entity.fireball.BlackFireball;
import dev.xkmc.l2complements.content.entity.fireball.SoulFireball;
import dev.xkmc.l2complements.content.entity.fireball.StrongFireball;
import dev.xkmc.l2complements.init.L2Complements;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;

public class LCEntities {

    public static final EntityEntry<SoulFireball> ETFB_SOUL = L2Complements.REGISTRATE.entity("soul_fire_charge", SoulFireball::new, MobCategory.MISC).properties(e -> e.sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10)).renderer(() -> LCEntities::addRenderer).defaultLang().register();

    public static final EntityEntry<StrongFireball> ETFB_STRONG = L2Complements.REGISTRATE.entity("strong_fire_charge", StrongFireball::new, MobCategory.MISC).properties(e -> e.sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10)).renderer(() -> LCEntities::addRenderer).defaultLang().register();

    public static final EntityEntry<BlackFireball> ETFB_BLACK = L2Complements.REGISTRATE.entity("black_fire_charge", BlackFireball::new, MobCategory.MISC).properties(e -> e.sized(1.0F, 1.0F).clientTrackingRange(4).updateInterval(10)).renderer(() -> LCEntities::addRenderer).defaultLang().register();

    @OnlyIn(Dist.CLIENT)
    private static <T extends Entity & ItemSupplier & ISizedItemEntity> EntityRenderer<T> addRenderer(EntityRendererProvider.Context ctx) {
        return new SpecialSpriteRenderer<>(ctx, ctx.getItemRenderer(), true);
    }

    public static void register() {
    }

    public static void registerEntityAttributes(EntityAttributeCreationEvent event) {
    }
}