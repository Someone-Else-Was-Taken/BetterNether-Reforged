package itsremurin.betternetherreforged.biomes;

import java.util.Random;

import itsremurin.betternetherreforged.blocks.BlockSoulSandstone;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.ParticleEffectAmbience;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.noise.OpenSimplexNoise;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.structures.StructureType;
import itsremurin.betternetherreforged.structures.plants.StructureBlackBush;
import itsremurin.betternetherreforged.structures.plants.StructureSoulGrass;
import itsremurin.betternetherreforged.structures.plants.StructureSoulVein;

public class NetherSoulPlain extends NetherBiome {
	private static final OpenSimplexNoise TERRAIN = new OpenSimplexNoise(245);
	private static final Mutable POS = new Mutable();

	public NetherSoulPlain(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(196, 113, 239)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.PORTAL, 0.02F)));
		addStructure("soul_vein", new StructureSoulVein(), StructureType.FLOOR, 0.5F, true);
		addStructure("black_bush", new StructureBlackBush(), StructureType.FLOOR, 0.02F, false);
		addStructure("soul_grass", new StructureSoulGrass(), StructureType.FLOOR, 0.3F, false);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		POS.setPos(pos);
		int d1 = MHelper.randRange(2, 4, random);

		for (int i = 0; i < d1; i++) {
			POS.setY(pos.getY() - i);
			if (BlocksHelper.isNetherGround(world.getBlockState(POS)))
				if (TERRAIN.eval(pos.getX() * 0.1, pos.getY() * 0.1, pos.getZ() * 0.1) > 0)
					BlocksHelper.setWithoutUpdate(world, POS, Blocks.SOUL_SOIL.getDefaultState());
				else
				BlocksHelper.setWithoutUpdate(world, POS, Blocks.SOUL_SAND.getDefaultState());
			else
				return;
		}

		int d2 = MHelper.randRange(5, 7, random);
		for (int i = d1; i < d2; i++) {
			POS.setY(pos.getY() - i);
			if (BlocksHelper.isNetherGround(world.getBlockState(POS)))
				BlocksHelper.setWithoutUpdate(world, POS, BlocksRegistry.SOUL_SANDSTONE.getDefaultState().with(BlockSoulSandstone.UP, i == d1));
			else
				return;
		}
	}
}