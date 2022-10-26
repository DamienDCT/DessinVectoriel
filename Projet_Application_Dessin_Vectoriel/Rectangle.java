import java.awt.*;

class Rectangle extends Forme{
    // Constructeur de la forme
    Rectangle(Color colorStroke, Color colorBcg,int x, int y, int dx, int dy, float stroke){
        super(colorStroke, colorBcg, stroke,x,y,dx,dy);
    }

    void paint(Graphics g){
        // On récupère le Graphics2D de notre JPanel pour l'épaisseur
    	Graphics2D g2d = (Graphics2D)g;
        // On définit l'épaisseur du trait
        BasicStroke epaisseur = new BasicStroke(this.stroke);
	    g2d.setStroke(epaisseur);
        // On dessine le rectangle de fond et par dessus le rectangle de contour
        g.setColor(colorBcg);
        g.fillRect(x,y,dx,dy);
        g.setColor(colorStroke);
        g.drawRect(x,y,dx,dy);
        // On remet le setStroke à 1.0f pour la prochaine forme
        g2d.setStroke(new BasicStroke(1.0f));
    }

    // Méthode pour avoir toujours un rectangle bien dessiné à l'écran peu importe les coordonnées
    public void setEndpoint(int x, int y, int dx, int dy){
        this.x = Math.min(x,dx);
        this.y = Math.min(y,dy);
        this.dx = Math.abs(x - dx);
        this.dy = Math.abs(y - dy);
    }
    
    // Méthode pour sélectionné la forme grâce aux coordonnées de la souris
    public boolean isSelected(int mouseX, int mouseY){
		int bordX = this.getMaxX();
		int bordY = this.getMaxY();
    	if(mouseX > this.x && mouseX < bordX){
    		if(mouseY > this.y && mouseY < bordY){
    			return true;
    		}
    	}
    	return false;
    }

    // Méthode pour changer la taille quand on redimensionne la forme
    public void setSelectedEndpoint(int x, int y, int formeX, int formeY, int formeDX, int formeDY){
        this.setEndpoint(x,y,formeX+formeDX, formeY+formeDY);
    }
    
    // Méthode pour déplacer une forme
    public void move(int mouseX, int mouseY){
        this.setX(mouseX);
        this.setY(mouseY);
    }

    private int getMaxX(){
    	return x + dx;
    }
    
    private int getMaxY(){
    	return y + dy;
    }

    public int getterX(){
        return this.x;
    }
    public int getterY(){
        return this.y;
    }

    public int getterDX(){
        return this.dx;
    }
    public int getterDY(){
        return this.dy;
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
