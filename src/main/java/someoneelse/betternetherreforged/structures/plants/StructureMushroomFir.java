package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockMushroomFir;
import someoneelse.betternetherreforged.blocks.BlockNetherMycelium;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureMushroomFir implements IStructure {
	Mutable npos = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (world.getBlockState(pos.down()).getBlock() == BlocksRegistry.NETHER_MYCELIUM) {
			int h = 3 + random.nextInt(5);
			for (int y = 1; y < h; y++)
				if (!world.isAirBlock(pos.up(y))) {
					h = y;
					break;
				}
			if (h < 3)
				return;

			BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.MUSHROOM_FIR
					.getDefaultState()
					.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.BOTTOM));
			int h2 = (h + 1) >> 1;
			h += pos.getY();
			h2 += pos.getY();
			npos.setPos(pos);
			for (int y = pos.getY() + 1; y < h2; y++) {
				npos.setY(y);
				BlocksHelper.setWithUpdate(world, npos, BlocksRegistry.MUSHROOM_FIR
						.getDefaultState()
						.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.MIDDLE));
			}
			for (int y = h2; y < h; y++) {
				npos.setY(y);
				BlocksHelper.setWithUpdate(world, npos, BlocksRegistry.MUSHROOM_FIR
						.getDefaultState()
						.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.TOP));
			}
			int h3 = (h2 + h) >> 1;
			for (int y = h2 - 1; y < h3; y++) {
				npos.setY(y);
				BlockPos branch;
				if (random.nextBoolean()) {
					branch = npos.north();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_BIG_S));
				}
				if (random.nextBoolean()) {
					branch = npos.south();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_BIG_N));
				}
				if (random.nextBoolean()) {
					branch = npos.east();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_BIG_W));
				}
				if (random.nextBoolean()) {
					branch = npos.west();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_BIG_E));
				}
			}
			for (int y = h3; y < h; y++) {
				npos.setY(y);
				BlockPos branch;
				if (random.nextBoolean()) {
					branch = npos.north();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_SMALL_S));
				}
				if (random.nextBoolean()) {
					branch = npos.south();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_SMALL_N));
				}
				if (random.nextBoolean()) {
					branch = npos.east();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_SMALL_W));
				}
				if (random.nextBoolean()) {
					branch = npos.west();
					if (world.isAirBlock(branch))
						BlocksHelper.setWithUpdate(world, branch, BlocksRegistry.MUSHROOM_FIR
								.getDefaultState()
								.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.SIDE_SMALL_E));
				}
			}
			npos.setY(h);
			if (world.isAirBlock(npos))
				BlocksHelper.setWithUpdate(world, npos, BlocksRegistry.MUSHROOM_FIR
						.getDefaultState()
						.with(BlockMushroomFir.SHAPE, BlockMushroomFir.MushroomFirShape.END));

			BlocksHelper.cover(world,
					pos.down(),
					BlocksRegistry.NETHER_MYCELIUM,
					BlocksRegistry.NETHER_MYCELIUM.getDefaultState().with(BlockNetherMycelium.IS_BLUE, true),
					5,
					random);
		}
	}
}