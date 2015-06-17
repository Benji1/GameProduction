/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Modules;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.builder.ElementBuilder;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.screen.Screen;
import gui.ModuleType;
import gui.dragAndDrop.builder.DraggableBuilder;
import items.EncapsulatingItem;
import services.config.ConfigReader;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import mygame.BasicShip;
import mygame.JBox2dNode;
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
import org.jbox2d.dynamics.joints.WeldJointDef;
import services.ServiceManager;

/**
 *
 * @author 1337
 */
public abstract class BasicModule extends JBox2dNode implements ContactListener {

    ConfigReader cr = ServiceManager.getConfigReader();
    protected int maxHealth = cr.getFromMap(cr.getBaseMap("Basic"), "MaxHealth", int.class);
    protected int health = maxHealth;
    protected float dropRateInPercent = cr.getFromMap(cr.getBaseMap("Basic"), "DropRateInPercent", float.class);
    protected float linearDampingFactor = cr.getFromMap(cr.getBaseMap("Thruster"), "LinearDamping", float.class);
    protected BasicShip ship;
    protected String moduleName;
    protected ColorRGBA color = ColorRGBA.Gray;
    protected Body body;
    protected Spatial spatial;
    protected Material material;
    protected ModuleType type;
    protected FacingDirection orientation;
    protected BodyDef bDef = new BodyDef();
    private float oldDamping = 10000f;
    public int group = 0;

    public BasicModule() {
        super();
    }

    public void toggleDamping() {
        float temp = oldDamping;
        oldDamping = body.getLinearDamping();
        body.setLinearDamping(temp);
    }

    private void lockToShip() {
        Point pos = ship.getActualPositionInGrid(this);

        lockTo(ship.getModule(new Point(pos.x + 1, pos.y)));
        lockTo(ship.getModule(new Point(pos.x - 1, pos.y)));
        lockTo(ship.getModule(new Point(pos.x, pos.y + 1)));
        lockTo(ship.getModule(new Point(pos.x, pos.y - 1)));
    }

    private void lockTo(BasicModule lockon) {
        if (lockon != null) {
            WeldJointDef wjDef = new WeldJointDef();
            wjDef.initialize(lockon.body, this.body, lockon.body.getPosition());
            wjDef.collideConnected = false;
            wjDef.frequencyHz = 0;
            PhysicsWorld.world.createJoint(wjDef);
        }
    }

    public String getModuleName() {
        return moduleName;
    }

    public Body getBody() {
        return body;
    }

    public BasicShip getShip() {
        return ship;
    }

    public Spatial getSpatial() {
        return this.spatial;
    }

    public Material getMaterial() {
        return this.material;
    }

