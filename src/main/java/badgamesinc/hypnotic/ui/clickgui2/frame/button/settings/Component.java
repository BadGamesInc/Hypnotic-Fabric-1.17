package badgamesinc.hypnotic.ui.clickgui2.frame.button.settings;

import badgamesinc.hypnotic.settings.Setting;
import badgamesinc.hypnotic.ui.clickgui2.frame.button.Button;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.util.math.MatrixStack;

public class Component {

	MinecraftClient mc = MinecraftClient.getInstance();
	protected TextRenderer tr = mc.textRenderer;
	int x, y;
	public Setting setting;
	
	public Component(int x, int y, Setting setting, Button parent) {
		
	}
	
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		
	}
	
	public void mouseClicked(double mouseX, double mouseY, int button) {
		
	}
	
	public void mouseReleased(int button) {
		
	}
	
	public boolean hovered(int mouseX, int mouseY) {
		return false;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
}
