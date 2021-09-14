package someoneelse.betternetherreforged.blocks;

import java.util.Random;

import net.minecraft.block.BlockState;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import someoneelse.betternetherreforged.BlocksHelper;

public class BlockSoulGrass extends BlockNetherGrass {
	@Override
	public boolean isValidPosition(BlockState state, IWorldReader world, BlockPos pos) {
		return BlocksHelper.isSoulSand(world.getBlockState(pos.down()));
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void animateTick(BlockState state, World world, BlockPos pos, Random random) {
		if (random.nextInt(4) == 0) {
			world.addParticle(
					ParticleTypes.PORTAL,
					pos.getX() + random.nextDouble(),
					pos.getY() + random.nextDouble() * 2,
					pos.getZ() + random.nextDouble(),
					random.nextDouble() * 0.05,
					-1,
					random.nextDouble() * 0.05);
		}
	}
}
