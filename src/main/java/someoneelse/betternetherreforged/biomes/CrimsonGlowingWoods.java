package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.ParticleEffectAmbience;
import net.minecraft.world.gen.feature.IFeatureConfig;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.entity.EntityFirefly;
import someoneelse.betternetherreforged.noise.OpenSimplexNoise;
import someoneelse.betternetherreforged.registry.CityRegistry;
import someoneelse.betternetherreforged.registry.ConfiguredStructureRegistry;
import someoneelse.betternetherreforged.registry.EntityRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.plants.StructureCrimsonFungus;
import someoneelse.betternetherreforged.structures.plants.StructureCrimsonGlowingTree;
import someoneelse.betternetherreforged.structures.plants.StructureCrimsonRoots;
import someoneelse.betternetherreforged.structures.plants.StructureGoldenVine;
import someoneelse.betternetherreforged.structures.plants.StructureWallMoss;
import someoneelse.betternetherreforged.structures.plants.StructureWallRedMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureWartBush;
import someoneelse.betternetherreforged.structures.plants.StructureWartSeed;

public class CrimsonGlowingWoods extends NetherBiome {
	private static final OpenSimplexNoise TERRAIN = new OpenSimplexNoise(614);


	public CrimsonGlowingWoods(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(51, 3, 3)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.CRIMSON_SPORE, 0.025F))
				.addMobSpawn(EntityRegistry.FLYING_PIG, 10, 2, 4));

		addStructure("crimson_glowing_tree", new StructureCrimsonGlowingTree(), StructureType.FLOOR, 0.2F, false);
		addStructure("wart_bush", new StructureWartBush(), StructureType.FLOOR, 0.05F, false);
		addStructure("wart_seed", new StructureWartSeed(), StructureType.FLOOR, 0.02F, true);
		addStructure("crimson_fungus", new StructureCrimsonFungus(), StructureType.FLOOR, 0.05F, true);
		addStructure("crimson_roots", new StructureCrimsonRoots(), StructureType.FLOOR, 0.2F, true);
		addStructure("golden_vine", new StructureGoldenVine(), StructureType.CEIL, 0.3F, true);
		addStructure("wall_moss", new StructureWallMoss(), StructureType.WALL, 0.8F, true);
		addStructure("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL, 0.4F, true);


	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		if (TERRAIN.eval(pos.getX() * 0.1, pos.getZ() * 0.1) > MHelper.randRange(0.5F, 0.7F, random))
			BlocksHelper.setWithoutUpdate(world, pos, Blocks.NETHER_WART_BLOCK.getDefaultState());
		else
			BlocksHelper.setWithoutUpdate(world, pos, Blocks.CRIMSON_NYLIUM.getDefaultState());
	}

}
