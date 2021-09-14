package someoneelse.betternetherreforged.entity;

import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.ImmutableSet;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.EntityType;

public class EntityTypeBuilder <T extends Entity> {

	private static final Logger LOGGER = LogManager.getLogger();
	private EntityClassification classification;
	private EntityType.IFactory<T> factory;
	private boolean saveable = true;
	private boolean summonable = true;
	private int trackRange = 5;
	private int trackedUpdateRate = 3;
	private Boolean forceTrackedVelocityUpdates;
	private boolean fireImmune = false;
	private boolean spawnableFarFromPlayer;
	private EntitySize size = EntitySize.flexible(-1.0f, -1.0f);
	private ImmutableSet<Block> specificSpawnBlocks = ImmutableSet.of();
	
	protected EntityTypeBuilder(EntityClassification classification, EntityType.IFactory<T> factory) {
		this.classification = classification;
		this.factory = factory;
		this.spawnableFarFromPlayer = classification == EntityClassification.CREATURE || classification == EntityClassification.MISC;
	}
	
	public static <T extends Entity> EntityTypeBuilder<T> create(EntityClassification classification, EntityType.IFactory<T> factory) {
		return new EntityTypeBuilder<>(classification, factory);
	}
	
	public EntityTypeBuilder<T> size(EntitySize size) {
		Objects.requireNonNull(size, "Cannot set null size");
		this.size = size;
		return this;
	}
	
	public EntityTypeBuilder<T> fireImmune() {
		this.fireImmune = true;
		return this;
	}
	
	public EntityTypeBuilder<T> disableSummon() {
		this.summonable = false;
		return this;
	}
	
	public EntityType<T> build() {

		EntityType<T> type = new EntityType<T>(this.factory, this.classification, this.saveable, this.summonable, this.fireImmune, this.spawnableFarFromPlayer, this.specificSpawnBlocks, size, trackRange, trackedUpdateRate);

		return type;
	}
}
