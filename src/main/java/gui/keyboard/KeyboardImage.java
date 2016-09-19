package gui.keyboard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

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
	
	private KeyMapper keyMapper;
	private Color[] additionalColors;
	private String[] chars2Color;
	private BufferedImage backgroundImage;
	private BufferedImage[] colorImages;
	private Dimension imageSize;
	
	public KeyboardImage(String localeID, Color backgroundColor, Color... additionalColors) {
		keyMapper = new KeyMapper(localeID);
		this.additionalColors = additionalColors.clone();
		chars2Color = new String[additionalColors.length];
		colorImages = new BufferedImage[additionalColors.length];
		try {
			backgroundImage = getImage(backgroundColor, localeID);
			for(int i=0; i<colorImages.length; ++i) {
				chars2Color[i] = "";
				colorImages[i] = getImage(additionalColors[i], localeID);
			}
		} catch (IOException e) {
			throw new RuntimeException("Missing image file.", e);
		}
		imageSize = new Dimension(backgroundImage.getWidth(), backgroundImage.getHeight());	
	}
	
	public void colorChar(char c, Color color) {
		int colIndex = findColorIndex(color);
		if(chars2Color[colIndex].indexOf(c) > 0)
			return;
		else
			chars2Color[colIndex] = chars2Color[colIndex] + c;
	}
	
	public void removeChar(char c, Color color) {
		int colIndex = findColorIndex(color);
		chars2Color[colIndex] = chars2Color[colIndex].replace(Character.toString(c), "");
	}
	
	private int findColorIndex(Color c) {
		for(int i=0; i<additionalColors.length; ++i) {
			if(additionalColors[i].equals(c)) {
				return i;
			}
		}
		throw new RuntimeException("Specified color not in initialized set.");
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
		for(int colorIndex=0; colorIndex<colorImages.length; ++colorIndex) {
			for(int charIndex=0; charIndex<chars2Color[colorIndex].length(); ++charIndex) {
				try {
					Coordinates[] coord = keyMapper.getCoordinatesFor(chars2Color[colorIndex].charAt(charIndex));
					for(Coordinates c : coord) {
						g.drawImage(colorImages[colorIndex], c.x1, c.y1, c.x2, c.y2,
								c.x1, c.y1, c.x2, c.y2, null);
					}
				} catch (NotInKeySetException e) {
					// Just do not try to color a char that is not in the key set.
				}
			}
		}
	}

}
