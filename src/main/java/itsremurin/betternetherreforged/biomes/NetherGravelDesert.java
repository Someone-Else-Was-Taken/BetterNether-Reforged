package itsremurin.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.ParticleEffectAmbience;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.registry.SoundsRegistry;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.plants.StructureAgave;
import itsremurin.betternetherreforged.structures.plants.StructureBarrelCactus;
import itsremurin.betternetherreforged.structures.plants.StructureNetherCactus;

public class NetherGravelDesert extends NetherBiome {
	public NetherGravelDesert(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(170, 48, 0)
				.setLoop(SoundsRegistry.AMBIENT_GRAVEL_DESERT)
				.setMood(SoundEvents.AMBIENT_NETHER_WASTES_MOOD)
				.setAdditions(SoundEvents.AMBIENT_NETHER_WASTES_ADDITIONS)
				.setMusic(SoundEvents.MUSIC_NETHER_NETHER_WASTES)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.ASH, 0.02F)));
		addStructure("nether_cactus", new StructureNetherCactus(), StructureType.FLOOR, 0.02F, true);
		addStructure("agave", new StructureAgave(), StructureType.FLOOR, 0.02F, true);
		addStructure("barrel_cactus", new StructureBarrelCactus(), StructureType.FLOOR, 0.02F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		for (int i = 0; i < 1 + random.nextInt(3); i++) {
			BlockPos p2 = pos.down(i);
			if (BlocksHelper.isNetherGround(world.getBlockState(p2)))
				if (world.isAirBlock(p2.down())) {
				BlocksHelper.setWithoutUpdate(world, p2, Blocks.NETHERRACK.getDefaultState());
				return;
				}
				else
				BlocksHelper.setWithoutUpdate(world, p2, Blocks.GRAVEL.getDefaultState());
		}
	}
}
