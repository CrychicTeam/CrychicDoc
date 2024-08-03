package noppes.npcs.client;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Hashtable;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import noppes.npcs.CustomNpcs;
import noppes.npcs.IChatMessages;
import noppes.npcs.entity.EntityNPCInterface;
import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ChatMessages implements IChatMessages {

    private static Map<String, ChatMessages> users = new Hashtable();

    protected static final RenderStateShard.TransparencyStateShard TRANSLUCENT_TRANSPARENCY = new RenderStateShard.TransparencyStateShard("translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final RenderStateShard.ShaderStateShard sharder = new RenderStateShard.ShaderStateShard(GameRenderer::m_172832_);

    protected static final RenderType type = RenderType.create("chatbubble", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, false, RenderType.CompositeState.builder().setCullState(new RenderStateShard.CullStateShard(true)).setLightmapState(new RenderStateShard.LightmapStateShard(true)).setShaderState(sharder).createCompositeState(true));

    protected static final RenderType typeDepth = RenderType.create("chatbubbledepth", DefaultVertexFormat.POSITION_COLOR_LIGHTMAP, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setCullState(new RenderStateShard.CullStateShard(true)).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setShaderState(sharder).setLightmapState(new RenderStateShard.LightmapStateShard(true)).setDepthTestState(new RenderStateShard.DepthTestStateShard("always", 519)).createCompositeState(false));

    private Map<Long, TextBlockClient> messages = new TreeMap();

    private int boxLength = 46;

    private float scale = 0.5F;

    private String lastMessage = "";

    private long lastMessageTime = 0L;

    @Override
    public void addMessage(String message, EntityNPCInterface npc) {
        if (CustomNpcs.EnableChatBubbles) {
            long time = System.currentTimeMillis();
            if (!message.equals(this.lastMessage) || this.lastMessageTime + 1000L <= time) {
                Map<Long, TextBlockClient> messages = new TreeMap(this.messages);
                messages.put(time, new TextBlockClient(message, this.boxLength * 4, true, Minecraft.getInstance().player, npc));
                if (messages.size() > 3) {
                    messages.remove(messages.keySet().iterator().next());
                }
                this.messages = messages;
                this.lastMessage = message;
                this.lastMessageTime = time;
            }
        }
    }

    @Override
    public void renderMessages(PoseStack PoseStack, MultiBufferSource typeBuffer, float textscale, boolean inRange, int lightmapUV) {
        Map<Long, TextBlockClient> messages = this.getMessages();
        if (!messages.isEmpty()) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            RenderSystem.setShader(GameRenderer::m_172832_);
            if (inRange) {
                this.render(PoseStack, typeBuffer, typeBuffer.getBuffer(typeDepth), textscale, false, lightmapUV);
            }
            this.render(PoseStack, typeBuffer, typeBuffer.getBuffer(type), textscale, true, lightmapUV);
        }
    }

    public void render(PoseStack poseStack, MultiBufferSource typeBuffer, VertexConsumer ivertex, float textScale, boolean depth, int lightmapUV) {
        Font font = Minecraft.getInstance().font;
        float var14 = 0.02666667F;
        int size = 0;
        for (TextBlockClient block : this.messages.values()) {
            size += block.lines.size();
        }
        Minecraft mc = Minecraft.getInstance();
        int textYSize = (int) ((float) (size * 9) * this.scale);
        poseStack.pushPose();
        poseStack.translate(0.0F, (float) textYSize * var14, 0.0F);
        poseStack.scale(textScale, textScale, textScale);
        poseStack.mulPose(mc.getEntityRenderDispatcher().cameraOrientation());
        poseStack.scale(-var14, -var14, var14);
        int black = depth ? -16777216 : -16777216;
        int white = depth ? -1140850689 : 1157627903;
        PoseStack.Pose entry = poseStack.last();
        Matrix4f matrix = entry.pose();
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 2), -2.0F, (float) (this.boxLength + 2), (float) (textYSize + 1), white, 0.11F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 1), -3.0F, (float) (this.boxLength + 1), -2.0F, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 1), (float) (textYSize + 2), -1.0F, (float) (textYSize + 1), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, 3.0F, (float) (textYSize + 2), (float) (this.boxLength + 1), (float) (textYSize + 1), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 3), -1.0F, (float) (-this.boxLength - 2), (float) textYSize, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (this.boxLength + 3), -1.0F, (float) (this.boxLength + 2), (float) textYSize, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 2), -2.0F, (float) (-this.boxLength - 1), -1.0F, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (this.boxLength + 2), -2.0F, (float) (this.boxLength + 1), -1.0F, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (-this.boxLength - 2), (float) (textYSize + 1), (float) (-this.boxLength - 1), (float) textYSize, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, (float) (this.boxLength + 2), (float) (textYSize + 1), (float) (this.boxLength + 1), (float) textYSize, black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, 0.0F, (float) (textYSize + 1), 3.0F, (float) (textYSize + 4), white, 0.11F);
        this.drawRect(ivertex, matrix, lightmapUV, -1.0F, (float) (textYSize + 4), 1.0F, (float) (textYSize + 5), white, 0.11F);
        this.drawRect(ivertex, matrix, lightmapUV, -1.0F, (float) (textYSize + 1), 0.0F, (float) (textYSize + 4), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, 3.0F, (float) (textYSize + 1), 4.0F, (float) (textYSize + 3), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, 2.0F, (float) (textYSize + 3), 3.0F, (float) (textYSize + 4), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, 1.0F, (float) (textYSize + 4), 2.0F, (float) (textYSize + 5), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, -2.0F, (float) (textYSize + 4), -1.0F, (float) (textYSize + 5), black, 0.1F);
        this.drawRect(ivertex, matrix, lightmapUV, -2.0F, (float) (textYSize + 5), 1.0F, (float) (textYSize + 6), black, 0.1F);
        poseStack.scale(this.scale, this.scale, this.scale);
        int index = 0;
        for (TextBlockClient block : this.messages.values()) {
            for (Component chat : block.lines) {
                font.drawInBatch(chat, (float) (-font.width(chat) / 2), (float) (index * 9), black, false, matrix, typeBuffer, !depth ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, 0, lightmapUV);
                index++;
            }
        }
        poseStack.popPose();
    }

    public void drawRect(VertexConsumer ivertex, Matrix4f matrix, int lightmapUV, float x, float y, float x2, float y2, int color, float z) {
        if (x < x2) {
            float j1 = x;
            x = x2;
            x2 = j1;
        }
        if (y < y2) {
            float j1 = y;
            y = y2;
            y2 = j1;
        }
        float f1 = (float) (color >> 16 & 0xFF) / 255.0F;
        float f2 = (float) (color >> 8 & 0xFF) / 255.0F;
        float f3 = (float) (color & 0xFF) / 255.0F;
        this.draw(ivertex, matrix, lightmapUV, x, y, z, f1, f2, f3);
        this.draw(ivertex, matrix, lightmapUV, x, y2, z, f1, f2, f3);
        this.draw(ivertex, matrix, lightmapUV, x2, y2, z, f1, f2, f3);
        this.draw(ivertex, matrix, lightmapUV, x2, y, z, f1, f2, f3);
    }

    private void draw(VertexConsumer ivertex, Matrix4f matrix, int lightmapUV, float x, float y, float z, float red, float green, float blue) {
        Vector4f v = new Vector4f(x, y, z, 1.0F);
        v.mul(matrix);
        ivertex.vertex((double) v.x(), (double) v.y(), (double) v.z()).color(red, green, blue, 1.0F).uv2(lightmapUV).endVertex();
    }

    public static ChatMessages getChatMessages(String username) {
        if (users.containsKey(username)) {
            return (ChatMessages) users.get(username);
        } else {
            ChatMessages chat = new ChatMessages();
            users.put(username, chat);
            return chat;
        }
    }

    private static boolean validPlayer(String username) {
        for (Player player : Minecraft.getInstance().level.players()) {
            if (username.equals(player.getName()) || username.equals(player.getDisplayName().getString())) {
                return true;
            }
        }
        return false;
    }

    private Map<Long, TextBlockClient> getMessages() {
        Map<Long, TextBlockClient> messages = new TreeMap();
        long time = System.currentTimeMillis();
        for (Entry<Long, TextBlockClient> entry : this.messages.entrySet()) {
            if (time <= (Long) entry.getKey() + 10000L) {
                messages.put((Long) entry.getKey(), (TextBlockClient) entry.getValue());
            }
        }
        return this.messages = messages;
    }

    public boolean hasMessage() {
        return !this.messages.isEmpty();
    }
}