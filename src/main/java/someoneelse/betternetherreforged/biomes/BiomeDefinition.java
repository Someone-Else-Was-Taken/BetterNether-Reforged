package someoneelse.betternetherreforged.biomes;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.util.datafix.fixes.BiomeName;
import net.minecraft.util.datafix.fixes.SpawnerEntityTypes;
import net.minecraft.util.registry.Registry;
import someoneelse.betternetherreforged.config.Configs;
import net.minecraft.client.audio.BackgroundMusicTracks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biome.Category;
import net.minecraft.world.biome.Biome.RainType;
import net.minecraft.world.biome.BiomeAmbience;
import net.minecraft.world.biome.BiomeGenerationSettings;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.biome.MoodSoundAmbience;
import net.minecraft.world.biome.ParticleEffectAmbience;
import net.minecraft.world.biome.SoundAdditionsAmbience;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.carver.ConfiguredCarvers;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilders;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.EntityRegistry;


public class BiomeDefinition {
	private final List<StructureFeature<?, ?>> structures = Lists.newArrayList();
	private final List<FeatureInfo> features = Lists.newArrayList();
	private final List<SpawnInfo> mobs = Lists.newArrayList();

	private ParticleEffectAmbience particleConfig;
	private SoundAdditionsAmbience additions;
	private MoodSoundAmbience mood;
	private SoundEvent music;
	private SoundEvent loop;


	private int waterFogColor = 329011;
	private int waterColor = 4159204;
	private int fogColor = 3344392;

	private boolean defaultOres = true;
	private boolean defaultMobs = true;
	private boolean defaultFeatures = true;
	private boolean defaultStructureFeatures = true;
	private boolean stalactites = true;
	private boolean bnStructures = true;

	private final ResourceLocation id;

	public BiomeDefinition(String name) {
		this.id = new ResourceLocation(BetterNether.MOD_ID, name.replace(' ', '_').toLowerCase());
	}

	public BiomeDefinition(ResourceLocation id) {
		this.id = id;
	}

	public BiomeDefinition setStalactites(boolean value) {
		stalactites = value;
		return this;
	}

	public BiomeDefinition setBNStructures(boolean value) {
		bnStructures = value;
		return this;
	}

