package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.ParticleEffectAmbience;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.decorations.StructureStalactite;
import someoneelse.betternetherreforged.structures.decorations.StructureStalagmite;
import someoneelse.betternetherreforged.structures.plants.StructureBoneReef;
import someoneelse.betternetherreforged.structures.plants.StructureGoldenLumabusVine;
import someoneelse.betternetherreforged.structures.plants.StructureJellyfishMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureReeds;
import someoneelse.betternetherreforged.structures.plants.StructureSepiaBoneGrass;

public class NetherSulfuricBoneReef extends NetherBiome {
	public NetherSulfuricBoneReef(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(154, 144, 49)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setStalactites(false)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.ASH, 0.01F)));

		addStructure("bone_stalactite", new StructureStalagmite(BlocksRegistry.BONE_STALACTITE, BlocksRegistry.BONE_BLOCK), StructureType.FLOOR, 0.05F, true);

		addStructure("nether_reed", new StructureReeds(), StructureType.FLOOR, 0.5F, false);
		addStructure("bone_reef", new StructureBoneReef(), StructureType.FLOOR, 0.2F, true);
		addStructure("jellyfish_mushroom", new StructureJellyfishMushroom(), StructureType.FLOOR, 0.02F, true);
		addStructure("sulfuric_bone_grass", new StructureSepiaBoneGrass(), StructureType.FLOOR, 0.1F, false);

		addStructure("bone_stalagmite", new StructureStalactite(BlocksRegistry.BONE_STALACTITE, BlocksRegistry.BONE_BLOCK), StructureType.CEIL, 0.05F, true);

		addStructure("golden_lumabus_vine", new StructureGoldenLumabusVine(), StructureType.CEIL, 0.3F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.SEPIA_MUSHROOM_GRASS.getDefaultState());
	}
}
