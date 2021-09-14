package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureWall implements IStructure {
	private static final Direction[] DIRECTIONS = HorizontalBlock.HORIZONTAL_FACING.getAllowedValues().toArray(new Direction[] {});
	private static final Direction[] SHUFFLED = new Direction[DIRECTIONS.length];
	private final Block plantBlock;

	public StructureWall(Block plantBlock) {
		this.plantBlock = plantBlock;
	}

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (world.isAirBlock(pos)) {
			BlockState state = getPlacementState(world, pos, random);
			if (state != null)
				BlocksHelper.setWithoutUpdate(world, pos, state);
		}
	}

	private BlockState getPlacementState(IServerWorld world, BlockPos pos, Random random) {
		BlockState blockState = plantBlock.getDefaultState();
		shuffle(random);
		for (int i = 0; i < 4; i++) {
			Direction direction = SHUFFLED[i];
			Direction direction2 = direction.getOpposite();
			blockState = blockState.with(HorizontalBlock.HORIZONTAL_FACING, direction2);
			if (blockState.isValidPosition(world, pos)) {
				return blockState;
			}
		}
		return null;
	}

	private void shuffle(Random random) {
		for (int i = 0; i < 4; i++)
			SHUFFLED[i] = DIRECTIONS[i];
		for (int i = 0; i < 4; i++) {
			int i2 = random.nextInt(4);
			Direction d = SHUFFLED[i2];
			SHUFFLED[i2] = SHUFFLED[i];
			SHUFFLED[i] = d;
		}
	}
}
