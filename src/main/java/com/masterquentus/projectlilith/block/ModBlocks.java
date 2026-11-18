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

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS =
            DeferredRegister.createBlocks(ProjectLilith.MOD_ID);

    private static DeferredBlock<Block> registerStone(String name, float hardness) {
        return registerBlock(name, props -> new Block(
                BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(hardness, 6.0F)));
    }

    public static final DeferredBlock<Block> HELLFIRE = registerBlock("hell_fire",
            properties -> new HellfireBlock(properties.strength(4f)
                    .instabreak().noOcclusion().randomTicks().lightLevel((state) -> 10).noLootTable().noCollission()));

    public static final DeferredBlock<Block> LILITH_TROPHY = registerBlock("lilith_trophy",
            properties -> new LilithTrophyBlock(properties.strength(2.0f).noOcclusion()));

    public static final DeferredBlock<Block> CRIMSON_STONE = registerStone("crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE = registerStone("crimson_cobblestone", 2.0F);
    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS = registerStone("crimson_stone_bricks", 1.5F);
    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE = registerStone("polished_crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CHISELED_CRIMSON_STONE = registerStone("chiseled_crimson_stone", 1.5F);
    public static final DeferredBlock<Block> CRACKED_CRIMSON_STONE = registerStone("cracked_crimson_stone", 1.5F);

    // Forge: new StairBlock(() -> CRIMSON_STONE.get().defaultBlockState(), props)
    public static final DeferredBlock<Block> CRIMSON_STONE_STAIRS = registerBlock("crimson_stone_stairs",
            props -> new StairBlock(CRIMSON_STONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE_STAIRS = registerBlock("crimson_cobblestone_stairs",
            props -> new StairBlock(CRIMSON_COBBLESTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS_STAIRS = registerBlock("crimson_stone_bricks_stairs",
            props -> new StairBlock(CRIMSON_STONE_BRICKS.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE_STAIRS = registerBlock("polished_crimson_stone_stairs",
            props -> new StairBlock(CRIMSON_COBBLESTONE.get().defaultBlockState(),
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_STONE_SLAB = registerBlock("crimson_stone_slab",
            props -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_COBBLESTONE_SLAB = registerBlock("crimson_cobblestone_slab",
            props -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_STONE_BRICKS_SLAB = registerBlock("crimson_stone_bricks_slab",
            props -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> POLISHED_CRIMSON_STONE_SLAB = registerBlock("polished_crimson_stone_slab",
            props -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).strength(1.5F, 6.0F)));

    public static final DeferredBlock<Block> CRIMSON_STONE_WALL = registerBlock("crimson_stone_wall",
            props -> new WallBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.COBBLESTONE_WALL).forceSolidOn()));

    // Forge: new ButtonBlock(props, BlockSetType.STONE, 20, false)
    public static final DeferredBlock<Block> CRIMSON_STONE_BUTTON = registerBlock("crimson_stone_button",
            props -> new ButtonBlock(BlockSetType.STONE, 20, BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_BUTTON)));

    // Forge: new PressurePlateBlock(Sensitivity.MOBS, props, BlockSetType.STONE)
    public static final DeferredBlock<Block> CRIMSON_STONE_PRESSURE_PLATE = registerBlock("crimson_stone_pressure_plate",
            props -> new PressurePlateBlock(BlockSetType.STONE,
                    BlockBehaviour.Properties.ofFullCopy(Blocks.STONE_PRESSURE_PLATE)));


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