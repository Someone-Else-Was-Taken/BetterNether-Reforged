package someoneelse.betternetherreforged.world;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;


import net.java.games.input.Component;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.settings.StructureSeparationSettings;
import net.minecraftforge.common.world.StructureSpawnManager;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.biomes.NetherBiome;
import someoneelse.betternetherreforged.blocks.BlockStalactite;
import someoneelse.betternetherreforged.config.Configs;
import someoneelse.betternetherreforged.world.structures.CityFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.WorldGenRegion;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.structure.Structure;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.MHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.registry.NetherBiomesRegistry;
import someoneelse.betternetherreforged.structures.StructureCaves;
import someoneelse.betternetherreforged.structures.StructurePath;
import someoneelse.betternetherreforged.structures.StructureType;

public class BNWorldGenerator {
	private static boolean hasCleaningPass;
	private static boolean hasFixPass;

	private static float cincinnasiteDensity;
	private static float rubyDensity;
	private static float lapisDensity;
	private static float structureDensity;
	private static float lavaStructureDensity;
	private static float globalDensity;

	private static final BlockState AIR = Blocks.AIR.getDefaultState();

	private static Mutable popPos = new Mutable();

	private static final NetherBiome[][][] BIOMES = new NetherBiome[8][64][8];

	private static final List<BlockPos> LIST_FLOOR = new ArrayList<BlockPos>(4096);
	private static final List<BlockPos> LIST_WALL = new ArrayList<BlockPos>(4096);
	private static final List<BlockPos> LIST_CEIL = new ArrayList<BlockPos>(4096);
	private static final List<BlockPos> LIST_LAVA = new ArrayList<BlockPos>(1024);
	private static final HashSet<Biome> MC_BIOMES = new HashSet<Biome>();

	private static boolean hasCaves;
	private static boolean hasPaths;

	private static StructureCaves caves;
	private static StructurePath paths;
	private static NetherBiome biome;

	protected static int biomeSizeXZ;
	protected static int biomeSizeY;
	protected static boolean volumetric;

	public static final CityFeature CITY = new CityFeature();
	public static final StructureFeature<NoFeatureConfig, ? extends Structure<NoFeatureConfig>> CITY_CONFIGURED = CITY.withConfiguration(NoFeatureConfig.field_236559_b_);



	public static void onModInit() {
		hasCleaningPass = Configs.GENERATOR.getBoolean("generator.world.terrain", "terrain_cleaning_pass", true);
		hasFixPass = Configs.GENERATOR.getBoolean("generator.world.terrain", "world_fixing_pass", true);

		hasCaves = Configs.GENERATOR.getBoolean("generator.world.environment", "generate_caves", true);
		hasPaths = Configs.GENERATOR.getBoolean("generator.world.environment", "generate_paths", true);

		cincinnasiteDensity = Configs.GENERATOR.getFloat("generator.world.ores", "cincinnasite_ore_density", 1F / 1024F);
		rubyDensity = Configs.GENERATOR.getFloat("generator.world.ores", "ruby_ore_density", 1F / 4000F);
		lapisDensity = Configs.GENERATOR.getFloat("generator.world.ores", "lapis_ore_density", 1F / 4000F);
		structureDensity = Configs.GENERATOR.getFloat("generator.world", "structures_density", 1F / 16F) * 1.0001F;
		lavaStructureDensity = Configs.GENERATOR.getFloat("generator.world", "lava_structures_density", 1F / 200F) * 1.0001F;
		globalDensity = Configs.GENERATOR.getFloat("generator.world", "global_plant_and_structures_density", 1F) * 1.0001F;

		biomeSizeXZ = Configs.GENERATOR.getInt("generator_world", "biome_size_xz", 200);
		biomeSizeY = Configs.GENERATOR.getInt("generator_world", "biome_size_y", 40);
		volumetric = Configs.GENERATOR.getBoolean("generator_world", "volumetric_biomes", true);

		int distance = Configs.GENERATOR.getInt("generator.world.cities", "distance", 64);
		int separation = distance >> 1;

		Configs.GENERATOR.getBoolean("generator.world.cities", "generate", true);



	}



	public static void init(long seed) {
		caves = new StructureCaves(seed);
		paths = new StructurePath(seed + 1);
	}

	private static NetherBiome getBiomeLocal(int x, int y, int z, Random random) {
		int px = (int) Math.round(x + random.nextGaussian() * 0.5) >> 1;
		int py = (int) Math.round(y + random.nextGaussian() * 0.5) >> 1;
		int pz = (int) Math.round(z + random.nextGaussian() * 0.5) >> 1;
		return BIOMES[clamp(px, 7)][clamp(py, 63)][clamp(pz, 7)];
	}

	private static int clamp(int x, int max) {
		return x < 0 ? 0 : x > max ? max : x;
	}

