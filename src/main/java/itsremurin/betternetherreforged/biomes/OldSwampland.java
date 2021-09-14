package itsremurin.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.noise.OpenSimplexNoise;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.registry.SoundsRegistry;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.plants.StructureBlackBush;
import itsremurin.betternetherreforged.structures.plants.StructureBlackVine;
import itsremurin.betternetherreforged.structures.plants.StructureFeatherFern;
import itsremurin.betternetherreforged.structures.plants.StructureJellyfishMushroom;
import itsremurin.betternetherreforged.structures.plants.StructureOldWillow;
import itsremurin.betternetherreforged.structures.plants.StructureReeds;
import itsremurin.betternetherreforged.structures.plants.StructureSmoker;
import itsremurin.betternetherreforged.structures.plants.StructureSoulVein;
import itsremurin.betternetherreforged.structures.plants.StructureSwampGrass;
import itsremurin.betternetherreforged.structures.plants.StructureWallBrownMushroom;
import itsremurin.betternetherreforged.structures.plants.StructureWallMoss;
import itsremurin.betternetherreforged.structures.plants.StructureWallRedMushroom;
import itsremurin.betternetherreforged.structures.plants.StructureWillow;
import itsremurin.betternetherreforged.structures.plants.StructureWillowBush;

public class OldSwampland extends NetherBiome {
	protected static final OpenSimplexNoise TERRAIN = new OpenSimplexNoise(523);

	public OldSwampland(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(137, 19, 78)
				.setLoop(SoundsRegistry.AMBIENT_SWAMPLAND)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST)
				.setDefaultMobs(false)
				.addMobSpawn(EntityType.STRIDER, 40, 2, 4)
				.addMobSpawn(EntityType.MAGMA_CUBE, 40, 2, 4));
		addStructure("old_willow", new StructureOldWillow(), StructureType.FLOOR, 0.02F, false);
		addStructure("willow", new StructureWillow(), StructureType.FLOOR, 0.02F, false);
		addStructure("willow_bush", new StructureWillowBush(), StructureType.FLOOR, 0.1F, true);
		addStructure("feather_fern", new StructureFeatherFern(), StructureType.FLOOR, 0.05F, true);
		addStructure("nether_reed", new StructureReeds(), StructureType.FLOOR, 0.8F, false);
		addStructure("soul_vein", new StructureSoulVein(), StructureType.FLOOR, 0.5F, false);
		addStructure("smoker", new StructureSmoker(), StructureType.FLOOR, 0.05F, false);
		addStructure("jellyfish_mushroom", new StructureJellyfishMushroom(), StructureType.FLOOR, 0.03F, true);
		addStructure("black_bush", new StructureBlackBush(), StructureType.FLOOR, 0.01F, false);
		addStructure("swamp_grass", new StructureSwampGrass(), StructureType.FLOOR, 0.4F, false);
		addStructure("black_vine", new StructureBlackVine(), StructureType.CEIL, 0.4F, true);
		addStructure("wall_moss", new StructureWallMoss(), StructureType.WALL, 0.8F, true);
		addStructure("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL, 0.8F, true);
		addStructure("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL, 0.8F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		double value = TERRAIN.eval(pos.getX() * 0.2, pos.getY() * 0.2, pos.getZ() * 0.2);
		if (value > 0.3 && validWalls(world, pos))
			BlocksHelper.setWithoutUpdate(world, pos, Blocks.LAVA.getDefaultState());
		else if (value > -0.3)
			BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.SWAMPLAND_GRASS.getDefaultState());
		else {
			value = TERRAIN.eval(pos.getX() * 0.5, pos.getZ() * 0.5);
			BlocksHelper.setWithoutUpdate(world, pos, value > 0 ? Blocks.SOUL_SAND.getDefaultState() : Blocks.SOUL_SOIL.getDefaultState());
		}
	}

	protected boolean validWalls(IWorld world, BlockPos pos) {
		return validWall(world, pos.down())
				&& validWall(world, pos.north())
				&& validWall(world, pos.south())
				&& validWall(world, pos.east())
				&& validWall(world, pos.west());
	}

	protected boolean validWall(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return BlocksHelper.isLava(state) || BlocksHelper.isNetherGround(state);
	}
}