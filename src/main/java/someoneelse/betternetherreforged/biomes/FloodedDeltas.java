package someoneelse.betternetherreforged.biomes;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.ParticleEffectAmbience;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.structures.StructureType;

public class FloodedDeltas extends NetherBiome {
	private static final Mutable POS = new Mutable();

	public FloodedDeltas(String name) {
		super(new BiomeDefinition(name)
				.setFogColor(104, 95, 112)
				.setLoop(SoundEvents.AMBIENT_BASALT_DELTAS_LOOP)
				.setAdditions(SoundEvents.AMBIENT_BASALT_DELTAS_ADDITIONS)
				.setMood(SoundEvents.AMBIENT_BASALT_DELTAS_MOOD)
				.setMusic(SoundEvents.MUSIC_NETHER_BASALT_DELTAS)
				.setStalactites(false)
				.setParticleConfig(new ParticleEffectAmbience(ParticleTypes.WHITE_ASH, 0.12F)));


		addStructure("blackstone_stalactite", STALACTITE_BLACKSTONE, StructureType.FLOOR, 0.2F, true);
		addStructure("stalactite_stalactite", STALACTITE_BASALT, StructureType.FLOOR, 0.2F, true);
		addStructure("blackstone_stalagmite", STALAGMITE_BLACKSTONE, StructureType.CEIL, 0.1F, true);
		addStructure("basalt_stalagmite", STALAGMITE_BASALT, StructureType.CEIL, 0.1F, true);
	}

	@Override
	public void genSurfColumn(IWorld world, BlockPos pos, Random random) {
		POS.setPos(pos);
		int d = MHelper.randRange(2, 4, random);
		BlockState state = isLavaValid(world, pos) ? Blocks.LAVA.getDefaultState() : (random.nextInt(16) > 0 ? Blocks.BASALT.getDefaultState() : Blocks.AIR.getDefaultState());
		BlocksHelper.setWithoutUpdate(world, POS, state);
		if (state.getBlock() == Blocks.LAVA)
			world.getChunk(pos.getX() >> 4, pos.getZ() >> 4).markBlockForPostprocessing(POS.setPos(pos.getX() & 15, pos.getY(), pos.getZ() & 15));
		POS.setPos(pos);
		for (int h = 1; h < d; h++) {
			POS.setY(pos.getY() - h);
			if (BlocksHelper.isNetherGround(world.getBlockState(POS)))
				BlocksHelper.setWithoutUpdate(world, POS, Blocks.BASALT.getDefaultState());
			else
				break;
		}
	}

	protected boolean isLavaValid(IWorld world, BlockPos pos) {
		return validWall(world, pos.down()) &&
				validWall(world, pos.north()) &&
				validWall(world, pos.south()) &&
				validWall(world, pos.east()) &&
				validWall(world, pos.west());
	}

	protected boolean validWall(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return BlocksHelper.isLava(state) || state.hasOpaqueCollisionShape(world, pos);
	}
}