	public static void populate(WorldGenRegion world, int sx, int sz, Random random) {
		// Structure Generator
		if (random.nextFloat() < structureDensity) {
			popPos.setPos(sx + random.nextInt(16), MHelper.randRange(33, 100, random), sz + random.nextInt(16));
			StructureType type = StructureType.FLOOR;
			boolean isAir = world.getBlockState(popPos).getMaterial().isReplaceable();
			boolean airUp = world.getBlockState(popPos.up()).getMaterial().isReplaceable() && world.getBlockState(popPos.up(3)).getMaterial().isReplaceable();
			boolean airDown = world.getBlockState(popPos.down()).getMaterial().isReplaceable() && world.getBlockState(popPos.down(3)).getMaterial().isReplaceable();
			NetherBiome biome = getBiomeLocal(popPos.getX() - sx, popPos.getY(), popPos.getZ() - sz, random);
			if (!isAir && !airUp && !airDown && random.nextInt(8) == 0)
				type = StructureType.UNDER;
			else {
				if (popPos.getY() < 45 || !biome.hasCeilStructures() || random.nextBoolean()) // Floor
				{
					if (!isAir) {
						while (!world.getBlockState(popPos).getMaterial().isReplaceable() && popPos.getY() > 1) {
							popPos.setY(popPos.getY() - 1);
						}
					}
					while (world.getBlockState(popPos.down()).getMaterial().isReplaceable() && popPos.getY() > 1) {
						popPos.setY(popPos.getY() - 1);
					}
				}
				else // Ceil
				{
					if (!isAir) {
						while (!world.getBlockState(popPos).getMaterial().isReplaceable() && popPos.getY() > 1) {
							popPos.setY(popPos.getY() + 1);
						}
					}
					while (!BlocksHelper.isNetherGroundMagma(world.getBlockState(popPos.up())) && popPos.getY() < 127) {
						popPos.setY(popPos.getY() + 1);
					}
					type = StructureType.CEIL;
				}
			}
			biome = getBiomeLocal(popPos.getX() - sx, popPos.getY(), popPos.getZ() - sz, random);
			if (world.getBlockState(popPos).getMaterial().isReplaceable()) {
				if (type == StructureType.FLOOR) {
					BlockState down = world.getBlockState(popPos.down());
					if (BlocksHelper.isNetherGroundMagma(down))
						biome.genFloorBuildings(world, popPos, random);
				}
				else if (type == StructureType.CEIL) {
					BlockState up = world.getBlockState(popPos.up());
					if (BlocksHelper.isNetherGroundMagma(up)) {
						biome.genCeilBuildings(world, popPos, random);
					}
				}
			}
			else
				biome.genUnderBuildings(world, popPos, random);
		}

		if (random.nextFloat() < lavaStructureDensity) {
			popPos.setPos(sx + random.nextInt(16), 32, sz + random.nextInt(16));
			if (world.isAirBlock(popPos) && BlocksHelper.isLava(world.getBlockState(popPos.down()))) {
				biome = getBiomeLocal(popPos.getX() - sx, popPos.getY(), popPos.getZ() - sz, random);
				biome.genLavaBuildings(world, popPos, random);
			}
		}

		LIST_LAVA.clear();
		LIST_FLOOR.clear();
		LIST_WALL.clear();
		LIST_CEIL.clear();

		int ex = sx + 16;
		int ez = sz + 16;

		for (int x = 0; x < 16; x++) {
			int wx = sx + x;
			for (int z = 0; z < 16; z++) {
				int wz = sz + z;
				for (int y = 1; y < 126; y++) {
					if (caves.isInCave(x, y, z))
						continue;

					biome = getBiomeLocal(x, y, z, random);

					popPos.setPos(wx, y, wz);
					BlockState state = world.getBlockState(popPos);
					boolean lava = BlocksHelper.isLava(state);
					if (lava || BlocksHelper.isNetherGroundMagma(state) || state.getBlock() == Blocks.GRAVEL) {
						if (!lava && ((state = world.getBlockState(popPos.up())).isAir() || !state.getMaterial().isOpaque() || !state.getMaterial().blocksMovement()) && state.getFluidState().isEmpty())// world.isAir(popPos.up()))
							biome.genSurfColumn(world, popPos, random);

						if (((x + y + z) & 1) == 0 && random.nextFloat() < globalDensity && random.nextFloat() < biome.getPlantDensity()) {
							// Ground Generation
							if (world.isAirBlock(popPos.up())) {
								if (lava)
									LIST_LAVA.add(popPos.up());
								else
									LIST_FLOOR.add(new BlockPos(popPos.up()));
							}

							// Ceiling Generation
							else if (world.isAirBlock(popPos.down())) {
								LIST_CEIL.add(new BlockPos(popPos.down()));
							}

							// Wall Generation
							else {
								boolean bNorth = world.isAirBlock(popPos.north());
								boolean bSouth = world.isAirBlock(popPos.south());
								boolean bEast = world.isAirBlock(popPos.east());
								boolean bWest = world.isAirBlock(popPos.west());
								if (bNorth || bSouth || bEast || bWest) {
									BlockPos objPos = null;
									if (bNorth)
										objPos = popPos.north();
									else if (bSouth)
										objPos = popPos.south();
									else if (bEast)
										objPos = popPos.east();
									else
										objPos = popPos.west();

									if ((popPos.getX() >= sx) && (popPos.getX() < ex) && (popPos.getZ() >= sz) && (popPos.getZ() < ez)) {
										boolean bDown = world.isAirBlock(objPos.down());
										boolean bUp = world.isAirBlock(objPos.up());

										if (bDown && bUp) {
											LIST_WALL.add(new BlockPos(objPos));
										}
									}
								}
							}
						}
						if (random.nextFloat() < cincinnasiteDensity)
							spawnOre(BlocksRegistry.CINCINNASITE_ORE.getDefaultState(), world, popPos, random, 6, 14);
						if (random.nextFloat() < rubyDensity)
							spawnOre(BlocksRegistry.NETHER_RUBY_ORE.getDefaultState(), world, popPos, random, 1, 5);
						if (random.nextFloat() < lapisDensity)
							spawnOre(BlocksRegistry.NETHER_LAPIS_ORE.getDefaultState(), world, popPos, random, 1, 6);
					}
				}
			}
		}

		for (BlockPos pos : LIST_LAVA) {
			if (world.isAirBlock(pos)) {
				biome = getBiomeLocal(pos.getX() - sx, pos.getY(), pos.getZ() - sz, random);
				if (biome != null)
					biome.genLavaObjects(world, pos, random);
			}
		}

		for (BlockPos pos : LIST_FLOOR)
			if (world.isAirBlock(pos)) {
				biome = getBiomeLocal(pos.getX() - sx, pos.getY(), pos.getZ() - sz, random);
				if (biome != null)
					biome.genFloorObjects(world, pos, random);
			}

		for (BlockPos pos : LIST_WALL)
			if (world.isAirBlock(pos)) {
				biome = getBiomeLocal(pos.getX() - sx, pos.getY(), pos.getZ() - sz, random);
				if (biome != null)
					biome.genWallObjects(world, pos, random);
			}

		for (BlockPos pos : LIST_CEIL)
			if (world.isAirBlock(pos)) {
				biome = getBiomeLocal(pos.getX() - sx, pos.getY(), pos.getZ() - sz, random);
				if (biome != null)
					biome.genCeilObjects(world, pos, random);
			}
	}

