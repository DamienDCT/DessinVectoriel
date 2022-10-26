import java.awt.*;
import java.io.Serializable;

// Classe abstraite pour gérer la forme de manière générale
abstract class Forme implements Serializable{
    // Variable pour serialize les objets
    private static final long serialVersionUID = 7173573940148970246L;
    Color colorStroke, colorBcg;        // Variables qui gère les couleurs d'une forme
    float stroke;       // Épaisseur d'une forme

	int x, y, dx, dy;       // Coordonnées de la forme

    // Constructeur pour le trait
    Forme(Color colorStroke, float stroke, int x, int y, int dx, int dy){
        this.colorStroke = colorStroke;
        this.colorBcg = Color.WHITE;
        this.stroke = stroke;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }
    
    // Constructeur pour les autres formes
    Forme(Color colorStroke, Color colorBcg, float stroke, int x, int y, int dx, int dy){
        this.colorStroke = colorStroke;
        this.colorBcg = colorBcg;
		this.stroke = stroke;

        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
    }

    // Méthode de dessin pour une forme
    abstract void paint(Graphics g);
    
    // Méthodes pour changer la taille de la forme
    abstract void setEndpoint(int x, int y, int dx, int dy);
    abstract void setSelectedEndpoint(int x, int y, int formeX, int formeY, int formeDX, int formeDY);
    
    // Méthode pour savoir si un clic de souris (x,y) est dans une forme
    abstract boolean isSelected(int x, int y);

    // Méthode pour get les coordonnées d'une forme
    abstract int getterX();
    abstract int getterY();
    abstract int getterDX();
    abstract int getterDY();

    // Méthode pour déplacer une forme selon les coordonnées (x,y) de la souris
    abstract void move(int mouseX, int mouseY);

    // Méthode pour modifier les coordonnées d'une forme
    abstract void setX(int x);
    abstract void setY(int y);
    abstract void setDX(int dx);
    abstract void setDY(int dy);
}
