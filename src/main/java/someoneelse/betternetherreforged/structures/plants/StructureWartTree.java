package someoneelse.betternetherreforged.structures.plants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.blocks.BlockWartSeed;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.IWorld;
import someoneelse.betternetherreforged.registry.BlocksRegistry;
import someoneelse.betternetherreforged.structures.StructureFuncScatter;

public class StructureWartTree extends StructureFuncScatter {
	private static final BlockState WART_BLOCK = Blocks.NETHER_WART_BLOCK.getDefaultState();
	private static final Direction[] HORIZONTAL = HorizontalBlock.HORIZONTAL_FACING.getAllowedValues().toArray(new Direction[] {});

	public StructureWartTree() {
		super(7);
	}

	@Override
	public void grow(IServerWorld world, BlockPos pos, Random random) {
		if (world.isAirBlock(pos.up(1)) && world.isAirBlock(pos.up(2))) {
			if (world.isAirBlock(pos.up(2).north()) && world.isAirBlock(pos.up(2).south()) && world.isAirBlock(pos.up(2).east()) && world.isAirBlock(pos.up(2).west()))
				if (world.isAirBlock(pos.up(3).north(2)) && world.isAirBlock(pos.up(3).south(2)) && world.isAirBlock(pos.up(3).east(2)) && world.isAirBlock(pos.up(3).west(2))) {
				int height = 5 + random.nextInt(5);
				int h2 = height - 1;
				int width = (height >>> 2) + 1;
				int offset = width >>> 1;
				List<BlockPos> seedBlocks = new ArrayList<BlockPos>();
				for (int x = 0; x < width; x++) {
				int px = x + pos.getX() - offset;
				for (int z = 0; z < width; z++) {
				int pz = z + pos.getZ() - offset;
				for (int y = 0; y < height; y++) {
				int py = y + pos.getY();
				POS.setPos(px, py, pz);
				if (isReplaceable(world.getBlockState(POS))) {
				if (y == 0 && !isReplaceable(world.getBlockState(POS.down())))
					BlocksHelper.setWithUpdate(world, POS, BlocksRegistry.WART_ROOTS.getDefaultState());
				else if (y < h2)
					BlocksHelper.setWithUpdate(world, POS, BlocksRegistry.WART_LOG.getDefaultState());
				else
				BlocksHelper.setWithUpdate(world, POS, WART_BLOCK);
				if (random.nextInt(8) == 0) {
				Direction dir = HORIZONTAL[random.nextInt(HORIZONTAL.length)];
				seedBlocks.add(new BlockPos(POS).offset(dir));
				}
				}
				}
				}
				}

				for (int x = 0; x < width; x++) {
				int px = x + pos.getX() - offset;
				for (int z = 0; z < width; z++) {
				int pz = z + pos.getZ() - offset;
				for (int y = 1; y < height >> 1; y++) {
				int py = pos.getY() - y;
				POS.setPos(px, py, pz);
				if (isReplaceable(world.getBlockState(POS))) {
				if (isReplaceable(world.getBlockState(POS.down())))
					BlocksHelper.setWithUpdate(world, POS, BlocksRegistry.WART_LOG.getDefaultState());
				else {
				BlocksHelper.setWithUpdate(world, POS, BlocksRegistry.WART_ROOTS.getDefaultState());
				break;
				}
				}
				}
				}
				}

				int headWidth = width + 2;
				offset++;
				height = height - width - 1 + pos.getY();
				for (int x = 0; x < headWidth; x++) {
				int px = x + pos.getX() - offset;
				for (int z = 0; z < headWidth; z++) {
				if (x != z && x != (headWidth - z - 1)) {
				int pz = z + pos.getZ() - offset;
				for (int y = 0; y < width; y++) {
				int py = y + height;
				POS.setPos(px, py, pz);
				if (world.isAirBlock(POS)) {
				BlocksHelper.setWithUpdate(world, POS, WART_BLOCK);
				for (int i = 0; i < 4; i++)
				seedBlocks.add(new BlockPos(POS).offset(Direction.values()[random.nextInt(6)]));
				}
				}
				}
				}
				}
				for (BlockPos pos2 : seedBlocks)
				PlaceRandomSeed(world, pos2);
				}
		}
	}

	private void PlaceRandomSeed(IWorld world, BlockPos pos) {
		BlockState seed = BlocksRegistry.WART_SEED.getDefaultState();
		if (isReplaceable(world.getBlockState(pos))) {
			if (isWart(world.getBlockState(pos.up())))
				seed = seed.with(BlockWartSeed.FACING, Direction.DOWN);
			else if (isWart(world.getBlockState(pos.down())))
				seed = seed.with(BlockWartSeed.FACING, Direction.UP);
			else if (isWart(world.getBlockState(pos.north())))
				seed = seed.with(BlockWartSeed.FACING, Direction.SOUTH);
			else if (isWart(world.getBlockState(pos.south())))
				seed = seed.with(BlockWartSeed.FACING, Direction.NORTH);
			else if (isWart(world.getBlockState(pos.east())))
				seed = seed.with(BlockWartSeed.FACING, Direction.WEST);
			else if (isWart(world.getBlockState(pos.west())))
				seed = seed.with(BlockWartSeed.FACING, Direction.EAST);
			BlocksHelper.setWithUpdate(world, pos, seed);
		}
	}

	private boolean isReplaceable(BlockState state) {
		Block block = state.getBlock();
		return state.getMaterial().isReplaceable() ||
				block == Blocks.AIR ||
				block == BlocksRegistry.WART_SEED ||
				block == BlocksRegistry.BLACK_BUSH ||
				block == BlocksRegistry.SOUL_VEIN ||
				block == BlocksRegistry.SOUL_LILY ||
				block == BlocksRegistry.SOUL_LILY_SAPLING ||
				block == Blocks.NETHER_WART;
	}

	private boolean isWart(BlockState state) {
		return state == WART_BLOCK || state.getBlock() == BlocksRegistry.WART_LOG;
	}

	@Override
	protected boolean isStructure(BlockState state) {
		return isWart(state);
	}

	@Override
	protected boolean isGround(BlockState state) {
		return BlocksHelper.isSoulSand(state);
	}
}
