package org.crbozz.multiscene;

import org.gearvrf.GVRContext;
import org.gearvrf.GVRDrawFrameListener;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.ISceneEvents;
import org.gearvrf.resonanceaudio.GVRAudioManager;
import org.gearvrf.resonanceaudio.GVRAudioSource;

import java.io.IOException;

final class R2D2Scene extends GVRScene implements GVRDrawFrameListener, ISceneEvents
{
    private static final float TOTAL = 8f; // The whole trajectory will take 8s
    private static final float FACTOR = (float)(Math.PI * 2.0) / TOTAL;

    private GVRSceneObject r2d2 = null;
    private float elapsed = 0f;

    private GVRAudioManager audioListener;
    private GVRAudioSource audioSource;

    public R2D2Scene(GVRContext gvrContext, GVRAudioManager audioManager) {
        super(gvrContext);
        audioListener = audioManager;
    }

    public void onInit(GVRContext ctx, GVRScene scene)
    {
        if (scene == this)
        {
            audioListener.clearSources();
            loadObjects(ctx);
            ctx.registerDrawFrameListener(this);
        }
    }

    public void onAfterInit()
    {
    }

    private void loadObjects(GVRContext gvrContext) {
        audioSource = new GVRAudioSource(gvrContext);

        try {
            r2d2 = gvrContext.getAssetLoader().loadModel("R2D2/R2D2.dae");
            r2d2.getTransform().setPosition(0f, 0f, -10f);
            addSceneObject(r2d2);

            r2d2.attachComponent(audioSource);
            audioListener.addSource(audioSource);
            audioSource.load("cube_sound.wav");
            audioSource.setVolume(5f);
            audioSource.play(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void disable() {
        audioListener.setEnable(false);
    }

    void enable() {
        audioListener.setEnable(true);
    }

    @Override
    public void onDrawFrame(float d) {
        elapsed += d;

        float delta = elapsed * FACTOR;
        float x = 10f * (float)Math.sin(delta);
        r2d2.getTransform().setPosition(x, 0f, -10f);
    }
}
