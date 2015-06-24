package netclient;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import netserver.WJSFServer;
import netutil.NetMessages.ClientEnteredMsg;
import netutil.NetMessages.NetMsg;

import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.network.Client;
import com.jme3.network.Message;
import com.jme3.network.MessageListener;
import com.jme3.scene.Geometry;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;

import netclient.otherGraphics.GItem;
import netclient.otherGraphics.GLaserProjectile;
import netclient.otherGraphics.GSpaceStation;
import netserver.modules.Explosion;
import netserver.modules.ShieldHitExplosion;
import netserver.services.ServiceManager;
import netutil.NetMessages;
import netutil.NetMessages.DeleteGraphicObjectMsg;
import netutil.NetMessages.ExplosionParticleMsg;
import netutil.NetMessages.GraphicObjPosAndRotMsg;
import netutil.NetMessages.ModuleActivatedMsg;
import netutil.NetMessages.ModuleDestroyedMsg;
import netutil.NetMessages.NearStationMsg;
import netutil.NetMessages.PlayerNameMsg;
import netutil.NetMessages.PosAndRotMsg;
import netutil.NetMessages.ShieldHitParticleMsg;
import netutil.NetMessages.ShipChangedMsg;
import netutil.NetMessages.SpawnItemMsg;
import netutil.NetMessages.SpawnLaserProjectileMsg;
import netutil.NetMessages.SpawnSpaceStationMsg;
import netutil.NetMessages.SpawnUniverseEntity;
import netutil.NetMessages.ToggleEditorMsg;

public class ClientNetMsgListener implements MessageListener<Client> {
    
    /**********************************
     ********** CLASS FIELDS  *********
     **********************************/
    
    
    private WJSFClient app;
    private ConcurrentLinkedQueue<String> msgQueue;
    
    
    
    /**********************************
     ********** CONSTRUCTORS  *********
     **********************************/
    
    public ClientNetMsgListener(WJSFClient app) {
        this.app = app;
        this.msgQueue = new ConcurrentLinkedQueue<String>();
    }
    
    
    /**********************************
     ************ METHODS  ************
     **********************************/
    
