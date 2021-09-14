package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;

public class BlockWillowTrunk extends BlockBaseNotFull {
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);
	private static final VoxelShape SHAPE_BOTTOM = makeCuboidShape(4, 0, 4, 12, 16, 12);
	private static final VoxelShape SHAPE_TOP = makeCuboidShape(4, 0, 4, 12, 12, 12);

	public BlockWillowTrunk() {
		super(MaterialBuilder.makeWood(MaterialColor.RED_TERRACOTTA).notSolid());
		this.setDropItself(false);
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, TripleShape.TOP));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(SHAPE) == TripleShape.TOP ? SHAPE_TOP : SHAPE_BOTTOM;
	}
}
