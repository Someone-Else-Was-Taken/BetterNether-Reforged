package itsremurin.betternetherreforged.blocks;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockGiantMold extends BlockBaseNotFull {
	private static final VoxelShape TOP_SHAPE = makeCuboidShape(2, 2, 2, 14, 14, 14);
	private static final VoxelShape MIDDLE_SHAPE = makeCuboidShape(5, 0, 5, 11, 16, 11);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public BlockGiantMold() {
		super(MaterialBuilder.makeWood(MaterialColor.GRAY).sound(SoundType.FUNGUS).notSolid());
		this.setDropItself(false);
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(SHAPE) == TripleShape.TOP ? TOP_SHAPE : MIDDLE_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		TripleShape shape = state.get(SHAPE);
		return shape == TripleShape.TOP ? new ItemStack(BlocksRegistry.GIANT_MOLD_SAPLING) : new ItemStack(BlocksRegistry.MUSHROOM_STEM);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		switch (state.get(SHAPE)) {
			case BOTTOM:
				return state;
			case MIDDLE:
			case TOP:
			default:
				return world.getBlockState(pos.down()).getBlock() == this ? state : Blocks.AIR.getDefaultState();
		}
	}
}
