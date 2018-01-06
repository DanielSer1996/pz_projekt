package i5b5.daniel.serszen.pz.view.scenes;

import javafx.scene.Parent;
import javafx.scene.Scene;

public abstract class AbstractCustomScene extends Scene{
    protected double widthDim;
    protected double heightDim;

    protected abstract Parent initRootPane();

    public AbstractCustomScene(Parent root) {
        super(root);
    }

    public double getWidthDim() {
        return widthDim;
    }

    public void setWidthDim(double widthDim) {
        this.widthDim = widthDim;
    }

    public double getHeightDim() {
        return heightDim;
    }

    public void setHeightDim(double heightDim) {
        this.heightDim = heightDim;
    }
}
