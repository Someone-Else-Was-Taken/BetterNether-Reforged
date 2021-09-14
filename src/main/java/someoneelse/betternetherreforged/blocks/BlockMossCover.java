package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;

public class BlockMossCover extends BlockMold {
	private static final VoxelShape SHAPE = makeCuboidShape(0, 0, 0, 16, 4, 16);

	public BlockMossCover() {
		super(MaterialColor.GREEN);
	}

	@Override
	public Block.OffsetType getOffsetType() {
		return Block.OffsetType.NONE;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return world.getBlockState(pos.down()).isSolidSide(world, pos, Direction.UP);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}
}