    @Override
    public void update(float tpf) {
        super.update(tpf);
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            health = 0;
            destroy();
        }
    }

    public int getHealth() {
        return health;
    }

    public void onPlaced(BasicShip ship) {
        this.ship = ship;

        create3DBody();
        int x = ship.getActualPositionInGrid(this).x * 2;
        int y = ship.getActualPositionInGrid(this).y * 2;
        
        if (ship.cockpitPos != null) {
            x += ship.cockpitPos.x;
            y += ship.cockpitPos.z;
        }
        
        generatePhysicsFixture(x, y, ship.colliderType, ship.collidingWith);
        setPhysicsCenter(body);

        this.attachChild(spatial);
        ship.attachChild(this);

        //lockToShip();
    }

    public void onPlacedBody(BasicShip ship) {
        this.ship = ship;

        create3DBody();
        int x = ship.getActualPositionInGrid(this).x * 2;
        int y = ship.getActualPositionInGrid(this).y * 2;
        
        if (ship.cockpitPos != null) {
            x += ship.cockpitPos.x;
            y += ship.cockpitPos.z;
        }

        generatePhysicsBody(x, y, ship.colliderType, ship.collidingWith);
        setPhysicsCenter(body);

        this.attachChild(spatial);
        ship.attachChild(this);

        //lockToShip();
    }

    protected void create3DBody() {
        Box box = new Box(1, 0.4f, 1);
        spatial = new Geometry("Box", box);
        spatial.setLocalTranslation(ship.getActualPositionInGrid(this).x*2, 0, ship.getActualPositionInGrid(this).y*2);
        material = new Material(ship.getApp().getAssetManager(), "Common/MatDefs/Light/Lighting.j3md");

        material.setBoolean("UseMaterialColors", true);
        material.setColor("Ambient", color);
        material.setColor("Diffuse", color);

        spatial.setMaterial(material);
    }

    public void onMovedToOtherShip(BasicShip s) {
        this.ship = s;
    }

    public void onRemove() {
        ship.removeModuleAt(ship.getActualPositionInGrid(this));
        if (!ship.hasStillModules()) {
            ship.delete();
        }
    }

    public void otherModulePlaced(BasicModule module, Point p) {
    }

    public void otherModuleRemoved(BasicModule module, Point p) {
    }

    private void generatePhysicsFixture(int x, int y, int colliderType, int collidingWith) {
        PolygonShape square = new PolygonShape();        
        square.setAsBox(1, 1, new Vec2(x/2, y/2), 0);
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        fDef.filter.categoryBits = colliderType;
        fDef.filter.maskBits = collidingWith;

        for (int i = 0; i < this.ship.modules.length; i++)
            for (int j = 0; j < this.ship.modules[0].length; j++)
                if (this.ship.modules[i][j] != null) 
                    this.body = this.ship.modules[i][j].body;
                
        body.createFixture(fDef);
        body.setUserData(this);
        body.setLinearDamping(linearDampingFactor*10000);
        PhysicsWorld.world.setContactListener(this);
    }

    private void generatePhysicsBody(int x, int y, int colliderType, int collidingWith) {
        PolygonShape square = new PolygonShape();
        square.setAsBox(1, 1);//, new Vec2(x, y), 0);

        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        fDef.filter.categoryBits = colliderType;
        fDef.filter.maskBits = collidingWith;

//        BodyDef bDef = new BodyDef();
        bDef.position.set(x, y);
        bDef.type = BodyType.DYNAMIC;

        body = PhysicsWorld.world.createBody(bDef);
        body.createFixture(fDef);
        body.setUserData(this);
        body.setLinearDamping(linearDampingFactor);
        PhysicsWorld.world.setContactListener(this);
    }

    public void destroy() {
        onRemove();

        Explosion exp = new Explosion(
                ship.getApp().getAssetManager(),
                new Vector3f(this.body.getPosition().x, 0, this.body.getPosition().y),
                ship.getApp().getRootNode());

        if (shouldSpawnItem()) {
            spawnItem();
        }
        this.detachAllChildren();
        ship.getApp().bodiesToRemove.add(body);
        ship.sperateInNewShips();
    }

    public boolean shouldSpawnItem() {
        Random rn = new Random();
        return rn.nextFloat() <= dropRateInPercent / 100;
    }

    public void spawnItem() {
        float angleRad = body.getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));

        ArrayList<Spatial> saveSpatials = new ArrayList<Spatial>();
        for (Spatial s : children) {
            saveSpatials.add(s.clone());
        }

        ship.getApp().itemsToCreate.add(new EncapsulatingItem(type, saveSpatials, body.getPosition(), q, ship.getApp()));
    }

    public void destroyWithoutSeperation() {
        onRemove();
        this.detachAllChildren();
        ship.getApp().bodiesToRemove.add(body);
    }

    // HELPER METHOD MAYBE SOMEWHERE ELSE WOULD BE A BETTER PLACE
    public static float fillNotOverLimit(float actualValue, float increase, float limit) {
        if (actualValue + increase > limit) {
            return limit;
        } else {
            return actualValue + increase;
        }
    }

    public void beginContact(Contact cntct) {
    }

    public void endContact(Contact cntct) {
    }

    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    public void postSolve(Contact cntct, ContactImpulse ci) {
    }

    public void buildGuiElement(int idCounter, Nifty nifty, Screen screen, Element targetParent) {
        String newId = "part-panel-" + type.getValue() + "-" + idCounter;

        Element element = new DraggableBuilder(newId) {
            {
                visibleToMouse(true);
                childLayout(ElementBuilder.ChildLayoutType.Center);
                panel(new PanelBuilder() {
                    {
                        backgroundImage("Interface/Images/Parts.png");
                        width("100%");
                        height("100%");
                        imageMode("sprite:100,100," + (type.getValue() * 4 + orientation.getSpriteValue()));
                    }
                });
            }
        }.build(nifty, screen, targetParent);

        element.setParent(targetParent);
    }

    public ModuleType getType() {
        return type;
    }

    public FacingDirection getOrientation() {
        return orientation;
    }
}
