package itsremurin.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import itsremurin.betternetherreforged.BlocksHelper;

public class BlockCommonPlant extends BlockBaseNotFull implements IGrowable {
	public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 3);

	public BlockCommonPlant(MaterialColor color) {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.BLACK)
				.sound(SoundType.CROP)
				.notSolid()
				.doesNotBlockMovement()
				.zeroHardnessAndResistance()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
	}

	public BlockCommonPlant(AbstractBlock.Properties settings) {
		super(settings);
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(AGE);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isNetherGround(world.getBlockState(pos.down()));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return state.get(AGE) < 3;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		if (age < 3)
			return BlocksHelper.isFertile(world.getBlockState(pos.down())) ? (random.nextBoolean()) : (random.nextInt(4) == 0);
		else
			return false;
	}

	protected boolean canGrowTerrain(World world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		if (age < 3)
			return BlocksHelper.isFertile(world.getBlockState(pos.down())) ? (random.nextInt(8) == 0) : (random.nextInt(16) == 0);
		else
			return false;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		int age = state.get(AGE);
		world.setBlockState(pos, state.with(AGE, age + 1));
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (canGrowTerrain(world, random, pos, state))
			grow(world, random, pos, state);
	}
}