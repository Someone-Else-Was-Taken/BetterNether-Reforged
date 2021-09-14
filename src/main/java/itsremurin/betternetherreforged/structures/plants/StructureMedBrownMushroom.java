package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.blocks.BlockBrownLargeMushroom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureMedBrownMushroom implements IStructure {
	private static final Mutable POS = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		Block under;
		if (world.getBlockState(pos.down()).getBlock() == BlocksRegistry.NETHER_MYCELIUM) {
			for (int i = 0; i < 10; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 2);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2);
				if (((x + z + 1) & 1) == 0) {
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
		int size = 2 + random.nextInt(3);
		for (int y = 1; y <= size; y++)
			if (!world.isAirBlock(pos.up(y))) {
				if (y < 3)
					return;
				size = y - 1;
				break;
			}
		boolean hasAir = true;
		for (int x = -1; x < 2; x++)
			for (int z = -1; z < 2; z++)
				hasAir = hasAir && world.isAirBlock(pos.up(size).add(x, 0, z));
		if (hasAir) {
			BlockState middle = BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.MIDDLE);
			BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.BOTTOM));
			for (int y = 1; y < size; y++)
				BlocksHelper.setWithUpdate(world, pos.up(y), middle);
			pos = pos.up(size);
			BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.TOP));
			BlocksHelper.setWithUpdate(world, pos.north(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.SIDE_N));
			BlocksHelper.setWithUpdate(world, pos.south(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.SIDE_S));
			BlocksHelper.setWithUpdate(world, pos.east(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.SIDE_E));
			BlocksHelper.setWithUpdate(world, pos.west(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.SIDE_W));
			BlocksHelper.setWithUpdate(world, pos.north().east(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.CORNER_N));
			BlocksHelper.setWithUpdate(world, pos.north().west(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.CORNER_W));
			BlocksHelper.setWithUpdate(world, pos.south().east(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.CORNER_E));
			BlocksHelper.setWithUpdate(world, pos.south().west(), BlocksRegistry.BROWN_LARGE_MUSHROOM
					.getDefaultState()
					.with(BlockBrownLargeMushroom.SHAPE, BlockBrownLargeMushroom.BrownMushroomShape.CORNER_S));
		}
	}
}

