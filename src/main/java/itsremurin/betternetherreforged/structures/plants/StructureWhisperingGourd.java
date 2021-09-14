package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.blocks.BlockWhisperingGourdVine;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureWhisperingGourd implements IStructure {
	private Mutable blockPos = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (pos.getY() < 90 || !BlocksHelper.isNetherrack(world.getBlockState(pos.up()))) return;

		int h = BlocksHelper.downRay(world, pos, 4);
		if (h < 1)
			return;
		h = MHelper.randRange(1, h, random);

		BlockState bottom = BlocksRegistry.WHISPERING_GOURD_VINE.getDefaultState().with(BlockWhisperingGourdVine.SHAPE, TripleShape.BOTTOM);
		BlockState middle = BlocksRegistry.WHISPERING_GOURD_VINE.getDefaultState().with(BlockWhisperingGourdVine.SHAPE, TripleShape.MIDDLE);
		BlockState top = BlocksRegistry.WHISPERING_GOURD_VINE.getDefaultState().with(BlockWhisperingGourdVine.SHAPE, TripleShape.TOP);

		blockPos.setPos(pos);
		for (int y = 0; y < h - 1; y++) {
			blockPos.setY(pos.getY() - y);
			BlocksHelper.setWithUpdate(world, blockPos, random.nextBoolean() ? top : middle);
		}
		BlocksHelper.setWithUpdate(world, blockPos.down(), bottom);
	}
}