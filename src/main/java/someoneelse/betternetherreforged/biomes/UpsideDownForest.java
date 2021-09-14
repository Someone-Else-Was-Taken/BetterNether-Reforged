package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.Blocks;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.decorations.StructureForestLitter;
import someoneelse.betternetherreforged.structures.plants.StructureAnchorTree;
import someoneelse.betternetherreforged.structures.plants.StructureAnchorTreeBranch;
import someoneelse.betternetherreforged.structures.plants.StructureAnchorTreeRoot;
import someoneelse.betternetherreforged.structures.plants.StructureCeilingMushrooms;
import someoneelse.betternetherreforged.structures.plants.StructureHookMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureJungleMoss;
import someoneelse.betternetherreforged.structures.plants.StructureMossCover;
import someoneelse.betternetherreforged.structures.plants.StructureNeonEquisetum;
import someoneelse.betternetherreforged.structures.plants.StructureNetherSakura;
import someoneelse.betternetherreforged.structures.plants.StructureNetherSakuraBush;
import someoneelse.betternetherreforged.structures.plants.StructureWallBrownMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureWallRedMushroom;
import someoneelse.betternetherreforged.structures.plants.StructureWhisperingGourd;

public class UpsideDownForest extends NetherBiome {
	public UpsideDownForest(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(111, 188, 111)
				.setLoop(SoundEvents.AMBIENT_CRIMSON_FOREST_LOOP)
				.setAdditions(SoundEvents.AMBIENT_CRIMSON_FOREST_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_CRIMSON_FOREST_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_CRIMSON_FOREST)
				.setBNStructures(false)
				.setStalactites(false));
		this.setNoiseDensity(0.5F);
		addStructure("anchor_tree", new StructureAnchorTree(), StructureType.CEIL, 0.2F, false);
		addStructure("anchor_tree_root", new StructureAnchorTreeRoot(), StructureType.CEIL, 0.03F, false);
		addStructure("anchor_tree_branch", new StructureAnchorTreeBranch(), StructureType.CEIL, 0.02F, true);
		addStructure("nether_sakura", new StructureNetherSakura(), StructureType.CEIL, 0.01F, true);
		addStructure("nether_sakura_bush", new StructureNetherSakuraBush(), StructureType.FLOOR, 0.01F, true);
		addStructure("moss_cover", new StructureMossCover(), StructureType.FLOOR, 0.6F, false);
		addStructure("jungle_moss", new StructureJungleMoss(), StructureType.WALL, 0.4F, true);
		addStructure("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL, 0.4F, true);
		addStructure("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL, 0.4F, true);
		addStructure("forest_litter", new StructureForestLitter(), StructureType.FLOOR, 0.1F, false);
		addStructure("ceiling_mushrooms", new StructureCeilingMushrooms(), StructureType.CEIL, 1F, false);
		addStructure("neon_equisetum", new StructureNeonEquisetum(), StructureType.CEIL, 0.1F, true);
		addStructure("hook_mushroom", new StructureHookMushroom(), StructureType.CEIL, 0.03F, true);
		addStructure("whispering_gourd", new StructureWhisperingGourd(), StructureType.CEIL, 0.02F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		BlocksHelper.setWithoutUpdate(world, pos, random.nextInt(3) == 0 ? BlocksRegistry.NETHERRACK_MOSS.getDefaultState() : Blocks.NETHERRACK.getDefaultState());
	}
}
