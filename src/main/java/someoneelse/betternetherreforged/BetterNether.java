package someoneelse.betternetherreforged;


import com.mojang.serialization.Codec;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.FlatChunkGenerator;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.settings.DimensionStructuresSettings;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import someoneelse.betternetherreforged.biomes.NetherBiome;
import someoneelse.betternetherreforged.blocks.BNRenderLayer;
import someoneelse.betternetherreforged.client.IRenderTypeable;
import someoneelse.betternetherreforged.config.Config;
import someoneelse.betternetherreforged.config.Configs;
import someoneelse.betternetherreforged.registry.*;
import someoneelse.betternetherreforged.world.BNWorldGenerator;
import someoneelse.betternetherreforged.world.NetherBiomeProvider;
import someoneelse.betternetherreforged.structures.StructureType;
import someoneelse.betternetherreforged.world.BNWorldGenerator;
import someoneelse.betternetherreforged.world.NetherBiomeProvider;
import someoneelse.betternetherreforged.world.structures.CityFeature;
import someoneelse.betternetherreforged.world.structures.piece.StructureTypes;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.datagen.DataGenerators;
import someoneelse.betternetherreforged.recipes.ItemRecipes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Mod(BetterNether.MOD_ID)
public class BetterNether
{
    // Directly reference a log4j logger.
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "betternether";
	private static boolean thinArmor = true;
	private static boolean lavafallParticles = true;
	private static float fogStart = 0.05F;
	private static float fogEnd = 0.5F;
    
    public BetterNether() {
    	IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    	IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;

    	
    	initOptions();
    	
    	modEventBus.addListener(this::commonSetup);
    	modEventBus.addListener(this::clientSetup);
    	
    	modEventBus.addListener(DataGenerators::gatherData);
    	forgeEventBus.addListener(FuelRegistry::onFuelBurnTimeEvent);
    	modEventBus.addListener(NetherBiomesRegistry::createRegistry);
    	modEventBus.addGenericListener(SoundEvent.class, SoundsRegistry::registerAll);
    	modEventBus.addGenericListener(EntityType.class, EntityRegistry::registerAll);
    	modEventBus.addGenericListener(Block.class, BlocksRegistry::registerAll);
    	modEventBus.addGenericListener(Item.class, ItemsRegistry::registerAll);
    	modEventBus.addGenericListener(TileEntityType.class, TileEntitiesRegistry::registerAll);
    	modEventBus.addGenericListener(NetherBiome.class, NetherBiomesRegistry::registerNetherBiomes);
    	modEventBus.addGenericListener(Biome.class, NetherBiomesRegistry::registerBiomes);

    	//StructureRegistry.DEFERRED_FEATURES.register(modEventBus);
    	//StructureRegistry.DEFERRED_STRUCTURES.register(modEventBus);
    	NetherBiomesRegistry.init();
    	StructureTypes.init();
		EntityRegistry.registerNetherEntities();
    	BNWorldGenerator.onModInit();
    	BrewingRegistry.register();
    	Config.save();
		//StructureRegistry.registerCity();
		StructureRegistry.STRUCTURES.register(modEventBus);
		StructureRegistry.FEATURES.register(modEventBus);

    	NetherTags.init();
    	
    	NetherBiomeProvider.register();
		CityRegistry.DEFERRED_REGISTRY_STRUCTURE.register(modEventBus);

		IEventBus forgeBus = MinecraftForge.EVENT_BUS;

		// The comments for BiomeLoadingEvent and StructureSpawnListGatherEvent says to do HIGH for additions.
		forgeBus.addListener(EventPriority.HIGH, this::biomeModification);

    }
    
    private void clientSetup(FMLClientSetupEvent event) {
		registerRenderLayers();
		EntityRenderRegistry.register();
		TileEntityRenderRegistry.register();
    }
    
    private void commonSetup(FMLCommonSetupEvent event) {
		StructurePieceRegistry.init();
		ItemRecipes.register();
		event.enqueueWork(() -> {
			CityRegistry.setupStructures();
			ConfiguredStructureRegistry.registerConfiguredStructures();

		});
    }
    
	private void initOptions() {
		thinArmor = Configs.MAIN.getBoolean("improvement", "smaller_armor_offset", true);
		lavafallParticles = Configs.MAIN.getBoolean("improvement", "lavafall_particles", true);
		float density = Configs.MAIN.getFloat("improvement", "fog_density[vanilla: 1.0]", 0.75F);
		changeFogDensity(density);
	}
	
