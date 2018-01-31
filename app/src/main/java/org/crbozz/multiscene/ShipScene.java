package org.crbozz.multiscene;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRDrawFrameListener;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.audio.GVRAudioListener;
import org.gearvrf.audio.GVRAudioSource;
import org.gearvrf.scene_objects.GVRSphereSceneObject;


final class ShipScene extends GVRScene implements GVRDrawFrameListener {
    private static final float TOTAL = 13f; // The whole trajectory will take 13s
    private static final float FACTOR = (float)(Math.PI * 2.0) / TOTAL;
    private static final float FACTOR2 = 360f / (TOTAL + 2f);

    private GVRSceneObject ship = null;

    private float elapsed = 0f;

    private GVRAudioListener audioListener;
    private GVRAudioSource audioSource;

    public ShipScene(GVRContext gvrContext) {
        super(gvrContext);

        loadObjects(gvrContext);

        gvrContext.registerDrawFrameListener(this);
    }

    private void loadObjects(GVRContext gvrContext) {
        audioListener = new GVRAudioListener(gvrContext, this);

        audioSource = new GVRAudioSource(gvrContext);

        GVRTexture texture = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.raw.color_sphere));
        ship = new GVRSphereSceneObject(gvrContext, true, texture);

        ship.getTransform().setPosition(-3f, 0f, -10f);
        ship.getTransform().setRotationByAxis(90f, 0f, -1f, 0f);
        ship.getTransform().setScale(2f, 2f, 2f);
        addSceneObject(ship);

        ship.attachComponent(audioSource);
        audioSource.load("campfire.wav");
        audioSource.setVolume(5f);
        audioSource.play(true);
    }

    @Override
    public void onDrawFrame(float d) {
        elapsed += d;

        float delta = elapsed * FACTOR;
        float a = (float)Math.sin(delta) * 10f;
        float b = (float)Math.cos(delta) * 10f;
        ship.getTransform().setPosition(a, a, b);

        float delta2 = 90f - elapsed * FACTOR2;
        ship.getTransform().setRotationByAxis(delta2, 0f, -1f, 0f);
    }
}
