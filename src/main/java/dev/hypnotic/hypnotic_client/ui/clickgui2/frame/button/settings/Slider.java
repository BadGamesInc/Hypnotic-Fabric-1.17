package dev.hypnotic.hypnotic_client.ui.clickgui2.frame.button.settings;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

import dev.hypnotic.hypnotic_client.settings.Setting;
import dev.hypnotic.hypnotic_client.settings.settingtypes.NumberSetting;
import dev.hypnotic.hypnotic_client.ui.HypnoticScreen;
import dev.hypnotic.hypnotic_client.ui.clickgui2.frame.button.Button;
import dev.hypnotic.hypnotic_client.utils.ColorUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class Slider extends Component {

	private NumberSetting numSet = (NumberSetting)setting;
	private Button parent;
	private boolean sliding;
	
	public Slider(int x, int y, Button parent, Setting setting) {
		super(x, y, setting, parent);
		this.parent = parent;
		this.setting = setting;
		this.numSet = (NumberSetting)setting;
		numSet.displayName = numSet.name + ": " + numSet.getValue();
	}
	
	double renderWidth;
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, int offset) {
		numSet.displayName = numSet.name + ": " + numSet.getValue();
		Color color = parent.parent.color;
		double diff = Math.min(parent.getWidth(), Math.max(0, mouseX - parent.getX()));

		double min = numSet.getMin();
		double max = numSet.getMax();
		
		renderWidth = (parent.getWidth()) * (numSet.getValue() - min) / (max - min);
		
		if (sliding) {
			if (diff == 0) {
				numSet.setValue(numSet.getMin());
			}
			else {
				double newValue = roundToPlace(((diff / parent.getWidth()) * (max - min) + min), 2);
				numSet.setValue(newValue);
			}
		}
		
		Screen.fill(matrices, parent.getX(), parent.getY() + offset + parent.getHeight(), parent.getX() + parent.getWidth(), parent.getY() + parent.getHeight() * 2 + offset, color.darker().darker().getRGB());
		Screen.fill(matrices, parent.getX(), parent.getY() + offset + parent.getHeight(), (int) (parent.getX() + renderWidth), parent.getY() + parent.getHeight() * 2 + offset, color.darker().getRGB());
		HypnoticScreen.fontSmall.drawWithShadow(matrices, ColorUtils.gray + numSet.name + ": " + ColorUtils.reset + numSet.getValue(), parent.getX() + 4, parent.getY() + offset  + (parent.getHeight()) + (parent.getHeight() / 4), -1);
		super.render(matrices, mouseX, mouseY, offset);
	}
	
	private static double roundToPlace(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	@Override
	public boolean hovered(int mouseX, int mouseY) {
		return mouseX >= parent.getX() && mouseX <= parent.getX() + parent.getWidth() && mouseY >= parent.getY() + this.offset + parent.getHeight() && mouseY <= parent.getY() + this.offset + parent.getHeight() * 2;
	}
	
	@Override
	public void mouseClicked(double mouseX, double mouseY, int button) {
		if (hovered((int)mouseX, (int)mouseY) && button == 0 && parent.isExtended()) {
			this.sliding = true;
		}
		super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public void mouseReleased(int button) {
		if (button == 0)
			sliding = false;
		super.mouseReleased(button);
	}
}