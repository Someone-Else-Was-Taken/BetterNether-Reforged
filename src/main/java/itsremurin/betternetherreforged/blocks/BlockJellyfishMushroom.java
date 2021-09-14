package itsremurin.betternetherreforged.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import itsremurin.betternetherreforged.blocks.materials.MaterialBuilder;
import itsremurin.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootContext;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import itsremurin.betternetherreforged.MHelper;
import itsremurin.betternetherreforged.registry.BlocksRegistry;
import itsremurin.betternetherreforged.registry.ItemsRegistry;

public class BlockJellyfishMushroom extends BlockBaseNotFull {
	private static final VoxelShape TOP_SHAPE = makeCuboidShape(1, 0, 1, 15, 16, 15);
	private static final VoxelShape MIDDLE_SHAPE = makeCuboidShape(5, 0, 5, 11, 16, 11);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);
	public static final EnumProperty<JellyShape> VISUAL = EnumProperty.create("visual", JellyShape.class);

	public BlockJellyfishMushroom() {
		super(MaterialBuilder.makeWood(MaterialColor.CYAN).hardnessAndResistance(0.1F).sound(SoundType.FUNGUS).notSolid().setLightLevel((state) -> {return 13;}));
		this.setRenderLayer(BNRenderLayer.TRANSLUCENT);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(SHAPE, VISUAL);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return state.get(SHAPE) == TripleShape.TOP ? TOP_SHAPE : MIDDLE_SHAPE;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public ItemStack getItem(IBlockReader world, BlockPos pos, BlockState state) {
		return new ItemStack(BlocksRegistry.JELLYFISH_MUSHROOM_SAPLING);
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
		switch (state.get(SHAPE)) {
			case BOTTOM:
				return world.getBlockState(pos.down()).isSolidSide(world, pos.down(), Direction.UP) ? state : Blocks.AIR.getDefaultState();
			case MIDDLE:
				return world.getBlockState(pos.up()).getBlock() == this && world.getBlockState(pos.down()).isSolidSide(world, pos.down(), Direction.UP) ? state : Blocks.AIR.getDefaultState();
			case TOP:
			default:
				return world.getBlockState(pos.down()).getBlock() == this ? state : Blocks.AIR.getDefaultState();
		}
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float distance) {
		if (world.getBlockState(pos).get(SHAPE) != TripleShape.TOP)
			return;
		if (entity.isSuppressingBounce())
			super.onFallenUpon(world, pos, entity, distance);
		else
			entity.onLivingFall(distance, 0.0F);
	}

	@Override
	public void onLanded(IBlockReader world, Entity entity) {
		if (entity.isSuppressingBounce())
			super.onLanded(world, entity);
		else
			this.bounce(entity);
	}

	private void bounce(Entity entity) {
		Vector3d vec3d = entity.getMotion();
		if (vec3d.y < 0.0D) {
			double d = entity instanceof LivingEntity ? 1.0D : 0.8D;
			entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
		}
	}

	@Override
	public void onEntityWalk(World world, BlockPos pos, Entity entity) {
		if (world.getBlockState(pos).get(SHAPE) != TripleShape.TOP) {
			super.onEntityWalk(world, pos, entity);
			return;
		}

		double d = Math.abs(entity.getMotion().y);
		if (d < 0.1D && !entity.isSuppressingBounce()) {
			double e = 0.4D + d * 0.2D;
			entity.setMotion(entity.getMotion().mul(e, 1.0D, e));
		}
		super.onEntityWalk(world, pos, entity);
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		if (state.get(SHAPE) == TripleShape.TOP) {
			return Lists.newArrayList(new ItemStack(BlocksRegistry.JELLYFISH_MUSHROOM_SAPLING, MHelper.randRange(1, 2, MHelper.RANDOM)),
					new ItemStack(ItemsRegistry.GLOWSTONE_PILE, MHelper.randRange(0, 2, MHelper.RANDOM)),
					new ItemStack(Items.SLIME_BALL, MHelper.randRange(0, 1, MHelper.RANDOM)));
		}
		else if (state.get(SHAPE) == TripleShape.BOTTOM)
			return Lists.newArrayList(new ItemStack(BlocksRegistry.JELLYFISH_MUSHROOM_SAPLING, MHelper.randRange(1, 2, MHelper.RANDOM)));
		else
			return Lists.newArrayList(new ItemStack(BlocksRegistry.MUSHROOM_STEM));
	}

	public static enum JellyShape implements IStringSerializable {
		NORMAL("normal"), SEPIA("sepia"), POOR("poor");

		final String name;

		JellyShape(String name) {
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
}
