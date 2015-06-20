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
        oldDamping = ship.getBody().getLinearDamping();
        ship.getBody().setLinearDamping(temp);
    }

    public String getModuleName() {
        return moduleName;
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
        setPhysicsCenter(ship.getBody());

        this.attachChild(spatial);
        ship.attachChild(this);
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
        square.setAsBox(1, 1);//, new Vec2(x, y), 0);
        
        FixtureDef fDef = new FixtureDef();
        fDef.shape = square;
        fDef.density = 1.0f;
        fDef.friction = 0.6f;
        fDef.filter.categoryBits = colliderType;
        fDef.filter.maskBits = collidingWith;
                
        ship.getBody().createFixture(fDef);
        ship.getBody().setUserData(this);
        ship.getBody().setLinearDamping(linearDampingFactor);
        PhysicsWorld.world.setContactListener(this);
    }

    public void destroy() {
        onRemove();

        Explosion exp = new Explosion(
                ship.getApp().getAssetManager(),
                new Vector3f(this.ship.getBody().getPosition().x, 0, this.ship.getBody().getPosition().y),
                ship.getApp().getRootNode());

        if (shouldSpawnItem()) {
            spawnItem();
        }
        this.detachAllChildren();
        ship.getApp().bodiesToRemove.add(ship.getBody());
        ship.sperateInNewShips();
    }

    public boolean shouldSpawnItem() {
        Random rn = new Random();
        return rn.nextFloat() <= dropRateInPercent / 100;
    }

    public void spawnItem() {
        float angleRad = ship.getBody().getAngle();
        Quaternion q = new Quaternion();
        q.fromAngleAxis(-angleRad, new Vector3f(0f, 1f, 0f));

        ArrayList<Spatial> saveSpatials = new ArrayList<Spatial>();
        for (Spatial s : children) {
            saveSpatials.add(s.clone());
        }

        ship.getApp().itemsToCreate.add(new EncapsulatingItem(type, saveSpatials, ship.getBody().getPosition(), q, ship.getApp()));
    }

    public void destroyWithoutSeperation() {
        onRemove();
        this.detachAllChildren();
        ship.getApp().bodiesToRemove.add(ship.getBody());
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
