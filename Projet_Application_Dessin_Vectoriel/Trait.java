import java.awt.*;

class Trait extends Forme{
    // Constructeur de la forme
    Trait(Color colorStroke,int x, int y, int dx, int dy, float stroke){
        super(colorStroke, stroke,x,y,dx,dy);
    }

    void paint(Graphics g){
        // On récupère le Graphics2D de notre JPanel pour l'épaisseur
	    Graphics2D g2d = (Graphics2D)g;
        // On définit l'épaisseur du trait
        BasicStroke epaisseur = new BasicStroke(this.stroke);
        // On dessine le trait
		g2d.setStroke(epaisseur);
        g.setColor(colorStroke);
        g.drawLine(x,y,dx,dy);
        // On remet le setStroke à 1.0f pour la prochaine forme
        g2d.setStroke(new BasicStroke(1.0f));
    }

    // Méthode pour avoir toujours un trait bien dessiné à l'écran peu importe les coordonnées
    public void setEndpoint(int x, int y, int dx, int dy){
    	this.x = x;
		this.y = y;
	    this.dx = dx;
	    this.dy = dy;
    }

    // Méthode pour sélectionné la forme grâce aux coordonnées de la souris    
    public boolean isSelected(int mouseX, int mouseY){
        if(this.x > this.dx && this.y > this.dy){
            if(mouseX > this.dx && mouseX < this.x && mouseY > this.dy && mouseY < this.y){
                return true;
            }
        } else if(this.x < this.dx && this.y < this.dy){
            if(mouseX > this.x && mouseX < this.dx && mouseY < this.dy && mouseY > this.y){
                return true;
            }
        } else if(this.x > this.dx && this.y < this.dy){
            if(mouseX > this.dx && mouseX < this.x && mouseY > this.y && mouseY < this.dy){
                return true;
            }
        } else if(this.x < this.dx && this.y > this.dy){
            if(mouseX > this.x && mouseX < this.dx && mouseY > this.dy && mouseY < this.y){
                return true;
            }
        }
    	return false;
    }

    // Méthode pour changer la taille quand on redimensionne la forme
    public void setSelectedEndpoint(int x, int y, int formeX, int formeY, int formeDX, int formeDY){
        this.setEndpoint(x, y, formeDX, formeDY);
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
