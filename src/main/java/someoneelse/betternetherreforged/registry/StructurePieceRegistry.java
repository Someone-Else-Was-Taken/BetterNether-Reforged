package someoneelse.betternetherreforged.registry;

import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraftforge.fml.common.Mod;
import someoneelse.betternetherreforged.BetterNether;
import someoneelse.betternetherreforged.world.structures.piece.CityPiece;

@Mod.EventBusSubscriber(modid = BetterNether.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class StructurePieceRegistry {

    //public static final IStructurePieceType CAVE = IStructurePieceType.register(CavePiece::new, "bncave");

    public static void init() {
        //IStructurePieceType NETHER_CITY = IStructurePieceType.register(CityPiece::new, "bncity");
    }
}
