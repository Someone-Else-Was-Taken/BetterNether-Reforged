package someoneelse.betternetherreforged.registry;

import java.util.*;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;

import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.ModList;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.biomes.*;
import someoneelse.betternetherreforged.config.Config;
import someoneelse.betternetherreforged.config.Configs;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;

public class NetherBiomesRegistry {

	public static IForgeRegistry<NetherBiome> REGISTRY;
	public static final ArrayList<NetherBiome> ALL_BIOMES = new ArrayList<>();
	public static final Map<Biome, NetherBiome> MUTABLE = Maps.newHashMap();
	private static final ArrayList<NetherBiome> GENERATOR = new ArrayList<NetherBiome>();
	
	public static final NetherBiome BIOME_EMPTY_NETHER = new NetherBiomeWrapper(new ResourceLocation("nether_wastes"));
	public static final NetherBiome BIOME_CRIMSON_FOREST = new NetherBiomeWrapper(new ResourceLocation("crimson_forest"));
	public static final NetherBiome BIOME_WARPED_FOREST = new NetherBiomeWrapper(new ResourceLocation("warped_forest"));
	public static final NetherBiome BIOME_BASALT_DELTAS = new NetherBiomeWrapper(new ResourceLocation("basalt_deltas"));

	public static final NetherBiome BIOME_GRAVEL_DESERT = new NetherGravelDesert("Gravel Desert");
	public static final NetherBiome BIOME_NETHER_JUNGLE = new NetherJungle("Nether Jungle");
	public static final NetherBiome BIOME_WART_FOREST = new NetherWartForest("Wart Forest");
	public static final NetherBiome BIOME_GRASSLANDS = new NetherGrasslands("Nether Grasslands");
	public static final NetherBiome BIOME_MUSHROOM_FOREST = new NetherMushroomForest("Nether Mushroom Forest");
	public static final NetherBiome BIOME_MUSHROOM_FOREST_EDGE = new NetherMushroomForestEdge("Nether Mushroom Forest Edge");
	public static final NetherBiome BIOME_WART_FOREST_EDGE = new NetherWartForestEdge("Wart Forest Edge");
	public static final NetherBiome BIOME_BONE_REEF = new NetherBoneReef("Bone Reef");
	public static final NetherBiome BIOME_SULFURIC_BONE_REEF = new NetherSulfuricBoneReef("Sulfuric Bone Reef");
	public static final NetherBiome BIOME_POOR_GRASSLANDS = new NetherPoorGrasslands("Poor Nether Grasslands");
	public static final NetherBiome NETHER_SWAMPLAND = new NetherSwampland("Nether Swampland");
	public static final NetherBiome NETHER_SWAMPLAND_TERRACES = new NetherSwamplandTerraces("Nether Swampland Terraces");
	public static final NetherBiome MAGMA_LAND = new NetherMagmaLand("Magma Land");
	public static final NetherBiome SOUL_PLAIN = new NetherSoulPlain("Soul Plain");
	public static final NetherBiome CRIMSON_GLOWING_WOODS = new CrimsonGlowingWoods("Crimson Glowing Woods");
	public static final NetherBiome OLD_WARPED_WOODS = new OldWarpedWoods("Old Warped Woods");
	public static final NetherBiome CRIMSON_PINEWOOD = new CrimsonPinewood("Crimson Pinewood");
	public static final NetherBiome OLD_FUNGIWOODS = new OldFungiwoods("Old Fungiwoods");
	public static final NetherBiome FLOODED_DELTAS = new FloodedDeltas("Flooded Deltas");
	public static final NetherBiome UPSIDE_DOWN_FOREST = new UpsideDownForest("Upside Down Forest");
	public static final NetherBiome OLD_SWAMPLAND = new OldSwampland("Old Swampland");
	
	private static int maxDefChance = 0;
	private static int maxChance = 0;

