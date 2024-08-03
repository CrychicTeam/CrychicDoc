package noppes.npcs;

import java.util.Random;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import noppes.npcs.client.parts.MpmPartData;
import noppes.npcs.packets.Packets;
import noppes.npcs.packets.client.PacketEyeBlink;
import noppes.npcs.shared.common.util.ColorUtil;
import noppes.npcs.shared.common.util.NopVector2i;
import noppes.npcs.shared.common.util.NopVector3f;

public class ModelEyeData extends MpmPartData {

    public static final ResourceLocation RESOURCE = new ResourceLocation("moreplayermodels", "eyes");

    public static final ResourceLocation RESOURCE_LEFT = new ResourceLocation("moreplayermodels", "eyes_left");

    public static final ResourceLocation RESOURCE_RIGHT = new ResourceLocation("moreplayermodels", "eyes_right");

    private final Random r = new Random();

    public boolean glint = true;

    public NopVector3f browThickness = new NopVector3f(1.0F, 0.4F, 1.0F);

    public NopVector2i eyePos = NopVector2i.ZERO;

    public boolean mirror = false;

    public int eyeSize = 0;

    public int skinType = 0;

    public boolean useLidTexture = false;

    public NopVector3f lidColor = ColorUtil.colorToRgb(11830381);

    public NopVector3f browColor = ColorUtil.colorToRgb(5982516);

    public long blinkStart = 0L;

    public boolean disableBlink = false;

    public ModelEyeData() {
        this.color = new NopVector3f[] { ColorUtil.colorToRgb(8368696), ColorUtil.colorToRgb(16247203), ColorUtil.colorToRgb(10526975), ColorUtil.colorToRgb(10987431), ColorUtil.colorToRgb(10791096), ColorUtil.colorToRgb(4210943), ColorUtil.colorToRgb(14188339), ColorUtil.colorToRgb(11685080), ColorUtil.colorToRgb(6724056), ColorUtil.colorToRgb(15066419), ColorUtil.colorToRgb(55610), ColorUtil.colorToRgb(8375321), ColorUtil.colorToRgb(15892389), ColorUtil.colorToRgb(10066329), ColorUtil.colorToRgb(5013401), ColorUtil.colorToRgb(8339378), ColorUtil.colorToRgb(3361970), ColorUtil.colorToRgb(6704179), ColorUtil.colorToRgb(6717235), ColorUtil.colorToRgb(10040115), ColorUtil.colorToRgb(16445005), ColorUtil.colorToRgb(6085589), ColorUtil.colorToRgb(4882687) }[this.r.nextInt(23)];
    }

    @Override
    public CompoundTag getNbt() {
        CompoundTag compound = super.getNbt();
        compound.putBoolean("Glint", this.glint);
        compound.putBoolean("UseLidTexture", this.useLidTexture);
        compound.putBoolean("Mirror", this.mirror);
        compound.putBoolean("DisableBlink", this.disableBlink);
        compound.putInt("SkinType", this.skinType);
        compound.putInt("EyeSize", this.eyeSize);
        compound.putInt("SkinColor", ColorUtil.rgbToColor(this.lidColor));
        compound.putInt("BrowColor", ColorUtil.rgbToColor(this.browColor));
        compound.putInt("PositionX", this.eyePos.x);
        compound.putInt("PositionY", this.eyePos.y);
        compound.putInt("BrowThickness", (int) (this.browThickness.y * 10.0F));
        return compound;
    }

    @Override
    public void setNbt(CompoundTag compound) {
        super.setNbt(compound);
        this.glint = compound.getBoolean("Glint");
        this.useLidTexture = compound.getBoolean("UseLidTexture");
        this.mirror = compound.getBoolean("Mirror");
        this.disableBlink = compound.getBoolean("DisableBlink");
        this.skinType = compound.getInt("SkinType");
        this.eyeSize = compound.getInt("EyeSize");
        this.lidColor = ColorUtil.colorToRgb(compound.getInt("SkinColor"));
        this.browColor = ColorUtil.colorToRgb(compound.getInt("BrowColor"));
        this.eyePos = new NopVector2i(compound.getInt("PositionX"), compound.getInt("PositionY"));
        this.browThickness = new NopVector3f(1.0F, (float) compound.getInt("BrowThickness") / 10.0F, 1.0F);
    }

    public void update(LivingEntity player) {
        if (player.isAlive() && !this.disableBlink) {
            if (this.blinkStart < 0L) {
                this.blinkStart++;
            } else if (this.blinkStart == 0L) {
                if (this.r.nextInt(140) == 1) {
                    this.blinkStart = System.currentTimeMillis();
                    if (!player.m_9236_().isClientSide) {
                        Packets.sendNearby(player, new PacketEyeBlink(player.m_19879_()));
                    }
                }
            } else if (System.currentTimeMillis() - this.blinkStart > 300L) {
                this.blinkStart = -20L;
            }
        }
    }

    @Override
    public ResourceLocation getUrlTexture() {
        ResourceLocation url = super.getUrlTexture();
        return url == null ? MissingTextureAtlasSprite.getLocation() : url;
    }
}