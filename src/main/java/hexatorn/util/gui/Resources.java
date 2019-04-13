package hexatorn.util.gui;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class Resources {

    public static URL getResources(String path) throws MalformedURLException {

        File file = new File("");
        String out = "";
        out += "file:/";
        out += file.getAbsolutePath();
        out += "/";
        out += path;

        out = out.replaceAll(" " , "%20");
        out = out.replaceAll("\\\\" , "/");
        return new URL(out);
    }

}
