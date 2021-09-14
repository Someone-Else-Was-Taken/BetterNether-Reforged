package someoneelse.betternetherreforged.structures.decorations;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureGeyser implements IStructure {
	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (BlocksHelper.isNetherrack(world.getBlockState(pos.down())))
			BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.GEYSER.getDefaultState());
	}
}