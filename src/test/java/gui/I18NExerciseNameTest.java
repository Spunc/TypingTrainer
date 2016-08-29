package gui;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import persistence.CreateInitialTestTables;
import persistence.DbAccess;
import persistence.DbBase;

/**
 * <p>Tests to ascertain that every non user-defined exercise name can be internationalized.
 * 
 * <p>That means, for every database entry Exercise.name there must be a key entry in
 * <i>resources.exerciseNameText_??.properties</i> for every supported language.
 * 
 * @author Lasse Osterhagen
 *
 */

public class I18NExerciseNameTest {
	
	private static final String systemLang = Locale.getDefault().getLanguage();
	private static final String exerciseNameSQLSelect =
			"SELECT name FROM exercises WHERE groupId<>" +
			persistence.Constants.userDefExerciseGroup;
	
	private static DbBase db = DbAccess.getInstance();
	private ArrayList<String> missings = new ArrayList<>();
	
	@BeforeClass
	public static void setUp() {
		db.connect(CreateInitialTestTables.PATH2TESTDB);
	}
	
	@Test
	public void testGerman() {
		Locale.setDefault(Locale.GERMAN);
		keysFor("txtBundles.exerciseNameText_de", exerciseNameSQLSelect);
	}
	
	@Test
	public void testEnglish() {
		Locale.setDefault(Locale.ENGLISH);
		keysFor("txtBundles.exerciseNameText", exerciseNameSQLSelect);
	}
	
	private void keysFor(String bundleBaseName, String sqlSelect) {
		ResourceBundle textBundle = ResourceBundle.getBundle(bundleBaseName); 
		db.processResultSet(sqlSelect, r-> {
			while(r.next()) {
				String key = r.getString(1);
				if(!textBundle.containsKey(key))
					missings.add(key);
			}
		});
		assertTrue("Missing name keys: " + missings.toString(), missings.isEmpty());
	}
	
	@AfterClass
	public static void tearDown() {
		db.disconnect();
		// Set back to default system locale
		Locale.setDefault(new Locale(systemLang));
	}

}
