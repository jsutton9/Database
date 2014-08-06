package commandTests;

import org.junit.Before;

import commands.BackupCommand;

public class BackupCommandTest extends AbstractCommandTest {
	
	@Override
	@Before
	public void setUp() throws Exception {
		command = new BackupCommand();
		good = new String[] {
				"Backup to foo.123;",
				"bACKup tO bar;",
				"  backup    to   abcd   ;"
		};
		bad = new String[] {
				"backupto a;",
				"backup tob;",
				"backup to ;"
		};
	}

}
