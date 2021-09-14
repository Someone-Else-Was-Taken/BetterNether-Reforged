package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public class BlockGrayMold extends BlockMold {
	private static final VoxelShape SHAPE = makeCuboidShape(4, 0, 4, 12, 8, 12);

	public BlockGrayMold() {
		super(MaterialColor.GRAY);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(3) == 0) {
			world.addParticle(ParticleTypes.MYCELIUM, pos.getX() + random.nextDouble(), pos.getY() + random.nextDouble() * 0.5, pos.getZ() + random.nextDouble(), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader view, BlockPos pos, ISelectionContext ePos) {
		Vector3d vec3d = state.getOffset(view, pos);
		return SHAPE.withOffset(vec3d.x, vec3d.y, vec3d.z);
	}
}