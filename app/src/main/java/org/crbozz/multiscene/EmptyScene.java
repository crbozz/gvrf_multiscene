package org.crbozz.multiscene;

import org.gearvrf.GVRAndroidResource;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRScene;
import org.gearvrf.GVRSceneObject;
import org.gearvrf.GVRTexture;

final class EmptyScene extends GVRScene {
    public EmptyScene(GVRContext gvrContext) {
        super(gvrContext);

        final GVRTexture texture = gvrContext.getAssetLoader().loadTexture(new GVRAndroidResource(gvrContext, R.raw.floor));
        GVRSceneObject floor = new GVRSceneObject(gvrContext, 120.0f, 120.0f, texture);

        floor.getTransform().setRotationByAxis(-90, 1, 0, 0);
        floor.getTransform().setPositionY(-1.5f);
        floor.getRenderData().setRenderingOrder(0);
        addSceneObject(floor);
    }
}
