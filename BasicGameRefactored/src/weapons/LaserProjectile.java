/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import Modules.BasicModule;
import Modules.Shield;
import ShipDesigns.TestShipDesigns;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import mygame.Main;
import mygame.PhysicsWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;

public class LaserProjectile extends Projectile implements ContactListener {

    protected Body body;
    protected Spatial spatial;
    protected Material material;

    public LaserProjectile(Vec2 spawnPoint, Vec2 fireDirection, Main app) {
        super(spawnPoint, fireDirection, app);
        this.startForce = cr.getFromMap(cr.getBaseMap("LaserProjectile"), "InitialAcceleration", float.class);
        this.lifetime = cr.getFromMap(cr.getBaseMap("LaserProjectile"), "Lifetime", float.class);

        createBox(spawnPoint, fireDirection);
    }

    private void createBox(Vec2 spawnPoint, Vec2 fireDirection) {
        Box box = new Box(0.9f, 0.1f, 0.1f);
        spatial = new Geometry("Box", box);
        material = new Material(app.getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", ColorRGBA.Red);
        material.setColor("Diffuse", ColorRGBA.Red);

        spatial.setMaterial(material);

        
        
        app.getRootNode().attachChild(spatial);
        app.getRootNode().attachChild(this);
        
        spatial.setLocalTranslation(0, 0, 0);
        this.setLocalTranslation(0, 0, 0);
        //Quaternion q = new Quaternion();
        
        //q.fromAngleAxis(-(float) Math.atan2(fireDirection.y, fireDirection.x), new Vector3f(0f, 1f, 0f));
        //this.setLocalRotation(q);
        //spatial.setLocalRotation(q);

        generatePhysicsBody(spawnPoint.x, spawnPoint.y);
        setPhysicsCenter(body);
        
    }

    private void generatePhysicsBody(float x, float y) {
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(0.9f, 0.1f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = rect;
        fDef.density = 1.0f;
        fDef.friction = 0.0f;
        fDef.filter.categoryBits = TestShipDesigns.CATEGORY_PROJECTILE;
        fDef.filter.maskBits = TestShipDesigns.MASK_PROJECTILE;

        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.angle = (float) Math.atan2(direction.y, direction.x);
        bDef.type = BodyType.DYNAMIC;
        bDef.bullet = true;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.setUserData(this);
        PhysicsWorld.world.setContactListener(this);
        body.applyForce(direction.mul(startForce), body.getPosition());
    }

    protected void updateBoxPosition() {
        Vector3f bodyPos = new Vector3f(
                (float) body.getWorldPoint(body.getLocalCenter()).x,
                0.0f,
                (float) body.getWorldPoint(body.getLocalCenter()).y);

        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));

        spatial.setLocalTranslation(bodyPos);
        spatial.setLocalRotation(q);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
       
        //spatial.setLocalTranslation(0,0,0);
        //spatial.setLocalRotation(getBodyAngle());
        //updateBoxPosition();
    }

    @Override
    public void die() {
        super.die();
        //System.out.println("Should be dead now");
        this.detachChild(spatial);
        app.bodiesToRemove.add(body);
        //this.removeFromParent();

    }

    public void beginContact(Contact cntct) {
        if (cntct.getFixtureA().getBody().getUserData() instanceof ShieldCollider) {
            handleShieldColliderCollision((ShieldCollider) cntct.getFixtureA().getBody().getUserData());
        }
        if (cntct.getFixtureB().getBody().getUserData() instanceof ShieldCollider) {
            handleShieldColliderCollision((ShieldCollider) cntct.getFixtureB().getBody().getUserData());
        }

        if (cntct.getFixtureA().getBody().getUserData() instanceof BasicModule) {
            handleBasicModuleCollision((BasicModule) cntct.getFixtureA().getBody().getUserData());
        }

        if (cntct.getFixtureB().getBody().getUserData() instanceof BasicModule) {
            handleBasicModuleCollision((BasicModule) cntct.getFixtureB().getBody().getUserData());
        }
    }

    public void handleBasicModuleCollision(BasicModule b) {
        if (b instanceof Shield) {
            Shield s = (Shield) b;
            if(s.getShieldCollider() != null) {
                // crappy workaround, because of shield collider and shield both getting the events, because they are welded?
            //  thus destroying the shield module, although it should not
            }else {
                s.takeDamage(100);
            }
            // crappy workaround, because of shield collider and shield both getting the events, because they are welded?
            //  thus destroying the shield module, although it should not
        } else {
            b.takeDamage(100);
        }
        die();
    }

    public void handleShieldColliderCollision(ShieldCollider s) {
        s.putDamgeToShieldModule(100f);
        die();
    }

    public void endContact(Contact cntct) {
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
}
