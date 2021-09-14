package someoneelse.betternetherreforged.structures.plants;

import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.HugeMushroomBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import someoneelse.betternetherreforged.structures.IStructure;

public class StructureWartCap implements IStructure {
	private static final BlockState INSIDE = Blocks.RED_MUSHROOM_BLOCK
			.getDefaultState()
			.with(HugeMushroomBlock.NORTH, false)
			.with(HugeMushroomBlock.SOUTH, false)
			.with(HugeMushroomBlock.EAST, false)
			.with(HugeMushroomBlock.WEST, false)
			.with(HugeMushroomBlock.UP, false)
			.with(HugeMushroomBlock.DOWN, false);
	private static final BlockState SKIN = Blocks.NETHER_WART_BLOCK
			.getDefaultState();
	private static final Mutable POS = new Mutable();

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (!isWall(world, pos) || pos.getY() > 57 || pos.getY() < 34 || world.isAirBlock(pos.down(3)))
			return;

		int radius = 3 + random.nextInt(3);
		int r2 = radius * radius;
		int side = radius * 2 + 1;
		int y1 = radius >> 1;
		BlockState[][][] shape = new BlockState[side][y1 + 1][side];

		for (int y = 0; y <= y1; y++) {
			POS.setY(pos.getY() + y);
			for (int x = -radius; x <= radius; x++) {
				POS.setX(pos.getX() + x);
				int sx = x + radius;
				for (int z = -radius; z <= radius; z++) {
					POS.setZ(pos.getZ() + z);
					int sz = z + radius;
					int d = x * x + y * y * 6 + z * z;
					if (d <= r2) {
						if ((y == y1) || (x == -radius) || (x == radius) || (z == -radius) || (z == radius))
							shape[sx][y][sz] = SKIN;
						else
							shape[sx][y][sz] = INSIDE;
					}
				}
			}
		}

		for (int y = 0; y < y1; y++) {
			for (int x = 1; x < side - 1; x++) {
				for (int z = 1; z < side - 1; z++) {
					if (shape[x][y][z] != null) {
						if (shape[x - 1][y][z] == null || shape[x + 1][y][z] == null || shape[x][y][z - 1] == null || shape[x][y][z + 1] == null || shape[x][y + 1][z] == null) {
							shape[x][y][z] = SKIN;
						}
					}
				}
			}
		}

		for (int y = 0; y <= y1; y++) {
			POS.setY(pos.getY() + y);
			for (int x = 0; x < side; x++) {
				POS.setX(pos.getX() + x - radius);
				for (int z = 0; z < side; z++) {
					POS.setZ(pos.getZ() + z - radius);
					if (shape[x][y][z] != null && world.isAirBlock(POS))
						BlocksHelper.setWithoutUpdate(world, POS, shape[x][y][z]);
				}
			}
		}
	}

	private boolean isWall(IServerWorld world, BlockPos pos) {
		for (Direction dir : HorizontalBlock.HORIZONTAL_FACING.getAllowedValues())
			if (world.getBlockState(pos.offset(dir)).getBlock() == Blocks.NETHER_BRICKS)
				return true;
		return false;
	}
}

