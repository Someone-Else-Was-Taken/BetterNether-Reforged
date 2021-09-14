package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.blocks.BlockWartSeed;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureWartBush implements IStructure {
	private static final Direction[] DIRS = new Direction[] { Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST };

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (world.isAirBlock(pos)) {
			BlocksHelper.setWithoutUpdate(world, pos, Blocks.NETHER_WART_BLOCK.getDefaultState());
			for (Direction dir : DIRS)
				setSeed(world, pos, dir);
		}
	}

	private void setSeed(IServerWorld world, BlockPos pos, Direction dir) {
		BlockPos p = pos.offset(dir);
		if (world.isAirBlock(p))
			BlocksHelper.setWithoutUpdate(world, p, BlocksRegistry.WART_SEED.getDefaultState().with(BlockWartSeed.FACING, dir));
	}
}