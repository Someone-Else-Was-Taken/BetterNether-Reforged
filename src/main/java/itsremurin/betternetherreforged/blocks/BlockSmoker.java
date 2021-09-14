package itsremurin.betternetherreforged.blocks;

import java.util.Random;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.BlocksHelper;

public class BlockSmoker extends BlockBaseNotFull {
	private static final VoxelShape TOP_SHAPE = makeCuboidShape(4, 0, 4, 12, 8, 12);
	private static final VoxelShape MIDDLE_SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public BlockSmoker() {
		super(MaterialBuilder.makeWood(MaterialColor.BROWN));
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, TripleShape.TOP));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (world.isAirBlock(pos.up()))
			world.addParticle(ParticleTypes.LARGE_SMOKE, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0, 0, 0);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(SHAPE) == TripleShape.TOP ? TOP_SHAPE : MIDDLE_SHAPE;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos)) {
			return Blocks.AIR.getDefaultState();
		}
		Block side = world.getBlockState(pos.up()).getBlock();
		if (side != this)
			return state.with(SHAPE, TripleShape.TOP);
		side = world.getBlockState(pos.down()).getBlock();
		if (side == this)
			return state.with(SHAPE, TripleShape.MIDDLE);
		else
			return state.with(SHAPE, TripleShape.BOTTOM);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState down = world.getBlockState(pos.down());
		return down.getBlock() == this || BlocksHelper.isNetherGround(down);
	}
}

