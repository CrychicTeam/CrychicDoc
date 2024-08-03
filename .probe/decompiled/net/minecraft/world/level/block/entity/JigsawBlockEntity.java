package net.minecraft.world.level.block.entity;

import java.util.Arrays;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.FrontAndTop;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.JigsawBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.pools.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;

public class JigsawBlockEntity extends BlockEntity {

    public static final String TARGET = "target";

    public static final String POOL = "pool";

    public static final String JOINT = "joint";

    public static final String NAME = "name";

    public static final String FINAL_STATE = "final_state";

    private ResourceLocation name = new ResourceLocation("empty");

    private ResourceLocation target = new ResourceLocation("empty");

    private ResourceKey<StructureTemplatePool> pool = ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation("empty"));

    private JigsawBlockEntity.JointType joint = JigsawBlockEntity.JointType.ROLLABLE;

    private String finalState = "minecraft:air";

    public JigsawBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.JIGSAW, blockPos0, blockState1);
    }

    public ResourceLocation getName() {
        return this.name;
    }

    public ResourceLocation getTarget() {
        return this.target;
    }

    public ResourceKey<StructureTemplatePool> getPool() {
        return this.pool;
    }

    public String getFinalState() {
        return this.finalState;
    }

    public JigsawBlockEntity.JointType getJoint() {
        return this.joint;
    }

    public void setName(ResourceLocation resourceLocation0) {
        this.name = resourceLocation0;
    }

    public void setTarget(ResourceLocation resourceLocation0) {
        this.target = resourceLocation0;
    }

    public void setPool(ResourceKey<StructureTemplatePool> resourceKeyStructureTemplatePool0) {
        this.pool = resourceKeyStructureTemplatePool0;
    }

    public void setFinalState(String string0) {
        this.finalState = string0;
    }

    public void setJoint(JigsawBlockEntity.JointType jigsawBlockEntityJointType0) {
        this.joint = jigsawBlockEntityJointType0;
    }

    @Override
    protected void saveAdditional(CompoundTag compoundTag0) {
        super.saveAdditional(compoundTag0);
        compoundTag0.putString("name", this.name.toString());
        compoundTag0.putString("target", this.target.toString());
        compoundTag0.putString("pool", this.pool.location().toString());
        compoundTag0.putString("final_state", this.finalState);
        compoundTag0.putString("joint", this.joint.getSerializedName());
    }

    @Override
    public void load(CompoundTag compoundTag0) {
        super.load(compoundTag0);
        this.name = new ResourceLocation(compoundTag0.getString("name"));
        this.target = new ResourceLocation(compoundTag0.getString("target"));
        this.pool = ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(compoundTag0.getString("pool")));
        this.finalState = compoundTag0.getString("final_state");
        this.joint = (JigsawBlockEntity.JointType) JigsawBlockEntity.JointType.byName(compoundTag0.getString("joint")).orElseGet(() -> JigsawBlock.getFrontFacing(this.m_58900_()).getAxis().isHorizontal() ? JigsawBlockEntity.JointType.ALIGNED : JigsawBlockEntity.JointType.ROLLABLE);
    }

    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.m_187482_();
    }

    public void generate(ServerLevel serverLevel0, int int1, boolean boolean2) {
        BlockPos $$3 = this.m_58899_().relative(((FrontAndTop) this.m_58900_().m_61143_(JigsawBlock.ORIENTATION)).front());
        Registry<StructureTemplatePool> $$4 = serverLevel0.m_9598_().registryOrThrow(Registries.TEMPLATE_POOL);
        Holder<StructureTemplatePool> $$5 = $$4.getHolderOrThrow(this.pool);
        JigsawPlacement.generateJigsaw(serverLevel0, $$5, this.target, int1, $$3, boolean2);
    }

    public static enum JointType implements StringRepresentable {

        ROLLABLE("rollable"), ALIGNED("aligned");

        private final String name;

        private JointType(String p_59455_) {
            this.name = p_59455_;
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }

        public static Optional<JigsawBlockEntity.JointType> byName(String p_59458_) {
            return Arrays.stream(values()).filter(p_59461_ -> p_59461_.getSerializedName().equals(p_59458_)).findFirst();
        }

        public Component getTranslatedName() {
            return Component.translatable("jigsaw_block.joint." + this.name);
        }
    }
}