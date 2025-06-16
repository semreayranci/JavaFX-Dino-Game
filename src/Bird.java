import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Bird extends GameObject{



   // Image image1;
    Image image2;


    public Bird(Image image1,Image image2, double x, double y, double width, double height) {

        super( image1, x, y, width, height);

        this.image2 = image2;

    }




    public void renderBird(GraphicsContext graphicsContext, Image imageNo) {
       // super.render(graphicsContext);

        graphicsContext.drawImage(imageNo, x, y,width,height);
    }
}
