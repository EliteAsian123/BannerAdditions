package eliteasian.mods.banneradditions.bannerpattern;

import net.minecraft.client.resources.ReloadListener;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.ArrayList;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BannerPatternTextureHandler extends ReloadListener<BannerPatternTextureHandler> {

    public static final BannerPatternTextureHandler INSTANCE = new BannerPatternTextureHandler();

    public static final List<ResourceLocation> bannerTextures = new ArrayList<>();
    public static final List<ResourceLocation> shieldTextures = new ArrayList<>();

    @Override
    protected BannerPatternTextureHandler prepare(IResourceManager resourceManagerIn, IProfiler profilerIn) {
        profilerIn.startTick();

        bannerTextures.clear();
        shieldTextures.clear();

        bannerTextures.addAll(findTextures(resourceManagerIn, "textures/entity/banner"));
        shieldTextures.addAll(findTextures(resourceManagerIn, "textures/entity/shield"));

        profilerIn.endTick();
        return INSTANCE;
    }

    @Override
    protected void apply(BannerPatternTextureHandler objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn) {

    }

    protected List<ResourceLocation> findTextures(IResourceManager resourceManagerIn, String pathIn) {
        List<ResourceLocation> list = new ArrayList<>();

        for(ResourceLocation resourceLocation : resourceManagerIn.getAllResourceLocations(pathIn, (p_215274_0_) ->
                p_215274_0_.endsWith(".png")
        )) {
            String path = resourceLocation.getPath();
            path = path.substring(9, path.length() - 4);

            list.add(new ResourceLocation(resourceLocation.getNamespace(), path));
        }

        return list;
    }
}
