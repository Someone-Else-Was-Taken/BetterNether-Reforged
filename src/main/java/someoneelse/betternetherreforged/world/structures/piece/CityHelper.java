package someoneelse.betternetherreforged.world.structures.piece;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.world.BNWorldGenerator;
import someoneelse.betternetherreforged.world.structures.CityFeature;


public class CityHelper {

	private static final Set<ChunkPos> POSITIONS = new HashSet<ChunkPos>(64);
	private static final Mutable POS = new Mutable();

	public static boolean stopStructGen(int chunkX, int chunkZ, ChunkGenerator chunkGenerator, long worldSeed, SharedSeedRandom chunkRandom) {
		StructureSeparationSettings config = chunkGenerator.func_235957_b_().func_236197_a_(BNWorldGenerator.CITY);
		if (config != null && config.func_236668_a_() > 0) collectNearby(chunkX, chunkZ, config, worldSeed, chunkRandom);
		return stopGeneration(chunkX, chunkZ);
	}

	private static void collectNearby(int chunkX, int chunkZ, StructureSeparationSettings config, long worldSeed, SharedSeedRandom chunkRandom) {
		int x1 = chunkX - 16;
		int x2 = chunkX + 16;
		int z1 = chunkZ - 16;
		int z2 = chunkZ + 16;

		POSITIONS.clear();
		for (int x = x1; x <= x2; x += 8) {
			for (int z = z1; z <= z2; z += 8) {
				ChunkPos chunk = BNWorldGenerator.CITY.getChunkPosForStructure(config, worldSeed, chunkRandom, x, z);
				POSITIONS.add(chunk);
			}
		}
	}
	
	private static void collectNearby(ServerWorld world, int chunkX, int chunkZ, StructureSeparationSettings config, long worldSeed, SharedSeedRandom chunkRandom) {
		int x1 = chunkX - 64;
		int x2 = chunkX + 64;
		int z1 = chunkZ - 64;
		int z2 = chunkZ + 64;

		POSITIONS.clear();
		POS.setY(64);
		for (int x = x1; x <= x2; x += 8) {
			POS.setX(x << 4);
			for (int z = z1; z <= z2; z += 8) {
				POS.setZ(z << 4);
				if (world.getBiome(POS).getGenerationSettings().hasStructure(BNWorldGenerator.CITY)) {
					ChunkPos chunk = BNWorldGenerator.CITY.getChunkPosForStructure(config, worldSeed, chunkRandom, x, z);
					POSITIONS.add(chunk);
				}
			}
		}
	}

	private static boolean stopGeneration(int chunkX, int chunkZ) {
		for (ChunkPos p : POSITIONS) {
			int dx = p.x - chunkX;
			int dz = p.z - chunkZ;
			if (dx * dx + dz * dz < CityFeature.RADIUS)
				return true;
		}
		return false;
	}

	private static long sqr(int x) {
		return (long) x * (long) x;
	}

	public static BlockPos getNearestCity(BlockPos pos, ServerWorld world) {
		int cx = pos.getX() >> 4;
		int cz = pos.getZ() >> 4;

		StructureSeparationSettings config = world.getChunkProvider().getChunkGenerator().func_235957_b_().func_236197_a_(BNWorldGenerator.CITY);
		if (config == null || config.func_236668_a_() < 1)
			return null;

		collectNearby(world, cx, cz, config, world.getSeed(), new SharedSeedRandom());
		Iterator<ChunkPos> iterator = POSITIONS.iterator();
		if (iterator.hasNext()) {
			ChunkPos nearest = POSITIONS.iterator().next();
			long d = sqr(nearest.x - cx) + sqr(nearest.z - cz);
			while (iterator.hasNext()) {
				ChunkPos n = iterator.next();
				long d2 = sqr(n.x - cx) + sqr(n.z - cz);
				if (d2 < d) {
					d = d2;
					nearest = n;
				}
			}
			return nearest.asBlockPos();
		}
		return null;
	}

}
