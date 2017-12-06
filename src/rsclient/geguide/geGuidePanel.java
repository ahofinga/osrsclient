/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Adam
 */
package rsclient.geguide;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.NumberFormat;
import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import net.miginfocom.swing.MigLayout;
import org.pushingpixels.trident.Timeline;


public class geGuidePanel extends JPanel {
    
    JTextField itemInputField;
    JLabel itemLabel;
    JLabel priceLabel;
    JLabel lowAlchLabel;
    JLabel highAlchLabel;
    JLabel updateLabel;
    JLabel imageLabel;
    BufferedImage image;
    ImageIcon imageIcon;
    
    JButton searchButton;
    Timeline rolloverTimeline;
    boolean isValidItem;
    static String baseURL = "http://oldschoolrunescape.wikia.com/wiki/" ;
    URL itemURL;
    
    public geGuidePanel()  {
        image = null;
        imageIcon = null;
        imageLabel = new JLabel();
        setLayout(new MigLayout("ins 5, center"));
        setBackground(Color.black);
        Border loweredbevel = BorderFactory.createEtchedBorder(EtchedBorder.RAISED);
        Font f = new Font(new JLabel().getFont().getFontName(), Font.BOLD, new JLabel().getFont().getSize() + 2);
        ImageIcon searchicon = new ImageIcon(getClass().getClassLoader().getResource("resources/searchicon20.png"));

        itemInputField = new JTextField();
        itemLabel = new JLabel("ITEM:");
        searchButton = new JButton();
       
        priceLabel = new JLabel("PRICE:");
        priceLabel.setFont(new Font(itemLabel.getFont().getFontName(), Font.BOLD, itemLabel.getFont().getSize()));
        priceLabel.setForeground(Color.white);
        
        lowAlchLabel = new JLabel("Low Alchemy:");
        lowAlchLabel.setFont(new Font(itemLabel.getFont().getFontName(), Font.BOLD, itemLabel.getFont().getSize()));
        lowAlchLabel.setForeground(Color.white);
        
        highAlchLabel = new JLabel("High Alchemy:");
        highAlchLabel.setFont(new Font(itemLabel.getFont().getFontName(), Font.BOLD, itemLabel.getFont().getSize()));
        highAlchLabel.setForeground(Color.white);
        
        updateLabel = new JLabel("Last updated:");
        updateLabel.setFont(new Font(itemLabel.getFont().getFontName(), Font.BOLD, itemLabel.getFont().getSize()));
        updateLabel.setForeground(Color.white);
        
        itemInputField.setBorder(loweredbevel);
        itemInputField.setBackground(new Color(101, 101, 101));
        
        itemLabel.setForeground(Color.white);
        itemLabel.setFont(new Font(itemLabel.getFont().getFontName(), Font.BOLD, itemLabel.getFont().getSize()));

        searchButton.setIcon(new javax.swing.ImageIcon(getClass().getClassLoader().getResource("resources/searchiconsquare3.png")));
        searchButton.setBorderPainted(false);
        searchButton.setFocusPainted(false);
        searchButton.setContentAreaFilled(false);
      
        add(itemLabel, "cell 0 0, gap 0, align left");
        add(searchButton, "cell 2 0,align right ");
        add(itemInputField, "width 60%, cell 1 0,align left,");
      
        add(priceLabel, "newline, spanx");
        add(imageLabel, "cell 2 1, height :50, align right, spany 3");
        add(lowAlchLabel, "newline, spanx");
        add(highAlchLabel, "newline, spanx");
        add(updateLabel, "newline, spanx");
        
        searchButton.addActionListener(new ItemSearchListener());
        itemInputField.addActionListener(new ItemSearchListener());

        isValidItem = false;
        setupAnimation();
      
    }
    
