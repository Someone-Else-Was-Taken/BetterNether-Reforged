package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockBlackApple extends BlockCommonPlant {
	private static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);

	public BlockBlackApple() {
		super(MaterialColor.ORANGE_TERRACOTTA);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.BLACK_APPLE_SEED);
	}
}
