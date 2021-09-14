package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockEyeVine extends BlockBaseNotFull {
	protected static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);

	public BlockEyeVine() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.RED)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.noDrops()
				.zeroHardnessAndResistance()
				.notSolid());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		setDropItself(false);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	public boolean propagatesSkylightDown(BlockState state, IBlockReader view, BlockPos pos) {
		return true;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		Block up = world.getBlockState(pos.up()).getBlock();
		Block down = world.getBlockState(pos.down()).getBlock();
		if (up != this && up != Blocks.NETHERRACK)
			return Blocks.AIR.getDefaultState();
		else if (down != this && !(down instanceof BlockEyeBase))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.EYE_SEED);
	}
}
