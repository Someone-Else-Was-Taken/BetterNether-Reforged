package itsremurin.betternetherreforged.world;

import java.util.List;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import itsremurin.betternetherreforged.biomes.NetherBiome;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryLookupCodec;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.provider.BiomeProvider;
import itsremurin.betternetherreforged.BetterNether;
import itsremurin.betternetherreforged.registry.NetherBiomesRegistry;

public class NetherBiomeProvider extends BiomeProvider {
	public static final Codec<NetherBiomeProvider> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(RegistryLookupCodec.getLookUpCodec(Registry.BIOME_KEY).forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.biomeRegistry;
		}), Codec.LONG.fieldOf("seed").stable().forGetter((theEndBiomeSource) -> {
			return theEndBiomeSource.seed;
		})).apply(instance, instance.stable(NetherBiomeProvider::new));
	});
	private BiomeMap map;
	private final long seed;
	private final Registry<Biome> biomeRegistry;

	public NetherBiomeProvider(Registry<Biome> biomeRegistry, long seed) {
		super(getBiomes(biomeRegistry));
		this.seed = seed;
		this.map = new BiomeMap(seed, BNWorldGenerator.biomeSizeXZ, BNWorldGenerator.biomeSizeY, BNWorldGenerator.volumetric);
		this.biomeRegistry = biomeRegistry;
		NetherBiomesRegistry.mapBiomes(biomeRegistry);
		/*
		if (Configs.GENERATOR.getBoolean("generator.world.cities", "generate", true)) {
			this.biomes.forEach((biome) -> {
				GenerationSettingsAccessor accessor = (GenerationSettingsAccessor) biome.getGenerationSettings();
				List<Supplier<StructureFeature<?, ?>>> structures = Lists.newArrayList(accessor.getStructureFeatures());
				structures.add(() -> { return BNWorldGenerator.CITY_CONFIGURED; });
				accessor.setStructureFeatures(structures);
			});
		}
		*/
	}
	
	private static List<Biome> getBiomes(Registry<Biome> biomeRegistry) {
		List<Biome> result = Lists.newArrayList();
		biomeRegistry.forEach((biome) -> {
			if (biome.getCategory() == Category.NETHER) {
				result.add(biome);
			}
		});
		return result;
	}

	@Override
	public Biome getNoiseBiome(int biomeX, int biomeY, int biomeZ) {
		NetherBiome netherBiome = map.getBiome(biomeX << 2, biomeY << 2, biomeZ << 2);
		if (biomeX == 0 && biomeZ == 0) {
			map.clearCache();
		}
		return netherBiome.getActualBiome();
	}

	@Override
	public BiomeProvider getBiomeProvider(long seed) {
		return new NetherBiomeProvider(biomeRegistry, seed);
	}

	@Override
	protected Codec<? extends BiomeProvider> getBiomeProviderCodec() {
		return CODEC;
	}

	public static void register() {
		Registry.register(Registry.BIOME_PROVIDER_CODEC, new ResourceLocation(BetterNether.MOD_ID, "nether_biome_source"), CODEC);
	}
}
