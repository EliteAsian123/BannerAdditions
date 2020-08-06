package eliteasian.mods.banneradditions;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.common.ForgeConfigSpec.IntValue;
import org.apache.commons.lang3.tuple.Pair;

public class BannerAdditionsConfig {

    public static class Config {
        public final BooleanValue safeMode;

        public final IntValue maxPatternCount;

        public Config(ForgeConfigSpec.Builder builder) {
            builder.comment("Common config for BannerAdditions. MAKE SURE BOTH THE SERVER AND THE CLIENT HAVE THE SAME CONFIG.")
                    .push("banneradditions");

            safeMode = builder.comment("Disable custom banner loading. Could break banners. Requires world restart.")
                    .worldRestart()
                    .define("safeMode", false);

            maxPatternCount = builder.comment("The maximum number of patterns on a banner (without commands). This value is six in vanilla Minecraft. May require game restart.")
                    .defineInRange("maxPatternCount", 12, 6, 12);

            builder.pop();
        }
    }

    public static final ForgeConfigSpec FORGE_SPEC;
    public static final Config CONFIG;

    static {
        final Pair<Config, ForgeConfigSpec> pair = new ForgeConfigSpec.Builder().configure(Config::new);

        CONFIG = pair.getLeft();
        FORGE_SPEC = pair.getRight();
    }
}
