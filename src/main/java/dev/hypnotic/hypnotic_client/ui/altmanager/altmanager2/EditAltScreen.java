package dev.hypnotic.hypnotic_client.ui.altmanager.altmanager2;

import java.awt.Color;

import dev.hypnotic.hypnotic_client.utils.render.RenderUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class EditAltScreen extends Screen {

	private Screen previousScreen;
	public TextFieldWidget usernameField;
	public TextFieldWidget passwordField;
	private String status;
	private Alt alt;
	
	public EditAltScreen(Screen previousScreen, Alt alt) {
		super(new LiteralText("EditAlt"));
		this.previousScreen = previousScreen;
		this.alt = alt;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		this.renderBackground(matrices);
		usernameField.render(matrices, mouseX, mouseY, delta);
		passwordField.render(matrices, mouseX, mouseY, delta);
		RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, status, this.width / 2, this.height / 2 - 100, -1);
		if (usernameField.getText().isEmpty() && !usernameField.isFocused())
			RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, "Username", this.width / 2 - 70, this.height / 2 + 6, new Color(100, 100, 100).getRGB());
		if (passwordField.getText().isEmpty() && !passwordField.isFocused())
			RenderUtils.drawCenteredStringWithShadow(matrices, textRenderer, "Password", this.width / 2 - 70, this.height / 2 + 36, new Color(100, 100, 100).getRGB());
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height / 2 + 60, 200, 20, new LiteralText("Confirm"), (button) -> {
			
			this.status = "Edited alt";
			alt.setEmail(usernameField.getText());
	        alt.setPassword(passwordField.getText());
	    }))).active = true;
		((ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 - 100, height / 2 + 85, 200, 20, new LiteralText("Back"), (button) -> {
	         MinecraftClient.getInstance().setScreen(previousScreen);
	    }))).active = true;
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	protected void init() {
		usernameField = new TextFieldWidget(textRenderer, width / 2 - 100, height / 2, 200, 20, new LiteralText("Username"));
		passwordField = new TextFieldWidget(textRenderer, width / 2 - 100, height / 2 + 30, 200, 20, new LiteralText("Password"));
		this.addSelectableChild(usernameField);
		this.addSelectableChild(passwordField);
		usernameField.setText(alt.getEmail());
		passwordField.setText(alt.getPassword());
		status = "Idle...";
		super.init();
	}
	
	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		usernameField.mouseClicked(mouseX, mouseY, button);
		passwordField.mouseClicked(mouseX, mouseY, button);
		return super.mouseClicked(mouseX, mouseY, button);
	}
	
	@Override
	public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
		return super.keyPressed(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
		return super.keyReleased(keyCode, scanCode, modifiers);
	}
	
	@Override
	public boolean charTyped(char chr, int modifiers) {
		return super.charTyped(chr, modifiers);
	}
}