	/**
	 * Set default ores generation
	 * 
	 * @param value
	 *            - if true (default) then default ores will be generated
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setDefaultOres(boolean value) {
		defaultOres = value;
		return this;
	}

	/**
	 * Set default nether structure features to be added
	 * 
	 * @param value
	 *            - if true (default) then default structure features (nether
	 *            fortresses, caves, etc.) will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setDefaultStructureFeatures(boolean value) {
		defaultStructureFeatures = value;
		return this;
	}

	/**
	 * Set default nether features to be added
	 * 
	 * @param value
	 *            - if true (default) then default features (small structures)
	 *            will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setDefaultFeatures(boolean value) {
		defaultFeatures = value;
		return this;
	}

	/**
	 * Set default Nether Wastes mobs to be added
	 * 
	 * @param value
	 *            - if true (default) then default mobs will be added into biome
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setDefaultMobs(boolean value) {
		defaultMobs = value;
		return this;
	}


	public BiomeDefinition setParticleConfig(ParticleEffectAmbience config) {
		this.particleConfig = config;
		return this;
	}

	/**
	 * Adds mob into biome
	 * 
	 * @param type
	 *            - {@link EntityType}
	 * @param weight
	 *            - cumulative spawning weight
	 * @param minGroupSize
	 *            - minimum count of mobs in the group
	 * @param maxGroupSize
	 *            - maximum count of mobs in the group
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addMobSpawn(EntityType<?> type, int weight, int minGroupSize, int maxGroupSize) {
		ResourceLocation eID = Registry.ENTITY_TYPE.getKey(type);
		if (eID == Registry.ENTITY_TYPE.getDefaultKey()) {
			String path = "generator.biome." + id.getNamespace() + "." + id.getPath() + ".mobs." + eID.getNamespace() + "." + eID.getPath();
			SpawnInfo info = new SpawnInfo();
			info.type = type;
			info.weight = Configs.BIOMES.getInt(path, "weight", weight);
			info.minGroupSize = Configs.BIOMES.getInt(path, "min_group_size", minGroupSize);
			info.maxGroupSize = Configs.BIOMES.getInt(path, "max_group_size", maxGroupSize);
			mobs.add(info);
		}
		return this;
	}





	/**
	 * Adds feature (small structure) into biome - plants, ores, small
	 * buildings, etc.
	 * 
	 * @param feature
	 *            - {@link StructureFeature} to add
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition addStructureFeature(StructureFeature<?, ?> feature) {
		structures.add(feature);
		return this;
	}

	public BiomeDefinition addFeature(GenerationStage.Decoration featureStep, ConfiguredFeature<?, ?> feature) {
		FeatureInfo info = new FeatureInfo();
		info.featureStep = featureStep;
		info.feature = feature;
		features.add(info);
		return this;
	}

	/**
	 * Sets biome fog color
	 * 
	 * @param r
	 *            - Red [0 - 255]
	 * @param g
	 *            - Green [0 - 255]
	 * @param b
	 *            - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setFogColor(int r, int g, int b) {
		String path = "generator.biome." + id.getNamespace() + "." + id.getPath() + ".fog_color";
		r = MathHelper.clamp(Configs.BIOMES.getInt(path, "red", r), 0, 255);
		g = MathHelper.clamp(Configs.BIOMES.getInt(path, "green", g), 0, 255);
		b = MathHelper.clamp(Configs.BIOMES.getInt(path, "blue", b), 0, 255);
		this.fogColor = MHelper.color(r, g, b);
		return this;
	}

	/**
	 * Sets biome water color
	 * 
	 * @param r
	 *            - Red [0 - 255]
	 * @param g
	 *            - Green [0 - 255]
	 * @param b
	 *            - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setWaterColor(int r, int g, int b) {
		String path = "generator.biome." + id.getNamespace() + "." + id.getPath() + ".water_color";
		r = MathHelper.clamp(Configs.BIOMES.getInt(path, "red", r), 0, 255);
		g = MathHelper.clamp(Configs.BIOMES.getInt(path, "green", g), 0, 255);
		b = MathHelper.clamp(Configs.BIOMES.getInt(path, "blue", b), 0, 255);
		this.waterColor = MHelper.color(r, g, b);
		return this;
	}

	/**
	 * Sets biome underwater fog color
	 * 
	 * @param r
	 *            - Red [0 - 255]
	 * @param g
	 *            - Green [0 - 255]
	 * @param b
	 *            - Blue [0 - 255]
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setWaterFogColor(int r, int g, int b) {
		String path = "generator.biome." + id.getNamespace() + "." + id.getPath() + ".water_fog_color";
		r = MathHelper.clamp(Configs.BIOMES.getInt(path, "red", r), 0, 255);
		g = MathHelper.clamp(Configs.BIOMES.getInt(path, "green", g), 0, 255);
		b = MathHelper.clamp(Configs.BIOMES.getInt(path, "blue", b), 0, 255);
		this.waterFogColor = MHelper.color(r, g, b);
		return this;
	}

	/**
	 * Plays in never-ending loop for as long as player is in the biome
	 * 
	 * @param loop
	 *            - SoundEvent
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setLoop(SoundEvent loop) {
		this.loop = loop;
		return this;
	}

	/**
	 * Plays commonly while the player is in the biome
	 * 
	 * @param mood
	 *            - SoundEvent
	 * @return this {@link BiomeDefinition}
	 */
	public BiomeDefinition setMood(SoundEvent mood) {
		this.mood = new MoodSoundAmbience(mood, 6000, 8, 2.0D);
		return this;
	}

	/**
	 * Set additional sounds. They plays once every 6000-17999 ticks while the
	 * player is in the biome
	 * 
	 * @param additions
	 *            - SoundEvent
	 * @return this BiomeDefenition
	 */
	public BiomeDefinition setAdditions(SoundEvent additions) {
		this.additions = new SoundAdditionsAmbience(additions, 0.0111);
		return this;
	}

	/**
	 * Set background music for biome
	 * 
	 * @param music
	 * @return
	 */
	public BiomeDefinition setMusic(SoundEvent music) {
		this.music = music;
		return this;
	}


