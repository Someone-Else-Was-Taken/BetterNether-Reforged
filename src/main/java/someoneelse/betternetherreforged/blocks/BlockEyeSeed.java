package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import someoneelse.betternetherreforged.BlocksHelper;
import someoneelse.betternetherreforged.structures.plants.StructureEye;

public class BlockEyeSeed extends BlockBaseNotFull implements IGrowable {
	private static final VoxelShape SHAPE = makeCuboidShape(4, 6, 4, 12, 16, 12);
	private static final StructureEye STRUCTURE = new StructureEye();

	public BlockEyeSeed() {
		super(AbstractBlock.Properties.create(Material.PLANTS, MaterialColor.RED)
				.sound(SoundType.CROP)
				.notSolid()
				.zeroHardnessAndResistance()
				.doesNotBlockMovement()
				.tickRandomly());
		this.setRenderLayer(BNRenderLayer.CUTOUT);
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		return SHAPE;
	}

	@Override
	public boolean canGrow(IBlockReader world, BlockPos pos, BlockState state, boolean isClient) {
		return true;
	}

	@Override
	public boolean canUseBonemeal(World world, Random random, BlockPos pos, BlockState state) {
		return random.nextInt(4) == 0 && world.getBlockState(pos.down()).getBlock() == Blocks.AIR;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		STRUCTURE.generate(world, pos, random);
	}

	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isNetherrack(world.getBlockState(pos.up()));
	}

	@Override
	public BlockState updatePostPlacement(BlockState state, Direction facing, BlockState neighborState, IWorld world, BlockPos pos, BlockPos neighborPos) {
		if (!isValidPosition(state, world, pos))
			return Blocks.AIR.getDefaultState();
		else
			return state;
	}

	@Override
	public void tick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.tick(state, world, pos, random);
		if (canUseBonemeal(world, random, pos, state)) {
			grow(world, random, pos, state);
		}
	}
}
