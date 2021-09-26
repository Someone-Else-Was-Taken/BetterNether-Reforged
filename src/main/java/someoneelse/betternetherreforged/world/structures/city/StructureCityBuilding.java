package someoneelse.betternetherreforged.world.structures.city;

import java.util.List;
import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.gen.feature.template.BlockIgnoreStructureProcessor;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.structures.StructureNBT;

public class StructureCityBuilding extends StructureNBT {
	protected static final BlockState AIR = Blocks.AIR.getDefaultState();

	private BoundingBox bb;
	public BlockPos[] ends;
	private Direction[] dirs;
	private BlockPos rotationOffset;
	private int offsetY;

	public StructureCityBuilding(String structure) {
		super(structure);
		this.offsetY = 0;
		init();
	}

	public StructureCityBuilding(String structure, int offsetY) {
		super(structure);
		this.offsetY = offsetY;
		init();
	}

	protected StructureCityBuilding(ResourceLocation location, Template structure) {
		super(location, structure);
		init();
	}

	private void init() {
		BlockPos size = structure.getSize();
		bb = new BoundingBox(size);
		List<BlockInfo> map = structure.func_215386_a(BlockPos.ZERO, new PlacementSettings(), Blocks.STRUCTURE_BLOCK, false);
		ends = new BlockPos[map.size()];
		dirs = new Direction[map.size()];
		int i = 0;
		BlockPos center = new BlockPos(size.getX() >> 1, size.getY(), size.getZ() >> 1);
		for (BlockInfo info : map) {
			ends[i] = info.pos;
			dirs[i++] = getDir(info.pos.add(-center.getX(), 0, -center.getZ()));
		}
		rotationOffset = new BlockPos(0, 0, 0);
		rotation = Rotation.NONE;
	}

	private Direction getDir(BlockPos pos) {
		int ax = Math.abs(pos.getX());
		int az = Math.abs(pos.getZ());
		int mx = Math.max(ax, az);
		if (mx == ax) {
			if (pos.getX() > 0)
				return Direction.EAST;
			else
				return Direction.WEST;
		}
		else {
			if (pos.getZ() > 0)
				return Direction.SOUTH;
			else
				return Direction.NORTH;
		}
	}

	public BoundingBox getBoungingBox() {
		return bb;
	}

	protected Rotation mirrorRotation(Rotation r) {
		switch (r) {
			case CLOCKWISE_90:
				return Rotation.COUNTERCLOCKWISE_90;
			default:
				return r;
		}
	}

	public void placeInChunk(IServerWorld world, BlockPos pos, MutableBoundingBox boundingBox, StructureProcessor paletteProcessor) {
		BlockPos p = pos.add(rotationOffset);
		structure.func_237144_a_(world, p, new PlacementSettings()
						.setRotation(rotation)
						.setMirror(mirror)
						.setBoundingBox(boundingBox)
						.addProcessor(paletteProcessor),
				world.getRandom());
	}

	public BlockPos[] getEnds() {
		return ends;
	}

	public int getEndsCount() {
		return ends.length;
	}

	public BlockPos getOffsettedPos(int index) {
		return ends[index].offset(dirs[index]);
	}

	public BlockPos getPos(int index) {
		return ends[index];
	}

	public StructureCityBuilding getRotated(Rotation rotation) {
		StructureCityBuilding building = this.clone();
		building.rotation = rotation;
		building.rotationOffset = building.structure.getSize().rotate(rotation);
		int x = building.rotationOffset.getX();
		int z = building.rotationOffset.getZ();
		if (x < 0)
			x = -x - 1;
		else
			x = 0;
		if (z < 0)
			z = -z - 1;
		else
			z = 0;
		building.rotationOffset = new BlockPos(x, 0, z);
		for (int i = 0; i < building.dirs.length; i++) {
			building.dirs[i] = rotated(building.dirs[i], rotation);
			building.ends[i] = building.ends[i].rotate(rotation).add(building.rotationOffset);
		}
		building.bb.rotate(rotation);
		building.offsetY = this.offsetY;
		return building;
	}

	public StructureCityBuilding getRandomRotated(Random random) {
		return getRotated(Rotation.values()[random.nextInt(4)]);
	}

	public StructureCityBuilding clone() {
		return new StructureCityBuilding(location, structure);
	}

	private Direction rotated(Direction dir, Rotation rotation) {
		Direction f;
		switch (rotation) {
			case CLOCKWISE_90:
				f = dir.rotateY();
				break;
			case CLOCKWISE_180:
				f = dir.getOpposite();
				break;
			case COUNTERCLOCKWISE_90:
				f = dir.rotateYCCW();
				break;
			default:
				f = dir;
				break;
		}
		return f;
	}

	public int getYOffset() {
		return offsetY;
	}

	public Rotation getRotation() {
		return rotation;
	}

	/*
	 * private static StructureProcessor makeProcessorReplace() { return new
	 * RuleStructureProcessor( ImmutableList.of( new StructureProcessorRule( new
	 * BlockMatchRuleTest(Blocks.STRUCTURE_BLOCK), AlwaysTrueRuleTest.INSTANCE,
	 * Blocks.AIR.getDefaultState() ) ) ); }
	 */

	@Override
	public MutableBoundingBox getBoundingBox(BlockPos pos) {
		return structure.getMutableBoundingBox(new PlacementSettings().setRotation(this.rotation).setMirror(mirror), pos.add(rotationOffset));
	}

	@Override
	public StructureCityBuilding setRotation(Rotation rotation) {
		this.rotation = rotation;
		rotationOffset = structure.getSize().rotate(rotation);
		return this;
	}
}