package dev.hypnotic.hypnotic_client.module.player;

import dev.hypnotic.hypnotic_client.event.EventTarget;
import dev.hypnotic.hypnotic_client.event.events.EventSendPacket;
import dev.hypnotic.hypnotic_client.mixin.PlayerMoveC2SPacketAccessor;
import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.module.Mod;
import dev.hypnotic.hypnotic_client.settings.settingtypes.ModeSetting;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.RaycastContext;

public class NoFall extends Mod {

	public ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet");
	
    public NoFall() {
        super("NoFall", "Prevents you from taking fall damage", Category.PLAYER);
        addSetting(mode);
    }

    @Override
    public void onTick() {
    	this.setDisplayName("NoFall " + ColorUtils.gray + mode.getSelected());
        super.onTick();
    }
    
    @EventTarget
    private void onSendPacket(EventSendPacket event) {
    	if (mc.player == null) return;
        if (mc.player.getAbilities().creativeMode
            || !(event.getPacket() instanceof PlayerMoveC2SPacket) || mc.player.isOnGround() || mc.player.fallDistance < 2.5) return;


        if ((mc.player.isFallFlying()) && mc.player.getVelocity().y < 1) {
            BlockHitResult result = mc.world.raycast(new RaycastContext(
                mc.player.getPos(),
                mc.player.getPos().subtract(0, 0.5, 0),
                RaycastContext.ShapeType.OUTLINE,
                RaycastContext.FluidHandling.NONE,
                mc.player)
            );

            if (result != null && result.getType() == HitResult.Type.BLOCK) {
                ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
            }
        }
        else {
            ((PlayerMoveC2SPacketAccessor) event.getPacket()).setOnGround(true);
        }
    }
}