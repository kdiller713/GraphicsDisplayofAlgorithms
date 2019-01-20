package Animation3D;

import Core.GDAForm;
import Core.Algorithm;

import simple3D.Viewer;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;

import javax.vecmath.Vector3f;

import com.sun.j3d.utils.geometry.Sphere;

import java.awt.Dimension;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MovingBall extends Algorithm {
    public static void main(String[] args) throws Exception {
        MovingBall df = new MovingBall();
        GDAForm form = new GDAForm(df, 500, 500, 50);
        form.run();
    }
    
    private float x;
    private TransformGroup tg;
    
    public MovingBall(){
        x = -1.0f;
        Viewer viewer = new Viewer();
        
        Sphere s = new Sphere(0.1f);
        
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3f(x, 0.0f, 0.0f));
        
        tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setTransform(trans);
        tg.addChild(s);
        
        BranchGroup bg = new BranchGroup();
        bg.addChild(tg);
        viewer.addBranchGroup(bg);
        
        viewer.addMove();
        viewer.addZoom();
        viewer.addRotate();
        viewer.setPreferredSize(new Dimension(500, 500));
        this.add(viewer);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                viewer.setPreferredSize(e.getComponent().getSize());
            }
        });
    }
    
    @Override
    public void tick() {
        x += 0.1f;
        
        if(x > 1.0f){
            x = -1.0f;
        }
        
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3f(x, 0.0f, 0.0f));
        tg.setTransform(trans);
    }

    @Override
    public boolean isDone() {
        return false; // This is never done
    }

    @Override
    public String getName() {
        return "Moving Ball";
    }
}
