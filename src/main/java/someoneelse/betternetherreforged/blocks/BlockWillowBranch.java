package someoneelse.betternetherreforged.blocks;

import java.util.List;
import java.util.function.ToIntFunction;

import com.google.common.collect.Lists;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockWillowBranch extends BlockBaseNotFull {
	private static final VoxelShape V_SHAPE = makeCuboidShape(4, 0, 4, 12, 16, 12);
	public static final EnumProperty<WillowBranchShape> SHAPE = EnumProperty.create("shape", WillowBranchShape.class);

	public BlockWillowBranch() {
		super(MaterialBuilder.makeWood(MaterialColor.RED_TERRACOTTA).notSolid().doesNotBlockMovement().setLightLevel(getLuminance()));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDropItself(false);
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, WillowBranchShape.MIDDLE));
	}

	protected static ToIntFunction<BlockState> getLuminance() {
		return (state) -> {
			return state.get(SHAPE) == WillowBranchShape.END ? 15 : 0;
		};
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader view, BlockPos pos) {
		return 1.0F;
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return V_SHAPE;
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (world.isAirBlock(pos.up()))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	public enum WillowBranchShape implements IStringSerializable {
		END("end"), MIDDLE("middle");

		final String name;

		WillowBranchShape(String name) {
			this.name = name;
		}

		@Override
		public String getString() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(state.get(SHAPE) == WillowBranchShape.END ? BlocksRegistry.WILLOW_TORCH : BlocksRegistry.WILLOW_LEAVES);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == WillowBranchShape.END) {
			return Lists.newArrayList(new ItemStack(BlocksRegistry.WILLOW_TORCH));
		}
		else {
			return Lists.newArrayList();
		}
	}
}
