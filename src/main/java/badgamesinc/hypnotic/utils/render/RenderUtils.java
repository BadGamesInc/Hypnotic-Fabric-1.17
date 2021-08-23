package badgamesinc.hypnotic.utils.render;

import java.awt.Color;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.module.render.IItemRenderer;
import badgamesinc.hypnotic.utils.ColorUtils;
import badgamesinc.hypnotic.utils.mixin.IMatrix4f;
import badgamesinc.hypnotic.utils.render.shader.ShaderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.Vector4f;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;

public class RenderUtils {
    
	// Made by lavaflowglow 11/19/2020 3:39 AM
	
		public static RenderUtils INSTANCE = new RenderUtils();
		static MinecraftClient mc = MinecraftClient.getInstance();
		public static boolean SetCustomYaw = false;
		public static float CustomYaw = 0;
		public int scaledWidth = 0;
		public int scaledHeight = 0;
		
		public void onTick() {
			scaledWidth = mc.getWindow().getScaledWidth();
			scaledHeight = mc.getWindow().getScaledHeight();
		}
		
		public static void setCustomYaw(float customYaw) {
			CustomYaw = customYaw;
			SetCustomYaw = true;
			mc.player.headYaw = customYaw;
		}
		
		public static void resetPlayerYaw() {
			SetCustomYaw = false;
		}
		
		public static float getCustomYaw() {
			
			return CustomYaw;
			
		}
		public static boolean SetCustomPitch = false;
		public static float CustomPitch = 0;
		
		public static void setCustomPitch(float customPitch) {
			CustomPitch = customPitch;
			SetCustomPitch = true;
		}
		
		public static void resetPlayerPitch() {
			SetCustomPitch = false;
		}
		
		public static float getCustomPitch() {
			
			return CustomPitch;
			
		}
		
		// Made by lavaflowglow 11/19/2020 3:39 AM
		
		public static WorldRenderer worldRenderer = mc.worldRenderer;
		public static Tessellator tessellator = Tessellator.getInstance();

		public static void fix(MatrixStack matrices) {
	        DrawableHelper.fill(matrices, 0, 0, 0, 0, -1);
	    }

		public static void drawTracerLine(double x, double y, double z, float red, float green, float blue, float alpha, float lineWdith) {
			GL11.glPushMatrix();
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        // GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_TEXTURE_2D);
	        GL11.glBlendFunc(770, 771);
	        GL11.glEnable(GL11.GL_BLEND);
	        GL11.glLineWidth(lineWdith);
	        GL11.glColor4f(red, green, blue, alpha);
	        GL11.glBegin(2);
	        GL11.glVertex3d(0.0D, 0.0D + mc.player.getStandingEyeHeight(), 0.0D);
	        GL11.glVertex3d(x, y, z);
	        GL11.glEnd();
	        GL11.glDisable(GL11.GL_BLEND);
	        GL11.glEnable(GL11.GL_TEXTURE_2D);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        GL11.glDisable(GL11.GL_LINE_SMOOTH);
	        GL11.glDisable(GL11.GL_BLEND);
	        // GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glPopMatrix();
		}
	    
