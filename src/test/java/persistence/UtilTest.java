package persistence;

import static org.junit.Assert.*;
import static org.hamcrest.core.IsInstanceOf.instanceOf;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTest {
	
	private static final int TRANSFORMVAL = 9;
	private static DbBase db = DbAccess.getInstance();
	
	@BeforeClass
	public static void setUp() {
		db.connect(CreateInitialTestTables.PATH2TESTDB);
	}
	
	@AfterClass
	public static void tearDown() {
		db.disconnect();
	}

	@Test
	public void testFillTableFromSelectString() {
		List<Object[]> rows = Util.fillTableFromSelect("SELECT * from test");
		// correctly retrieving 1st row, 1st column
		assertThat(rows.get(0)[0], instanceOf(Integer.class));
		// correctly retrieving 1st row, 2nd column
		assertEquals(rows.get(0)[1], CreateInitialTestTables.ROW1_STR);
		// correctly retrieving 2nd row, 3rd column
		assertEquals(rows.get(1)[2], CreateInitialTestTables.ROW2_INT);
	}

	@Test
	public void testFillTableFromSelectStringObjectTransformerArray() {
		Util.ObjectTransformer[] obTr = new persistence.Util.ObjectTransformer[3];
		obTr[2] = v -> ((int) v) + TRANSFORMVAL;
		List<Object[]> rows = Util.fillTableFromSelect("SELECT * from test", obTr);
		// No transformation to second column
		assertEquals(rows.get(0)[1], CreateInitialTestTables.ROW1_STR);
		// Transformation to third column
		assertEquals(rows.get(0)[2], CreateInitialTestTables.ROW1_INT + TRANSFORMVAL);
		// Transformation to third column must apply to all rows (test second row)
		assertEquals(rows.get(1)[2], CreateInitialTestTables.ROW2_INT + TRANSFORMVAL);
	}

}
