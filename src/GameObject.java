import javafx.scene.Node;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

/**
 * With this object, you can store information of position like x, y, width, height of your object.
 * Also, you can store image of your object and render it.
 */
public class GameObject {
    private Image image;


    double x;
    double y;
    double width;
    double height;

    final double initialY;


    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    // public Node getImage() {
    //     return ;
    // }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    // Define your own properties like x, y, width, height etc.

    public GameObject(Image image, double x, double y, double width, double height) {
        this.image = image;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;

        this.initialY = y;

    }

    public void setLocation(double apses, double ordinate) {
        this.x = apses;
        this.y = ordinate;
    }

    public void changeApses(double apses) { //changes apses by parameter "ordinate"
        this.x += apses;

    }

    public void changeOrdinate(double ordinate) { //changes ordinate by parameter "ordinate"
        this.y += ordinate;

    }


    public double getInitialY() {

        return initialY;
    }


    public void render(GraphicsContext graphicsContext) {
        // Hint: You can take a GraphicsContext object as a parameter and call drawImage() method to draw an image on your game canvas.
        graphicsContext.drawImage(image, x, y, width, height);

    }

}
