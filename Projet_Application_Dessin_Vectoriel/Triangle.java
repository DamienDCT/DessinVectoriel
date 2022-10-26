import java.awt.*;

class Triangle extends Forme{
    // Constructeur de la forme
    Triangle(Color colorStroke, Color colorBcg, int x, int y, int dx, int dy, float stroke){
        super(colorStroke, colorBcg, stroke,x,y,dx,dy);
    }

    void paint(Graphics g){	
        // Calcul des coordonnées des trois points du triangle
        int[] xPoints = {dx,((x - dx)/2)+dx,x};
		int[] yPoints = {dy,y,dy};
        // On récupère le Graphics2D de notre JPanel pour l'épaisseur
        Graphics2D g2d = (Graphics2D)g;
        // On définit l'épaisseur du trait
        BasicStroke epaisseur = new BasicStroke(this.stroke);
	    g2d.setStroke(epaisseur);
        // On dessine le triangle
        g.setColor(colorBcg);
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		g.setColor(colorStroke);
        g.drawPolygon(xPoints, yPoints, xPoints.length);
        // On remet le setStroke à 1.0f pour la prochaine forme
        g2d.setStroke(new BasicStroke(1.0f));
    }

    // Méthode pour avoir toujours un trait bien dessiné à l'écran peu importe les coordonnées
    public void setEndpoint(int x, int y, int dx, int dy){
        this.x = x;
        this.dx = dx;
        if(y > dy){
            this.y = dy;
            this.dy = y;
        } else {
            this.y = y;
            this.dy = dy;
        }
    }

    // Méthode pour changer la taille quand on redimensionne la forme
    public void setSelectedEndpoint(int x, int y, int formeX, int formeY, int formeDX, int formeDY){
        this.setEndpoint(x,y,formeX+formeDX, formeY+formeDY);
    }
    
    // Méthode pour sélectionné la forme grâce aux coordonnées de la souris 
    public boolean isSelected(int mouseX, int mouseY){
        return mouseX > this.x && mouseX < this.dx && mouseY > this.y && mouseY < this.dy;
    }

    // Méthode pour déplacer une forme
    public void move(int mouseX, int mouseY){
        int decalageX, decalageY;
        decalageX = this.x - mouseX;
        decalageY = this.y - mouseY;
        this.setX(mouseX);
        this.setY(mouseY);
        this.setDX(this.dx - decalageX);
        this.setDY(this.dy - decalageY);
    }
    
    private int getMaxX(){
    	return x + dx;
    }
    
    private int getMaxY(){
    	return y + dy;
    }

    public int getterX(){
        return Math.min(this.x,this.dx);
    }
    public int getterY(){
        return Math.min(this.y,this.dy);
    }

    public int getterDX(){
        return Math.abs(this.x - this.dx);
    }
    public int getterDY(){
        return Math.abs(this.y - this.dy);
    }
    
    public void setX(int x){
        this.x = x;
    }
    public void setY(int y){
        this.y = y;
    }
    public void setDX(int dx){
        this.dx = dx;
    }
    public void setDY(int dy){
        this.dy = dy;
    }
}
