package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureVanillaMushroom implements IStructure {
	private Mutable npos = new Mutable();

	private boolean canPlaceAt(IWorld world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == BlocksRegistry.NETHER_MYCELIUM;
	}

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (canPlaceAt(world, pos)) {
			BlockState state = random.nextBoolean() ? Blocks.RED_MUSHROOM.getDefaultState() : Blocks.BROWN_MUSHROOM.getDefaultState();
			for (int i = 0; i < 16; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 4);
				int z = pos.getZ() + (int) (random.nextGaussian() * 4);
				int y = pos.getY() + random.nextInt(8);
				for (int j = 0; j < 8; j++) {
					npos.setPos(x, y - j, z);
					if (world.isAirBlock(npos) && canPlaceAt(world, npos)) {
						BlocksHelper.setWithoutUpdate(world, npos, state);
					}
				}
			}
		}
	}
}
