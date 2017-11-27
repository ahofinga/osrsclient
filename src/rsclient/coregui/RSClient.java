/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rsclient.coregui;

import java.awt.Color;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;
import net.miginfocom.swing.MigLayout;
import org.pircbotx.exception.IrcException;
import rsclient.toolbar.MainToolBar;
import rsloader.Loader;
import rsloader.Loader.Game;
import rsreflection.Reflector;

/**
 *
 * @author ben
 */
public class RSClient {

	public static Reflector reflector = null;

	public static void main(String[] args) throws IrcException, IOException {

		Toolkit.getDefaultToolkit().setDynamicLayout(true);
		System.setProperty("sun.awt.noerasebackground", "true");
		JFrame.setDefaultLookAndFeelDecorated(true);
		JDialog.setDefaultLookAndFeelDecorated(true);

		try {
			UIManager.setLookAndFeel("de.muntjak.tinylookandfeel.TinyLookAndFeel");

		} catch (Exception e) {
			e.printStackTrace();
		}

		initUI();
	}

	public static void initUI() {
		JFrame mainwnd = new JFrame("Luna - Open source OSRS Client");
		Image icon = Toolkit.getDefaultToolkit().getImage("resources/lunaicon.png");
		mainwnd.setIconImage(icon);
		
	
		MainToolBar toolbar = new MainToolBar();
    
		mainwnd.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		final JPanel mainpanel = new JPanel(new MigLayout("fill, insets 5"));
		mainpanel.setBackground(Color.black);
		mainpanel.add(toolbar, "dock north, growx, gap 0");
		toolbar.setVisible(true);
                                
      		mainwnd.getContentPane().add(mainpanel);
		final JPanel gamepanel = new JPanel(new MigLayout(" gap 0, ins 0 "));
		gamepanel.setBackground(Color.gray);

		boolean debug = false;

		gamepanel.setVisible(true);
		mainpanel.add(gamepanel, "height 503:503,width 765:765, cell 0 0, growx, growy"); //height 503:503:503,width 765:765:765,
		gamepanel.setVisible(true);

		final JPanel sidepanel = new JPanel(new MigLayout("ins 0"));
		final JPanel bottompanel = new JPanel(new MigLayout("ins 0"));
		sidepanel.setBackground(Color.black);
		bottompanel.setBackground(Color.black);

                sidepanel.add(new SidePane(), "width 250:250, height 503, cell 0 0, spany, push, growy");
		bottompanel.add(new BottomPane(), "height 200:200, width 765, cell 0 0,spanx, push, growx ");
		mainpanel.add(sidepanel, "width 250, height 503, cell 1 0,growy, spany, dock east ");
		mainpanel.add(bottompanel, "height 200, width 765,cell 0 1, growx, dock south");

                final JButton fullScreen = new JButton("Fullscreen");
                fullScreen.addActionListener(new ActionListener(){
                    boolean full = false;
                    @Override
                    public void actionPerformed(ActionEvent e){
                        fullscreen(full, mainpanel, sidepanel, bottompanel, gamepanel);
                        full = !full;
                    }
                });
                toolbar.add(fullScreen);
                
		mainwnd.setVisible(true);

		mainwnd.pack();

		if (debug) {
			gamepanel.add(new JPanel(), "width 765, height 503, cell 0 0");

			//GameSession game = new GameSession();
			//reflector.setHooks(HookImporter.readJSON("/home/ben/hooks.json"));
			//IRCSession mainsesh = new IRCSession();
			//mainsesh.connect("irc.swiftirc.net");
			//mainsesh.joinChannel("#night");
                        
                        
		}
		if (!debug) {
			try {
				LoadingPanel l = new LoadingPanel();
				gamepanel.add(l, "width 765:765, height 503:503, cell 0 0, growx, growy, push");
				final Loader loader = new Loader(Game.OSRS, gamepanel);
				gamepanel.add(loader.applet, "width 765:765, height 503:503, dock center, growx, growy, push");
				gamepanel.remove(l);
				reflector = loader.loader;
                                
			} catch (IllegalArgumentException ex) {
				Logger.getLogger(RSClient.class.getName()).log(Level.SEVERE, null, ex);

			}

		}

	}
        
        
        private static void fullscreen(boolean full, JPanel mainpanel, JPanel sidepanel, JPanel bottompanel, JPanel gamepanel){
            if(!full){
                        mainpanel.remove(sidepanel);
                        mainpanel.remove(bottompanel);
                    } else {
                        mainpanel.add(sidepanel, "width 250, height 503, cell 1 0,growy, spany, dock east ");
                        mainpanel.add(bottompanel, "height 200, width 765,cell 0 1, growx, dock south"); 
                    }
                        gamepanel.revalidate();
                        gamepanel.repaint();
        }
}