	private static void makeLocalBiomes(WorldGenRegion world, int sx, int sz) {
		MC_BIOMES.clear();
		for (int x = 0; x < 8; x++) {
			popPos.setX(sx + (x << 1) + 2);
			for (int y = 0; y < 64; y++) {
				popPos.setY((y << 1) + 2);
				for (int z = 0; z < 8; z++) {
					popPos.setZ(sz + (z << 1) + 2);
					Biome b = world.getBiome(popPos);
					BIOMES[x][y][z] = NetherBiomesRegistry.getFromBiome(b);
					MC_BIOMES.add(b);
				}
			}
		}
	}

	public static void prePopulate(WorldGenRegion world, int sx, int sz, Random random) {
		makeLocalBiomes(world, sx, sz);

		if (hasCaves) {
			popPos.setPos(sx, 0, sz);
			caves.generate(world, popPos, random);
		}

		if (hasCleaningPass) {
			List<BlockPos> pos = new ArrayList<BlockPos>();
			BlockPos up;
			BlockPos down;
			BlockPos north;
			BlockPos south;
			BlockPos east;
			BlockPos west;
			for (int y = 32; y < 110; y++) {
				popPos.setY(y);
				for (int x = 0; x < 16; x++) {
					popPos.setX(x | sx);
					for (int z = 0; z < 16; z++) {
						popPos.setZ(z | sz);
						if (canReplace(world, popPos)) {
							up = popPos.up();
							down = popPos.down();
							north = popPos.north();
							south = popPos.south();
							east = popPos.east();
							west = popPos.west();
							if (world.isAirBlock(north) && world.isAirBlock(south))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(east) && world.isAirBlock(west))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(up) && world.isAirBlock(down))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(popPos.north().east().down()) && world.isAirBlock(popPos.south().west().up()))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(popPos.south().east().down()) && world.isAirBlock(popPos.north().west().up()))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(popPos.north().west().down()) && world.isAirBlock(popPos.south().east().up()))
								pos.add(new BlockPos(popPos));
							else if (world.isAirBlock(popPos.south().west().down()) && world.isAirBlock(popPos.north().east().up()))
								pos.add(new BlockPos(popPos));
						}
					}
				}
			}
			for (BlockPos p : pos) {
				BlocksHelper.setWithoutUpdate(world, p, AIR);
				up = p.up();
				BlockState state = world.getBlockState(up);
				if (!state.getBlock().isValidPosition(state, world, up))
					BlocksHelper.setWithoutUpdate(world, up, AIR);
			}
		}

		if (hasPaths) {
			popPos.setPos(sx, 0, sz);
			paths.generate(world, popPos, random);
		}
	}

	private static boolean canReplace(IWorld world, BlockPos pos) {
		BlockState state = world.getBlockState(pos);
		return BlocksHelper.isNetherGround(state) || state.getBlock() == Blocks.GRAVEL;
	}

	private static void spawnOre(BlockState state, IWorld world, BlockPos pos, Random random, int minSize, int maxSize) {
		int size = MHelper.randRange(minSize, maxSize, random);
		for (int i = 0; i < size; i++) {
			BlockPos local = pos.add(random.nextInt(3), random.nextInt(3), random.nextInt(3));
			if (BlocksHelper.isNetherrack(world.getBlockState(local))) {
				BlocksHelper.setWithoutUpdate(world, local, state);
			}
		}
	}

	public static void cleaningPass(IWorld world, int sx, int sz) {
		if (hasFixPass) {
			fixBlocks(world, sx, 30, sz, sx + 15, 110, sz + 15);
		}
	}

	private static void fixBlocks(IWorld world, int x1, int y1, int z1, int x2, int y2, int z2) {
		// List<BlockPos> lavafalls = Lists.newArrayList();
		// List<BlockPos> update = Lists.newArrayList();

		for (int y = y1; y <= y2; y++) {
			popPos.setY(y);
			for (int x = x1; x <= x2; x++) {
				popPos.setX(x);
				for (int z = z1; z <= z2; z++) {
					popPos.setZ(z);

					BlockState state = world.getBlockState(popPos);

					/*
					 * if (y > 32 && BlocksHelper.isLava(state) &&
					 * !BlocksHelper.isLava(world.getBlockState(popPos.down())))
					 * {
					 * 
					 * if (world.isAir(popPos.down())) { Mutable p = new
					 * Mutable().set(popPos.down()); while(likeAir(world, p)) {
					 * lavafalls.add(p.toImmutable()); p.move(Direction.DOWN); }
					 * update.add(p.up()); } else { for(Direction dir:
					 * BlocksHelper.HORIZONTAL) { BlockPos start =
					 * popPos.offset(dir); if (likeAir(world, start)) { Mutable
					 * p = new Mutable().set(start); while(likeAir(world, p)) {
					 * lavafalls.add(p.toImmutable()); p.move(Direction.DOWN); }
					 * update.add(p.up()); } } }
					 * 
					 * continue; }
					 */

					if (!state.isValidPosition(world, popPos)) {
						BlocksHelper.setWithoutUpdate(world, popPos, AIR);
						continue;
					}

					if (!state.isSolid() && world.getBlockState(popPos.up()).getBlock() == Blocks.NETHER_BRICKS) {
						BlocksHelper.setWithoutUpdate(world, popPos, Blocks.NETHER_BRICKS.getDefaultState());
						continue;
					}

					if (BlocksHelper.isLava(state) && world.isAirBlock(popPos.up()) && world.isAirBlock(popPos.down())) {
						BlocksHelper.setWithoutUpdate(world, popPos, AIR);
						continue;
					}

					if (state.getBlock() == Blocks.NETHER_WART_BLOCK || state.getBlock() == Blocks.WARPED_WART_BLOCK) {
						if (world.isAirBlock(popPos.down()) && world.isAirBlock(popPos.up()) && world.isAirBlock(popPos.north()) && world.isAirBlock(popPos.south()) && world.isAirBlock(popPos.east()) && world.isAirBlock(popPos.west()))
							BlocksHelper.setWithoutUpdate(world, popPos, AIR);
						continue;
					}

					if (state.getBlock() instanceof BlockStalactite && !(state = world.getBlockState(popPos.down())).hasOpaqueCollisionShape(world, popPos.down()) && !(state.getBlock() instanceof BlockStalactite)) {
						Mutable sp = new Mutable().setPos(popPos);
						while (world.getBlockState(sp).getBlock() instanceof BlockStalactite) {
							BlocksHelper.setWithoutUpdate(world, sp, AIR);
							sp.offset(Direction.UP);
						}
						continue;
					}
				}
			}
		}
	}

	public static HashSet<Biome> getPopulateBiomes() {
		return MC_BIOMES;
	}
}
