package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.registry.EntityRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.plants.StructureBlackBush;
import someoneelse.betternetherreforged.structures.plants.StructureNetherWart;
import someoneelse.betternetherreforged.structures.plants.StructureWartSeed;

public class NetherWartForestEdge extends NetherBiome {
	public NetherWartForestEdge(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(191, 28, 28)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST)
				.addMobSpawn(EntityRegistry.FLYING_PIG, 20, 2, 4));
		addStructure("nether_wart", new StructureNetherWart(), StructureType.FLOOR, 0.02F, false);
		addStructure("wart_seed", new StructureWartSeed(), StructureType.FLOOR, 0.01F, false);
		addStructure("black_bush", new StructureBlackBush(), StructureType.FLOOR, 0.01F, false);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		switch (random.nextInt(3)) {
			case 0:
				super.genSurfColumn(world, pos, random);
				break;
			case 1:
				BlocksHelper.setWithoutUpdate(world, pos, Blocks.SOUL_SAND.getDefaultState());
				break;
			case 2:
				BlocksHelper.setWithoutUpdate(world, pos, Blocks.SOUL_SOIL.getDefaultState());
				break;
			case 3:
				BlocksHelper.setWithoutUpdate(world, pos, BlocksRegistry.NETHERRACK_MOSS.getDefaultState());
				break;
		}
		for (int i = 1; i < random.nextInt(3); i++) {
			BlockPos down = pos.down(i);
			if (random.nextInt(3) == 0 && BlocksHelper.isNetherGround(world.getBlockState(down)))
				BlocksHelper.setWithoutUpdate(world, down, Blocks.SOUL_SAND.getDefaultState());
		}
	}
}