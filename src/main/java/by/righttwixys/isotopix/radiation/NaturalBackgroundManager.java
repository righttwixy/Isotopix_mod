package by.righttwixys.isotopix.radiation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NaturalBackgroundManager {

    public static float calculateEnvironmentalBackgroundMicroR(ServerLevel level, BlockPos pos) {
        float backgroundMicroR = 0.0f;


        int y = pos.getY();
        if (y < 0) {

            backgroundMicroR += Math.max(1.0f, 4.0f + (y / 32.0f));
        } else if (y <= 70) {

            backgroundMicroR += 3.6f + ((y - 64) * 0.05f);
        } else {

            float heightFactor = (y - 70) / 40.0f;
            backgroundMicroR += 3.6f + (float) Math.pow(2.2, heightFactor);
        }


        int stoneCount = 0;
        int graniteCount = 0;
        int deepslateCount = 0;

        for (BlockPos near : BlockPos.betweenClosed(pos.offset(-2, -2, -2), pos.offset(2, 2, 2))) {
            BlockState state = level.getBlockState(near);
            if (state.is(Blocks.GRANITE)) graniteCount++;
            else if (state.is(Blocks.DEEPSLATE)) deepslateCount++;
            else if (!state.isAir()) stoneCount++;
        }

        backgroundMicroR += (stoneCount * 0.06f);
        backgroundMicroR += (deepslateCount * 0.12f);
        backgroundMicroR += (graniteCount * 0.35f);


        if (y < 50 && !level.canSeeSky(pos)) {

            int airBlocks = 0;
            for (BlockPos near : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
                if (level.getBlockState(near).isAir()) {
                    airBlocks++;
                }
            }
            if (airBlocks > 4) {
                backgroundMicroR += 4.5f + (float) ((50 - y) * 0.12);
            }
        }


        return Math.max(8.0f, backgroundMicroR);
    }
}