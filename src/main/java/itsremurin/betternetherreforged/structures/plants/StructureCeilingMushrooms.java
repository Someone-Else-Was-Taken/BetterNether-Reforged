package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureCeilingMushrooms implements IStructure {
	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (pos.getY() < 90) return;
		pos = pos.up();
		if (canPlace(world, pos)) BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.CEILING_MUSHROOMS.getDefaultState());
		if (canPlace(world, pos.north())) BlocksHelper.setWithUpdate(world, pos.north(), BlocksRegistry.CEILING_MUSHROOMS.getDefaultState());
		if (canPlace(world, pos.south())) BlocksHelper.setWithUpdate(world, pos.south(), BlocksRegistry.CEILING_MUSHROOMS.getDefaultState());
		if (canPlace(world, pos.east())) BlocksHelper.setWithUpdate(world, pos.east(), BlocksRegistry.CEILING_MUSHROOMS.getDefaultState());
		if (canPlace(world, pos.west())) BlocksHelper.setWithUpdate(world, pos.west(), BlocksRegistry.CEILING_MUSHROOMS.getDefaultState());
	}

	private boolean canPlace(IServerWorld world, BlockPos pos) {
		return BlocksHelper.isNetherrack(world.getBlockState(pos)) && world.getBlockState(pos.down()).getMaterial().isReplaceable();
	}
}