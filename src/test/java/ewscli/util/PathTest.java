package ewscli.util;

import ewscli.DefaultParams;
import org.junit.Test;

import static org.junit.Assert.*;

public class PathTest {


    @Test
    public void defaultConfigPath() {
        System.out.println(DefaultParams.defaultConfigPath());
        assertNotNull(DefaultParams.defaultConfigPath());
    }

    @Test
    public void regulatePath() {
        assertEquals("A/B/C/D", Path.regulatePath("/A/B/C/D/", "/"));
        assertEquals("A/B/C/D", Path.regulatePath("A/B/C/D/"));
        assertEquals("\uD83C\uDDEE\uD83C\uDDEA/B/C/D", Path.regulatePath("\uD83C\uDDEE\uD83C\uDDEA/B/C/D/", "/"));
        assertEquals("hogehoge/fuga/fuga/aaa", Path.regulatePath("///hogehoge/fuga//fuga/aaa//", "/"));
    }

    @Test
    public void regulatePathRootOnly () {
        assertEquals("", Path.regulatePath("", "/"));
        assertEquals("", Path.regulatePath("/", "/"));
        assertEquals("", Path.regulatePath("//", "/"));
        assertEquals("", Path.regulatePath("/////", "/"));
    }
}