package top.theillusivec4.curios.api.event;

import com.google.common.collect.ImmutableList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.util.Tuple;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEvent;
import top.theillusivec4.curios.api.type.capability.ICurio;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

public class DropRulesEvent extends LivingEvent {

    private final DamageSource source;

    private final int lootingLevel;

    private final boolean recentlyHit;

    private final ICuriosItemHandler curioHandler;

    private final List<Tuple<Predicate<ItemStack>, ICurio.DropRule>> overrides = new ArrayList();

    public DropRulesEvent(LivingEntity entity, ICuriosItemHandler handler, DamageSource source, int lootingLevel, boolean recentlyHit) {
        super(entity);
        this.source = source;
        this.lootingLevel = lootingLevel;
        this.recentlyHit = recentlyHit;
        this.curioHandler = handler;
    }

    public DamageSource getSource() {
        return this.source;
    }

    public int getLootingLevel() {
        return this.lootingLevel;
    }

    public boolean isRecentlyHit() {
        return this.recentlyHit;
    }

    public ICuriosItemHandler getCurioHandler() {
        return this.curioHandler;
    }

    public void addOverride(Predicate<ItemStack> predicate, ICurio.DropRule dropRule) {
        this.overrides.add(new Tuple<>(predicate, dropRule));
    }

    public ImmutableList<Tuple<Predicate<ItemStack>, ICurio.DropRule>> getOverrides() {
        return ImmutableList.copyOf(this.overrides);
    }
}