	    public static int rainbow(final int delay) {
	        double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 20.0);
	        rainbowState %= 360.0;
	        return Color.getHSBColor((float)(rainbowState / 360.0), 0.8f, 0.7f).getRGB();
	    }

	    public static void setColor(Color c) {
	        GL11.glColor4d(c.getRed() / 255f, c.getGreen() / 255f, c.getBlue() / 255f, c.getAlpha() / 255f);
	    }
	    
	    public static void drawCircle(float cx, float cy, float r, int num_segments, int c) {
	        GL11.glPushMatrix();
	        cx *= 2.0f;
	        cy *= 2.0f;
	        float f = (float)(c >> 24 & 255) / 255.0f;
	        float f1 = (float)(c >> 16 & 255) / 255.0f;
	        float f2 = (float)(c >> 8 & 255) / 255.0f;
	        float f3 = (float)(c & 255) / 255.0f;
	        float theta = (float)(6.2831852 / (double)num_segments);
	        float p = (float)Math.cos(theta);
	        float s = (float)Math.sin(theta);
	        float x = r *= 2.0f;
	        float y = 0.0f;
	        RenderUtils.enableGL2D();
	        GL11.glScalef((float)0.5f, (float)0.5f, (float)0.5f);
	        GL11.glColor4f((float)f1, (float)f2, (float)f3, (float)f);
	        GL11.glBegin((int)2);
	        for (int ii = 0; ii < num_segments; ++ii) {
	            GL11.glVertex2f((float)(x + cx), (float)(y + cy));
	            float t = x;
	            x = p * x - s * y;
	            y = s * t + p * y;
	        }
	        GL11.glEnd();
	        GL11.glScalef((float)2.0f, (float)2.0f, (float)2.0f);
	        RenderUtils.disableGL2D();
	        GlStateManager._clearColor(1.0f, 1.0f, 1.0f, 1.0f);
	        GL11.glPopMatrix();
	    }
	    
	    public static void enableGL2D() {
	        GL11.glDisable((int)2929);
	        GL11.glEnable((int)3042);
	        GL11.glDisable((int)3553);
	        GL11.glBlendFunc((int)770, (int)771);
	        GL11.glDepthMask((boolean)true);
	        GL11.glEnable((int)2848);
	        GL11.glHint((int)3154, (int)4354);
	        GL11.glHint((int)3155, (int)4354);
	    }

	    public static void disableGL2D() {
	        GL11.glEnable((int)3553);
	        GL11.glDisable((int)3042);
	        GL11.glEnable((int)2929);
	        GL11.glDisable((int)2848);
	        GL11.glHint((int)3154, (int)4352);
	        GL11.glHint((int)3155, (int)4352);
	    }
		
		
		public static void disableGL3D() {
	        GL11.glEnable(3553);
	        GL11.glEnable(2929);
	        GL11.glDisable(3042);
	        GL11.glEnable(3008);
	        GL11.glDepthMask(true);
	        GL11.glCullFace(1029);
	        GL11.glDisable(2848);
	        GL11.glHint(3154, 4352);
	        GL11.glHint(3155, 4352);
	    }
		
		public static void enableGL3D(final float lineWidth) {
	        GL11.glDisable(3008);
	        GL11.glEnable(3042);
	        GL11.glBlendFunc(770, 771);
	        GL11.glDisable(3553);
	        GL11.glDisable(2929);
	        GL11.glDepthMask(false);
	        GL11.glEnable(2884);
	      //  Shaders.disableLightmap();
	        //Shaders.disableFog();
	        GL11.glEnable(2848);
	        GL11.glHint(3154, 4354);
	        GL11.glHint(3155, 4354);
	        GL11.glLineWidth(lineWidth);
	    }
		
		public static void setColor(final int colorHex) {
	        final float alpha = (colorHex >> 24 & 0xFF) / 255.0f;
	        final float red = (colorHex >> 16 & 0xFF) / 255.0f;
	        final float green = (colorHex >> 8 & 0xFF) / 255.0f;
	        final float blue = (colorHex & 0xFF) / 255.0f;
	        GL11.glColor4f(red, green, blue, (alpha == 0.0f) ? 1.0f : alpha);
	    }
		
		/*
		 * 	NOTIFICATION
		 */
		
		/*public static void prepareScissorBox(float x, float y, float x2, float y2) {
		      ScaledResolution scale = new ScaledResolution(mc);
		      int factor = scale.getScaleFactor();
		      GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
		   }*/
		 
		 public static void drawFilledCircle(final int xx, final int yy, final float radius, final int color) {
		        int sections = 50;
		        double dAngle = 2 * Math.PI / sections;
		        float x, y;

		        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

		        GL11.glEnable(GL11.GL_BLEND);
		        GL11.glDisable(GL11.GL_TEXTURE_2D);
		        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		        GL11.glEnable(GL11.GL_LINE_SMOOTH);
		        GL11.glBegin(GL11.GL_TRIANGLE_FAN);

		        for (int i = 0; i < sections; i++) {
		            x = (float) (radius * Math.sin((i * dAngle)));
		            y = (float) (radius * Math.cos((i * dAngle)));

		            GL11.glColor4f(new Color(color).getRed() / 255F, new Color(color).getGreen() / 255F, new Color(color).getBlue() / 255F, new Color(color).getAlpha() / 255F);
		            GL11.glVertex2f(xx + x, yy + y);
		        }

		        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

		        GL11.glEnd();

		        GL11.glPopAttrib();
		    }
		 
		 public static double getScaleFactor() {
		        return mc.getWindow().getScaleFactor();
		    }

		    public static int getScaledWidth() {
		        return mc.getWindow().getScaledWidth();
		    }

		    public static int getScaledHeight() {
		        return mc.getWindow().getScaledHeight();
		    }
		 
		 public static void drawRoundedRect(MatrixStack matrices, int left, int top, int right, int bottom, int smooth, Color color){
		        DrawableHelper.fill(matrices, left + smooth, top, right - smooth, bottom, color.getRGB());
		        DrawableHelper.fill(matrices, left, top + smooth, right, bottom - smooth, color.getRGB());
		        drawFilledCircle((int)left + smooth, (int)top + smooth, smooth, color.getRGB());
		        drawFilledCircle((int)right - smooth, (int)top + smooth, smooth, color.getRGB());
		        drawFilledCircle((int)right - smooth, (int)bottom - smooth, smooth, color.getRGB());
		        drawFilledCircle((int)left + smooth, (int)bottom - smooth, smooth, color.getRGB());

		    }
		 
		 public static void drawHLine(double x, double y, double x1, double y1, float width, int color) {
		        float var11 = (color >> 24 & 0xFF) / 255.0F;
		        float var6 = (color >> 16 & 0xFF) / 255.0F;
		        float var7 = (color >> 8 & 0xFF) / 255.0F;
		        float var8 = (color & 0xFF) / 255.0F;
		        GlStateManager._enableBlend();
		        GlStateManager._enableTexture();
		        GlStateManager._blendFuncSeparate(770, 771, 1, 0);
		        GlStateManager._clearColor(var6, var7, var8, var11);
		        GL11.glPushMatrix();
		        GL11.glLineWidth(width);
		        GL11.glBegin(GL11.GL_LINE_STRIP);
		        GL11.glVertex2d(x, y);
		        GL11.glVertex2d(x1, y1);
		        GL11.glEnd();

		        GL11.glLineWidth(1);


		        GL11.glPopMatrix();
		        GlStateManager._enableTexture();
		        GlStateManager._disableBlend();
		        GlStateManager._clearColor(1, 1, 1, 1);

		    }
		 
		 public static void drawBorderRect(double x, double y, double x1, double y1, int color, double lwidth) {
		        drawHLine(x, y, x1, y, (float) lwidth, color);
		        drawHLine(x1, y, x1, y1, (float) lwidth, color);
		        drawHLine(x, y1, x1, y1, (float) lwidth, color);
		        drawHLine(x, y1, x, y, (float) lwidth, color);
		    }
		 
		 /*public static void scissor(double x, double y, double width, double height) {
		        ScaledResolution sr = new ScaledResolution(mc);
		        final double scale = sr.getScaleFactor();

		        y = sr.getScaledHeight() - y;

		        x *= scale;
		        y *= scale;
		        width *= scale;
		        height *= scale;

		        GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
		    }*/
		 

		 /*
		    GL11.glPushMatrix();
			GL11.glEnable(3089);
			RenderUtils.scissor(mouseX, mouseY, mouseY, partialTicks);
			GL11.glDisable(3089);
			GL11.glPopMatrix();
		  
		  */

		 public static void drawCenteredStringWithShadow(MatrixStack matrices, TextRenderer textRenderer, String text, int x, int y, int color) {
			 Screen.drawStringWithShadow(matrices, textRenderer, text, x - textRenderer.getWidth(text) / 2, y, color);
		 }
		 
		 public static Vec3d getRenderPosition(BlockPos blockPos) {
	         double minX = blockPos.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
	         double minY = blockPos.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
	         double minZ = blockPos.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;
	         return new Vec3d(minX, minY, minZ);
		 }
		 
		 public static void setup3DRender(boolean disableDepth) {
		        RenderSystem.setShader(GameRenderer::getPositionColorShader);
		        RenderSystem.disableTexture();
		        RenderSystem.enableBlend();
		        RenderSystem.defaultBlendFunc();
		        if (disableDepth)
		            RenderSystem.disableDepthTest();
		        RenderSystem.depthMask(MinecraftClient.isFabulousGraphicsOrBetter());
		        RenderSystem.enableCull();
		    }
		 
		 public static void end3DRender() {
		        RenderSystem.enableTexture();
		        RenderSystem.disableCull();
		        RenderSystem.disableBlend();
		        RenderSystem.enableDepthTest();
		        RenderSystem.depthMask(true);
		        RenderSystem.setShader(GameRenderer::getPositionColorShader);
		    }
		 
		 public static void drawFilledBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		        Matrix4f matrix4f = matrixStack.peek().getModel();
		        Color color1 = color;

		        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		        if (draw)
		        	bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
		        float minX = (float)bb.minX;
		        float minY = (float)bb.minY;
		        float minZ = (float)bb.minZ;
		        float maxX = (float)bb.maxX;
		        float maxY = (float)bb.maxY;
		        float maxZ = (float)bb.maxZ;

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

		        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        if (draw) {
			        bufferBuilder.end();
			        BufferRenderer.draw(bufferBuilder);
		        }
		    }
		 
		 public static void drawOutlineBox(MatrixStack matrixStack, Box bb, Color color, boolean draw) {
		        Color color1 = color;
		        Matrix4f matrix4f = matrixStack.peek().getModel();

		        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
		        if (draw)
		        	bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES/*LINES*/, VertexFormats.POSITION_COLOR);

		        VoxelShape shape = VoxelShapes.cuboid(bb);
		        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
		            bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		            bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
		        });
		        if (draw) {
			        bufferBuilder.end();
			        BufferRenderer.draw(bufferBuilder);
		        }
		 }

		 public static Vec3d getEntityRenderPosition(Entity entity, double partial) {
		        double x = entity.prevX + ((entity.getX() - entity.prevX) * partial) - mc.getEntityRenderDispatcher().camera.getPos().x;
		        double y = entity.prevY + ((entity.getY() - entity.prevY) * partial) - mc.getEntityRenderDispatcher().camera.getPos().y;
		        double z = entity.prevZ + ((entity.getZ() - entity.prevZ) * partial) - mc.getEntityRenderDispatcher().camera.getPos().z;
		        return new Vec3d(x, y, z);
		 }

	    public static Vec3d getRenderPosition(double x, double y, double z, MatrixStack matrixStack) {
	        Matrix4f matrix = matrixStack.peek().getModel();
	        double minX = x - mc.getEntityRenderDispatcher().camera.getPos().x;
	        double minY = y - mc.getEntityRenderDispatcher().camera.getPos().y;
	        double minZ = z - mc.getEntityRenderDispatcher().camera.getPos().z;
	        Vector4f vector4f = new Vector4f((float)minX, (float)minY, (float)minZ, 1.f);
	        vector4f.transform(matrix);
	        return new Vec3d(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	    }
	
	    public static Vec3d getRenderPosition(Vec3d vec3d, MatrixStack matrixStack) {
	        Matrix4f matrix = matrixStack.peek().getModel();
	        double minX = vec3d.getX() - mc.getEntityRenderDispatcher().camera.getPos().x;
	        double minY = vec3d.getY() - mc.getEntityRenderDispatcher().camera.getPos().y;
	        double minZ = vec3d.getZ() - mc.getEntityRenderDispatcher().camera.getPos().z;
	        Vector4f vector4f = new Vector4f((float)minX, (float)minY, (float)minZ, 1.f);
	        vector4f.transform(matrix);
	        return new Vec3d(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	    }
	    
	    public static void drawEntityBox(MatrixStack matrixstack, Entity entity, double x, double y, double z, Color color) {
	        setup3DRender(true);
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, -1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);

	        Box bb = new Box(x - entity.getWidth() + 0.25, y, z - entity.getWidth() + 0.25, x + entity.getWidth() - 0.25, y + entity.getHeight() + 0.1, z + entity.getWidth() - 0.25);
	        if (entity instanceof ItemEntity)
	            bb = new Box(x - 0.15, y + 0.1f, z - 0.15, x + 0.15, y + 0.5, z + 0.15);


//	        drawFilledBox(matrixstack, bb, new Color(color.getRed(), color.getGreen(), color.getBlue(), 130), true);
	        RenderSystem.lineWidth(1.5f);

	        drawOutlineBox(matrixstack, bb, color, true);

	        end3DRender();
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, 1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);
	    }
	    
	    public static void drawNametag(MatrixStack matrixstack, Entity entity, double x, double y, double z, Color color) {
	        setup3DRender(true);
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, -1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);

	        DrawableHelper.fill(matrixstack, 10, 10, 100, 100, -1);
	        RenderSystem.lineWidth(1.5f);

	        end3DRender();
	        matrixstack.translate(x, y, z);
	        matrixstack.multiply(new Quaternion(new Vec3f(0, 1, 0), 0, true));
	        matrixstack.translate(-x, -y, -z);
	    }
	    
	    public static void setup2DRender(boolean disableDepth) {
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        if (disableDepth)
	            RenderSystem.disableDepthTest();
	    }

	    public static void end2DRender() {
	        RenderSystem.disableBlend();
	        RenderSystem.enableTexture();
	        RenderSystem.enableDepthTest();
	    }

	    public static void setup2DProjection() {
	        Matrix4x4 ortho = Matrix4x4.ortho2DMatrix(0, getScaledWidth(), getScaledHeight(), 0, -0.1f, 1000.f);
	        ShaderUtils.INSTANCE.setProjectionMatrix(ortho);
	        ShaderUtils.INSTANCE.setModelViewMatrix(Matrix4x4.copyFromRowMajor(RenderSystem.getModelViewMatrix()));
	    }
	    
	    public static void bindTexture(Identifier identifier) {
	        RenderSystem.setShaderTexture(0, identifier);
	    }
	    
	    public static void shaderColor(int hex) {
	        float alpha = (hex >> 24 & 0xFF) / 255.0F;
	        float red = (hex >> 16 & 0xFF) / 255.0F;
	        float green = (hex >> 8 & 0xFF) / 255.0F;
	        float blue = (hex & 0xFF) / 255.0F;
	        RenderSystem.setShaderColor(red, green, blue, alpha);
	    }
	    
	    public static int getPercentColor(float percent) {
	        if (percent <= 15)
	            return new Color(255, 0, 0).getRGB();
	        else if (percent <= 25)
	            return new Color(255, 75, 92).getRGB();
	        else if (percent <= 50)
	            return new Color(255, 123, 17).getRGB();
	        else if (percent <= 75)
	            return new Color(255, 234, 0).getRGB();
	        return new Color(0, 255, 0).getRGB();
	    }
	    
	    public static void fill(MatrixStack matrixStack, float x1, float y1, float x2, float y2, int color) {
	        Matrix4f matrix = matrixStack.peek().getModel();
	        float j;
	        if (x1 < x2) {
	            j = x1;
	            x1 = x2;
	            x2 = j;
	        }

	        if (y1 < y2) {
	            j = y1;
	            y1 = y2;
	            y2 = j;
	        }

	        float f = (float)(color >> 24 & 255) / 255.0F;
	        float g = (float)(color >> 16 & 255) / 255.0F;
	        float h = (float)(color >> 8 & 255) / 255.0F;
	        float k = (float)(color & 255) / 255.0F;
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        RenderSystem.enableBlend();
	        RenderSystem.disableTexture();
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        bufferBuilder.vertex(matrix, (float)x1, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y2, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x2, (float)y1, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.vertex(matrix, (float)x1, (float)y1, 0.0F).color(g, h, k, f).next();
	        bufferBuilder.end();
	        BufferRenderer.draw(bufferBuilder);
	        RenderSystem.enableTexture();
	        RenderSystem.disableBlend();
	    }
    
	    public static void drawFace(MatrixStack matrixStack, float x, float y, int renderScale, Identifier id) {
	        try {
	            bindTexture(id);
	            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
	            drawTexture(matrixStack, x, y, 8 * renderScale, 8 * renderScale, 40 * renderScale, 8 * renderScale, 8 * renderScale, 8 * renderScale, 64 * renderScale, 64 * renderScale);
	        }catch (Exception e){}
	    }
	    
	    public void drawTexture(MatrixStack matrices, float x, float y, float u, float v, float width, float height, int textureWidth, int textureHeight) {
	        drawTexture(matrices, x, y, width, height, u, v, width, height, textureWidth, textureHeight);
	    }

	    private static void drawTexture(MatrixStack matrices, float x, float y, float width, float height, float u, float v, float regionWidth, float regionHeight, int textureWidth, int textureHeight) {
	        drawTexture(matrices, x, x + width, y, y + height, 0, regionWidth, regionHeight, u, v, textureWidth, textureHeight);
	    }

	    private static void drawTexture(MatrixStack matrices, float x0, float y0, float x1, float y1, int z, float regionWidth, float regionHeight, float u, float v, int textureWidth, int textureHeight) {
	        drawTexturedQuad(matrices.peek().getModel(), x0, y0, x1, y1, z, (u + 0.0F) / (float)textureWidth, (u + (float)regionWidth) / (float)textureWidth, (v + 0.0F) / (float)textureHeight, (v + (float)regionHeight) / (float)textureHeight);
	    }

	    public static void drawTexturedQuad(Matrix4f matrices, float x0, float x1, float y0, float y1, float z, float u0, float u1, float v0, float v1) {
	        RenderSystem.setShader(GameRenderer::getPositionTexShader);
	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
	        bufferBuilder.vertex(matrices, (float)x0, (float)y1, (float)z).texture(u0, v1).next();
	        bufferBuilder.vertex(matrices, (float)x1, (float)y1, (float)z).texture(u1, v1).next();
	        bufferBuilder.vertex(matrices, (float)x1, (float)y0, (float)z).texture(u1, v0).next();
	        bufferBuilder.vertex(matrices, (float)x0, (float)y0, (float)z).texture(u0, v0).next();
	        bufferBuilder.end();
	        BufferRenderer.draw(bufferBuilder);
	    }
	    
	    public static void drawItem(ItemStack stack, float xPosition, float yPosition) {
	        drawItem(stack, xPosition, yPosition, 1);
	    }
	    public static void drawItem(ItemStack stack, float xPosition, float yPosition, float scale) {
	        String amountText = stack.getCount() != 1 ? stack.getCount() + "" : "";
	        IItemRenderer iItemRenderer = (IItemRenderer) mc.getItemRenderer();
	        iItemRenderer.renderItemIntoGUI(stack, xPosition, yPosition);
	        renderGuiItemOverlay(mc.textRenderer, stack, xPosition - 0.5f, yPosition + 1, scale, amountText);
	    }
	    public static void renderGuiItemOverlay(TextRenderer renderer, ItemStack stack, float x, float y, float scale, @Nullable String countLabel) {
	        if (!stack.isEmpty()) {
	            MatrixStack matrixStack = new MatrixStack();
	            if (stack.getCount() != 1 || countLabel != null) {
	                String string = countLabel == null ? String.valueOf(stack.getCount()) : countLabel;
	                matrixStack.translate(0.0D, 0.0D, (double)(mc.getItemRenderer().zOffset + 200.0F));
	                VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
	                renderer.draw(string, (float)(x + 19 - 2 - renderer.getWidth(string)), (float)(y + 6 + 3), 16777215, true, matrixStack.peek().getModel(), immediate, false, 0, 15728880);
	                immediate.draw();
	            }

	            if (stack.isItemBarVisible()) {
	                RenderSystem.disableDepthTest();
	                RenderSystem.disableTexture();
	                RenderSystem.disableBlend();
	                int i = stack.getItemBarStep();
	                int j = stack.getItemBarColor();
	                fill(matrixStack, x + 2, y + 13, x + 2 + 13, y + 13 + 2, 0xff000000);
	                fill(matrixStack, x + 2, y + 13, x + 2 + i, y + 13 + 1, new Color(j >> 16 & 255, j >> 8 & 255, j & 255, 255).getRGB());
	                RenderSystem.enableBlend();
	                RenderSystem.enableTexture();
	                RenderSystem.enableDepthTest();
	            }

	            ClientPlayerEntity clientPlayerEntity = mc.player;
	            float f = clientPlayerEntity == null ? 0.0F : clientPlayerEntity.getItemCooldownManager().getCooldownProgress(stack.getItem(), MinecraftClient.getInstance().getTickDelta());
	            if (f > 0.0F) {
	                RenderSystem.disableDepthTest();
	                RenderSystem.disableTexture();
	                RenderSystem.enableBlend();
	                RenderSystem.defaultBlendFunc();
	                Tessellator tessellator2 = Tessellator.getInstance();
	                BufferBuilder bufferBuilder2 = tessellator2.getBuffer();
	                renderGuiQuad(bufferBuilder2, x, y + MathHelper.floor(16.0F * (1.0F - f)), 16, MathHelper.ceil(16.0F * f), 255, 255, 255, 127);
	                RenderSystem.enableTexture();
	                RenderSystem.enableDepthTest();
	            }

	        }
	    }
	    
	    private static void renderGuiQuad(BufferBuilder buffer, float x, float y, float width, float height, int red, int green, int blue, int alpha) {
	        buffer.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
	        buffer.vertex((double) (x + 0), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + 0), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + width), (double) (y + height), 0.0D).color(red, green, blue, alpha).next();
	        buffer.vertex((double) (x + width), (double) (y + 0), 0.0D).color(red, green, blue, alpha).next();
	        Tessellator.getInstance().draw();
	    }
	    
	    public static Vec3d to2D(Vec3d worldPos, MatrixStack matrixStack) {
	        Vec3d bound = getRenderPosition(worldPos, matrixStack);
	        Vec3d twoD = to2D(bound.x, bound.y, bound.z);
	        return new Vec3d(twoD.x, twoD.y, twoD.z);
	    }
	    
	    private static Vec3d to2D(double x, double y, double z) {
	        int displayHeight = mc.getWindow().getHeight();
	        Vector3D screenCoords = new Vector3D();
	        int[] viewport = new int[4];
	        GL11.glGetIntegerv(GL11.GL_VIEWPORT, viewport);
	        Matrix4x4 matrix4x4Proj = Matrix4x4.copyFromColumnMajor(RenderSystem.getProjectionMatrix());
	        Matrix4x4 matrix4x4Model = Matrix4x4.copyFromColumnMajor(RenderSystem.getModelViewMatrix());
	        matrix4x4Proj.mul(matrix4x4Model).project((float) x, (float) y, (float) z, viewport, screenCoords);

	        return new Vec3d(screenCoords.x / getScaleFactor(), (displayHeight - screenCoords.y) / getScaleFactor(), screenCoords.z);
	    }
	    
	    public static Vec3d getPos(Entity entity, float yOffset, float partialTicks, MatrixStack matrixStack) {
	        Vec3d bound = getEntityRenderPosition(entity, partialTicks).add(0, yOffset, 0);
	        Vector4f vector4f = new Vector4f((float)bound.x, (float)bound.y, (float)bound.z, 1.f);
	        vector4f.transform(matrixStack.peek().getModel());
	        Vec3d twoD = to2D(vector4f.getX(), vector4f.getY(), vector4f.getZ());
	        return new Vec3d(twoD.x, twoD.y, twoD.z);
	    }

		public static Vec3d getRenderPosition(double x, double y, double z) {
        double minX = x - mc.getEntityRenderDispatcher().camera.getPos().x;
        double minY = y - mc.getEntityRenderDispatcher().camera.getPos().y;
        double minZ = z - mc.getEntityRenderDispatcher().camera.getPos().z;
        return new Vec3d(minX, minY, minZ);
    }
		
		public static void renderOutlineIntern(Vec3d start, Vec3d dimensions, MatrixStack stack, BufferBuilder buffer) {
	        Camera c = mc.gameRenderer.getCamera();
	        Vec3d camPos = c.getPos();
	        start = start.subtract(camPos);
	        Vec3d end = start.add(dimensions);
	        Matrix4f matrix = stack.peek().getModel();
	        float x1 = (float) start.x;
	        float y1 = (float) start.y;
	        float z1 = (float) start.z;
	        float x2 = (float) end.x;
	        float y2 = (float) end.y;
	        float z2 = (float) end.z;

	        buffer.vertex(matrix, x1, y1, z1).next();
	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x1, y1, z1).next();

	        buffer.vertex(matrix, x1, y2, z1).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();
	        buffer.vertex(matrix, x2, y2, z1).next();
	        buffer.vertex(matrix, x2, y2, z1).next();
	        buffer.vertex(matrix, x1, y2, z1).next();

	        buffer.vertex(matrix, x1, y1, z1).next();
	        buffer.vertex(matrix, x1, y2, z1).next();

	        buffer.vertex(matrix, x2, y1, z1).next();
	        buffer.vertex(matrix, x2, y2, z1).next();

	        buffer.vertex(matrix, x2, y1, z2).next();
	        buffer.vertex(matrix, x2, y2, z2).next();

	        buffer.vertex(matrix, x1, y1, z2).next();
	        buffer.vertex(matrix, x1, y2, z2).next();
	    }
		
		public static BufferBuilder renderPrepare(Color color) {
	        float red = color.getRed() / 255f;
	        float green = color.getGreen() / 255f;
	        float blue = color.getBlue() / 255f;
	        float alpha = color.getAlpha() / 255f;
	        RenderSystem.setShader(GameRenderer::getPositionShader);
	        GL11.glDepthFunc(GL11.GL_ALWAYS);
	        RenderSystem.setShaderColor(red, green, blue, alpha);
	        RenderSystem.lineWidth(2f);
	        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
	                VertexFormats.POSITION);
	        return buffer;
	    }
		
		public static void drawFilledBox(MatrixStack matrixStack, Box bb, int color, boolean draw) {
	        Matrix4f matrix4f = matrixStack.peek().getModel();
	        Color color1 = ColorUtils.getColor(color);

	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        if (draw)
	        	bufferBuilder.begin(VertexFormat.DrawMode.QUADS/*QUADS*/, VertexFormats.POSITION_COLOR);
	        float minX = (float)bb.minX;
	        float minY = (float)bb.minY;
	        float minZ = (float)bb.minZ;
	        float maxX = (float)bb.maxX;
	        float maxY = (float)bb.maxY;
	        float maxZ = (float)bb.maxZ;

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, maxX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, maxX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();

	        bufferBuilder.vertex(matrix4f, minX, minY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, minY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, maxZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        bufferBuilder.vertex(matrix4f, minX, maxY, minZ).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        if (draw) {
		        bufferBuilder.end();
		        BufferRenderer.draw(bufferBuilder);
	        }
	    }
		
		public static void drawFilledBox(MatrixStack matrixStack, Box bb, int color) {
	    	drawFilledBox(matrixStack, bb, color, true);
	    }
		
		public static void drawOutlineBox(MatrixStack matrixStack, Box bb, int color) {
	    	drawOutlineBox(matrixStack, bb, color, true);
	    }

	    public static void drawOutlineBox(MatrixStack matrixStack, Box bb, int color, boolean draw) {
	        Color color1 = ColorUtils.getColor(color);
	        Matrix4f matrix4f = matrixStack.peek().getModel();

	        BufferBuilder bufferBuilder = Tessellator.getInstance().getBuffer();
	        if (draw)
	        	bufferBuilder.begin(VertexFormat.DrawMode.DEBUG_LINES/*LINES*/, VertexFormats.POSITION_COLOR);

	        VoxelShape shape = VoxelShapes.cuboid(bb);
	        shape.forEachEdge((x1, y1, z1, x2, y2, z2) -> {
	            bufferBuilder.vertex(matrix4f, (float)x1, (float)y1, (float)z1).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	            bufferBuilder.vertex(matrix4f, (float)x2, (float)y2, (float)z2).color(color1.getRed(), color1.getGreen(), color1.getBlue(), color1.getAlpha()).next();
	        });
	        if (draw) {
		        bufferBuilder.end();
		        BufferRenderer.draw(bufferBuilder);
	        }
	    }

	    //you can call renderOutlineIntern multiple times to save performance
	    public static void renderOutline(Vec3d start, Vec3d dimensions, Color color, MatrixStack stack) {
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.enableBlend();
	        BufferBuilder buffer = renderPrepare(color);

	        renderOutlineIntern(start, dimensions, stack, buffer);

	        buffer.end();
	        BufferRenderer.draw(buffer);
	        GL11.glDepthFunc(GL11.GL_LEQUAL);
	        RenderSystem.disableBlend();
	    }

		public static void drawBox(MatrixStack matrixstack, Box bb, int color) {
        setup3DRender(true);

        drawFilledBox(matrixstack, bb, color & 0x70ffffff);
        RenderSystem.lineWidth(1);
        drawOutlineBox(matrixstack, bb, color);

        end3DRender();
    }
		
		public static void line(Vec3d start, Vec3d end, Color color, MatrixStack matrices) {
	        float red = color.getRed() / 255f;
	        float green = color.getGreen() / 255f;
	        float blue = color.getBlue() / 255f;
	        float alpha = color.getAlpha() / 255f;
	        Camera c = mc.gameRenderer.getCamera();
	        Vec3d camPos = c.getPos();
	        start = start.subtract(camPos);
	        end = end.subtract(camPos);
	        Matrix4f matrix = matrices.peek().getModel();
	        float x1 = (float) start.x;
	        float y1 = (float) start.y;
	        float z1 = (float) start.z;
	        float x2 = (float) end.x;
	        float y2 = (float) end.y;
	        float z2 = (float) end.z;
	        BufferBuilder buffer = Tessellator.getInstance().getBuffer();
	        RenderSystem.setShader(GameRenderer::getPositionColorShader);
	        GL11.glDepthFunc(GL11.GL_ALWAYS);
	        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
	        RenderSystem.defaultBlendFunc();
	        RenderSystem.enableBlend();
	        buffer.begin(VertexFormat.DrawMode.DEBUG_LINES,
	                VertexFormats.POSITION_COLOR);

	        buffer.vertex(matrix, x1, y1, z1).color(red, green, blue, alpha).next();
	        buffer.vertex(matrix, x2, y2, z2).color(red, green, blue, alpha).next();

	        buffer.end();

	        BufferRenderer.draw(buffer);
	        GL11.glDepthFunc(GL11.GL_LEQUAL);
	        RenderSystem.disableBlend();
	    }
		
		public static Vec3d center() {
			Vec3d pos = new Vec3d(0, 0, 1);

	        if (mc.options.bobView) {
	            MatrixStack bobViewMatrices = new MatrixStack();

	            bobView(bobViewMatrices);
	            bobViewMatrices.peek().getModel().invert();

	            pos = ((IMatrix4f) (Object) bobViewMatrices.peek().getModel()).mul(pos);
	        }
	        return new Vec3d(pos.x, -pos.y, pos.z)
	            .rotateX(-(float) Math.toRadians(mc.gameRenderer.getCamera().getPitch()))
	            .rotateY(-(float) Math.toRadians(mc.gameRenderer.getCamera().getYaw()))
	            .add(mc.gameRenderer.getCamera().getPos());
		}
		
		private static void bobView(MatrixStack matrices) {
	        Entity cameraEntity = MinecraftClient.getInstance().getCameraEntity();

	        if (cameraEntity instanceof PlayerEntity) {
	        	PlayerEntity playerEntity = (PlayerEntity)cameraEntity;
	            float f = MinecraftClient.getInstance().getTickDelta();
	            float g = playerEntity.horizontalSpeed - playerEntity.prevHorizontalSpeed;
	            float h = -(playerEntity.horizontalSpeed + g * f);
	            float i = MathHelper.lerp(f, playerEntity.prevStrideDistance, playerEntity.strideDistance);

	            matrices.translate(-(MathHelper.sin(h * 3.1415927f) * i * 0.5), -(-Math.abs(MathHelper.cos(h * 3.1415927f) * i)), 0);
	            matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(MathHelper.sin(h * 3.1415927f) * i * 3));
	            matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(Math.abs(MathHelper.cos(h * 3.1415927f - 0.2f) * i) * 5));
	        }
	    }
		
		public static Vec3d getCrosshairVector() {

	        ClientPlayerEntity player = mc.player;

	        float f = 0.017453292F;
	        float pi = (float) Math.PI;

	        assert player != null;
	        float f1 = MathHelper.cos(-player.getYaw() * f - pi);
	        float f2 = MathHelper.sin(-player.getYaw() * f - pi);
	        float f3 = -MathHelper.cos(-player.getPitch() * f);
	        float f4 = MathHelper.sin(-player.getPitch() * f);

	        return new Vec3d(f2 * f3, f4, f1 * f3).add(mc.player.getX(), mc.player.getY() + 1.5, mc.player.getZ());
	    }
		
		public static Box getBoundingBox(BlockPos pos) {
	        try {
	            assert mc.world != null;
	            return mc.world.getBlockState(pos).getOutlineShape(mc.world, pos).getBoundingBox().offset(pos);
	        } catch(Exception e) {
	            return null;
	        }
	    }
		
		public static void fillAndBorder(MatrixStack matrixStack, float left, float top, float right, float bottom, int bcolor, int icolor, float f) {
	        fill(matrixStack, left + f, top + f, right - f, bottom - f, icolor);
	        fill(matrixStack, left, top, left + f, bottom, bcolor);
	        fill(matrixStack, left + f, top, right, top + f, bcolor);
	        fill(matrixStack, left + f, bottom - f, right, bottom, bcolor);
	        fill(matrixStack, right - f, top + f, right, bottom - f, bcolor);
	    }

		public static void applyRegionalRenderOffset(MatrixStack matrixStack)
	{
		Vec3d camPos = mc.getBlockEntityRenderDispatcher().camera.getPos();
		BlockPos blockPos = mc.getBlockEntityRenderDispatcher().camera.getBlockPos();
		
		int regionX = (blockPos.getX() >> 9) * 512;
		int regionZ = (blockPos.getZ() >> 9) * 512;
		
		matrixStack.translate(regionX - camPos.x, -camPos.y,
			regionZ - camPos.z);
	}
}