package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockRedLargeMushroom;
import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureGiantMold implements IStructure {
	Mutable npos = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		Block under;
		if (world.getBlockState(pos.down()).getBlock() == BlocksRegistry.NETHER_MYCELIUM) {
			for (int i = 0; i < 10; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 2);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2);
				int y = pos.getY() + random.nextInt(6);
				for (int j = 0; j < 16; j++) {
					npos.setPos(x, y - j, z);
					under = world.getBlockState(npos.down()).getBlock();
					if (under == BlocksRegistry.NETHER_MYCELIUM) {
						grow(world, npos, random);
					}
				}
			}
		}
	}

	public void grow(IServerWorld world, BlockPos pos, Random random) {
		int size = 2 + random.nextInt(6);
		for (int y = 1; y <= size; y++)
			if (!world.isAirBlock(pos.up(y))) {
				if (y == 1)
					return;
				size = y - 1;
				break;
			}
		BlockState middle = BlocksRegistry.GIANT_MOLD.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.MIDDLE);
		for (int y = 1; y < size; y++)
			BlocksHelper.setWithUpdate(world, pos.up(y), middle);
		BlocksHelper.setWithUpdate(world, pos.up(size), BlocksRegistry.GIANT_MOLD.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.TOP));
		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.GIANT_MOLD.getDefaultState().with(BlockRedLargeMushroom.SHAPE, TripleShape.BOTTOM));
	}
}

