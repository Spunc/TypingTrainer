package gui.keyboard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import javax.imageio.ImageIO;

import gui.keyboard.KeyMapper.NotInKeySetException;

@SuppressWarnings("serial")
public class KeyboardImage extends Component {
	
	public enum Color {
		BLACK_WHITE("bw"),
		RED("red"),
		YELLOW("yellow"),
		GREEN("green");
		private String fileName;
		private Color(String fileName) {this.fileName = fileName;}
	}
	
	private static class ColoredChar {
		char c;
		Color col;
		ColoredChar(char c, Color col) {
			this.c = c;
			this.col = col;
		}
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof ColoredChar))
				return false;
			ColoredChar o = (ColoredChar) obj;
			return c == o.c && col == o.col;
		}
	}
	
	private KeyMapper keyMapper;
	private BufferedImage backgroundImage;
	private Map<Color, BufferedImage> colorImages;
	private Dimension imageSize;
	private LinkedList<ColoredChar> chars2color = new LinkedList<>();
	
	public KeyboardImage(String localeID, Color backgroundColor, Color... additionalColors) {
		keyMapper = new KeyMapper(localeID);
		colorImages = new HashMap<>(additionalColors.length);
		try {
			backgroundImage = getImage(backgroundColor, localeID);
			for(Color c : additionalColors) {
				colorImages.put(c, getImage(c, localeID));
			}
		} catch (IOException e) {
			throw new RuntimeException("Missing image file.", e);
		}
		imageSize = new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight());	
	}
	
	public void colorChar(char c, Color color) {
		chars2color.add(new ColoredChar(c, color));
	}
	
	public void removeChar(char c, Color color) {
		chars2color.remove(new ColoredChar(c, color));
	}
	
	private static BufferedImage getImage(Color c, String localeID) throws IOException {
		return ImageIO.read(KeyboardImage.class.getResourceAsStream(
				"im_" + c.fileName + '_' + localeID + ".gif"));
	}
	
	@Override
	public Dimension getPreferredSize() {
		return imageSize;
	}
	
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroundImage, 0, 0, null);
		for(ColoredChar cc : chars2color) {
			Coordinates[] coord;
			try {
				coord = keyMapper.getCoordinatesFor(cc.c);
				for(Coordinates c: coord) {
					g.drawImage(colorImages.get(cc.col), c.x1, c.y1, c.x2, c.y2,
							c.x1, c.y1, c.x2, c.y2, null);
				}
			} catch (NotInKeySetException e) {
				// char does not exist in key set: just do nothing
			}

		}
	}

}
