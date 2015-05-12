/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weapons;

import Modules.BasicModule;
import static Modules.BasicModule.fillNotOverLimit;
import Modules.Shield;
import ShipDesigns.TestShipDesigns;
import com.jme3.material.Material;
import com.jme3.material.RenderState.BlendMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.queue.RenderQueue.Bucket;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import mygame.PhysicsWorld;
import org.jbox2d.callbacks.ContactImpulse;
import org.jbox2d.callbacks.ContactListener;
import org.jbox2d.collision.Manifold;
import org.jbox2d.collision.shapes.CircleShape;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.BodyDef;
import org.jbox2d.dynamics.BodyType;
import org.jbox2d.dynamics.FixtureDef;
import org.jbox2d.dynamics.contacts.Contact;
import org.jbox2d.dynamics.joints.WeldJointDef;

/**
 *
 * @author 1337
 */
public class ShieldCollider extends Node implements ContactListener {

    Shield s;
    protected ColorRGBA color = new ColorRGBA(0f, 0.5f, 0.5f, 0.3f);
    protected ColorRGBA dmgColor = new ColorRGBA(0.5f, 0.8f, 0.8f, 0.8f);
    protected Body body;
    protected Spatial spatial;
    protected Material material;
    protected float shieldDmgMax = 2;
    protected float shieldDmg = shieldDmgMax;
    protected float shieldDmgRegen = 2;
    protected float shieldRadius = 5;
    
    public ShieldCollider(Shield s) {
        this.s = s;

        Sphere sphere = new Sphere(32, 32, shieldRadius);
        spatial = new Geometry("Sphere", sphere);
        material = new Material(s.getShip().getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);
        material.getAdditionalRenderState().setBlendMode(BlendMode.Alpha);
        spatial.setQueueBucket(Bucket.Transparent);

        spatial.setMaterial(material);
        generatePhysicsBody();

        s.attachChild(this);
        this.attachChild(spatial);
        lockToShield();
    }

    public void update(float tpf) {
        Vector3f bodyPos = new Vector3f(
                (float) body.getWorldPoint(body.getLocalCenter()).x, 0.0f, (float) body.getWorldPoint(body.getLocalCenter()).y);

        spatial.setLocalTranslation(bodyPos);

        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));
        spatial.setLocalRotation(q);

        shieldDmg = fillNotOverLimit(shieldDmg, shieldDmgRegen * tpf, shieldDmgMax);

        ColorRGBA c = new ColorRGBA();
        c.interpolate(dmgColor, color, shieldDmg / shieldDmgMax);
        material.setColor("Ambient", c);
        material.setColor("Diffuse", c);
    }

    public void beginContact(Contact cntct) {
    }

    public void endContact(Contact cntct) {
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
    }

    public void putDamgeToShieldModule(float amount) {
        shieldDmg = 0;
        s.takeDamageOnShield(amount);
    }

    public void die() {
        this.detachChild(spatial);
        s.getShip().getApp().bodiesToRemove.add(body);
    }

    private void generatePhysicsBody() {
        CircleShape circle = new CircleShape();
        circle.m_p.set(0, 0);
        circle.m_radius = shieldRadius;

        //PolygonShape square = new PolygonShape();
        //square.setAsBox(1, 1);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = circle;
        fDef.density = 0.0f;
        fDef.friction = 0.0f;
        fDef.filter.categoryBits = TestShipDesigns.CATEGORY_SHIELD;
        fDef.filter.maskBits = TestShipDesigns.MASK_SHIELD;
        //fDef.restitution = 0.5f;

        // set body                        
        BodyDef bDef = new BodyDef();
        bDef.position.set(s.getBody().getWorldCenter().x, s.getBody().getWorldCenter().y);
        bDef.type = BodyType.DYNAMIC;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.setUserData(this);
        PhysicsWorld.world.setContactListener(this);
    }

    private void lockToShield() {
        WeldJointDef wjDef = new WeldJointDef();
        wjDef.initialize(s.getBody(), this.body, s.getBody().getPosition());
        wjDef.collideConnected = false;
        PhysicsWorld.world.createJoint(wjDef);
    }
}