	public static void init() {
		ForgeRegistries.BIOMES.forEach((biome) -> {
			if (biome.getCategory() == Category.NETHER) {
				ResourceLocation id = ForgeRegistries.BIOMES.getKey(biome);
				Configs.GENERATOR.getFloat("biomes." + id.getNamespace() + ".main", id.getPath() + "_chance", 1);
			}
		});

		registerNetherBiome(BIOME_GRAVEL_DESERT);
		registerNetherBiome(BIOME_NETHER_JUNGLE);
		
		registerNetherBiome(BIOME_EMPTY_NETHER);
		registerNetherBiome(BIOME_CRIMSON_FOREST);
		registerNetherBiome(BIOME_WARPED_FOREST);
		registerNetherBiome(BIOME_BASALT_DELTAS);
		registerNetherBiome(BIOME_WART_FOREST);
		registerNetherBiome(BIOME_GRASSLANDS);
		registerNetherBiome(BIOME_MUSHROOM_FOREST);
		registerEdgeBiome(BIOME_MUSHROOM_FOREST_EDGE, BIOME_MUSHROOM_FOREST, 2);
		registerEdgeBiome(BIOME_WART_FOREST_EDGE, BIOME_WART_FOREST, 2);
		registerNetherBiome(BIOME_BONE_REEF);
		registerSubBiome(BIOME_SULFURIC_BONE_REEF, BIOME_BONE_REEF, 0.3F);
		registerSubBiome(BIOME_POOR_GRASSLANDS, BIOME_GRASSLANDS, 0.3F);
		registerNetherBiome(NETHER_SWAMPLAND);
		registerSubBiome(NETHER_SWAMPLAND_TERRACES, NETHER_SWAMPLAND, 1F);
		registerNetherBiome(MAGMA_LAND);
		registerSubBiome(SOUL_PLAIN, BIOME_WART_FOREST, 1F);
		registerSubBiome(CRIMSON_GLOWING_WOODS, BIOME_CRIMSON_FOREST, 0.3F);
		registerSubBiome(OLD_WARPED_WOODS, BIOME_WARPED_FOREST, 1F);
		registerSubBiome(CRIMSON_PINEWOOD, BIOME_CRIMSON_FOREST, 0.3F);
		registerSubBiome(OLD_FUNGIWOODS, BIOME_MUSHROOM_FOREST, 0.3F);
		registerSubBiome(FLOODED_DELTAS, BIOME_BASALT_DELTAS, 1F);
		registerNetherBiome(UPSIDE_DOWN_FOREST);
		registerSubBiome(OLD_SWAMPLAND, NETHER_SWAMPLAND, 1F);
	}
	
	public static void registerNetherBiomes(RegistryEvent.Register<NetherBiome> e) {
		IForgeRegistry<NetherBiome> r = e.getRegistry();
		for (NetherBiome nb : ALL_BIOMES) {
			if (nb.getRegistryName() == null) {
				nb.setRegistryName(nb.getID());
			}
			r.register(nb);
		}
	}
	
	public static void registerBiomes(RegistryEvent.Register<Biome> e) {
		IForgeRegistry<Biome> r = e.getRegistry();
		for (NetherBiome nb : ALL_BIOMES) {
			if (!ForgeRegistries.BIOMES.containsKey(nb.getID())) {
				nb.getBiome().setRegistryName(nb.getID());
				r.register(nb.getBiome());
			}
		}
	}
	
	public static void createRegistry(RegistryEvent.NewRegistry e) {
		RegistryBuilder<NetherBiome> r = new RegistryBuilder<NetherBiome>();
		r.setName(new ResourceLocation(BetterNether.MOD_ID, "nether_biomes"));
		r.setType(NetherBiome.class);
		REGISTRY = r.create();
	}
	
