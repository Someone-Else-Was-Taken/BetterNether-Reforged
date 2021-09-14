package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.blocks.BlockNeonEquisetum;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureNeonEquisetum implements IStructure {
	private Mutable blockPos = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (pos.getY() < 90 || !BlocksHelper.isNetherrack(world.getBlockState(pos.up()))) return;

		int h = BlocksHelper.downRay(world, pos, 10);
		if (h < 3)
			return;
		h = MHelper.randRange(3, h, random);

		BlockState bottom = BlocksRegistry.NEON_EQUISETUM.getDefaultState().with(BlockNeonEquisetum.SHAPE, TripleShape.BOTTOM);
		BlockState middle = BlocksRegistry.NEON_EQUISETUM.getDefaultState().with(BlockNeonEquisetum.SHAPE, TripleShape.MIDDLE);
		BlockState top = BlocksRegistry.NEON_EQUISETUM.getDefaultState().with(BlockNeonEquisetum.SHAPE, TripleShape.TOP);

		blockPos.setPos(pos);
		for (int y = 0; y < h - 2; y++) {
			blockPos.setY(pos.getY() - y);
			BlocksHelper.setWithUpdate(world, blockPos, top);
		}
		blockPos.setY(blockPos.getY() - 1);
		BlocksHelper.setWithUpdate(world, blockPos, middle);
		blockPos.setY(blockPos.getY() - 1);
		BlocksHelper.setWithUpdate(world, blockPos, bottom);
	}
}