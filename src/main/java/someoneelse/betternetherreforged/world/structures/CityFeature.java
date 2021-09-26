package someoneelse.betternetherreforged.world.structures;

import java.util.List;


import com.mojang.serialization.Codec;
import net.minecraft.util.SharedSeedRandom;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.provider.BiomeProvider;
import net.minecraft.world.gen.GenerationStage;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.world.structures.city.CityGenerator;
import someoneelse.betternetherreforged.world.structures.piece.CavePiece;
import someoneelse.betternetherreforged.world.structures.piece.CityPiece;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.DynamicRegistries;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.structure.StructureStart;
import net.minecraft.world.gen.feature.template.TemplateManager;
import someoneelse.betternetherreforged.world.structures.city.palette.Palettes;


public class CityFeature extends Structure<NoFeatureConfig> {

	private final int distance;
	private final int separation;

	public CityFeature(Codec<NoFeatureConfig> codec) {
		super(codec);
		distance = 16;//BNConfig.CitySeparation;
		separation = distance >> 1;
	}


	private static final CityGenerator PIECES = new CityGenerator();

	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return CityFeature.Start::new;
	}

	public String getStructureName() {
		return "city";
	}

	@Override
	public GenerationStage.Decoration getDecorationStage() {
		return GenerationStage.Decoration.STRONGHOLDS;
	}


	public class Start extends StructureStart<NoFeatureConfig> {

		public Start(Structure<NoFeatureConfig> structure, int chunkx, int chunkz,
					 MutableBoundingBox bounds, int reference, long seed) {
			super(structure, chunkx, chunkz, bounds, reference, seed);
		}

		@Override
		public void func_230364_a_(DynamicRegistries p_230364_1_, ChunkGenerator chunkGenerator, TemplateManager structureManager, int x, int z, Biome biome, NoFeatureConfig featureConfig) {
			int px = (x << 4) | 8;
			int pz = (z << 4) | 8;
			int y = 40;
			if (chunkGenerator instanceof FlatChunkGenerator)
			{
				y = chunkGenerator.getHeight(px, pz, Type.WORLD_SURFACE);
			}

			BlockPos center = new BlockPos(px, y, pz);













			//CityPalette citypalette = Palettes.getRandom(this.rand);
			List<CityPiece> buildings = PIECES.generate(center, this.rand, Palettes.EMPTY);
			MutableBoundingBox cityBox = MutableBoundingBox.getNewBoundingBox();
			for (CityPiece p: buildings)
				cityBox.expandTo(p.getBoundingBox());

			int d1 = Math.max((center.getX() - cityBox.minX), (cityBox.maxX - center.getX()));
			int d2 = Math.max((center.getZ() - cityBox.minZ), (cityBox.maxZ - center.getZ()));
			int radius = Math.max(d1, d2);
			if (radius / 2 + center.getY() < cityBox.maxY)
			{
				radius = (cityBox.maxY - center.getY()) / 2;
			}

			if (!(chunkGenerator instanceof FlatChunkGenerator))
			{
				CavePiece cave = new CavePiece(center, radius, rand);
				this.components.add(cave);
				this.components.addAll(buildings);
				this.bounds = cave.getBoundingBox();
			}
			else
			{
				this.components.addAll(buildings);
				this.recalculateStructureSize();
			}

			this.bounds.minX -= 12;
			this.bounds.maxX += 12;
			this.bounds.minZ -= 12;
			this.bounds.maxZ += 12;
		}



	}



	/*
	private static CityGenerator generator;
	public static final int RADIUS = 8 * 8;

	public CityFeature() {
		super(NoFeatureConfig.field_236558_a_);
	}





	public static void initGenerator() {
		generator = new CityGenerator();
	}
	
	@Override
	public IStartFactory<NoFeatureConfig> getStartFactory() {
		return CityFeature.CityStart::new;
	}

	public static class CityStart extends StructureStart<NoFeatureConfig> {
		public CityStart(Structure<NoFeatureConfig> structureFeature, int chunkX, int chunkZ, MutableBoundingBox blockBox, int i, long l) {
			super(structureFeature, chunkX, chunkZ, blockBox, i, l);
		}

		public String getStructureName() {
			return "nether_city";
		}



		@Override
		public void func_230364_a_(DynamicRegistries dynamicRegistryManager, ChunkGenerator chunkGenerator, TemplateManager structureManager, int x, int z, Biome biome, NoFeatureConfig featureConfig) {
			int px = (x << 4) | 8;
			int pz = (z << 4) | 8;
			int y = 40;
			if (chunkGenerator instanceof FlatChunkGenerator) {
				y = chunkGenerator.getHeight(px, pz, Type.WORLD_SURFACE);
			}

			BlockPos center = new BlockPos(px, y, pz);

			// CityPalette palette = Palettes.getRandom(random);
			List<CityPiece> buildings = generator.generate(center, this.rand, Palettes.EMPTY);
			MutableBoundingBox cityBox = MutableBoundingBox.getNewBoundingBox();
			for (CityPiece p : buildings)
				cityBox.expandTo(p.getBoundingBox());

			int d1 = Math.max((center.getX() - cityBox.minX), (cityBox.maxX - center.getX()));
			int d2 = Math.max((center.getZ() - cityBox.minZ), (cityBox.maxZ - center.getZ()));
			int radius = Math.max(d1, d2);
			if (radius / 2 + center.getY() < cityBox.maxY) {
				radius = (cityBox.maxY - center.getY()) / 2;
			}

			if (!(chunkGenerator instanceof FlatChunkGenerator)) {
				CavePiece cave = new CavePiece(center, radius, rand);
				this.components.add(cave);
				this.components.addAll(buildings);
				this.bounds = cave.getBoundingBox();
			}
			else {
				this.components.addAll(buildings);
				this.recalculateStructureSize();
			}

			/*
			 * for (CityPiece p: buildings) { int count =
			 * p.getBoundingBox().getBlockCountX() *
			 * p.getBoundingBox().getBlockCountY() *
			 * p.getBoundingBox().getBlockCountZ(); if (count > 0) { count =
			 * random.nextInt(count / 512); for (int i = 0; i < count; i++)
			 * this.children.add(new DestructionPiece(p.getBoundingBox(),
			 * random)); } }
			 */

	/*
			this.bounds.minX -= 12;
			this.bounds.maxX += 12;
			this.bounds.minZ -= 12;
			this.bounds.maxZ += 12;

		}
	}
	*/
}
