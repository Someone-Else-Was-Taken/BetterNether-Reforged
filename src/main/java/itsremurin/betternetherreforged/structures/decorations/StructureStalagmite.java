package itsremurin.betternetherreforged.structures.decorations;

import java.util.Random;

import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.blocks.BlockStalactite;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IServerWorld;
import itsremurin.betternetherreforged.structures.IStructure;


public class StructureStalagmite implements IStructure {
	private static final Mutable POS = new Mutable();
	private final Block block;
	private final Block under;
	private final Block[] ground;

	public StructureStalagmite(Block block, Block under) {
		this.block = block;
		ground = null;
		this.under = under;
	}

	public StructureStalagmite(Block block, Block under, Block... ground) {
		this.block = block;
		this.under = under;
		this.ground = ground;
	}

	private boolean canPlaceAt(IServerWorld world, BlockPos pos) {
		return world.isAirBlock(pos) && (ground == null ? BlocksHelper.isNetherGround(world.getBlockState(pos.down())) : groundContains(world.getBlockState(pos.down()).getBlock()));
	}

	private boolean groundContains(Block block) {
		for (Block b : ground)
			if (b == block)
				return true;
		return false;
	}

	@Override
	public void generate(IServerWorld world, BlockPos pos, Random random) {
		if (canPlaceAt(world, pos)) {
			for (int i = 0; i < 16; i++) {
				int x = pos.getX() + (int) (random.nextGaussian() * 2F);
				int z = pos.getZ() + (int) (random.nextGaussian() * 2F);
				POS.setPos(x, pos.getY(), z);
				int y = pos.getY() - BlocksHelper.downRay(world, POS, 8);

				if (canPlaceAt(world, POS)) {
					int dx = x - pos.getX();
					int dz = z - pos.getZ();
					float dist = 4 - (float) Math.sqrt(dx * dx + dz * dz);
					if (dist < 1)
						dist = 1;
					int h = (int) (random.nextInt((int) Math.ceil(dist + 1)) + dist + random.nextInt(2));
					h = h > 8 ? 8 : h;
					POS.setY(y);
					int h2 = BlocksHelper.upRay(world, POS, h + 2) + 1;
					POS.setY(y + h2);
					boolean stalagnate = h2 <= h && BlocksHelper.isNetherrack(world.getBlockState(POS));
					if (h2 <= h)
						h = h2;
					int offset = stalagnate ? (h < 8 ? MHelper.randRange(0, 8 - h, random) : 0) : 0;

					if (under != null && h > 2) {
						POS.setY(y - 1);
						BlocksHelper.setWithoutUpdate(world, POS, under.getDefaultState());
						if (stalagnate) {
							POS.setY(y + h);
							BlocksHelper.setWithoutUpdate(world, POS, under.getDefaultState());
						}
					}
					for (int n = 0; n < h; n++) {
						POS.setY(y + n);
						if (!world.isAirBlock(POS))
							break;
						int size = stalagnate ? (Math.abs(h / 2 - n) + offset) : (h - n - 1);
						BlocksHelper.setWithoutUpdate(world, POS, block.getDefaultState().with(BlockStalactite.SIZE, size));
					}
				}
			}
		}
	}
}