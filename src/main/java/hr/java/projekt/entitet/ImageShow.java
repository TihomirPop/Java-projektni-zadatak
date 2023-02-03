package hr.java.projekt.entitet;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.nio.file.Path;
import java.util.List;
import java.util.Set;

public class ImageShow<T extends Show> {
    private T show;
    private ImageView imageView;

    public ImageShow(T show) {
        this.show = show;
        this.imageView = new ImageView(new Image(Path.of(show.getSlika()).toAbsolutePath().toString(), 0, 150, true, false));
        imageView.setPreserveRatio(true);
        imageView.setFitHeight(64);
    }

    public T getShow() {
        return show;
    }

    public void setShow(T show) {
        this.show = show;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }
}
