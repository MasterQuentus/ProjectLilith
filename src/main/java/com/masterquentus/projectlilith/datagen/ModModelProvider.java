package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FireBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.MultiPartBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

        @Override
        protected void registerModels() {
            withExistingParent("lilith_contract", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/lilith_contract"));
            withExistingParent("lilith_soul", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/lilith_soul"));
            withExistingParent("flint_and_hellfire", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/flint_and_hellfire"));
            withExistingParent("hellfire_charge", "item/generated").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/hellfire_charge"));
            withExistingParent("crimson_fang", "item/handheld").texture("layer0", ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "item/crimson_fang"));
        }
    }
}