	private void registerRenderLayers() {
		RenderType cutout = RenderType.getCutout();
		RenderType translucent = RenderType.getTranslucent();
		ForgeRegistries.BLOCKS.forEach(block -> {
			if (block instanceof IRenderTypeable) {
				BNRenderLayer layer = ((IRenderTypeable) block).getRenderLayer();
				if (layer == BNRenderLayer.CUTOUT)
					RenderTypeLookup.setRenderLayer(block, cutout);
				else if (layer == BNRenderLayer.TRANSLUCENT)
					RenderTypeLookup.setRenderLayer(block, translucent);
			}
		});
	}
	
	public static boolean hasThinArmor() {
		return thinArmor;
	}

	public static void setThinArmor(boolean value) {
		thinArmor = value;
	}

	public static boolean hasLavafallParticles() {
		return lavafallParticles;
	}
	
	public static void changeFogDensity(float density) {
		fogStart = -0.45F * density + 0.5F;
		fogEnd = -0.5F * density + 1;
	}

	public static float getFogStart() {
		return fogStart;
	}

	public static float getFogEnd() {
		return fogEnd;
	}


	public void biomeModification(final BiomeLoadingEvent event) {
		event.getGeneration().getStructures().add(() -> ConfiguredStructureRegistry.CONFIGURED_CITY);
	}

	public static <T extends IForgeRegistryEntry<T>> T register(IForgeRegistry<T> registry, T entry, String registryKey)
	{
		entry.setRegistryName(new ResourceLocation(BetterNether.MOD_ID, registryKey));
		registry.register(entry);
		return entry;
	}

	private static Method GETCODEC_METHOD;
	public void addDimensionalSpacing(final WorldEvent.Load event) {
		if(event.getWorld() instanceof ServerWorld){
			ServerWorld serverWorld = (ServerWorld)event.getWorld();

			/*
			 * Skip Terraforged's chunk generator as they are a special case of a mod locking down their chunkgenerator.
			 * They will handle your structure spacing for your if you add to WorldGenRegistries.NOISE_GENERATOR_SETTINGS in your structure's registration.
			 * This here is done with reflection as this tutorial is not about setting up and using Mixins.
			 * If you are using mixins, you can call the codec method with an invoker mixin instead of using reflection.
			 */
			try {
				if(GETCODEC_METHOD == null) GETCODEC_METHOD = ObfuscationReflectionHelper.findMethod(ChunkGenerator.class, "func_230347_a_");
				ResourceLocation cgRL = Registry.CHUNK_GENERATOR_CODEC.getKey((Codec<? extends ChunkGenerator>) GETCODEC_METHOD.invoke(serverWorld.getChunkProvider().generator));
				if(cgRL != null && cgRL.getNamespace().equals("terraforged")) return;
			}
			catch(Exception e){
				BetterNether.LOGGER.error("Was unable to check if " + serverWorld.getDimensionKey().getLocation() + " is using Terraforged's ChunkGenerator.");
			}

			/*
			 * Prevent spawning our structure in Vanilla's superflat world as
			 * people seem to want their superflat worlds free of modded structures.
			 * Also that vanilla superflat is really tricky and buggy to work with in my experience.
			 */
			if(serverWorld.getChunkProvider().getChunkGenerator() instanceof FlatChunkGenerator &&
					serverWorld.getDimensionKey().equals(World.THE_NETHER)){
				return;
			}

			/*
			 * putIfAbsent so people can override the spacing with dimension datapacks themselves if they wish to customize spacing more precisely per dimension.
			 * Requires AccessTransformer  (see resources/META-INF/accesstransformer.cfg)
			 *
			 * NOTE: if you add per-dimension spacing configs, you can't use putIfAbsent as WorldGenRegistries.NOISE_GENERATOR_SETTINGS in FMLCommonSetupEvent
			 * already added your default structure spacing to some dimensions. You would need to override the spacing with .put(...)
			 * And if you want to do dimension blacklisting, you need to remove the spacing entry entirely from the map below to prevent generation safely.
			 */
			Map<Structure<?>, StructureSeparationSettings> tempMap = new HashMap<>(serverWorld.getChunkProvider().generator.func_235957_b_().func_236195_a_());
			tempMap.putIfAbsent(CityRegistry.CITY.get(), DimensionStructuresSettings.field_236191_b_.get(CityRegistry.CITY.get()));

		}
	}

}
