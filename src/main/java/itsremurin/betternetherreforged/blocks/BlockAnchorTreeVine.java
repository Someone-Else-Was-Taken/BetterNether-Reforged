package itsremurin.betternetherreforged.blocks;

import java.util.function.ToIntFunction;

import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.registry.BlocksRegistry;


public class BlockAnchorTreeVine extends BlockBaseNotFull {
	protected static final VoxelShape SHAPE_SELECTION = Block.makeCuboidShape(4, 0, 4, 12, 16, 12);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public BlockAnchorTreeVine() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.GREEN)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.noDrops()
				.zeroHardnessAndResistance()
				.notSolid()
				.setLightLevel(getLuminance()));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		setDropItself(false);
	}

	protected static ToIntFunction<BlockState> getLuminance() {
		return (state) -> {
			return state.get(SHAPE) == TripleShape.BOTTOM ? 15 : 0;
		};
	}

	public AbstractBlock.OffsetType getOffsetType() {
		return AbstractBlock.OffsetType.XZ;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPE_SELECTION.withOffset(vec3d.x, vec3d.y, vec3d.z);
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
		if (up != this && up != BlocksRegistry.ANCHOR_TREE_LEAVES && up != Blocks.NETHERRACK)
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.ANCHOR_TREE_LEAVES);
	}
}