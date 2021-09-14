package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.RubeusLog;
import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureRubeusBush implements IStructure {
	private static final Mutable POS = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (!world.isAirBlock(pos) || !world.isAirBlock(pos.up()) || !world.isAirBlock(pos.up(15)))
			return;

		float r = random.nextFloat() * 3 + 1;
		int count = (int) r;

		for (int i = 0; i < count; i++) {
			float fr = r - i;
			int ir = (int) Math.ceil(fr);
			float r2 = fr * fr;

			int x1 = pos.getX() - ir;
			int x2 = pos.getX() + ir;
			int z1 = pos.getZ() - ir;
			int z2 = pos.getZ() + ir;

			POS.setY(pos.getY() + i);

			for (int x = x1; x < x2; x++) {
				POS.setX(x);
				int sqx = x - pos.getX();
				sqx *= sqx;
				for (int z = z1; z < z2; z++) {
					int sqz = z - pos.getZ();
					sqz *= sqz;
					POS.setZ(z);
					if (sqx + sqz < r2 + random.nextFloat() * r) {
						setIfAir(world, POS, BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
					}
				}
			}
		}

		BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.RUBEUS_BARK.getDefaultState().with(RubeusLog.SHAPE, TripleShape.MIDDLE));
		setIfAir(world, pos.up(), BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
		setIfAir(world, pos.north(), BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
		setIfAir(world, pos.south(), BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
		setIfAir(world, pos.east(), BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
		setIfAir(world, pos.west(), BlocksRegistry.RUBEUS_LEAVES.getDefaultState());
	}

	private void setIfAir(IWorld world, BlockPos pos, BlockState state) {
		if (world.isAirBlock(pos))
			BlocksHelper.setWithoutUpdate(world, pos, state);
	}
}
