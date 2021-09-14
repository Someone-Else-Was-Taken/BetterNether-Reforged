package someoneelse.betternetherreforged.blocks;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;

public class BlockCincinnasite extends BlockBase {
	public BlockCincinnasite() {
		super(AbstractBlock.Properties.create(Material.IRON, MaterialColor.YELLOW)
				.hardnessAndResistance(3F, 10F)
				.setRequiresTool()
				.sound(SoundType.METAL));
	}
}
