package Protein;

import Core.GDAForm;
import Core.Algorithm;

import simple3D.Viewer;

import javax.media.j3d.BranchGroup;
import javax.media.j3d.Transform3D;
import javax.media.j3d.TransformGroup;
import javax.media.j3d.ColoringAttributes;
import javax.media.j3d.Appearance;

import javax.vecmath.Vector3f;
import javax.vecmath.Color3f;

import javax.swing.JLabel;

import com.sun.j3d.utils.geometry.Sphere;

import java.awt.Dimension;
import java.awt.Color;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import java.util.Scanner;

import java.io.File;

public class PDBAnimation extends Algorithm {
    public static void main(String[] args) throws Exception {
        PDBAnimation df;
        
        if(args.length > 0){
            df = new PDBAnimation(args[0]);
        }else{
            df = new PDBAnimation("Data/md1UBQ.pdb");
        }
        
        GDAForm form = new GDAForm(df, 500, 500, 10);
        form.run();
    }
    
    private static final float SCALE_FACTOR = 25.0f;
    private static final float RADIUS = 1.4f / SCALE_FACTOR;
    
    private int step;
    private float[][][] locationSteps;
    private TransformGroup[] atms;
    private JLabel stepLbl;
    
    public PDBAnimation(String pdbFile){
        step = 0;
        int[] data = getData(pdbFile);
        readPDBFile(pdbFile, data[0], data[1]);
        
        stepLbl = new JLabel("Step: " + step);
        this.add(stepLbl);
        
        Viewer viewer = new Viewer();
        viewer.addMove();
        viewer.addZoom();
        viewer.addRotate();
        viewer.setPreferredSize(new Dimension(500, 500));
        this.add(viewer);
        
        BranchGroup bg = new BranchGroup();
        
        for(int i = 0; i < atms.length; i++){
            bg.addChild(atms[i]);
        }
        
        viewer.addBranchGroup(bg);
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e){
                viewer.setPreferredSize(e.getComponent().getSize());
            }
        });
    }
    
    private void readPDBFile(String pdbFile, int modelCount, int atmCount){
        locationSteps = new float[modelCount][atmCount][3];
        atms = new TransformGroup[atmCount];
        String[] elementTypes = new String[atmCount];
        boolean[] skipped = new boolean[atmCount];
        
        try{
            Scanner sc = new Scanner(new File(pdbFile));
            int modelInd = -1;
            int atomInd = -1;
            int ind = 0;
            String line = null;
            float min = Float.MAX_VALUE;
            float max = Float.MIN_VALUE;
            
            while(sc.hasNextLine()){
                line = sc.nextLine();
                
                if(line.startsWith("MODEL")){
                    modelInd++;
                    atomInd = -1;
                }else if(line.startsWith("ATOM")){
                    atomInd++;
                    String[] split = line.split(" ");
                    ind = -1;
                    
                    for(int i = 0; i < 6; i++){
                        ind = getNextNonEmpty(split, ind + 1);
                    }
                    
                    ind = getNextNonEmpty(split, ind + 1);
                    locationSteps[modelInd][atomInd][0] = Float.parseFloat(split[ind]) / SCALE_FACTOR;
                    ind = getNextNonEmpty(split, ind + 1);
                    locationSteps[modelInd][atomInd][1] = Float.parseFloat(split[ind]) / SCALE_FACTOR;
                    ind = getNextNonEmpty(split, ind + 1);
                    locationSteps[modelInd][atomInd][2] = Float.parseFloat(split[ind]) / SCALE_FACTOR;
                    
                    min = Math.min(min, locationSteps[modelInd][atomInd][0]);
                    max = Math.max(max, locationSteps[modelInd][atomInd][0]);
                    min = Math.min(min, locationSteps[modelInd][atomInd][1]);
                    max = Math.max(max, locationSteps[modelInd][atomInd][1]);
                    min = Math.min(min, locationSteps[modelInd][atomInd][2]);
                    max = Math.max(max, locationSteps[modelInd][atomInd][2]);
                    
                    for(int i = 9; i < 11; i++){
                        ind = getNextNonEmpty(split, ind + 1);
                    }
                    
                    ind = getNextNonEmpty(split, ind + 1);
                    elementTypes[atomInd] = split[ind];
                    skipped[atomInd] = true;
                }
            }
                
            sc.close();
        
            float mid = (max + min) / 2.0f;
            
            for(int i = 0; i < locationSteps.length; i++){
                for(int j = 0; j < locationSteps[i].length; j++){
                    for(int k = 0; k < locationSteps[i][j].length; k++){
                        locationSteps[i][j][k] = locationSteps[i][j][k] - mid;
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
        
        int nullCount = 0;
        // Create the spheres
        for(int i = 0; i < atmCount; i++){
            atms[i] = createAtom(locationSteps[0][i], elementTypes[i]);
        }
    }
    
    private TransformGroup createAtom(float[] location, String element){
        Appearance ap = new Appearance();
        ColoringAttributes ca = new ColoringAttributes();
        
        switch(element){
        case "C":
            ca.setColor(new Color3f(Color.GREEN));
            break;
        case "O":
            ca.setColor(new Color3f(Color.RED));
            break;
        case "N":
            ca.setColor(new Color3f(Color.BLUE));
            break;
        case "S":
            ca.setColor(new Color3f(Color.YELLOW));
            break;
        default:
            ca.setColor(new Color3f(Color.WHITE));
        }
        
        ap.setColoringAttributes(ca);
        
        Sphere s = new Sphere(RADIUS, ap);
        
        Transform3D trans = new Transform3D();
        trans.setTranslation(new Vector3f(location));
        
        TransformGroup tg = new TransformGroup();
        tg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);
        tg.setTransform(trans);
        tg.addChild(s);
        
        return tg;
    }
    
    // Counts the number of models (index 0)
    // Counts the number of atoms (index 1)
    private int[] getData(String pdbFile){
        int atmCount = 0;
        int modelCount = 0;
        
        try{
            Scanner sc = new Scanner(new File(pdbFile));
            String lastLine = null;
            String line = null;
            int ind = 0;
            
            while(sc.hasNextLine()){
                line = sc.nextLine();
                
                if(line.startsWith("MODEL")){
                    modelCount++;
                }else if(line.startsWith("ATOM") && modelCount == 1){
                    atmCount++;
                }
                
                lastLine = line;
            }
            
            sc.close();
        }catch(Exception e){
            throw new RuntimeException("Failed to read file: " + e.getMessage());
        }
        
        return new int[] {modelCount, atmCount};
    }
    
    private int getNextNonEmpty(String[] a, int s){
        for(int i = s; i < a.length; i++){
            if(!a[i].isEmpty()) return i;
        }
        
        return -1;
    }
    
    @Override
    public void tick() {
        step = (step + 1) % locationSteps.length;
    
        for(int i = 0; i < atms.length; i++){
            Transform3D trans = new Transform3D();
            trans.setTranslation(new Vector3f(locationSteps[step][i]));
            atms[i].setTransform(trans);
        }
        
        stepLbl.setText("Step: " + step);
    }

    @Override
    public boolean isDone() {
        return false; // This is never done
    }

    @Override
    public String getName() {
        return "Molecular Dynamics in Java";
    }
}
