package com.masterquentus.projectlilith.block;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.custom.HellfireBlock;
import com.masterquentus.projectlilith.block.custom.LilithTrophyBlock;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ProjectLilith.MOD_ID);

    private static final float RESISTANCE = 6.0F;

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    private static BlockBehaviour.Properties stoneProps(float hardness) {
        return BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(hardness, RESISTANCE);
    }

    private static DeferredBlock<Block> registerStone(String name, float hardness) {
        return registerBlock(name, props -> new Block(stoneProps(hardness)));
    }

    private static DeferredBlock<Block> registerStairs(String name, Supplier<Block> base, float hardness) {
        return registerBlock(name, props -> new StairBlock(base.get().defaultBlockState(), stoneProps(hardness)));
    }

    private static DeferredBlock<Block> registerSlab(String name, float hardness) {
        return registerBlock(name, props -> new SlabBlock(stoneProps(hardness)));
    }

    // ---------------------------------------------------------------------
    // Non-building blocks
    // ---------------------------------------------------------------------

    public static final DeferredBlock<Block> HELLFIRE = registerBlock("hell_fire",
            properties -> new HellfireBlock(properties.strength(4f)
                    .instabreak().noOcclusion().randomTicks().lightLevel((state) -> 10).noLootTable().noCollission()));

    public static final DeferredBlock<Block> LILITH_TROPHY = registerBlock("lilith_trophy",
            properties -> new LilithTrophyBlock(properties.strength(2.0f).noOcclusion()));

    // ---------------------------------------------------------------------
    // Crimson Stone set (stone-like = 1.5, cobblestone-like = 2.0, same as vanilla)
    // ---------------------------------------------------------------------

    public static final DeferredBlock<Block> CRIMSON_STONE = registerStone("crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE = registerStone("crimson_cobblestone", 2.0F);
    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS = registerStone("crimson_stone_bricks", 1.5F);
    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE = registerStone("polished_crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CHISELED_CRIMSON_STONE = registerStone("chiseled_crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CRACKED_CRIMSON_STONE = registerStone("cracked_crimson_stone", 1.5F);

    public static final DeferredBlock<Block> CRIMSON_STONE_STAIRS =
            registerStairs("crimson_stone_stairs", () -> CRIMSON_STONE.get(), 1.5F);
    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE_STAIRS =
            registerStairs("crimson_cobblestone_stairs", () -> CRIMSON_COBBLESTONE.get(), 2.0F);
    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS_STAIRS =
            registerStairs("crimson_stone_bricks_stairs", () -> CRIMSON_STONE_BRICKS.get(), 1.5F);
    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE_STAIRS =
            registerStairs("polished_crimson_stone_stairs", () -> POLISHED_CRIMSON_STONE.get(), 1.5F);

    public static final DeferredBlock<Block> CRIMSON_STONE_SLAB = registerSlab("crimson_stone_slab", 1.5F);
    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE_SLAB = registerSlab("crimson_cobblestone_slab", 2.0F);
    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS_SLAB = registerSlab("crimson_stone_bricks_slab", 1.5F);
    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE_SLAB = registerSlab("polished_crimson_stone_slab", 1.5F);

    // Crafted from Crimson Cobblestone, so it uses cobblestone hardness (2.0)
    public static final DeferredBlock<Block> CRIMSON_STONE_WALL = registerBlock("crimson_stone_wall",
            props -> new WallBlock(stoneProps(2.0F).forceSolidOn()));

    public static final DeferredBlock<Block> CRIMSON_STONE_BUTTON = registerBlock("crimson_stone_button",
            props -> new ButtonBlock(BlockSetType.STONE, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)));

    public static final DeferredBlock<Block> CRIMSON_STONE_PRESSURE_PLATE = registerBlock("crimson_stone_pressure_plate",
            props -> new PressurePlateBlock(BlockSetType.STONE,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)));

    // ---------------------------------------------------------------------
    // Registration
    // ---------------------------------------------------------------------

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Function<BlockBehaviour.Properties, T> function) {
        DeferredBlock<T> toReturn = BLOCKS.registerBlock(name, function);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.registerSimpleBlockItem(name, block);
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}