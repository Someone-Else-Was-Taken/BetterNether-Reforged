package itsremurin.betternetherreforged.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.util.IWorldPosCallable;


@Mixin(WorkbenchContainer.class)
public abstract class WorkbenchContainerMixin {
	@Shadow
	@Final
	private IWorldPosCallable worldPosCallable;

	@Inject(method = "canInteractWith", at = @At("HEAD"), cancellable = true)
	private void canUse(PlayerEntity player, CallbackInfoReturnable<Boolean> info) {
		info.setReturnValue(worldPosCallable.applyOrElse((world, pos) -> {
			return world.getBlockState(pos).getBlock() instanceof CraftingTableBlock;
		}, true));
		info.cancel();
	}
}