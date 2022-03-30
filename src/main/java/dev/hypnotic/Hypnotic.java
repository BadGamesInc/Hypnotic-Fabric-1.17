/*
* Copyright (C) 2022 Hypnotic Development
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU General Public License for more details.
*
* You should have received a copy of the GNU General Public License
* along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package dev.hypnotic;

import static dev.hypnotic.utils.MCUtils.mc;

import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import dev.hypnotic.config.ConfigManager;
import dev.hypnotic.config.SaveLoad;
import dev.hypnotic.event.EventManager;
import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.misc.IRC;
import dev.hypnotic.module.render.CustomFont;
import dev.hypnotic.scripting.ScriptManager;
import dev.hypnotic.ui.HUD;
import dev.hypnotic.ui.OptionsScreen;
import dev.hypnotic.ui.altmanager.altmanager2.AltsFile;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.IRCClient;
import dev.hypnotic.utils.font.FontManager;
import dev.hypnotic.utils.input.MouseUtils;
import dev.hypnotic.utils.player.DamageUtils;
import dev.hypnotic.utils.render.RenderUtils;
import dev.hypnotic.utils.world.BlockIterator;
import ladysnake.satin.api.event.ShaderEffectRenderCallback;
import ladysnake.satin.api.managed.ManagedShaderEffect;
import ladysnake.satin.api.managed.ShaderEffectManager;
import ladysnake.satin.api.managed.uniform.Uniform1f;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.util.Identifier;

public class Hypnotic implements ModInitializer {

	public static final Hypnotic INSTANCE = new Hypnotic();
	public static final Executor EXECUTOR = Executors.newCachedThreadPool();
	public static final Logger LOGGER = LogManager.getLogger("Hypnotic");
	public static String name = "Hypnotic",
			version = "r1000",
			fullName = name + "-" + version,
			hypnoticDir = mc.runDirectory.getPath() + File.separator + "Hypnotic",
			scriptDir = hypnoticDir + "/scripts",
			chatPrefix = ColorUtils.red + name + ColorUtils.gray + ": ";
	public ModuleManager moduleManager;
	public ScriptManager scriptManager;
	public EventManager eventManager;
	public ConfigManager cfgManager;
	public SaveLoad saveload;
	public String[] devUUIDs = {
			"09e5dd42-19b9-488a-bb4b-cc19bdf068b7",
			"c0052794-2f10-4f2c-b535-150db217f45d"
	};
	
	private boolean hasShutdown = false;
	
	public final ManagedShaderEffect blur = ShaderEffectManager.getInstance().manage(new Identifier("hypnotic", "shaders/post/fade_in_blur.json"),
            shader -> shader.setUniformValue("Radius", 8f));
	private final Uniform1f blurProgress = blur.findUniform1f("Progress");
	private float start;
	public final ManagedShaderEffect bloom = ShaderEffectManager.getInstance().manage(new Identifier("hypnotic", "shaders/post/bloom.json"));
	
	/*
	 * Called when Minecraft initializes.
	 * This is called AFTER mixins are injected
	 * Should probably inject the register function 
	 * into MinecraftClient.java but i'm too lazy
	 */
	@Override
	public void onInitialize() {
		System.out.println("Loading Hypnotic stuff");
		ModuleManager.INSTANCE.loadModules();
		register();
		hasShutdown = false;
	}
	
	/*
	 * Registers all of the good stuff
	 */
	private void register() {
		moduleManager = ModuleManager.INSTANCE;
		scriptManager = ScriptManager.INSTANCE;
		eventManager = EventManager.INSTANCE;
		EventManager.INSTANCE.register(HUD.INSTANCE);
		EventManager.INSTANCE.register(DamageUtils.getInstance());
		EventManager.INSTANCE.register(BlockIterator.INSTANCE);
		EventManager.INSTANCE.register(MouseUtils.class);
		ShaderEffectRenderCallback.EVENT.register(tickDelta -> {
			
			if (start < 1 && mc.currentScreen != null) start += 0.05f;
			else if (start > 0 && mc.currentScreen == null) start -= 0.05f;
			if ((start > 0) && mc.world != null && !OptionsScreen.INSTANCE.disableBlur.isEnabled()) {
				blur.setUniformValue("Radius", OptionsScreen.INSTANCE.blurIntensity.getValueFloat());
				blurProgress.set(mc.currentScreen instanceof ChatScreen ? 1f : start);
				if (mc.currentScreen instanceof ChatScreen) {
					start = 0;
					RenderUtils.startScissor(2, mc.getWindow().getScaledHeight() - 14, mc.getWindow().getScaledWidth() - 2, 12);
				}
				blur.render(tickDelta);
				if (mc.currentScreen instanceof ChatScreen) RenderUtils.endScissor();
			}
		});
	}
	
	/*
	 * Loads all* of the stuff that should be saved
	 */
	public void loadFiles() {
		SaveLoad.INSTANCE.load();
		if (ConfigManager.INSTANCE.config.exists()) {
            ConfigManager.INSTANCE.loadConfig();
        }
		ConfigManager.INSTANCE.loadConfigs();
        AltsFile.INSTANCE.loadAlts();
        if (ModuleManager.INSTANCE.getModule(CustomFont.class).isEnabled()) FontManager.setMcFont(false);
        else FontManager.setMcFont(true);
        
        if (ModuleManager.INSTANCE.getModule(IRC.class).isEnabled()) IRCClient.INSTNACE.init("");
        
        Thread configDaemon = new Thread(() -> {
            while (!hasShutdown && ModuleManager.INSTANCE.modules != null) {
                try {
                    Thread.sleep(30000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ConfigManager.INSTANCE.saveConfig();
                SaveLoad.INSTANCE.save();
            }
        });
        configDaemon.setDaemon(true);
        configDaemon.start();
	}
	
	public void shutdown() {
		if (!hasShutdown) {
			System.out.println("Stopping Hypnotic services...");
			AltsFile.INSTANCE.saveAlts();
			ConfigManager.INSTANCE.saveConfig();
			ConfigManager.INSTANCE.saveAll();
			SaveLoad.INSTANCE.save();
			hasShutdown = true;
		}
	}
}
