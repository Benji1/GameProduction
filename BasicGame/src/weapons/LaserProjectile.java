/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import config.ConfigReader;
import mygame.Main;
import mygame.PhysicsWorld;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;

public class LaserProjectile extends Projectile {
    
    protected Body body;
    protected Spatial spatial;
    protected Material material;
    
    public LaserProjectile(Vec2 spawnPoint, Vec2 fireDirection, Main app) {
        super(spawnPoint, fireDirection, app);
        this.startForce = ConfigReader.getFromMap(ConfigReader.getBaseMap("Weapon"), "LaserProjectile_Speed", float.class);
        this.lifetime = ConfigReader.getFromMap(ConfigReader.getBaseMap("Weapon"), "LaserProjectile_Lifetime", float.class);

        createBox(spawnPoint);  
    }   
    
    private void createBox(Vec2 spawnPoint) {
        Box box = new Box(0.5f, 0.5f, 0.5f);
        spatial = new Geometry("Box", box);
        spatial.scale(1f, 0.2f, 1f);
        material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");
        
        material.setBoolean("UseMaterialColors",true);
        material.setColor("Ambient", ColorRGBA.Red);
        material.setColor("Diffuse", ColorRGBA.Red);

        spatial.setMaterial(material);

        app.getRootNode().attachChild(this);
        this.attachChild(spatial);

        generatePhysicsBody(spawnPoint.x, spawnPoint.y);
    }
    
    private void generatePhysicsBody(float x, float y) {
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(0.3f, 0.3f);
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = rect;
        fDef.density = 1.0f;
        fDef.friction = 0.0f;
        
        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        //bDef.angle = 1f;
        bDef.type = BodyType.DYNAMIC;
        
        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.applyForce(direction.mul(startForce), body.getPosition());
    }    
    
    protected void updateBoxPosition() {
        Vector3f bodyPos = new Vector3f(
                (float)body.getWorldPoint(body.getLocalCenter()).x, 
                0.0f, 
                (float)body.getWorldPoint(body.getLocalCenter()).y);      
        
        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
        
        spatial.setLocalTranslation(bodyPos);
        spatial.setLocalRotation(q); 
    }
    
    @Override
    public void update(float delta) {
        super.update(delta);
        updateBoxPosition();
    }
    
    @Override
    public void die() {
        super.die();
        PhysicsWorld.world.destroyBody(body);
        this.removeFromParent();
    }
    
}
