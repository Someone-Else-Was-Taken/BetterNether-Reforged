
package someoneelse.betternetherreforged.compat;

import net.minecraft.block.ComposterBlock;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.fml.ModList;

public class ModGet {

    public static boolean isLoaded(String modID) {
        return ModList.get().isLoaded(modID);
    }
}
