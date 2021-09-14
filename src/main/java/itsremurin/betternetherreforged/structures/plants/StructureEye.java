package itsremurin.betternetherreforged.structures.plants;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.IStructure;

public class StructureEye implements IStructure {
	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		int h = random.nextInt(19) + 5;
		int h2 = BlocksHelper.downRay(world, pos, h);

		if (h2 < 5)
			return;

		h2 -= 1;

		BlockState vineState = BlocksRegistry.EYE_VINE.getDefaultState();
		BlockState eyeState = random.nextBoolean() ? BlocksRegistry.EYEBALL.getDefaultState() : BlocksRegistry.EYEBALL_SMALL.getDefaultState();

		for (int y = 0; y < h2; y++)
			BlocksHelper.setWithUpdate(world, pos.down(y), vineState);

		BlocksHelper.setWithUpdate(world, pos.down(h2), eyeState);
	}
}