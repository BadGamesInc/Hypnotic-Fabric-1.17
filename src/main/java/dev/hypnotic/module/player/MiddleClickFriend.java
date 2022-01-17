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
package dev.hypnotic.module.player;

import org.lwjgl.glfw.GLFW;

import dev.hypnotic.config.friends.Friend;
import dev.hypnotic.config.friends.FriendManager;
import dev.hypnotic.event.EventTarget;
import dev.hypnotic.event.events.EventMouseButton;
import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.ColorUtils;
import dev.hypnotic.utils.Wrapper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class MiddleClickFriend extends Mod {

	public MiddleClickFriend() {
		super("MCF", "Middle click for friends", Category.MISC);
	}

	@EventTarget
	public void mouseClicked(EventMouseButton event) {
		if (event.getButton() == GLFW.GLFW_MOUSE_BUTTON_MIDDLE && event.getClickType() == EventMouseButton.ClickType.IN_GAME) {
			HitResult hitResult = mc.crosshairTarget;
			if (hitResult != null && hitResult.getType() == HitResult.Type.ENTITY) {
				PlayerEntity player = (PlayerEntity) ((EntityHitResult) hitResult).getEntity();
				if (!FriendManager.INSTANCE.isFriend(player) && !mc.options.keySneak.isPressed()) {
					FriendManager.INSTANCE.add(new Friend(player.getName().asString()));
					Wrapper.tellPlayer("Added " + ColorUtils.green + player.getName().asString() + ColorUtils.white + " to your friends list");
				} 
				if (mc.options.keySneak.isPressed() && FriendManager.INSTANCE.isFriend(player)) {
					for (Friend friend : FriendManager.INSTANCE.friends) {
						if (friend.name == player.getName().asString()) {
							FriendManager.INSTANCE.friends.remove(friend);
							Wrapper.tellPlayer("Removed " + ColorUtils.red + friend.name + ColorUtils.white + " from your friends list");
						}
					}
					
				}
			}
		}
	}
}