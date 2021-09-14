package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;

public class BlockRedMold extends BlockMold {
	private static final VoxelShape SHAPE = makeCuboidShape(2, 0, 2, 14, 12, 14);

	public BlockRedMold() {
		super(MaterialColor.RED_TERRACOTTA);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}
}
