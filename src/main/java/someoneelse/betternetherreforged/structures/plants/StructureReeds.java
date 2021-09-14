package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockNetherReed;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureReeds implements IStructure {
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (world.isAirBlock(pos) && BlocksRegistry.NETHER_REED.isValidPosition(world.getBlockState(pos), world, pos)) {
			BlockState med = BlocksRegistry.NETHER_REED.getDefaultState().with(BlockNetherReed.TOP, false);
			int h = random.nextInt(3);
			for (int i = 0; i < h; i++) {
				BlockPos posN = pos.up(i);
				BlockPos up = posN.up();
				if (world.isAirBlock(posN)) {
					if (world.isAirBlock(up))
						BlocksHelper.setWithUpdate(world, posN, med);
					else {
						BlocksHelper.setWithUpdate(world, posN, BlocksRegistry.NETHER_REED.getDefaultState());
						return;
					}
				}
			}
			BlocksHelper.setWithUpdate(world, pos.up(h), BlocksRegistry.NETHER_REED.getDefaultState());
		}
	}
}