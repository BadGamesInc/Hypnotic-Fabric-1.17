package dev.hypnotic.hypnotic_client.ui.clickgui;

import static dev.hypnotic.hypnotic_client.utils.MCUtils.mc;

import java.awt.Color;

import dev.hypnotic.hypnotic_client.module.Category;
import dev.hypnotic.hypnotic_client.ui.HypnoticScreen;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import dev.hypnotic.hypnotic_client.utils.font.FontManager;
import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;

public class CategoryButton {

	int x, y, width, height, fadeIn;
	Category category;
	ClickGUI parent;
	
	public CategoryButton(int x, int y, Category category, ClickGUI parent) {
        this.x = x;
        this.y = y;
        this.category = category;
        this.parent = parent;
        this.width = 100;
        this.height = 30;
        this.fadeIn = 0;
    }
	
	public void init() {
		fadeIn = 0;
	}
	
	
	public void render(MatrixStack matrices, int mouseX, int mouseY) {
		this.height = 30;
		this.width = 100;
		if (fadeIn < 255)
			fadeIn+=5;
		if (hovered(mouseX, mouseY))
			RenderUtils.fill(matrices, x, y, x + width, y + height, new Color(0, 0, 0, 100).getRGB());
		if (parent.dragging && parent.currentCategory == this.category) {
			RenderUtils.fill(matrices, x, y, x + width, y + height, ColorUtils.defaultClientColor);
		}
		HypnoticScreen.font.drawWithShadow(matrices, category.name, x + width / 3, y + 11, -1);
		FontManager.icons.drawWithShadow(matrices, category.icon, x + width / 6, y + 11, -1);
		
	}
	
	public boolean hovered(double mouseX, double mouseY) {
		return mouseX > x && mouseX < x + width && mouseY > y && mouseY < y + height;
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered(mouseX, mouseY) && button == 0) {
			parent.currentCategory = category;
			mc.getSoundManager().play(PositionedSoundInstance.ambient(SoundEvents.UI_BUTTON_CLICK, 1, 1));
		}
	}
}