	public Biome build() {
		MobSpawnInfo.Builder spawnSettings = new MobSpawnInfo.Builder();
		BiomeGenerationSettings.Builder generationSettings = new BiomeGenerationSettings.Builder();
		BiomeAmbience.Builder effects = new BiomeAmbience.Builder();



		if (defaultMobs) addDefaultMobs(spawnSettings);
		mobs.forEach((spawn) -> {
			spawnSettings.withSpawner(spawn.type.getClassification(), new MobSpawnInfo.Spawners(spawn.type, spawn.weight, spawn.minGroupSize, spawn.maxGroupSize));
		});






		generationSettings.withSurfaceBuilder(ConfiguredSurfaceBuilders.field_244183_o);
		structures.forEach((structure) -> generationSettings.withStructure(structure));
		features.forEach((info) -> generationSettings.withFeature(info.featureStep, info.feature));
		if (defaultOres) DefaultBiomeFeatures.withCommonNetherBlocks(generationSettings);
		if (defaultStructureFeatures) addDefaultStructures(generationSettings);
		if (defaultFeatures) addDefaultFeatures(generationSettings);

		effects.withSkyColor(fogColor).setWaterColor(waterColor).setWaterFogColor(waterFogColor).setFogColor(fogColor);
		if (loop != null) effects.setAmbientSound(loop);
		if (mood != null) effects.setMoodSound(mood);
		if (additions != null) effects.setAdditionsSound(additions);
		if (particleConfig != null) effects.setParticle(particleConfig);
		effects.setMusic(BackgroundMusicTracks.getDefaultBackgroundMusicSelector(music != null ? music : SoundEvents.MUSIC_NETHER_NETHER_WASTES));

		return new Biome.Builder()
				.precipitation(RainType.NONE)
				.category(Category.NETHER)
				.depth(0.1F)
				.scale(0.2F)
				.temperature(2.0F)
				.downfall(0.0F)
				.setEffects(effects.build())
				.withMobSpawnSettings(spawnSettings.copy())
				.withGenerationSettings(generationSettings.build())
				.build();
	}


	private void addDefaultStructures(BiomeGenerationSettings.Builder generationSettings) {
		generationSettings.withStructure(StructureFeatures.RUINED_PORTAL_NETHER);
		generationSettings.withStructure(StructureFeatures.FORTRESS);
		generationSettings.withStructure(StructureFeatures.BASTION_REMNANT);
		generationSettings.withCarver(GenerationStage.Carving.AIR, ConfiguredCarvers.field_243772_f);
		generationSettings.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION, Features.SPRING_LAVA);
	}

	private void addDefaultFeatures(BiomeGenerationSettings.Builder generationSettings) {
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_OPEN);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_FIRE);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.PATCH_SOUL_FIRE);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE_EXTRA);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.GLOWSTONE);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.BROWN_MUSHROOM_NETHER);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.RED_MUSHROOM_NETHER);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.ORE_MAGMA);
		generationSettings.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION, Features.SPRING_CLOSED);
	}

	private void addDefaultMobs(MobSpawnInfo.Builder spawnSettings) {
		spawnSettings.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.GHAST, 50, 4, 4));
		spawnSettings.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4));
		spawnSettings.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.MAGMA_CUBE, 2, 4, 4));
		spawnSettings.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.ENDERMAN, 1, 4, 4));
		spawnSettings.withSpawner(EntityClassification.MONSTER, new MobSpawnInfo.Spawners(EntityType.PIGLIN, 15, 4, 4));
		spawnSettings.withSpawner(EntityClassification.CREATURE, new MobSpawnInfo.Spawners(EntityType.STRIDER, 60, 1, 2));
	}

	private static final class SpawnInfo {
		EntityType<?> type;
		int weight;
		int minGroupSize;
		int maxGroupSize;
	}



	private static final class FeatureInfo {
		GenerationStage.Decoration featureStep;
		ConfiguredFeature<?, ?> feature;
	}

	public ResourceLocation getID() {
		return id;
	}

	public boolean hasStalactites() {
		return stalactites;
	}

	public boolean hasBNStructures() {
		return bnStructures;
	}
}