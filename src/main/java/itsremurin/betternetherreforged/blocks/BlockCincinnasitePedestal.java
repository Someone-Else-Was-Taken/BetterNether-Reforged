package itsremurin.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import itsremurin.betternetherreforged.registry.BlocksRegistry;

public class BlockCincinnasitePedestal extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = Block.makeCuboidShape(2, 0, 2, 14, 16, 14);

	public BlockCincinnasitePedestal() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).notSolid());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}
}
