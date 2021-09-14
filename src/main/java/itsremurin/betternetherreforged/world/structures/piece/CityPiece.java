package itsremurin.betternetherreforged.world.structures.piece;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.NBTUtil;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.structure.StructureManager;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.TemplateManager;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.world.structures.city.BuildingStructureProcessor;
import itsremurin.betternetherreforged.world.structures.city.StructureCityBuilding;
import itsremurin.betternetherreforged.world.structures.city.palette.CityPalette;
import itsremurin.betternetherreforged.world.structures.city.palette.Palettes;


public class CityPiece extends CustomPiece {
	private static final Mutable POS = new Mutable();

	private StructureProcessor paletteProcessor;
	private StructureCityBuilding building;
	private CityPalette palette;
	private BlockPos pos;

	public CityPiece(StructureCityBuilding building, BlockPos pos, int id, CityPalette palette) {
		super(StructureTypes.NETHER_CITY, id);
		this.building = building;
		this.pos = pos.toImmutable();
		this.boundingBox = building.getBoundingBox(pos);
		this.palette = palette;
		this.paletteProcessor = new BuildingStructureProcessor(palette);
	}

	protected CityPiece(TemplateManager manager, CompoundNBT tag) {
		super(StructureTypes.NETHER_CITY, tag);
		this.building = new StructureCityBuilding(tag.getString("building"), tag.getInt("offset"));
		this.building = this.building.getRotated(Rotation.values()[tag.getInt("rotation")]);
		this.building.setMirror(Mirror.values()[tag.getInt("mirror")]);
		this.pos = NBTUtil.readBlockPos(tag.getCompound("pos"));
		this.boundingBox = building.getBoundingBox(pos);
		this.palette = Palettes.getPalette(tag.getString("palette"));
		this.paletteProcessor = new BuildingStructureProcessor(palette);
	}

	@Override
	protected void readAdditional(CompoundNBT tag) {
		tag.putString("building", building.getName());
		tag.putInt("rotation", building.getRotation().ordinal());
		tag.putInt("mirror", building.getMirror().ordinal());
		tag.putInt("offset", building.getYOffset());
		tag.put("pos", NBTUtil.writeBlockPos(pos));
		tag.putString("palette", palette.getName());
	}

	@Override
	public boolean func_230383_a_(ISeedReader world, StructureManager arg, ChunkGenerator chunkGenerator, Random random, MutableBoundingBox blockBox, ChunkPos chunkPos, BlockPos blockPos) {
		if (!this.boundingBox.intersectsWith(blockBox))
			return true;

		MutableBoundingBox clamped = new MutableBoundingBox(boundingBox);

		clamped.minX = Math.max(clamped.minX, blockBox.minX);
		clamped.maxX = Math.min(clamped.maxX, blockBox.maxX);

		clamped.minY = Math.max(clamped.minY, blockBox.minY);
		clamped.maxY = Math.min(clamped.maxY, blockBox.maxY);

		clamped.minZ = Math.max(clamped.minZ, blockBox.minZ);
		clamped.maxZ = Math.min(clamped.maxZ, blockBox.maxZ);

		building.placeInChunk(world, pos, clamped, paletteProcessor);

		IChunk chunk = world.getChunk(chunkPos.x, chunkPos.z);

		BlockState state;
		for (int x = clamped.minX; x <= clamped.maxX; x++)
			for (int z = clamped.minZ; z <= clamped.maxZ; z++) {
				POS.setPos(x, clamped.minY, z);
				state = world.getBlockState(POS);
				if (!state.isAir() && state.hasOpaqueCollisionShape(world, POS)) {
					for (int y = clamped.minY - 1; y > 4; y--) {
						POS.setY(y);
						BlocksHelper.setWithoutUpdate(world, POS, state);
						if (BlocksHelper.isNetherGroundMagma(world.getBlockState(POS.down())))
							break;
					}
				}

				// POS.set(x - clamped.minX, clamped.minY - clamped.minZ, z);
				for (int y = clamped.minY; y <= clamped.maxY; y++) {
					POS.setY(y);
					chunk.markBlockForPostprocessing(POS);
				}
			}

		return true;
	}
}