	public static void mapBiomes(Registry<Biome> biomeRegistry) {
		GENERATOR.clear();
		GENERATOR.addAll(REGISTRY.getValues());
		
		MUTABLE.clear();
		for (NetherBiome netherBiome : NetherBiomesRegistry.getAllBiomes()) {
			Biome biome = biomeRegistry.getOrDefault(netherBiome.getID());
			netherBiome.setActualBiome(biome);
			MUTABLE.put(biome, netherBiome);
		}
		
		if (maxDefChance == 0) maxDefChance = maxChance;
		maxChance = maxDefChance;

		Iterator<Biome> iterator = biomeRegistry.iterator();
		while (iterator.hasNext()) {
			Biome biome = iterator.next();
			if (biome.getCategory() == Category.NETHER && !MUTABLE.containsKey(biome)) {
				ResourceLocation id = biomeRegistry.getKey(biome);
				NetherBiome netherBiome = new NetherBiomeWrapper(biomeRegistry.getKey(biome), biome);
				netherBiome.setActualBiome(biome);
				MUTABLE.put(biome, netherBiome);

				float chance = Configs.GENERATOR.getFloat("biomes." + id.getNamespace() + ".main", id.getPath() + "_chance", 1);
				if (chance > 0.0F) {
					maxChance += chance;
					netherBiome.setGenChance(maxChance);
					String path = "generator.biome." + netherBiome.getID().getNamespace() + "." + netherBiome.getID().getPath();
					netherBiome.setPlantDensity(Configs.BIOMES.getFloat(path, "plants_and_structures_density", 1));
					netherBiome.build();
					GENERATOR.add(netherBiome);
				}
			}
		}
		
		Config.save();
	}
	
	private static void register(NetherBiome biome) {
		if (ForgeRegistries.BIOMES.getKeys().contains(biome.getID())) {
			biome.getBiome().setRegistryName(biome.getID());
			ForgeRegistries.BIOMES.register(biome.getBiome());
		}
	}
	
	public static void registerNetherBiome(NetherBiome biome) {
		float chance = Configs.GENERATOR.getFloat("biomes." + biome.getID().getNamespace() + ".main", biome.getID().getPath() + "_chance", 1);
		if (chance > 0.0F) {
			maxChance += chance;
			String path = "generator.biome." + biome.getID().getNamespace() + "." + biome.getID().getPath();
			biome.setPlantDensity(Configs.BIOMES.getFloat(path, "plants_and_structures_density", 1));
			biome.setGenChance(maxChance);
			biome.build();
			biome.setRegistryName(biome.getID());
			ALL_BIOMES.add(biome);
		}
	}
	
	public static void registerEdgeBiome(NetherBiome biome, NetherBiome mainBiome, int size) {
		String regName = biome.getRawBiomeRegistryName();
		float sizeConf = Configs.GENERATOR.getFloat("biomes.betternether.edge", regName + "_size", size);
		if (sizeConf > 0.0F) {
			String path = "generator.biome." + biome.getID().getNamespace() + "." + biome.getID().getPath();
			biome.setPlantDensity(Configs.BIOMES.getFloat(path, "plants_and_structures_density", 1));
			mainBiome.setEdge(biome);
			mainBiome.setEdgeSize(sizeConf);
			biome.build();
			ALL_BIOMES.add(biome);
		}
	}
	
	public static void registerSubBiome(NetherBiome biome, NetherBiome mainBiome, float chance) {
		String regName = biome.getRawBiomeRegistryName();
		chance = Configs.GENERATOR.getFloat("biomes.betternether.variation", regName + "_chance", chance);
		if (chance > 0.0F) {
			String path = "generator.biome." + biome.getID().getNamespace() + "." + biome.getID().getPath();
			biome.setPlantDensity(Configs.BIOMES.getFloat(path, "plants_and_structures_density", 1));
			mainBiome.addSubBiome(biome, chance);
			biome.build();
			ALL_BIOMES.add(biome);
		}
	}
	
	public static NetherBiome getBiome(Random random) {
		float chance = random.nextFloat() * maxChance;
		for (NetherBiome biome : GENERATOR)
			if (biome.canGenerate(chance))
				return biome;
		return BIOME_EMPTY_NETHER;
	}

	public static NetherBiome getFromBiome(Biome biome) {
		return MUTABLE.getOrDefault(biome, BIOME_EMPTY_NETHER);
	}

	public static ArrayList<NetherBiome> getRegisteredBiomes() {
		return new ArrayList<NetherBiome>(REGISTRY.getValues());
	}

	public static ArrayList<NetherBiome> getAllBiomes() {
		return ALL_BIOMES;
	}



}
