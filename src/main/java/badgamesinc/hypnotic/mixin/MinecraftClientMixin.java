package badgamesinc.hypnotic.mixin;

import static badgamesinc.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import badgamesinc.hypnotic.Hypnotic;
import badgamesinc.hypnotic.event.events.EventTick;
import badgamesinc.hypnotic.module.ModuleManager;
import badgamesinc.hypnotic.module.misc.DiscordRPCModule;
import badgamesinc.hypnotic.ui.HypnoticMainMenu;
import badgamesinc.hypnotic.utils.world.BlockIterator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.world.SelectWorldScreen;
import net.minecraft.client.render.RenderTickCounter;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow @Final private RenderTickCounter renderTickCounter;
	
	@Inject(at = @At("TAIL"), method = "updateWindowTitle()V")
	public void updateWindowTitle(CallbackInfo info) {
		MinecraftClient.getInstance().getWindow().setTitle(Hypnotic.fullName);
	}
	
	@Inject(at = @At("HEAD"), method = "tick")
    private void onPreTick(CallbackInfo info) {
		BlockIterator.INSTANCE.onTick();
		EventTick event = new EventTick();
		event.call();
		if (ModuleManager.INSTANCE.getModule(DiscordRPCModule.class).isEnabled()) {
			if (mc.player == null) {
				if (mc.currentScreen instanceof HypnoticMainMenu) DiscordRPCModule.status = "In main menu";
				if (mc.currentScreen instanceof SelectWorldScreen) DiscordRPCModule.status = "Browsing worlds";
				if (mc.currentScreen instanceof MultiplayerScreen) DiscordRPCModule.status = "Browsing servers";
			} else {
				if (mc.isInSingleplayer()) DiscordRPCModule.status = "Playing singlelplayer";
				if (mc.getCurrentServerEntry() != null && !mc.isInSingleplayer()) DiscordRPCModule.status = "Playing multiplayer on " + mc.getCurrentServerEntry().address;
			}
		}
	}
	
	@Inject(at = @At("TAIL"), method = "scheduleStop")
	public void onShutdown(CallbackInfo ci) {
		Hypnotic.INSTANCE.shutdown();
	}
}
