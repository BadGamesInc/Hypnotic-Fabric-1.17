package badgamesinc.hypnotic.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import com.mojang.blaze3d.systems.RenderSystem;

import badgamesinc.hypnotic.event.events.EventRender2DNoScale;
import badgamesinc.hypnotic.event.events.EventRender3D;
import badgamesinc.hypnotic.event.events.EventRenderHand;
import badgamesinc.hypnotic.module.render.Freecam;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.utils.mixin.IVec3d;
import badgamesinc.hypnotic.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

	@Shadow public abstract void updateTargetedEntity(float tickDelta);
	
	@Inject(method = "renderWorld", at = @At(value = "INVOKE_STRING", target = "Lnet/minecraft/util/profiler/Profiler;swap(Ljava/lang/String;)V", args = { "ldc=hand" }), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onRenderWorld(float tickDelta, long limitTime, MatrixStack matrices, CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
		if (mc == null || mc.world == null || mc.player == null) return;

        mc.getProfiler().push("hypnotic-client_render");

        EventRender3D event = new EventRender3D(matrices, tickDelta, mc.getCameraEntity().getPos().x, mc.getCameraEntity().getPos().y, mc.getCameraEntity().getPos().z);

        event.call();
        RenderSystem.applyModelViewMatrix();
        mc.getProfiler().pop();
    }
	
	@Inject(method = "render", at = @At(value = "INVOKE", target = "net/minecraft/client/render/WorldRenderer.drawEntityOutlinesFramebuffer()V"))
    public void renderForEvent(float float_1, long long_1, boolean boolean_1, CallbackInfo ci) {
        RenderUtils.setup2DProjection();
        new EventRender2DNoScale().call();
    }
	
	@Inject(method = "renderHand", at = @At("HEAD"), cancellable = true)
    public void renderHand(MatrixStack matrices, Camera camera, float tickDelta, CallbackInfo ci) {
        EventRenderHand event = new EventRenderHand(matrices);
        event.call();
        if (event.isCancelled())
            ci.cancel();
    }
	
	private boolean freecamSet = false;

    @Inject(method = "updateTargetedEntity", at = @At("HEAD"), cancellable = true)
    private void updateTargetedEntityInvoke(float tickDelta, CallbackInfo info) {
        Freecam freecam = ModuleManager.INSTANCE.getModule(Freecam.class);

        if ((freecam.isEnabled()) && mc.getCameraEntity() != null && !freecamSet) {
            info.cancel();
            Entity cameraE = mc.getCameraEntity();

            double x = cameraE.getX();
            double y = cameraE.getY();
            double z = cameraE.getZ();
            double prevX = cameraE.prevX;
            double prevY = cameraE.prevY;
            double prevZ = cameraE.prevZ;
            float yaw = cameraE.getYaw();
            float pitch = cameraE.getPitch();
            float prevYaw = cameraE.prevYaw;
            float prevPitch = cameraE.prevPitch;

                ((IVec3d) cameraE.getPos()).set(freecam.pos.x, freecam.pos.y - cameraE.getEyeHeight(cameraE.getPose()), freecam.pos.z);
                cameraE.prevX = freecam.prevPos.x;
                cameraE.prevY = freecam.prevPos.y - cameraE.getEyeHeight(cameraE.getPose());
                cameraE.prevZ = freecam.prevPos.z;

            freecamSet = true;
            updateTargetedEntity(tickDelta);
            freecamSet = false;

            ((IVec3d) cameraE.getPos()).set(x, y, z);
            cameraE.prevX = prevX;
            cameraE.prevY = prevY;
            cameraE.prevZ = prevZ;
            cameraE.setYaw(yaw);
            cameraE.setPitch(pitch);
            cameraE.prevYaw = prevYaw;
            cameraE.prevPitch = prevPitch;
        }
    }
}