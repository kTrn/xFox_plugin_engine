package com.aura.engine.module.shell;

import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;

import com.aura.client.AuraClient;
import com.aura.engine.AuraEngine;
import com.aura.engine.AuraScreen;

public abstract class AbstractShellFrame extends AbstractShell {
	public AbstractShellFrame(AuraClient aura, AuraScreen screen, AuraEngine scene, TypeShell type, int keyAction) {
		super(aura, screen, scene, type, keyAction);
	}

	@Override
	public void doUpdate() {
		if (!isVisible() && !isPinned()) 
			return;
		
		updateFrame();
		updateObjects();
		implDoUpdate();		
	}
	
	private void updateFrame() {
		// � impl.
	}
	
	@Override
	public void doDraw(Graphics2D g) {
		if (!isVisible() && !isPinned()) 
			return;
		
		drawFrame(g);
		drawObjects(g);
		implDoDraw(g);
	}
	
	public void drawFrame(Graphics2D g) {
		Composite temp = g.getComposite();
		g.setColor(Color.BLACK);
		getScene().setComposite(g, .75f);
		g.fillRect(0, 0, 
			getScene().getScreenInfo().frameWidth, 
			getScene().getScreenInfo().frameHeight);
		getScene().setComposite(g, temp);
	}
	
	@Override
	public int getX() {
		return 0;
	}
	@Override
	public int getY() {
		return 0;
	}
	@Override
	public int getWidth() {
		return getScene().getScreenInfo().frameWidth;
	}
	@Override
	public int getHeight() {
		return getScene().getScreenInfo().frameHeight;
	}
	@Override
	public int getHeaderYDecal() {
		return 0;
	}
}