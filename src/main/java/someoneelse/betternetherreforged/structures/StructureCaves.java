package someoneelse.betternetherreforged.structures;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.noise.OpenSimplexNoise;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;

public class StructureCaves implements IStructure {
	private static final boolean[][][] MASK = new boolean[16][24][16];
	private static final Mutable B_POS = new Mutable();
	private static int offset = 12;
	private OpenSimplexNoise heightNoise;
	private OpenSimplexNoise rigidNoise;
	private OpenSimplexNoise distortX;
	private OpenSimplexNoise distortY;

	public StructureCaves(long seed) {
		Random random = new Random(seed);
		heightNoise = new OpenSimplexNoise(random.nextLong());
		rigidNoise = new OpenSimplexNoise(random.nextLong());
		distortX = new OpenSimplexNoise(random.nextLong());
		distortY = new OpenSimplexNoise(random.nextLong());
	}

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		boolean isVoid = true;
		offset = (int) (getHeight(pos.getX() + 8, pos.getZ() + 8) - 12);

		for (int x = 0; x < 16; x++) {
			int wx = pos.getX() + x;
			for (int z = 0; z < 16; z++) {
				int wz = pos.getZ() + z;

				double height = getHeight(wx, wz);
				double rigid = getRigid(wx, wz);

				for (int y = 0; y < 24; y++) {
					int wy = offset + y;

					double hRigid = Math.abs(wy - height);
					double sdf = -opSmoothUnion(-hRigid / 30, -rigid, 0.15);

					if (sdf < 0.15) {
						MASK[x][y][z] = true;
						isVoid = false;
					}
					else
						MASK[x][y][z] = false;
				}
			}
		}

		if (isVoid)
			return;

		for (int x = 0; x < 16; x++) {
			int wx = pos.getX() + x;
			for (int z = 0; z < 16; z++) {
				int wz = pos.getZ() + z;
				for (int y = 23; y >= 0; y--) {
					int wy = offset + y;
					B_POS.setPos(wx, wy, wz);
					if (MASK[x][y][z] && BlocksHelper.isNetherGroundMagma(world.getBlockState(B_POS))) {
						/*
						 * if (world.getBlockState(B_POS.up()).getBlock() ==
						 * Blocks.NETHER_WART_BLOCK)
						 * BlocksHelper.setWithoutUpdate(world, B_POS,
						 * Blocks.NETHER_WART_BLOCK.getDefaultState()); else
						 */
						BlocksHelper.setWithoutUpdate(world, B_POS, Blocks.AIR.getDefaultState());
					}
				}
			}
		}
	}

	private double getHeight(int x, int z) {
		return heightNoise.eval(x * 0.01, z * 0.01) * 32 + 64;
	}

	private double getRigid(int x, int z) {
		return Math.abs(rigidNoise.eval(
				x * 0.02 + distortX.eval(x * 0.05, z * 0.05) * 0.2,
				z * 0.02 + distortY.eval(x * 0.05, z * 0.05) * 0.2)) * 0.6;

		// return Math.abs(rigidNoise.eval(x * 0.02, z * 0.02));
	}

	private double mix(double dist1, double dist2, double blend) {
		return dist1 * (1 - blend) + dist2 * blend;
	}

	private double opSmoothUnion(double dist1, double dist2, double blend) {
		double h = 0.5 + 0.5 * (dist2 - dist1) / blend;
		h = h > 1 ? 1 : h < 0 ? 0 : h;
		return mix(dist2, dist1, h) - blend * h * (1 - h);
	}

	public boolean isInCave(int x, int y, int z) {
		int y2 = y - offset;
		if (y2 >= 0 && y < 24)
			return MASK[x][y2][z];
		else
			return false;
	}
}