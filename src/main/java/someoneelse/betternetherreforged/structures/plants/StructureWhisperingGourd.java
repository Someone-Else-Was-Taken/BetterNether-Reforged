package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.blocks.BlockWhisperingGourdVine;
import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

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