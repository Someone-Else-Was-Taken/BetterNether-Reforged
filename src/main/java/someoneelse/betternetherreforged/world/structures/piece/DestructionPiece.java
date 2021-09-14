package someoneelse.betternetherreforged.world.structures.piece;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.TemplateManager;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;

public class DestructionPiece extends CustomPiece {
	private static final Mutable POS = new Mutable();

	private BlockPos center;
	private int radius;
	private int radSqr;
	private int minY;
	private int maxY;

	public DestructionPiece(MutableBoundingBox bounds, Random random) {
		super(StructureTypes.DESTRUCTION, random.nextInt());
		radius = random.nextInt(5) + 1;
		radSqr = radius * radius;
		center = new BlockPos(
				MHelper.randRange(bounds.minX, bounds.maxX, random),
				MHelper.randRange(bounds.minY, bounds.maxY, random),
				MHelper.randRange(bounds.minZ, bounds.maxZ, random));
		makeBoundingBox();
	}

	protected DestructionPiece(TemplateManager manager, CompoundNBT tag) {
		super(StructureTypes.DESTRUCTION, tag);
		this.center = NBTUtil.readBlockPos(tag.getCompound("center"));
		this.radius = tag.getInt("radius");
		radSqr = radius * radius;
		makeBoundingBox();
	}

	@Override
	protected void readAdditional(CompoundNBT tag) {
		tag.put("center", NBTUtil.writeBlockPos(center));
		tag.putInt("radius", radius);
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager structureAccessor, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		for (int x = blockBox.minX; x <= blockBox.maxX; x++) {
			int px = x - center.getX();
			px *= px;
			for (int z = blockBox.minZ; z <= blockBox.maxZ; z++) {
				int pz = z - center.getZ();
				pz *= pz;
				for (int y = minY; y <= maxY; y++) {
					int py = (y - center.getY()) << 1;
					py *= py;
					if (px + py + pz <= radSqr + random.nextInt(radius)) {
						POS.setPos(x, y, z);
						if (!world.isAirBlock(POS)) {
							if (random.nextBoolean())
								BlocksHelper.setWithoutUpdate(world, POS, CAVE_AIR);
							else {
								int dist = BlocksHelper.downRay(world, POS, maxY - 5);
								if (dist > 0) {
									BlockState state = world.getBlockState(POS);
									BlocksHelper.setWithoutUpdate(world, POS, CAVE_AIR);
									POS.setY(POS.getY() - dist);
									BlocksHelper.setWithoutUpdate(world, POS, state);
								}
							}
						}
					}
				}
			}
		}
		return true;
	}

	private void makeBoundingBox() {
		int x1 = center.getX() - radius;
		int x2 = center.getX() + radius;
		minY = Math.max(22, center.getY() - radius);
		if (minY < 38)
			minY = 38;
		maxY = Math.min(96, center.getY() + radius);
		int z1 = center.getZ() - radius;
		int z2 = center.getZ() + radius;
		this.boundingBox = new MutableBoundingBox(x1, minY, z1, x2, maxY, z2);
	}
}