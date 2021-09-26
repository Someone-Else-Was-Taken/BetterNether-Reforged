package someoneelse.betternetherreforged.registry;

import java.sql.Struct;
import java.util.Map;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;


import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.config.Configs;
import someoneelse.betternetherreforged.entity.EntityChair;
import someoneelse.betternetherreforged.structures.IStructure;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.structures.decorations.StructureCrystal;
import someoneelse.betternetherreforged.structures.decorations.StructureGeyser;
import someoneelse.betternetherreforged.world.structures.CityFeature;
import net.minecraft.world.gen.FlatGenerationSettings;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.structures.plants.*;
import someoneelse.betternetherreforged.world.structures.piece.CityPiece;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StructureRegistry {
	private static final Map<StructureType, Map<String, IStructure>> REGISTRY;

	public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BetterNether.MOD_ID);
	public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterNether.MOD_ID);


	public static void registerFeature(String name, IStructure structure, StructureType type) {
		REGISTRY.get(type).put(name, structure);
	}

	static {
		REGISTRY = Maps.newHashMap();
		for (StructureType type : StructureType.values()) {
			Map<String, IStructure> list = Maps.newHashMap();
			REGISTRY.put(type, list);
		}

		registerFeature("agave", new StructureAgave(), StructureType.FLOOR);
		registerFeature("barrel_cactus", new StructureBarrelCactus(), StructureType.FLOOR);
		registerFeature("big_warped_tree", new StructureBigWarpedTree(), StructureType.FLOOR);
		registerFeature("black_apple", new StructureBlackApple(), StructureType.FLOOR);
		registerFeature("black_bush", new StructureBlackBush(), StructureType.FLOOR);
		registerFeature("bone_reef", new StructureBoneReef(), StructureType.FLOOR);
		registerFeature("bush_rubeus", new StructureRubeusBush(), StructureType.FLOOR);
		registerFeature("crimson_fungus", new StructureCrimsonFungus(), StructureType.FLOOR);
		registerFeature("crimson_glowing_tree", new StructureCrimsonGlowingTree(), StructureType.FLOOR);
		registerFeature("crimson_pinewood", new StructureCrimsonPinewood(), StructureType.FLOOR);
		registerFeature("crimson_roots", new StructureCrimsonRoots(), StructureType.FLOOR);
		registerFeature("egg_plant", new StructureEggPlant(), StructureType.FLOOR);
		registerFeature("geyser", new StructureGeyser(), StructureType.FLOOR);
		registerFeature("giant_mold", new StructureGiantMold(), StructureType.FLOOR);
		registerFeature("gray_mold", new StructureGrayMold(), StructureType.FLOOR);
		registerFeature("ink_bush", new StructureInkBush(), StructureType.FLOOR);
		registerFeature("jungle_plant", new StructureJunglePlant(), StructureType.FLOOR);
		registerFeature("large_brown_mushroom", new StructureMedBrownMushroom(), StructureType.FLOOR);
		registerFeature("large_red_mushroom", new StructureMedRedMushroom(), StructureType.FLOOR);
		registerFeature("magma_flower", new StructureMagmaFlower(), StructureType.FLOOR);
		registerFeature("mushroom_fir", new StructureMushroomFir(), StructureType.FLOOR);
		registerFeature("nether_cactus", new StructureNetherCactus(), StructureType.FLOOR);
		registerFeature("nether_grass", new StructureNetherGrass(), StructureType.FLOOR);
		registerFeature("swamp_grass", new StructureSwampGrass(), StructureType.FLOOR);
		registerFeature("nether_reed", new StructureReeds(), StructureType.FLOOR);
		registerFeature("nether_wart", new StructureNetherWart(), StructureType.FLOOR);
		registerFeature("obsidian_crystals", new StructureCrystal(), StructureType.FLOOR);
		registerFeature("old_brown_mushrooms", new StructureOldBrownMushrooms(), StructureType.FLOOR);
		registerFeature("old_red_mushrooms", new StructureOldRedMushrooms(), StructureType.FLOOR);
		registerFeature("orange_mushroom", new StructureOrangeMushroom(), StructureType.FLOOR);
		registerFeature("red_mold", new StructureRedMold(), StructureType.FLOOR);
		registerFeature("rubeus_tree", new StructureRubeus(), StructureType.FLOOR);
		registerFeature("smoker", new StructureSmoker(), StructureType.FLOOR);
		registerFeature("soul_grass", new StructureSoulGrass(), StructureType.FLOOR);
		registerFeature("soul_lily", new StructureSoulLily(), StructureType.FLOOR);
		registerFeature("soul_vein", new StructureSoulVein(), StructureType.FLOOR);
		registerFeature("stalagnate", new StructureStalagnate(), StructureType.FLOOR);
		registerFeature("twisted_vine", new StructureTwistedVines(), StructureType.FLOOR);
		registerFeature("vanilla_mushrooms", new StructureVanillaMushroom(), StructureType.FLOOR);
		registerFeature("warped_fungus", new StructureWarpedFungus(), StructureType.FLOOR);
		registerFeature("warped_roots", new StructureWarpedRoots(), StructureType.FLOOR);
		registerFeature("wart_bush", new StructureWartBush(), StructureType.FLOOR);
		registerFeature("wart_deadwood", new StructureWartDeadwood(), StructureType.FLOOR);
		registerFeature("wart_seed", new StructureWartSeed(), StructureType.FLOOR);
		registerFeature("wart_tree", new StructureWartTree(), StructureType.FLOOR);
		registerFeature("willow", new StructureWillow(), StructureType.FLOOR);

		registerFeature("black_vine", new StructureBlackVine(), StructureType.CEIL);
		registerFeature("eye", new StructureEye(), StructureType.CEIL);
		registerFeature("flowered_vine", new StructureBloomingVine(), StructureType.CEIL);
		registerFeature("golden_vine", new StructureGoldenVine(), StructureType.CEIL);

		registerFeature("lucis", new StructureLucis(), StructureType.WALL);
		registerFeature("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL);
		registerFeature("wall_moss", new StructureWallMoss(), StructureType.WALL);
		registerFeature("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL);



	}

	public static final RegistryObject<Structure<NoFeatureConfig>> NETHER_CITY = register("nether_city", new CityFeature(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.STRONGHOLDS);

	private static <T extends Structure<?>> RegistryObject<T> register(String name, T structure, GenerationStage.Decoration decoration) {
		Structure.NAME_STRUCTURE_BIMAP.put(BetterNether.MOD_ID + ":" + name, structure);
		Structure.STRUCTURE_DECORATION_STAGE_MAP.put(structure, decoration);
		return STRUCTURES.register(name, () -> structure);
	}



	/**
	 * Registers the configured structure which is what gets added to the biomes.
	 * Noticed we are not using a forge registry because there is none for configured structures.
	 *
	 * We can register configured structures at any time before a world is clicked on and made.
	 * But the best time to register configured features by code is honestly to do it in FMLCommonSetupEvent.
	 */

	/*
	private static final Map<StructureType, Map<String, IStructure>> REGISTRY;

	private static final int cityDistance = Configs.GENERATOR.getInt("generator.world.cities", "distance", 64);
	private static final int citySeparation = cityDistance >> 1;

	public static final DeferredRegister<Structure<?>> DEFERRED_STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterNether.MOD_ID);
	public static final DeferredRegister<Feature<?>> DEFERRED_FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BetterNether.MOD_ID);

	Structure<NoFeatureConfig> NETHER_CITY = registerStructure("nether_city", new CityFeature(), GenerationStage.Decoration.STRONGHOLDS, new StructureSeparationSettings(cityDistance, citySeparation, 1234));
	public static <S extends Structure<NoFeatureConfig>> S registerStructure(String name, S structure, GenerationStage.Decoration stage, StructureSeparationSettings separation) {
		RegistryObject<S> entry = DEFERRED_STRUCTURES.register(name, () -> structure);
		S struct = entry.get();
		Structure.NAME_STRUCTURE_BIMAP.put(name, struct);
		Structure.STRUCTURE_DECORATION_STAGE_MAP.put(struct, stage);
		DimensionStructuresSettings.field_236191_b_ = ImmutableMap.<Structure<?>, StructureSeparationSettings>builder().putAll(DimensionStructuresSettings.field_236191_b_).put(struct, separation).build();
		FlatGenerationSettings.STRUCTURES.put(structure, structure.withConfiguration(IFeatureConfig.NO_FEATURE_CONFIG));
		return struct;
	}


	public static void registerFeature(String name, IStructure structure, StructureType type) {
		REGISTRY.get(type).put(name, structure);
	}

	public static IStructure getStructure(String name, StructureType type) {
		return REGISTRY.get(type).get(name);
	}

	static {
		REGISTRY = Maps.newHashMap();
		for (StructureType type : StructureType.values()) {
			Map<String, IStructure> list = Maps.newHashMap();
			REGISTRY.put(type, list);
		}

		registerFeature("agave", new StructureAgave(), StructureType.FLOOR);
		registerFeature("barrel_cactus", new StructureBarrelCactus(), StructureType.FLOOR);
		registerFeature("big_warped_tree", new StructureBigWarpedTree(), StructureType.FLOOR);
		registerFeature("black_apple", new StructureBlackApple(), StructureType.FLOOR);
		registerFeature("black_bush", new StructureBlackBush(), StructureType.FLOOR);
		registerFeature("bone_reef", new StructureBoneReef(), StructureType.FLOOR);
		registerFeature("bush_rubeus", new StructureRubeusBush(), StructureType.FLOOR);
		registerFeature("crimson_fungus", new StructureCrimsonFungus(), StructureType.FLOOR);
		registerFeature("crimson_glowing_tree", new StructureCrimsonGlowingTree(), StructureType.FLOOR);
		registerFeature("crimson_pinewood", new StructureCrimsonPinewood(), StructureType.FLOOR);
		registerFeature("crimson_roots", new StructureCrimsonRoots(), StructureType.FLOOR);
		registerFeature("egg_plant", new StructureEggPlant(), StructureType.FLOOR);
		registerFeature("geyser", new StructureGeyser(), StructureType.FLOOR);
		registerFeature("giant_mold", new StructureGiantMold(), StructureType.FLOOR);
		registerFeature("gray_mold", new StructureGrayMold(), StructureType.FLOOR);
		registerFeature("ink_bush", new StructureInkBush(), StructureType.FLOOR);
		registerFeature("jungle_plant", new StructureJunglePlant(), StructureType.FLOOR);
		registerFeature("large_brown_mushroom", new StructureMedBrownMushroom(), StructureType.FLOOR);
		registerFeature("large_red_mushroom", new StructureMedRedMushroom(), StructureType.FLOOR);
		registerFeature("magma_flower", new StructureMagmaFlower(), StructureType.FLOOR);
		registerFeature("mushroom_fir", new StructureMushroomFir(), StructureType.FLOOR);
		registerFeature("nether_cactus", new StructureNetherCactus(), StructureType.FLOOR);
		registerFeature("nether_grass", new StructureNetherGrass(), StructureType.FLOOR);
		registerFeature("swamp_grass", new StructureSwampGrass(), StructureType.FLOOR);
		registerFeature("nether_reed", new StructureReeds(), StructureType.FLOOR);
		registerFeature("nether_wart", new StructureNetherWart(), StructureType.FLOOR);
		registerFeature("obsidian_crystals", new StructureCrystal(), StructureType.FLOOR);
		registerFeature("old_brown_mushrooms", new StructureOldBrownMushrooms(), StructureType.FLOOR);
		registerFeature("old_red_mushrooms", new StructureOldRedMushrooms(), StructureType.FLOOR);
		registerFeature("orange_mushroom", new StructureOrangeMushroom(), StructureType.FLOOR);
		registerFeature("red_mold", new StructureRedMold(), StructureType.FLOOR);
		registerFeature("rubeus_tree", new StructureRubeus(), StructureType.FLOOR);
		registerFeature("smoker", new StructureSmoker(), StructureType.FLOOR);
		registerFeature("soul_grass", new StructureSoulGrass(), StructureType.FLOOR);
		registerFeature("soul_lily", new StructureSoulLily(), StructureType.FLOOR);
		registerFeature("soul_vein", new StructureSoulVein(), StructureType.FLOOR);
		registerFeature("stalagnate", new StructureStalagnate(), StructureType.FLOOR);
		registerFeature("twisted_vine", new StructureTwistedVines(), StructureType.FLOOR);
		registerFeature("vanilla_mushrooms", new StructureVanillaMushroom(), StructureType.FLOOR);
		registerFeature("warped_fungus", new StructureWarpedFungus(), StructureType.FLOOR);
		registerFeature("warped_roots", new StructureWarpedRoots(), StructureType.FLOOR);
		registerFeature("wart_bush", new StructureWartBush(), StructureType.FLOOR);
		registerFeature("wart_deadwood", new StructureWartDeadwood(), StructureType.FLOOR);
		registerFeature("wart_seed", new StructureWartSeed(), StructureType.FLOOR);
		registerFeature("wart_tree", new StructureWartTree(), StructureType.FLOOR);
		registerFeature("willow", new StructureWillow(), StructureType.FLOOR);

		registerFeature("black_vine", new StructureBlackVine(), StructureType.CEIL);
		registerFeature("eye", new StructureEye(), StructureType.CEIL);
		registerFeature("flowered_vine", new StructureBloomingVine(), StructureType.CEIL);
		registerFeature("golden_vine", new StructureGoldenVine(), StructureType.CEIL);

		registerFeature("lucis", new StructureLucis(), StructureType.WALL);
		registerFeature("wall_brown_mushroom", new StructureWallBrownMushroom(), StructureType.WALL);
		registerFeature("wall_moss", new StructureWallMoss(), StructureType.WALL);
		registerFeature("wall_red_mushroom", new StructureWallRedMushroom(), StructureType.WALL);


	}

	public static void registerCity() {
		//public static final DeferredRegister<Structure<?>> STRUCTURES = DeferredRegister.create(ForgeRegistries.STRUCTURE_FEATURES, BetterNether.MOD_ID);
		//public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, BetterNether.MOD_ID);

		//Structure<NoFeatureConfig> NETHER_CITY = registerStructure("nether_city", new CityFeature(NoFeatureConfig.field_236558_a_), GenerationStage.Decoration.STRONGHOLDS, new StructureSeparationSettings(cityDistance, citySeparation, 1234));
		//public static final RegistryObject<Structure<NoFeatureConfig>> NETHER_CITY = STRUCTURES.register("nether_city", () -> (new CityFeature(NoFeatureConfig.field_236558_a_)));


	}
	*/




}