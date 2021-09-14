package itsremurin.betternetherreforged.registry;

import java.util.ArrayList;
import java.util.List;

import itsremurin.betternetherreforged.BetterNether;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.registries.IForgeRegistry;

public class SoundsRegistry {
	private static final List<SoundEvent> SOUNDS = new ArrayList<>();
	
	public static final SoundEvent AMBIENT_MUSHROOM_FOREST = register("betternether.ambient.mushroom_forest");
	public static final SoundEvent AMBIENT_GRAVEL_DESERT = register("betternether.ambient.gravel_desert");
	public static final SoundEvent AMBIENT_NETHER_JUNGLE = register("betternether.ambient.nether_jungle");
	public static final SoundEvent AMBIENT_SWAMPLAND = register("betternether.ambient.swampland");

	public static final SoundEvent MOB_FIREFLY_FLY = register("betternether.mob.firefly.fly");
	public static final SoundEvent MOB_JELLYFISH = register("betternether.mob.jellyfish");
	public static final SoundEvent MOB_NAGA_IDLE = register("betternether.mob.naga_idle");
	public static final SoundEvent MOB_NAGA_ATTACK = register("betternether.mob.naga_attack");
	public static final SoundEvent MOB_SKULL_FLIGHT = register("betternether.mob.skull_flight");

	private static SoundEvent register(String id) {
		ResourceLocation loc = new ResourceLocation(BetterNether.MOD_ID, id);
		SoundEvent sound = new SoundEvent(loc);
		sound.setRegistryName(loc);
		SOUNDS.add(sound);
		return sound;
	}

	public static void registerAll(RegistryEvent.Register<SoundEvent> e) {
		IForgeRegistry<SoundEvent> r = e.getRegistry();
		for (SoundEvent s : SOUNDS) {
			r.register(s);
		}
	}
}