    @SuppressWarnings("unchecked")
    @Override
    public void messageReceived(Client source, Message m) {
        if(m instanceof NetMsg) {
            NetMsg msg = (NetMsg)m;
            msgQueue.add(msg.getMessage());
        } else if (m instanceof ClientEnteredMsg) {
            final ClientEnteredMsg msg = (ClientEnteredMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    ClientShip ship = new ClientShip(msg.getName(), msg.getId(), msg.getShip(), msg.getModulesInBase(), app);
                    
                    if(app.gameRunState.playerShip == null && app.client.getId() == ship.id) {	// new ship is this player
                        app.gameRunState.playerShip = ship;
                        ServiceManager.getEditorManager().setShip(app.gameRunState.playerShip);
                        app.gameRunState.initKeys();
                        
                        app.gameRunState.localRootNode.attachChild(ship.shipRoot);
                    } else {
                        // dont add if already added
                        for(ClientShip s : app.gameRunState.clientShips)
                            if(s.id == ship.id)
                                return null;
                        
                        app.gameRunState.clientShips.add(ship);
                        app.gameRunState.localRootNode.attachChild(ship.shipRoot);
                    }

                    ship.shipRoot.setLocalTranslation(msg.getPos());

                    return null;
                }
            });
        } else if (m instanceof PosAndRotMsg) {
            final PosAndRotMsg msg = (PosAndRotMsg)m;
            this.app.enqueue(new PosAndRotMsgHandler(app, msg));
        } else if(m instanceof NearStationMsg) {
            final NearStationMsg msg = (NearStationMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if(app.gameRunState.playerShip != null && app.gameRunState.playerShip.id == msg.getId()) {
                        app.gameRunState.nearStation = msg.getNearby();
                    }
                    return null;
                }
            });
        } else if (m instanceof ShipChangedMsg) {
            final ShipChangedMsg msg = (ShipChangedMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.playerShip.id == msg.getShipId()) {
                        app.gameRunState.playerShip.setModules(msg.getModules());
                        app.gameRunState.playerShip.setItemsInBase(msg.getModulesInBase());
                    } else {
                        for (ClientShip s : app.gameRunState.clientShips) {
                            if (s.id == msg.getShipId()) {
                                s.setModules(msg.getModules());
                                s.setItemsInBase(msg.getModulesInBase());
                            }
                        }
                    }
                    
                    return null;
                }
            });
        } else if (m instanceof ModuleActivatedMsg) {
            final ModuleActivatedMsg msg = (ModuleActivatedMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.playerShip.id == msg.getShipId()) {
                        if (msg.isActive()) {
                            app.gameRunState.playerShip.activateModule(msg.getXPos(), msg.getYPos());
                        } else {
                            app.gameRunState.playerShip.deactivateModule(msg.getXPos(), msg.getYPos());
                        }
                    } else {
                        for (ClientShip s : app.gameRunState.clientShips) {
                            if (s.id == msg.getShipId()) {
                                if (msg.isActive()) {
                                    s.activateModule(msg.getXPos(), msg.getYPos());
                                } else {
                                    s.deactivateModule(msg.getXPos(), msg.getYPos());
                                }
                            }
                        }
                    }
                    
                    return null;
                }
            });
        } else if (m instanceof ToggleEditorMsg) {
            final ToggleEditorMsg msg = (ToggleEditorMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.playerShip.id == msg.getShipId()) {
                        //app.gameRunState.playerShip.setItemsInBase(msg.getModulesInBase());
                        app.gameRunState.toggleEditor(msg.getModulesInBase());
                    } else {
                        for (ClientShip s : app.gameRunState.clientShips) {
                            if (s.id == msg.getShipId()) {
                                s.setItemsInBase(msg.getModulesInBase());
                            }
                        }
                    }
                    
                    return null;
                }
            });
        } else if (m instanceof SpawnLaserProjectileMsg) {
            final SpawnLaserProjectileMsg msg = (SpawnLaserProjectileMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    GLaserProjectile projectile = new GLaserProjectile(msg.getSpawnPoint(), msg.getDir(), app);
                    app.gameRunState.graphicObjects.put(msg.getId(), projectile);
                    return null;
                }
            });
        } else if (m instanceof GraphicObjPosAndRotMsg) {
            final GraphicObjPosAndRotMsg msg = (GraphicObjPosAndRotMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.graphicObjects.get(msg.getId()) != null) {
                        app.gameRunState.graphicObjects.get(msg.getId()).update(msg);
                    }
                    
                    return null;                    
                }
            });
        } else if (m instanceof DeleteGraphicObjectMsg) {
            final DeleteGraphicObjectMsg msg = (DeleteGraphicObjectMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.graphicObjects.get(msg.getId()) != null) {
                        app.gameRunState.graphicObjects.get(msg.getId()).delete();
                    }
                    return null;
                }
            });
        } else if(m instanceof ModuleDestroyedMsg) {
            final ModuleDestroyedMsg msg = (ModuleDestroyedMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    if (app.gameRunState.playerShip.id == msg.getShipId()) {
                        app.gameRunState.playerShip.setModules(msg.getModules());
                    } else {
                        for (ClientShip s : app.gameRunState.clientShips) {
                            if (s.id == msg.getShipId()) {
                                s.setModules(msg.getModules());
                            }
                        }
                    }
                    return null;
                }
            });
        } else if (m instanceof SpawnItemMsg) {
            final SpawnItemMsg msg = (SpawnItemMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    GItem item = new GItem(msg.getOrientedModule(), msg.getSpawnPoint(), msg.getRot(), app);
                    app.gameRunState.graphicObjects.put(msg.getId(), item); 
                    return null;
                }
            });
        } else if (m instanceof SpawnSpaceStationMsg) {
            final SpawnSpaceStationMsg msg = (SpawnSpaceStationMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    GSpaceStation station = new GSpaceStation(msg.getSpawnPoint(), app);
                    app.gameRunState.graphicObjects.put(msg.getId(), station);
                    return null;
                }
            });
        } else if (m instanceof ExplosionParticleMsg) {
            final ExplosionParticleMsg msg = (ExplosionParticleMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    Explosion exp = new Explosion(
                            app.getAssetManager(), 
                            new Vector3f (msg.getSpawnPoint().x, 0, msg.getSpawnPoint().y), 
                            app.gameRunState.localRootNode
                            );
                    return null;
                }
            });
        } else if (m instanceof ShieldHitParticleMsg) {
            final ShieldHitParticleMsg msg = (ShieldHitParticleMsg)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                    ShieldHitExplosion exp = new ShieldHitExplosion(
                            app.getAssetManager(), 
                            new Vector3f (msg.getSpawnPoint().x, 0, msg.getSpawnPoint().y), 
                            app.gameRunState.localRootNode
                            );
                    return null;
                }
            });

        } else if (m instanceof PlayerNameMsg) {
        	final PlayerNameMsg msg = (PlayerNameMsg)m;
            
        	this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                	if(msg.getId() == app.gameRunState.playerShip.id) {
                		app.gameRunState.playerShip.name = msg.getName();
                		app.gameRunState.playerShip.info.setText(msg.getName());
                	} else {
	                	for (ClientShip s : app.gameRunState.clientShips) {
	                        if (s.id == msg.getId()) {
	                            s.name = msg.getName();
	                            s.info.setText(msg.getName());
	                        }
	                    }
                	}
                	return null;
                }
            });
        } else if (m instanceof SpawnUniverseEntity) {
            final SpawnUniverseEntity msg = (SpawnUniverseEntity)m;
            
            this.app.enqueue(new Callable() {
                public Object call() throws Exception {
                	app.gameRunState.uemanager.addEntity(msg.spawnX, msg.spawnY, msg.size, msg.texture, msg.color, msg.light, msg.ID);

                    return null;
                }
            });
        }
    }
    
    public void update(float tpf) {
        // handle string msgs
        String msg = this.msgQueue.poll();
        if(msg != null) {
            //Logger.getLogger(ClientNetMsgListener.class.getName()).log(Level.INFO, msg);
        }
    }
}