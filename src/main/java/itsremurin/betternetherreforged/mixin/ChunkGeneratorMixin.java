package itsremurin.betternetherreforged.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.crash.CrashReport;
import net.minecraft.crash.ReportedException;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.structure.StructureManager;
import itsremurin.betternetherreforged.world.BNWorldGenerator;

@Mixin(ChunkGenerator.class)
public abstract class ChunkGeneratorMixin {
	private static final SharedSeedRandom RANDOM = new SharedSeedRandom();
	private static final Mutable POS = new Mutable();

	@Inject(method = "func_230351_a_", at = @At("HEAD"), cancellable = true)
	private void customPopulate(WorldGenRegion region, StructureManager accessor, CallbackInfo info) {
		int chunkX = region.getMainChunkX();
		int chunkZ = region.getMainChunkZ();
		if (!region.isRemote() && isNetherBiome(region, chunkX, chunkZ)) {
			RANDOM.setBaseChunkSeed(chunkX, chunkZ);
			int sx = chunkX << 4;
			int sz = chunkZ << 4;
			BNWorldGenerator.prePopulate(region, sx, sz, RANDOM);

			long featureSeed = RANDOM.setDecorationSeed(region.getSeed(), chunkX, chunkZ);
			ChunkGenerator generator = (ChunkGenerator) (Object) this;
			for (Biome biome : BNWorldGenerator.getPopulateBiomes()) {
				try {
					biome.generateFeatures(accessor, generator, region, featureSeed, RANDOM, new BlockPos(sx, 0, sz));
				}
				catch (Exception e) {
					CrashReport crashReport = CrashReport.makeCrashReport(e, "Biome decoration");
					crashReport
							.makeCategory("Generation")
							.addDetail("CenterX", region.getMainChunkX())
							.addDetail("CenterZ", region.getMainChunkZ())
							.addDetail("Seed", featureSeed)
							.addDetail("Biome", biome);
					throw new ReportedException(crashReport);
				}
			}

			BNWorldGenerator.populate(region, sx, sz, RANDOM);
			BNWorldGenerator.cleaningPass(region, sx, sz);

			info.cancel();
		}
	}

	private boolean isNetherBiome(WorldGenRegion world, int cx, int cz) {
		POS.setPos(cx << 4, 0, cx << 4);
		return isNetherBiome(world.getBiome(POS)) ||
				isNetherBiome(world.getBiome(POS.add(0, 0, 7))) ||
				isNetherBiome(world.getBiome(POS.add(0, 0, 15))) ||
				isNetherBiome(world.getBiome(POS.add(7, 0, 0))) ||
				isNetherBiome(world.getBiome(POS.add(7, 0, 7))) ||
				isNetherBiome(world.getBiome(POS.add(7, 0, 15))) ||
				isNetherBiome(world.getBiome(POS.add(15, 0, 0))) ||
				isNetherBiome(world.getBiome(POS.add(15, 0, 7))) ||
				isNetherBiome(world.getBiome(POS.add(15, 0, 15)));
	}

	private boolean isNetherBiome(Biome biome) {
		return biome.getCategory() == Category.NETHER;
	}
}
