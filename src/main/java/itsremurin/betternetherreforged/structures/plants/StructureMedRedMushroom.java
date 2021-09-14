package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.blocks.BlockRedLargeMushroom;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureMedRedMushroom implements IStructure {
	private static final Mutable POS = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		Block under;
		if (world.getBlockState(pos.down()).getBlock() == BlocksRegistry.NETHER_MYCELIUM) {
			for (int i = 0; i < 10; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 2);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2);
				if (((x + z) & 1) == 0) {
					if (random.nextBoolean()) {
						x += random.nextBoolean() ? 1 : -1;
					}
					else {
						z += random.nextBoolean() ? 1 : -1;
					}
				}
				int y = pos.getY() + random.nextInt(6);
				for (int j = 0; j < 12; j++) {
					POS.setPos(x, y - j, z);
					under = world.getBlockState(POS.down()).getBlock();
					if (under == BlocksRegistry.NETHER_MYCELIUM) {
						grow(world, POS, random);
					}
				}
			}
		}
	}

	public void grow(IServerWorld world, BlockPos pos, Random random) {
		int size = 1 + random.nextInt(4);
		for (int y = 1; y <= size; y++)
			if (!world.isAirBlock(pos.up(y))) {
				if (y == 1)
					return;
				size = y - 1;
				break;
			}
		BlockState middle = BlocksRegistry.RED_LARGE_MUSHROOM.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.MIDDLE);
		for (int y = 1; y < size; y++)
			BlocksHelper.setWithUpdate(world, pos.up(y), middle);
		BlocksHelper.setWithUpdate(world, pos.up(size), BlocksRegistry.RED_LARGE_MUSHROOM.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.TOP));
		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.RED_LARGE_MUSHROOM.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.BOTTOM));
	}
}

