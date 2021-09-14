package itsremurin.betternetherreforged.structures.decorations;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureGeyser implements IStructure {
	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (BlocksHelper.isNetherrack(world.getBlockState(pos.down())))
			BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.GEYSER.getDefaultState());
	}
}