package org.crbozz.multiscene;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import org.gearvrf.GVRActivity;
import org.gearvrf.GVRContext;
import org.gearvrf.GVRMain;
import org.gearvrf.resonanceaudio.GVRAudioManager;

import org.xml.sax.HandlerBase;

public class MainActivity extends GVRActivity {

    private TheMain theMain;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        theMain = new TheMain();
        setMain(theMain);
    }

    @Override
    protected void onPause() {
        super.onPause();
        theMain.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        theMain.resume();
    }

    private class TheMain extends GVRMain {
        private GVRAudioManager audioManager;
        private IntroScene introScene;
        private R2D2Scene r2D2Scene;
        private EmptyScene emptyScene;
        private ShipScene shipScene;

        private final Object mLock = new Object();
        private boolean mStop = false;
        private Handler mHandler;

        @Override
        public void onInit(final GVRContext gvrContext) {
            audioManager = new GVRAudioManager(gvrContext);
            introScene = new IntroScene(gvrContext, audioManager);
            r2D2Scene = new R2D2Scene(gvrContext, audioManager);
            emptyScene = new EmptyScene(gvrContext);
            shipScene = new ShipScene(gvrContext, audioManager);
            audioManager.setEnable(true);
            gvrContext.setMainScene(introScene);

            mHandler = new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message message) {
                    synchronized (mLock) {
                        if (mStop) {
                            return true;
                        }
                    }

                    if (gvrContext.getMainScene() == introScene) {
                        gvrContext.setMainScene(r2D2Scene);
                    } else if (gvrContext.getMainScene() == r2D2Scene) {
                        audioManager.clearSources();
                        gvrContext.setMainScene(emptyScene);
                    } else if (gvrContext.getMainScene() == emptyScene) {
                        gvrContext.setMainScene(shipScene);
                    } else {
                        gvrContext.setMainScene(introScene);
                    }

                    mHandler.sendEmptyMessageDelayed(0, 10000);

                    return false;
                }
            });
            mHandler.sendEmptyMessageDelayed(0, 10000);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    r2D2Scene.disable();
                }
            }, 15000);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    r2D2Scene.enable();
                }
            }, 30000);
        }

        void pause() {
            if (mStop) {
                return;
            }

            synchronized (mLock) {
                mStop = true;
            }
        }

        void resume() {
            if (!mStop) {
                return;
            }
            audioManager.setEnable(true);
            mStop = false;
            mHandler.sendEmptyMessageDelayed(0, 10000);
        }
    }
}
