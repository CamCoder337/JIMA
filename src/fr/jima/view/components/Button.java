package fr.jima.view.components;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Button extends JButton {
    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private Color borderColor;
    private int radius = 20;


    public Button(String text,Color backgroundColor, Color borderColor, int x, int y){
        this.setColor(backgroundColor);
        this.setText(text);
        this.borderColor = borderColor;
        setContentAreaFilled(false);
        this.setForeground(Color.white);
        this.setBounds(x,y,150,50);
    }

    public Button(String text,Color backgroundColor, Color borderColor, int x, int y, int width, int height){
        this.setColor(backgroundColor);
        this.setText(text);
        this.borderColor = borderColor;
        setContentAreaFilled(false);
        this.setForeground(Color.white);
        this.setBounds(x,y,width,height);
    }

    public void OnCliked(){
        this.setColor(Color.white);
        this.setForeground(Color.decode("#515099"));
        this.borderColor = Color.decode("#515099");
    }

    public void OnRealeased(){
        this.setColor(Color.decode("#515099"));
        this.setForeground(Color.white);
        this.borderColor = Color.white;
    }



    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        setBackground(color);
    }

    public Color getColorOver() {
        return colorOver;
    }

    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
    }

    public Color getColorClick() {
        return colorClick;
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
    }

    public Color getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }



    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        //  Paint Border
        g2.setColor(borderColor);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        g2.setColor(getBackground());
        g2.fillRoundRect(2, 2, getWidth() - 4, getHeight() - 4, radius, radius);
        super.paintComponent(g);
    }
}
