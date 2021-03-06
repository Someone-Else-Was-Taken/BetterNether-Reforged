package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.ParticleEffectAmbience;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.registry.SoundsRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.plants.StructureGrayMold;
import someoneelse.betternetherreforged.structures.plants.StructureMedBrownMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureMedRedMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureOldBrownMushrooms;
import someoneelse.betternetherreforged.structures.plants.StructureOldRedMushrooms;
import someoneelse.betternetherreforged.structures.plants.StructureRedMold;
import someoneelse.betternetherreforged.structures.plants.StructureVanillaMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureWallBrownMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureWallRedMushroom;

public class OldFungiwoods extends NetherBiome {
	public OldFungiwoods(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(166, 38, 95)
				.setLoop(SoundsRegistry.AMBIENT_MUSHROOM_FOREST)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.MYCELIUM, 0.1F)));
		this.setNoiseDensity(0.5F);
		addStructure("old_red_mushrooms", new StructureOldRedMushrooms(), StructureType.FLOOR, 0.1F, false);
		addStructure("old_brown_mushrooms", new StructureOldBrownMushrooms(), StructureType.FLOOR, 0.1F, false);
		addStructure("large_red_mushroom", new StructureMedRedMushroom(), StructureType.FLOOR, 0.12F, true);
		addStructure("large_brown_mushroom", new StructureMedBrownMushroom(), StructureType.FLOOR, 0.12F, true);
		addStructure("vanilla_mushrooms", new StructureVanillaMushroom(), StructureType.FLOOR, 0.5F, false);
		addStructure("red_mold", new StructureRedMold(), StructureType.FLOOR, 0.9F, true);
		addStructure("gray_mold", new StructureGrayMold(), StructureType.FLOOR, 0.9F, true);
		addStructure("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL, 0.9F, true);
		addStructure("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL, 0.9F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.NETHER_MYCELIUM.getDefaultState());
	}
}
