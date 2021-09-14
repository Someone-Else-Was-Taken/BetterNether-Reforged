package someoneelse.betternetherreforged.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.google.common.collect.Lists;

import someoneelse.betternetherreforged.blocks.shapes.TripleShape;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockNeonEquisetum extends BlockBaseNotFull implements IGrowable {
	protected static final VoxelShape SHAPE_SELECTION = makeCuboidShape(2, 0, 2, 14, 16, 14);
	public static final EnumProperty<TripleShape> SHAPE = EnumProperty.create("shape", TripleShape.class);

	public BlockNeonEquisetum() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.GREEN)
				.sound(SoundType.CROP)
				.doesNotBlockMovement()
				.noDrops()
				.zeroHardnessAndResistance()
				.notSolid()
				.setLightLevel((state) -> {return 15;}));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDefaultState(getStateContainer().getBaseState().with(SHAPE, TripleShape.BOTTOM));
		setDropItself(false);
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
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		BlockState up = world.getBlockState(pos.up());
		return up.getBlock() == this || BlocksHelper.isNetherrack(up);
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack itemStack) {
		BlockPos upPos = pos.up();
		if (world.getBlockState(upPos).getBlock() == this) {
			world.setBlockState(upPos, getDefaultState().with(SHAPE, TripleShape.MIDDLE));
			upPos = upPos.up();
			if (world.getBlockState(upPos).getBlock() == this) {
				world.setBlockState(upPos, getDefaultState().with(SHAPE, TripleShape.TOP));
			}
		}
	}

	@Override
	public List<ItemStack> getDrops(BlockState state, LootContext.Builder builder) {
		ItemStack tool = builder.get(LootParameters.TOOL);
		if (tool != null && tool.getItem().isIn(Tags.Items.SHEARS) || EnchantmentHelper.getEnchantmentLevel(Enchantments.SILK_TOUCH, tool) > 0) {
			return Lists.newArrayList(new ItemStack(this.asItem()));
		}
		else {
			return Lists.newArrayList();
		}
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		Mutable blockPos = new Mutable().setPos(pos);
		for (int y = pos.getY() - 1; y > 1; y--) {
			blockPos.setY(y);
			if (world.getBlockState(blockPos).getBlock() != this)
				return world.getBlockState(blockPos).getBlock() == Blocks.AIR;
		}
		return false;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		Mutable blockPos = new Mutable().setPos(pos);
		for (int y = pos.getY(); y > 1; y--) {
			blockPos.setY(y);
			if (world.getBlockState(blockPos).getBlock() != this)
				break;
		}
		BlocksHelper.setWithoutUpdate(world, blockPos, this.getDefaultState());
		this.onBlockPlacedBy(world, blockPos, state, null, null);
	}
}
