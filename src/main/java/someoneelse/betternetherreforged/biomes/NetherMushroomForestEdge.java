package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.plants.StructureGrayMold;
import someoneelse.betternetherreforged.structures.plants.StructureOrangeMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureRedMold;
import someoneelse.betternetherreforged.structures.plants.StructureVanillaMushroom;

public class NetherMushroomForestEdge extends NetherBiome {
	public NetherMushroomForestEdge(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(200, 121, 157)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST));
		addStructure("orange_mushroom", new StructureOrangeMushroom(), StructureType.FLOOR, 0.05F, true);
		addStructure("vanilla_mushrooms", new StructureVanillaMushroom(), StructureType.FLOOR, 0.1F, false);
		addStructure("red_mold", new StructureRedMold(), StructureType.FLOOR, 0.5F, false);
		addStructure("gray_mold", new StructureGrayMold(), StructureType.FLOOR, 0.5F, false);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		if (random.nextInt(4) > 0)
			BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.NETHER_MYCELIUM.getDefaultState());
		else if (random.nextBoolean())
			BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.NETHERRACK_MOSS.getDefaultState());
	}
}