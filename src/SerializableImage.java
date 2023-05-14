import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.Serializable;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class SerializableImage implements Serializable {
    private BufferedImage image = null;
    public SerializableImage(){
        super();
    }

    public SerializableImage(BufferedImage image1){
        this();
        setImage(image1);
    }

    public BufferedImage getImage(){
        return image;
    }

    public void setImage(BufferedImage image2){
        this.image = image2;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ImageIO.write(getImage(), "png", new MemoryCacheImageOutputStream(out));
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException {
        setImage(ImageIO.read(new MemoryCacheImageInputStream(in)));
    }
}
