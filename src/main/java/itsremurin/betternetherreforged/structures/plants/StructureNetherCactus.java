package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.blocks.BlockNetherCactus;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureNetherCactus implements IStructure {
	private Mutable npos = new Mutable();

	private boolean canPlaceAt(IWorld world, BlockPos pos) {
		return world.getBlockState(pos.down()).getBlock() == Blocks.GRAVEL;
	}

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (canPlaceAt(world, pos)) {
			BlockState top = BlocksRegistry.NETHER_CACTUS.getDefaultState();
			BlockState bottom = BlocksRegistry.NETHER_CACTUS.getDefaultState().with(BlockNetherCactus.TOP, false);
			for (int i = 0; i < 16; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 4);
				int z = pos.getZ() + (int) (random.nextGaussian() * 4);
				if (((x + z + pos.getY()) & 1) == 0) {
					if (random.nextBoolean()) {
						x += random.nextBoolean() ? 1 : -1;
					}
					else {
						z += random.nextBoolean() ? 1 : -1;
					}
				}
				int y = pos.getY() + random.nextInt(8);
				for (int j = 0; j < 8; j++) {
					npos.setPos(x, y - j, z);
					if (world.isAirBlock(npos) && canPlaceAt(world, npos)) {
						int h = random.nextInt(3);
						for (int n = 0; n < h; n++)
							BlocksHelper.setWithUpdate(world, npos.up(n), bottom);
						BlocksHelper.setWithUpdate(world, npos.up(h), top);
						break;
					}
				}
			}
		}
	}
}