package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ToolType;
import itsremurin.betternetherreforged.BlocksHelper;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BlockLucisMushroom extends BlockBaseNotFull {
	private static final VoxelShape V_SHAPE = makeCuboidShape(0, 0, 0, 16, 9, 16);
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final EnumProperty<EnumShape> SHAPE = EnumProperty.create("shape", EnumShape.class);

	public BlockLucisMushroom() {
		super(AbstractBlock.Properties.create(Material.ORGANIC, MaterialColor.YELLOW)
				.harvestTool(ToolType.AXE)
				.sound(SoundType.WOOD)
				.hardnessAndResistance(1F)
				.setLightLevel((state) -> {return 15;})
				.notSolid());
		this.setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(SHAPE, EnumShape.CORNER));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING, SHAPE);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return V_SHAPE;
	}

	public enum EnumShape implements IStringSerializable {
		CORNER("corner"), SIDE("side"), CENTER("center");

		final String name;

		EnumShape(String name) {
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
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		return Lists.newArrayList(new ItemStack(BlocksRegistry.LUCIS_SPORE), new ItemStack(ItemsRegistry.GLOWSTONE_PILE, MHelper.randRange(2, 4, MHelper.RANDOM)));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		if (mirror == Mirror.FRONT_BACK) {
			if (state.get(SHAPE) == EnumShape.SIDE) state = state.with(FACING, state.get(FACING).rotateYCCW());
			if (state.get(FACING) == Direction.NORTH) return state.with(FACING, Direction.WEST);
			if (state.get(FACING) == Direction.WEST) return state.with(FACING, Direction.NORTH);
			if (state.get(FACING) == Direction.SOUTH) return state.with(FACING, Direction.EAST);
			if (state.get(FACING) == Direction.EAST) return state.with(FACING, Direction.SOUTH);
		}
		else if (mirror == Mirror.LEFT_RIGHT) {
			if (state.get(SHAPE) == EnumShape.SIDE) state = state.with(FACING, state.get(FACING).rotateYCCW());
			if (state.get(FACING) == Direction.NORTH) return state.with(FACING, Direction.EAST);
			if (state.get(FACING) == Direction.EAST) return state.with(FACING, Direction.NORTH);
			if (state.get(FACING) == Direction.SOUTH) return state.with(FACING, Direction.WEST);
			if (state.get(FACING) == Direction.WEST) return state.with(FACING, Direction.SOUTH);
		}
		return state;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.LUCIS_SPORE);
	}
}
