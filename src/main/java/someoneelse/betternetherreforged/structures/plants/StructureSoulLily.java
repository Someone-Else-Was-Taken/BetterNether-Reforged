package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockSoulLily;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureSoulLily implements IStructure {
	Mutable npos = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		Block under;
		if (world.getBlockState(pos.down()).getBlock() == Blocks.SOUL_SAND) {
			for (int i = 0; i < 10; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 2);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2);
				int y = pos.getY() + random.nextInt(6);
				for (int j = 0; j < 6; j++) {
					npos.setPos(x, y - j, z);
					if (npos.getY() > 31) {
						under = world.getBlockState(npos.down()).getBlock();
						if (under == Blocks.SOUL_SAND && world.isAirBlock(npos)) {
							growTree(world, npos, random);
						}
					}
					else
						break;
				}
			}
		}
	}

	private void growTree(IServerWorld world, BlockPos pos, Random random) {
		if (world.getBlockState(pos.down()).getBlock() == Blocks.SOUL_SAND) {
			if (world.isAirBlock(pos.up())) {
				if (world.isAirBlock(pos.up(2)) && isAirSides(world, pos.up(2))) {
					growBig(world, pos);
				}
				else
					growMedium(world, pos);
			}
			else
				growSmall(world, pos);
		}
	}

	public void growSmall(IWorld world, BlockPos pos) {
		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.SOUL_LILY.getDefaultState());
	}

	public void growMedium(IWorld world, BlockPos pos) {
		BlocksHelper.setWithUpdate(world, pos,
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.MEDIUM_BOTTOM));
		BlocksHelper.setWithUpdate(world, pos.up(),
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.MEDIUM_TOP));
	}

	public void growBig(IWorld world, BlockPos pos) {
		BlocksHelper.setWithUpdate(world, pos, BlocksRegistry.SOUL_LILY
				.getDefaultState()
				.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_BOTTOM));
		BlocksHelper.setWithUpdate(world, pos.up(),
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_MIDDLE));
		BlockPos up = pos.up(2);
		BlocksHelper.setWithUpdate(world, up,
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_TOP_CENTER));
		BlocksHelper.setWithUpdate(world, up.north(), BlocksRegistry.SOUL_LILY
				.getDefaultState()
				.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_TOP_SIDE_S));
		BlocksHelper.setWithUpdate(world, up.south(),
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_TOP_SIDE_N));
		BlocksHelper.setWithUpdate(world, up.east(),
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_TOP_SIDE_W));
		BlocksHelper.setWithUpdate(world, up.west(),
				BlocksRegistry.SOUL_LILY
						.getDefaultState()
						.with(BlockSoulLily.SHAPE, BlockSoulLily.SoulLilyShape.BIG_TOP_SIDE_E));
	}

	private boolean isAirSides(IWorld world, BlockPos pos) {
		return world.isAirBlock(pos.north()) && world.isAirBlock(pos.south()) && world.isAirBlock(pos.east()) && world.isAirBlock(pos.west());
	}
}