package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.config.Configs;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.registries.ForgeRegistries;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.registry.BlocksRegistry;

public class BlockStatueRespawner extends BlockBaseNotFull {
	private static final VoxelShape SHAPE = makeCuboidShape(1, 0, 1, 15, 16, 15);
	private static final RedstoneParticleData EFFECT = new RedstoneParticleData(1, 0, 0, 1.0F);
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	public static final BooleanProperty TOP = BooleanProperty.create("top");
	private final ItemStack requiredItem;

	public BlockStatueRespawner() {
		super(AbstractBlock.Properties.from(BlocksRegistry.CINCINNASITE_BLOCK).notSolid().setLightLevel((state) -> {return 15;}));
		this.setRenderLayer(BNRenderLayer.CUTOUT);
		this.setDefaultState(getStateContainer().getBaseState().with(FACING, Direction.NORTH).with(TOP, false));
		this.setDropItself(false);

		String itemName = Configs.MAIN.getString("respawn_statue", "respawn_item", ForgeRegistries.ITEMS.getKey(Items.GLOWSTONE).toString());
		Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));
		if (item == Items.AIR)
			item = Items.GLOWSTONE;
		int count = Configs.MAIN.getInt("respawn_statue", "item_count", 4);
		requiredItem = new ItemStack(item, count);
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> stateManager) {
		stateManager.add(FACING, TOP);
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext ctx) {
		return this.getDefaultState().with(FACING, ctx.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	public ActionResultType onBlockActivated(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockRayTraceResult hit) {
		ItemStack stack = player.getHeldItemMainhand();
		if (stack.getItem() == requiredItem.getItem() && stack.getCount() >= requiredItem.getCount()) {
			float y = state.get(TOP) ? 0.4F : 1.4F;
			if (!player.isCreative()) {
				player.getHeldItemMainhand().shrink(requiredItem.getCount());
			}
			for (int i = 0; i < 50; i++)
				world.addParticle(EFFECT,
						pos.getX() + world.rand.nextFloat(),
						pos.getY() + y + world.rand.nextFloat() * 0.2,
						pos.getZ() + world.rand.nextFloat(), 0, 0, 0);
			player.sendStatusMessage(new TranslationTextComponent("message.spawn_set", new Object[0]), true);
			if (!world.isRemote) {
				((ServerPlayerEntity) player).func_242111_a(world.getDimensionKey(), pos, player.getRotationYawHead(), false, true);
			}
			player.playSound(SoundEvents.ITEM_TOTEM_USE, 0.7F, 1.0F);
			return ActionResultType.SUCCESS;
		}
		else {
			player.sendStatusMessage(new TranslationTextComponent("message.spawn_help", requiredItem), true);
		}
		return ActionResultType.SUCCESS;
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		if (state.get(TOP))
			return true;
		BlockState up = world.getBlockState(pos.up());
		return up.isAir() || (up.getBlock() == this && up.get(TOP));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		if (!world.isRemote())
			BlocksHelper.setWithUpdate((ServerWorld) world, pos.up(), state.with(TOP, true));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (state.get(TOP)) {
			return world.getBlockState(pos.down()).getBlock() == this ? state : Blocks.AIR.getDefaultState();
		}
		else {
			return world.getBlockState(pos.up()).getBlock() == this ? state : Blocks.AIR.getDefaultState();
		}
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return BlocksHelper.rotateHorizontal(state, rotation, FACING);
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirror) {
		return BlocksHelper.mirrorHorizontal(state, mirror, FACING);
	}

	@Override
	public void onBlockHarvested(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (player.isCreative() && state.get(TOP) && world.getBlockState(pos.down()).getBlock() == this) {
			world.setBlockState(pos.down(), Blocks.AIR.getDefaultState());
		}
		super.onBlockHarvested(world, pos, state, player);
	}
}

