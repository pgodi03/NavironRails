package navironrails.fxext;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import navironrails.civilsystems.railwaysystem.Terminal;

public class TerminalRep extends RailRep implements GFXRep {
    static final File TERMINAL_IMG_FILE  = new File("src/resources/icons/terminalImg.png");
    static Image img = new Image(TERMINAL_IMG_FILE.toURI().toString());
    
    CouplingRep coupling = null;
    
    Terminal terminal;
    
    public TerminalRep(double x, double y){
        super(x, y);
        
        this.setFill(new ImagePattern(TerminalRep.img));
        terminal = new Terminal();
    }
}
