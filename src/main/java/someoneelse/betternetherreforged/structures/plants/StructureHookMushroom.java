package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureHookMushroom implements IStructure {
	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (pos.getY() < 90 || !BlocksHelper.isNetherrack(world.getBlockState(pos.up()))) return;
		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.HOOK_MUSHROOM.getDefaultState());
	}
}