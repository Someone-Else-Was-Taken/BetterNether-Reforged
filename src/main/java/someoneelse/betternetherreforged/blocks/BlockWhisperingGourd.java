package someoneelse.betternetherreforged.blocks;

import someoneelse.betternetherreforged.blocks.materials.MaterialBuilder;
import net.minecraft.block.material.MaterialColor;

public class BlockWhisperingGourd extends BlockBase {
	public BlockWhisperingGourd() {
		super(MaterialBuilder.makeWood(MaterialColor.BLUE));
	}
}
