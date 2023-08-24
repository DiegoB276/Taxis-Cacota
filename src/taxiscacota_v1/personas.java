/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package taxiscacota_v1;

import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;

/**
 *
 * @author Francela Moreno
 */
public class personas {

    private int x;
    private int y;
    private int id_ubi_usuario;
    static final int d = 20;
    Image imagen;

    public personas(int x, int y, int id) {
        this.x = x;
        this.y = y;
        imagen = new ImageIcon("C:\\Users\\diego\\Documents\\Proyecto-Taxis-Cacota\\src\\img\\persona.png").getImage();
        this.id_ubi_usuario = id;
    }

    public void pintarpersonas(Graphics g) {
        Graphics2D graphics = (Graphics2D) g;
        graphics.drawImage(imagen, (this.x - getD() / 2) - 15, (this.y - getD() / 2) - 40, null);
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public static int getD() {
        return d;
    }

    public int getId_Ubi_Usuario() {
        return id_ubi_usuario;
    }
}
