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
package dev.hypnotic.module.render;

import dev.hypnotic.module.Category;
import dev.hypnotic.module.Mod;
import dev.hypnotic.utils.mixin.ISimpleOption;

public class Fullbright extends Mod {

	public Fullbright() {
		super("Fullbright", "Turns your gamma very high", Category.RENDER);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTick() {
		((ISimpleOption<Double>)(Object)mc.options.getGamma()).setValueUnrestricted(100.0d);
		super.onTick();
	}
	
	@Override
	public void onDisable() {
		mc.options.getGamma().setValue(0.0);
		super.onDisable();
	}
}