        private void setupAnimation() {
        rolloverTimeline = new Timeline(itemInputField);
        rolloverTimeline.addPropertyToInterpolate("background", itemInputField.getBackground(), new Color(121, 121, 121));
        rolloverTimeline.setDuration(150);
        
        itemInputField.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                rolloverTimeline.play();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                rolloverTimeline.playReverse();
            }
        });

    }
        
    private void loadInfo(String item) throws IOException {
        BufferedReader in = null;
        try{
        itemURL = new URL(baseURL + "Exchange:" + item);
        URLConnection yc = itemURL.openConnection();
        in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] tokens = inputLine.split("[>*<*]");
            for(int i = 0; i < tokens.length; i++) {
                if(tokens[i].contains("Price:")){
                   priceLabel.setText("PRICE: " + tokens[i+4]);
                   
                }
                if(tokens[i].contains("Low Alchemy:")){
                    if(tokens[i+2].replaceAll(" ", "").isEmpty()){
                        lowAlchLabel.setText("Low Alchemy: " + tokens[i + 4]);
                    } else {
                        lowAlchLabel.setText("Low Alchemy: " + tokens[i + 2]);
                    }
                }
                if(tokens[i].contains("High Alchemy:")){
                    if(tokens[i+2].replaceAll(" ", "").isEmpty()){
                        highAlchLabel.setText("High Alchemy: " + tokens[i + 4]);
                    } else {
                        highAlchLabel.setText("High Alchemy: " + tokens[i + 2]);
                    }
                }
                if(tokens[i].contains("Last updated:")){
                    updateLabel.setText("Last updated: " + tokens[i + 14]);
                }
            }
        }        
        
        isValidItem = true;
        }
        catch(Exception e){
            isValidItem = false;
        } finally {
            if (in != null) {
                in.close();
            }
        }
        if(isValidItem) {
            loadimage(item);
        }
    }
    
    private void loadimage(String item) throws IOException{
        BufferedReader in = null;
        String urlText = "https://ewebdesign.com/wp-content/uploads/2015/10/7-Flickering-404-Error-Page-by-Spiderone.jpg"; //404 not found image
   
        try{
        itemURL = new URL(baseURL + item);
        URLConnection yc = itemURL.openConnection();
        in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            String[] tokens = inputLine.split("\"");
            for(int i = 0; i < tokens.length; i++) {
                if(tokens[i].contains("/" + item + ".png")){ 
                   urlText = tokens[i];
                   break;
                }
            }
        }        
        
        isValidItem = true;
        }
        catch(Exception e){}
        finally {
            if (in != null) {
                in.close();
            }
        }
             
        try{
        URL imageURL = new URL(urlText); 
        imageIcon = new ImageIcon(imageURL);
        Image oldimg = imageIcon.getImage(); 
        Image newimg = oldimg.getScaledInstance(50, 50,  java.awt.Image.SCALE_FAST);
        imageIcon = new ImageIcon(newimg);  
        } catch(Exception e) {e.printStackTrace();}
        imageLabel.setIcon(imageIcon);
    }
    
    private class ItemSearchListener implements ActionListener {
                @Override
                public void actionPerformed(ActionEvent ae) {
                    String formatter = itemInputField.getText().replace(" ", "_").toLowerCase(); //format request
                    final String itemName = formatter.substring(0, 1).toUpperCase() + formatter.substring(1);
                    Runnable r1;
                    r1 = new Runnable() {
                        @Override
                        public void run() {
                            try {
                                itemLabel.setText("loading..");
                                loadInfo(itemName);
                            } catch (IOException e) {
                                //e.printStackTrace();
                                isValidItem = false;
                            } finally {
                                if(isValidItem){
                                    itemLabel.setText("ITEM:");
                                } else {
                                    itemLabel.setText("INVALID:");
                                    priceLabel.setText("PRICE:");
                                    lowAlchLabel.setText("Low Alchemy:");
                                    highAlchLabel.setText("High Alchemy:");
                                    updateLabel.setText("Last updated:");
                                }
                            }
                        }
                    };
                    new Thread(r1).start();
                } 
    
    
    }
    
}
