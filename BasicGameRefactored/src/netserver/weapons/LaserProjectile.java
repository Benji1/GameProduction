/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package netserver.weapons;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import netserver.WJSFServer;
import netserver.modules.BasicModule;
import netserver.physics.PhysicsWorld;
import netserver.shipdesigns.TestShipDesigns;
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

    public LaserProjectile(Vec2 spawnPoint, Vec2 fireDirection, WJSFServer app) {
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
        
        spatial.setLocalTranslation(new Vector3f(spawnPoint.x, 0, spawnPoint.y));
        this.setLocalTranslation(0, 0, 0);
        Quaternion q = new Quaternion();
        
        q.fromAngleAxis(-(float) Math.atan2(fireDirection.y, fireDirection.x), new Vector3f(0f, 1f, 0f));
        spatial.setLocalRotation(q);

        generatePhysicsBody(spawnPoint.x, spawnPoint.y);
        setPhysicsCenter(body);
    }

    private void generatePhysicsBody(float x, float y) {
        PolygonShape rect = new PolygonShape();
        rect.setAsBox(0.9f, 0.1f);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = rect;
        fDef.density = 0.01f;
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
        updateBoxPosition();
    }

    @Override
    public void delete() {
        super.delete();
        PhysicsWorld.world.destroyBody(body);
        spatial.removeFromParent();
        this.removeFromParent();
    }

    public void handleBasicModuleCollision(BasicModule m) {
        m.takeDamage(100);
        markForDeletion();
    }

    public void handleShieldColliderCollision(ShieldCollider s) {
        s.putDamgeToShieldModule(100f, new Vector3f(body.getPosition().x, 0f, body.getPosition().y));
        markForDeletion();
    }
}
