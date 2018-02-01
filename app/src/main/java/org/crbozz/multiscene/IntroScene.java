package org.crbozz.multiscene;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;
import org.gearvrf.ISceneEvents;
import org.gearvrf.resonanceaudio.GVRAudioManager;
import org.gearvrf.resonanceaudio.GVRAudioSource;

final class IntroScene extends GVRScene implements ISceneEvents
{
    private GVRAudioManager audioListener;
    private GVRAudioSource boardL;
    private GVRAudioSource boardR;

    IntroScene(GVRContext gvrContext, GVRAudioManager audioManager) {
        super(gvrContext);
        audioListener = audioManager;
    }

    public void onInit(GVRContext ctx, GVRScene scene)
    {
        if (scene == this)
        {
            audioListener.clearSources();
            createObjects(ctx);
        }
    }

    public void onAfterInit()
    {
    }

    private void createObjects(GVRContext gvrContext) {
        GVRTexture leftTex = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.raw.pattern));
        GVRSceneObject leftDoor = new GVRSceneObject(gvrContext, 4f, 6f, leftTex);
        leftDoor.getTransform().setPosition(-8f, 0f, -8f);
        leftDoor.getTransform().setRotationByAxis(45f, 0f, 1f, 0f);
        addSceneObject(leftDoor);

        GVRTexture rightTex = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.raw.plants_text));
        GVRSceneObject rightDoor = new GVRSceneObject(gvrContext, 5f, 5f, rightTex);
        rightDoor.getTransform().setPosition(8f, 0f, -8f);
        rightDoor.getTransform().setRotationByAxis(45f, 0f, -1f, 0f);
        addSceneObject(rightDoor);
        boardL = new GVRAudioSource(gvrContext);
        leftDoor.attachComponent(boardL);
        audioListener.addSource(boardL);
        boardL.load("cartoon009.wav");
        boardL.setVolume(5f);

        boardR = new GVRAudioSource(gvrContext);
        rightDoor.attachComponent(boardR);
        audioListener.addSource(boardR);
        boardR.load("cartoon001a.wav");
        boardR.setVolume(1f);
        boardR.setVolume(5f);

        boardL.play(true);
        boardR.play(true);
    }
}
