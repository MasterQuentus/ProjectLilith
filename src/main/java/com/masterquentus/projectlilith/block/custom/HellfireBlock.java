package com.masterquentus.projectlilith.block.custom;

import com.masterquentus.projectlilith.Config;
import com.masterquentus.projectlilith.item.entity.LilithEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class HellfireBlock extends FireBlock {

    public HellfireBlock(Properties properties) {
        super(properties.replaceable());
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (level instanceof Level realLevel) {
            List<? extends String> allowed = Config.HELLFIRE_DIMENSIONS.get();
            return allowed.isEmpty() || allowed.contains(realLevel.dimension().location().toString());
        }
        return true;
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState,
                                     LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        return this.canSurvive(state, level, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    // Never extinguish naturally
    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        // Prevent natural extinguishing
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {}

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return false;
    }

    // Not flammable
    @Override
    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction face) {
        return false;
    }

    // Unique particles and sound
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (random.nextInt(4) == 0) {
            level.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                    SoundEvents.FIRE_AMBIENT, SoundSource.BLOCKS,
                    1.0F, random.nextFloat() * 0.4F + 0.8F, false);
        }

        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;

        level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, x, y, z,
                (random.nextDouble() - 0.5) * 0.1,
                (random.nextDouble() - 0.5) * 0.1,
                (random.nextDouble() - 0.5) * 0.1);
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        // Don't burn Lilith or her bats with her own fire
        if (entity instanceof LilithEntity || entity instanceof LilithEntity.LilithBat) return;

        super.entityInside(state, level, pos, entity);
        entity.hurt(level.damageSources().cactus(), 1.0F);
        entity.igniteForSeconds(1.0F);

    }

    @Override
    public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
        level.playSound(null, pos, SoundEvents.FIRE_EXTINGUISH, SoundSource.BLOCKS, 1.0F, 1.0F);
        return super.playerWillDestroy(level, pos, state, player);
    }
}