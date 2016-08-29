package gui;

import static org.junit.Assert.*;

import java.util.Locale;

import static gui.Util.getGUIText;

import org.junit.AfterClass;
import org.junit.Test;

public class I18NTest {
	
	private static final String TESTKEY = "TestI18n";
	private static final String systemLang = Locale.getDefault().getLanguage();
	
	@Test
	public void testGermanLocale() {
		Locale.setDefault(Locale.GERMAN);
		assertEquals(getGUIText(TESTKEY), "Deutsch");
	}
	
	@Test
	public void testEnglishLocale() {
		Locale.setDefault(Locale.ENGLISH);
		assertEquals(getGUIText(TESTKEY), "English");
	}
	
	@Test
	public void testUnimplementedLocale() {
		Locale.setDefault(Locale.CHINESE);
		assertEquals(getGUIText(TESTKEY), "English");
	}
	
	@AfterClass
	public static void tearDown() {
		// Set back to default system locale
		Locale.setDefault(new Locale(systemLang));
	}

}
