package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockWillowBranch;
import someoneelse.betternetherreforged.blocks.BlockWillowLeaves;
import someoneelse.betternetherreforged.blocks.BlockWillowTrunk;
import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureWillow implements IStructure {
	private static final Direction[] HOR = HorizontalBlock.HORIZONTAL_FACING.getAllowedValues().toArray(new Direction[] {});

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (!BlocksHelper.isNetherGround(world.getBlockState(pos.down())))
			return;

		int h2 = 5 + random.nextInt(3);

		int mh = BlocksHelper.upRay(world, pos.up(), h2);
		if (mh < 5)
			return;

		h2 = Math.min(h2, mh);

		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.WILLOW_TRUNK.getDefaultState().with(BlockWillowTrunk.SHAPE, TripleShape.BOTTOM));
		for (int h = 1; h < h2; h++)
			if (world.isAirBlock(pos.up(h)))
				BlocksHelper.setWithUpdate(world, pos.up(h), BlocksRegistry.WILLOW_TRUNK.getDefaultState().with(BlockWillowTrunk.SHAPE, TripleShape.MIDDLE));
		if (world.isAirBlock(pos.up(h2)))
			BlocksHelper.setWithUpdate(world, pos.up(h2), BlocksRegistry.WILLOW_TRUNK.getDefaultState().with(BlockWillowTrunk.SHAPE, TripleShape.TOP));

		for (int i = 0; i < 4; i++)
			branch(world, pos.up(h2).offset(HOR[i]), 3 + random.nextInt(2), random, HOR[i], pos.up(h2), 0);

		BlocksHelper.setWithUpdate(world, pos.up(h2 + 1), BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, Direction.UP));
		for (int i = 0; i < 4; i++)
			BlocksHelper.setWithUpdate(world, pos.up(h2 + 1).offset(HOR[i]), BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, HOR[i]));
	}

	private void branch(IServerWorld world, BlockPos pos, int length, Random random, Direction direction, BlockPos center, int level) {
		if (level > 5)
			return;
		Mutable bpos = new Mutable().setPos(pos);
		BlocksHelper.setWithUpdate(world, bpos, BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, direction));
		vine(world, pos.down(), 1 + random.nextInt(1));
		Direction preDir = direction;
		int l2 = length * length;
		for (int i = 0; i < l2; i++) {
			Direction dir = random.nextInt(3) > 0 ? preDir : random.nextBoolean() ? preDir.rotateY() : preDir.rotateYCCW();// HOR[random.nextInt(4)];
			BlockPos p = bpos.offset(dir);
			if (world.isAirBlock(p)) {
				bpos.setPos(p);
				if (bpos.manhattanDistance(center) > length)
					break;
				BlocksHelper.setWithUpdate(world, bpos, BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, dir));

				if (random.nextBoolean()) {
					BlocksHelper.setWithUpdate(world, bpos.up(), BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, Direction.UP));
				}

				if (random.nextInt(3) == 0) {
					bpos.setY(bpos.getY() - 1);
					BlocksHelper.setWithUpdate(world, bpos, BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, Direction.DOWN));
				}

				if (random.nextBoolean())
					vine(world, bpos.down(), 1 + random.nextInt(4));

				if (random.nextBoolean()) {
					Direction right = dir.rotateY();
					BlockPos p2 = bpos.offset(right);
					if (world.isAirBlock(p2))
						branch(world, p2, length, random, right, center, level + 1);
					right = right.getOpposite();
					p2 = bpos.offset(right);
					if (world.isAirBlock(p2))
						branch(world, p2, length, random, right, center, level + 1);
				}

				Direction dir2 = HOR[random.nextInt(4)];
				BlockPos p2 = bpos.offset(dir2);
				if (world.isAirBlock(p2))
					BlocksHelper.setWithUpdate(world, p2, BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, dir2));

				preDir = dir;
			}
		}

		if (random.nextBoolean()) {
			if (world.isAirBlock(bpos))
				BlocksHelper.setWithUpdate(world, bpos, BlocksRegistry.WILLOW_LEAVES.getDefaultState().with(BlockWillowLeaves.FACING, preDir));
		}
	}

	private void vine(IServerWorld world, BlockPos pos, int length) {
		if (!world.isAirBlock(pos))
			return;

		for (int i = 0; i < length; i++) {
			BlockPos p = pos.down(i);
			if (world.isAirBlock(p.down()))
				BlocksHelper.setWithUpdate(world, p, BlocksRegistry.WILLOW_BRANCH.getDefaultState().with(BlockWillowBranch.SHAPE, BlockWillowBranch.WillowBranchShape.MIDDLE));
			else {
				BlocksHelper.setWithUpdate(world, p, BlocksRegistry.WILLOW_BRANCH.getDefaultState().with(BlockWillowBranch.SHAPE, BlockWillowBranch.WillowBranchShape.END));
				return;
			}
		}
		BlocksHelper.setWithUpdate(world, pos.down(length), BlocksRegistry.WILLOW_BRANCH.getDefaultState().with(BlockWillowBranch.SHAPE, BlockWillowBranch.WillowBranchShape.END));
	}
}

