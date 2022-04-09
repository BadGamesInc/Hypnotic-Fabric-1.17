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
*/
package dev.hypnotic.mixin;

import static dev.hypnotic.utils.MCUtils.mc;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import dev.hypnotic.module.ModuleManager;
import dev.hypnotic.module.exploit.AntiCactus;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
* @author BadGamesInc
*/
@Mixin(CactusBlock.class)
public class CactusBlockMixin {

	@Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
	public void onDamage(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
		System.out.println("e");
		if (ModuleManager.INSTANCE.getModule(AntiCactus.class).isEnabled() && entity == mc.player) ci.cancel();
	}
}
