package com.aura.engine.exemple;

import com.aura.base.Aura;
import com.aura.base.container.AbstractAuraContainer;
import com.aura.base.manager.configuration.ConfigCM;
import com.aura.base.utils.AuraLogger;
import com.aura.client.AuraClient;
import com.aura.engine.AuraDisplayMode;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;
import com.aura.engine.module.EMConsoleUI;
import com.aura.engine.module.EMEntity;
import com.aura.engine.module.EMWorld;
import com.aura.engine.module.shell.ShellFrameMenu;
import com.aura.engine.packet.EPPacketCM;
import com.aura.engine.plugin.EngineClientPlugin;
import com.aura.engine.plugin.EngineServerPlugin;
import com.aura.server.AuraServer;

public class ExempleEngineClient_Solo {

	public static void main(String[] args) {
		final AuraServer selfServer = new AuraServer() {
			@Override
			public void initPlugin(final Aura self) {
				addPlugin(new EngineServerPlugin(this));
			}
		};
		selfServer.getNetworkManager().online();
		
		AbstractAuraContainer<AuraClient> container = new AbstractAuraContainer<AuraClient>() {
			@Override
			public AuraClient init() {
				AuraClient client = new AuraClient(
//						"test", "ress\\log\\client.log"
						) {
					@Override
					public void initPlugin(final Aura self) {
						self.getCfgManager().setConfigStringValue(ConfigCM.SOCKET_LOGIN, "USER_"+Math.round((float) (Math.random()*100)), true);
						EngineClientPlugin pl = new EngineClientPlugin(this) {
							@Override
							public AuraScreen createScreen(AuraClient a) {
								return new AuraScreen(a.getBuildRevision(), a, new AuraDisplayMode(a)) {
									@Override
									public AuraEngine createEngine(final AuraClient client, final AuraScreen screen) {
										return new AuraEngine(client, screen, 50) {
											@Override
											public void init() {
												attachModule(0, new EMWorld(client, screen, this));
												attachModule(0, new EMEntity(client, screen, this));
												attachModule(1, new EMConsoleUI(client, screen, this));
												attachModule(1, new ShellFrameMenu(client, screen, this));
											}
											@Override
											public void play() {
												super.play();
												
												// � mettre en commentaire
												setDebug(true);
												
												// INIT SELF-SCENE -> provoque l'affichage des entit�es (request-server)
												((AuraClient) getMainAura()).getNetworkManager().envoyerPaquet(
													getMainAura().createPacket(EPPacketCM.REQUEST_ENTITY));
												((AuraClient) getMainAura()).getNetworkManager().envoyerPaquet(
													getMainAura().createPacket(EPPacketCM.ENTITY_MOVE));
												
												AuraLogger.config(getMainAura().getSide(), "Welcome ! Tape /help");
											}
										};
									}
								};
							}
						};
						pl.setSelfServer(selfServer);
						addPlugin(pl); 
					}
				};
				AuraLogger.debug(true);
				return client;
			}
		};
		container.getAura().getBuildRevision();
	}
}