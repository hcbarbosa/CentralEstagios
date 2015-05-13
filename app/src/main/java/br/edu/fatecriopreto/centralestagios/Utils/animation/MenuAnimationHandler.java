
package br.edu.fatecriopreto.centralestagios.Utils.animation;

import android.animation.Animator;
import android.graphics.Point;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import br.edu.fatecriopreto.centralestagios.Utils.*;

/**
 * An abstract class that is a prototype for the actual animation handlers
 */
public abstract class MenuAnimationHandler {

    // There are only two distinct animations at the moment.
    protected enum ActionType {OPENING, CLOSING}

    protected FloatingActionMenu menu;

    public MenuAnimationHandler() {
    }

    public void setMenu(FloatingActionMenu menu) {
        this.menu = menu;
    }


    public void animateMenuOpening(Point center) {
        if(menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.");
        }

    }


    public void animateMenuClosing(Point center) {
        if(menu == null) {
            throw new NullPointerException("MenuAnimationHandler cannot animate without a valid FloatingActionMenu.");
        }
    }


    protected void restoreSubActionViewAfterAnimation(FloatingActionMenu.Item subActionItem, ActionType actionType) {
        ViewGroup.LayoutParams params = subActionItem.view.getLayoutParams();
        subActionItem.view.setTranslationX(0);
        subActionItem.view.setTranslationY(0);
        subActionItem.view.setRotation(0);
        subActionItem.view.setScaleX(1);
        subActionItem.view.setScaleY(1);
        subActionItem.view.setAlpha(1);
        if(actionType == ActionType.OPENING) {
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
            if(menu.isSystemOverlay()) {
                WindowManager.LayoutParams overlayParams = (WindowManager.LayoutParams) menu.getOverlayContainer().getLayoutParams();
                lp.setMargins(subActionItem.x - overlayParams.x, subActionItem.y - overlayParams.y, 0, 0);
            }
            else {
                lp.setMargins(subActionItem.x, subActionItem.y, 0, 0);
            }
            subActionItem.view.setLayoutParams(lp);
        }
        else if(actionType == ActionType.CLOSING) {
            Point center = menu.getActionViewCenter();
            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) params;
            if(menu.isSystemOverlay()) {
                WindowManager.LayoutParams overlayParams = (WindowManager.LayoutParams) menu.getOverlayContainer().getLayoutParams();
                lp.setMargins(center.x - overlayParams.x - subActionItem.width / 2, center.y - overlayParams.y - subActionItem.height / 2, 0, 0);
            }
            else {
                lp.setMargins(center.x - subActionItem.width / 2, center.y - subActionItem.height / 2, 0, 0);
            }
            subActionItem.view.setLayoutParams(lp);
            menu.removeViewFromCurrentContainer(subActionItem.view);

            if(menu.isSystemOverlay()) {
                // When all the views are removed from the overlay container,
                // we also need to detach it
                if (menu.getOverlayContainer().getChildCount() == 0) {
                    menu.detachOverlayContainer();
                }
            }
        }
    }


    public class LastAnimationListener implements Animator.AnimatorListener {

        @Override
        public void onAnimationStart(Animator animation) {
            setAnimating(true);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            setAnimating(false);
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            setAnimating(false);
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
            setAnimating(true);
        }
    }

    public abstract boolean isAnimating();
    protected abstract void setAnimating(boolean animating);
}
