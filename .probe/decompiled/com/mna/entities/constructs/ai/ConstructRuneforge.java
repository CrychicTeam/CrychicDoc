package com.mna.entities.constructs.ai;

import com.mna.Registries;
import com.mna.api.entities.construct.ConstructCapability;
import com.mna.api.entities.construct.IConstruct;
import com.mna.api.entities.construct.ai.ConstructAITask;
import com.mna.api.entities.construct.ai.ConstructCommandTileEntityInteract;
import com.mna.blocks.tileentities.RunicAnvilTile;
import com.mna.entities.constructs.ai.base.ConstructTasks;
import java.util.EnumSet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.registries.IForgeRegistry;

public class ConstructRuneforge extends ConstructCommandTileEntityInteract<BlockEntity, ConstructRuneforge> {

    private static final ConstructCapability[] requiredCaps = new ConstructCapability[] { ConstructCapability.SMITH };

    private int interactTimer = this.getInteractTime(ConstructCapability.SMITH);

    public ConstructRuneforge(IConstruct<?> construct, ResourceLocation guiIcon) {
        super(construct, guiIcon, BlockEntity.class);
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return super.canUse() && this.getTileEntity() instanceof RunicAnvilTile;
    }

    @Override
    public void tick() {
        super.m_8037_();
        AbstractGolem c = this.getConstructAsEntity();
        if (this.getTileEntity() == null || !(this.getTileEntity() instanceof RunicAnvilTile)) {
            this.exitCode = 1;
            this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.te_missing", new Object[0]));
            c.f_21345_.removeGoal(this);
        }
        if (this.doMove()) {
            if (this.interactTimer > 0) {
                this.interactTimer--;
            } else {
                this.interactTimer = this.getInteractTime(ConstructCapability.SMITH);
                this.faceBlockPos(this.blockPos);
                this.preInteract();
                this.forgeNext();
            }
        }
    }

    private void forgeNext() {
        RunicAnvilTile te = (RunicAnvilTile) this.getTileEntity();
        AbstractGolem c = this.getConstructAsEntity();
        if (te != null) {
            Player player = this.createFakePlayer();
            if (player == null) {
                this.exitCode = 1;
                return;
            }
            if (te.m_8020_(0).isEmpty() || te.m_8020_(1).isEmpty()) {
                if (!this.isSuccess()) {
                    this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runeforge_missing_items", new Object[0]));
                }
                this.exitCode = 1;
                return;
            }
            this.construct.getHandWithCapability(ConstructCapability.SMITH).ifPresent(h -> c.m_6674_(h));
            int advanceResponse = te.advanceCrafting(player, 5, true);
            if (advanceResponse == 0) {
                c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                return;
            }
            if (advanceResponse == 4) {
                c.m_9236_().playSound(null, c.m_20185_(), c.m_20186_(), c.m_20189_(), SoundEvents.ANVIL_PLACE, SoundSource.BLOCKS, 1.0F, 1.0F);
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runeforge_success", new Object[] { this.translate(te.m_8020_(0)) }));
                this.exitCode = 0;
            } else if (advanceResponse == 1) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runeforge_no_recipe", new Object[0]));
                this.exitCode = 1;
            } else if (advanceResponse == 2) {
                this.pushDiagnosticMessage(this.translate("mna.constructs.feedback.runeforge_low_tier", new Object[0]));
                this.exitCode = 1;
            }
        }
    }

    @Override
    public void start() {
        super.start();
        this.interactTimer = this.getInteractTime(ConstructCapability.SMITH);
    }

    @Override
    public ResourceLocation getType() {
        return ((IForgeRegistry) Registries.ConstructTasks.get()).getKey(ConstructTasks.RUNEFORGE);
    }

    public ConstructRuneforge duplicate() {
        return new ConstructRuneforge(this.construct, this.guiIcon).copyFrom(this);
    }

    public ConstructRuneforge copyFrom(ConstructAITask<?> other) {
        super.copyFrom(other);
        return this;
    }

    @Override
    public ConstructCapability[] requiredCapabilities() {
        return requiredCaps;
    }

    @Override
    protected String getPointIdentifier() {
        return "runeforge.point";
    }
}