package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.*;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModModelProvider {

    public static class BlockStates extends BlockStateProvider {
        public BlockStates(PackOutput output, ExistingFileHelper exFileHelper) {
            super(output, ProjectLilith.MOD_ID, exFileHelper);
        }

        @Override
        protected void registerStatesAndModels() {
            horizontalBlock(
                    ModBlocks.LILITH_TROPHY.get(),
                    new ModelFile.UncheckedModelFile(ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "block/lilith_trophy"))
            );

            registerHellfireBlock(ModBlocks.HELLFIRE.get());

            // Full Cube Blocks + BlockItems
            blockWithItem(ModBlocks.CRIMSON_STONE);
            blockWithItem(ModBlocks.CRIMSON_COBBLESTONE);
            blockWithItem(ModBlocks.CRIMSON_STONE_BRICKS);
            blockWithItem(ModBlocks.POLISHED_CRIMSON_STONE);
            blockWithItem(ModBlocks.CHISELED_CRIMSON_STONE);
            blockWithItem(ModBlocks.CRACKED_CRIMSON_STONE);

            // Stairs
            stairsBlock((StairBlock) ModBlocks.CRIMSON_STONE_STAIRS.get(), blockTexture(ModBlocks.CRIMSON_STONE.get()));
            stairsBlock((StairBlock) ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get(), blockTexture(ModBlocks.CRIMSON_COBBLESTONE.get()));
            stairsBlock((StairBlock) ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get(), blockTexture(ModBlocks.CRIMSON_STONE_BRICKS.get()));
            stairsBlock((StairBlock) ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get(), blockTexture(ModBlocks.POLISHED_CRIMSON_STONE.get()));

            // Slabs
            slabBlock((SlabBlock) ModBlocks.CRIMSON_STONE_SLAB.get(), blockTexture(ModBlocks.CRIMSON_STONE.get()), blockTexture(ModBlocks.CRIMSON_STONE.get()));
            slabBlock((SlabBlock) ModBlocks.CRIMSON_COBBLESTONE_SLAB.get(), blockTexture(ModBlocks.CRIMSON_COBBLESTONE.get()), blockTexture(ModBlocks.CRIMSON_COBBLESTONE.get()));
            slabBlock((SlabBlock) ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get(), blockTexture(ModBlocks.CRIMSON_STONE_BRICKS.get()), blockTexture(ModBlocks.CRIMSON_STONE_BRICKS.get()));
            slabBlock((SlabBlock) ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get(), blockTexture(ModBlocks.POLISHED_CRIMSON_STONE.get()), blockTexture(ModBlocks.POLISHED_CRIMSON_STONE.get()));

            // Wall, Button, Pressure Plate
            wallBlock((WallBlock) ModBlocks.CRIMSON_STONE_WALL.get(), blockTexture(ModBlocks.CRIMSON_COBBLESTONE.get()));
            buttonBlock((ButtonBlock) ModBlocks.CRIMSON_STONE_BUTTON.get(), blockTexture(ModBlocks.CRIMSON_STONE.get()));
            pressurePlateBlock((PressurePlateBlock) ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get(), blockTexture(ModBlocks.CRIMSON_STONE.get()));

            // BlockItems for Stairs, Slabs, and Pressure Plate
            blockItem(ModBlocks.CRIMSON_STONE_STAIRS);
            blockItem(ModBlocks.CRIMSON_COBBLESTONE_STAIRS);
            blockItem(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS);
            blockItem(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS);

            blockItem(ModBlocks.CRIMSON_STONE_SLAB);
            blockItem(ModBlocks.CRIMSON_COBBLESTONE_SLAB);
            blockItem(ModBlocks.CRIMSON_STONE_BRICKS_SLAB);
            blockItem(ModBlocks.POLISHED_CRIMSON_STONE_SLAB);

            blockItem(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE);
        }

        private void blockWithItem(DeferredBlock<?> deferredBlock) {
            simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
        }

        private void blockItem(DeferredBlock<?> deferredBlock) {
            simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile(ProjectLilith.MOD_ID + ":block/" + deferredBlock.getId().getPath()));
        }

        private void registerHellfireBlock(Block hellfire) {
            ResourceLocation fire0 = ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "block/hell_fire_0");
            ResourceLocation fire1 = ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "block/hell_fire_1");

            ModelFile floor0 = models().withExistingParent("hell_fire_floor0", mcLoc("block/template_fire_floor")).texture("fire", fire0).renderType("cutout");
            ModelFile floor1 = models().withExistingParent("hell_fire_floor1", mcLoc("block/template_fire_floor")).texture("fire", fire1).renderType("cutout");

            ModelFile side0 = models().withExistingParent("hell_fire_side0", mcLoc("block/template_fire_side")).texture("fire", fire0).renderType("cutout");
            ModelFile side1 = models().withExistingParent("hell_fire_side1", mcLoc("block/template_fire_side")).texture("fire", fire1).renderType("cutout");
            ModelFile sideAlt0 = models().withExistingParent("hell_fire_side_alt0", mcLoc("block/template_fire_side_alt")).texture("fire", fire0).renderType("cutout");
            ModelFile sideAlt1 = models().withExistingParent("hell_fire_side_alt1", mcLoc("block/template_fire_side_alt")).texture("fire", fire1).renderType("cutout");

            ModelFile up0 = models().withExistingParent("hell_fire_up0", mcLoc("block/template_fire_up")).texture("fire", fire0).renderType("cutout");
            ModelFile up1 = models().withExistingParent("hell_fire_up1", mcLoc("block/template_fire_up")).texture("fire", fire1).renderType("cutout");
            ModelFile upAlt0 = models().withExistingParent("hell_fire_up_alt0", mcLoc("block/template_fire_up_alt")).texture("fire", fire0).renderType("cutout");
            ModelFile upAlt1 = models().withExistingParent("hell_fire_up_alt1", mcLoc("block/template_fire_up_alt")).texture("fire", fire0).renderType("cutout");

            MultiPartBlockStateBuilder builder = getMultipartBuilder(hellfire);

            builder.part().modelFile(floor0).nextModel().modelFile(floor1).addModel().end();

            builder.part()
                    .modelFile(side0).nextModel().modelFile(side1).nextModel()
                    .modelFile(sideAlt0).nextModel().modelFile(sideAlt1).addModel()
                    .condition(FireBlock.NORTH, true).end();

            builder.part()
                    .modelFile(side0).rotationY(90).nextModel().modelFile(side1).rotationY(90).nextModel()
                    .modelFile(sideAlt0).rotationY(90).nextModel().modelFile(sideAlt1).rotationY(90).addModel()
                    .condition(FireBlock.EAST, true).end();

            builder.part()
                    .modelFile(side0).rotationY(180).nextModel().modelFile(side1).rotationY(180).nextModel()
                    .modelFile(sideAlt0).rotationY(180).nextModel().modelFile(sideAlt1).rotationY(180).addModel()
                    .condition(FireBlock.SOUTH, true).end();

            builder.part()
                    .modelFile(side0).rotationY(270).nextModel().modelFile(side1).rotationY(270).nextModel()
                    .modelFile(sideAlt0).rotationY(270).nextModel().modelFile(sideAlt1).rotationY(270).addModel()
                    .condition(FireBlock.WEST, true).end();

            builder.part()
                    .modelFile(up0).nextModel().modelFile(up1).nextModel()
                    .modelFile(upAlt0).nextModel().modelFile(upAlt1).addModel()
                    .condition(FireBlock.UP, true).end();
        }
    }

    public static class ItemModels extends ItemModelProvider {
        public ItemModels(PackOutput output, ExistingFileHelper existingFileHelper) {
            super(output, ProjectLilith.MOD_ID, existingFileHelper);
        }

        private void generated(String name) {
            withExistingParent(name, "item/generated")
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/" + name));
        }

        @Override
        protected void registerModels() {
            withExistingParent("lilith_contract", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/lilith_contract"));
            withExistingParent("lilith_soul", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/lilith_soul"));
            withExistingParent("flint_and_hellfire", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/flint_and_hellfire"));
            withExistingParent("hellfire_charge", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/hellfire_charge"));
            withExistingParent("crimson_fang", "item/handheld").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/crimson_fang"));

            generated("abyssal_helmet");
            generated("abyssal_chestplate");
            generated("abyssal_leggings");
            generated("abyssal_boots");

            // Lilith Spawn Egg
            withExistingParent("lilith_spawn_egg", mcLoc("item/template_spawn_egg"));

            // Inventory models for Wall & Button
            buttonInventory("crimson_stone_button", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "block/crimson_stone"));
            wallInventory("crimson_stone_wall", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "block/crimson_cobblestone"));
        }
    }
}