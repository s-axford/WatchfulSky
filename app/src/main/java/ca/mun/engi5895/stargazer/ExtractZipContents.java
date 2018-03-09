package ca.mun.engi5895.stargazer;

/*****************************************************************/
/* Copyright 2013 Code Strategies                                */
/* This code may be freely used and distributed in any project.  */
/* However, please do not remove this credit if you publish this */
/* code in paper or electronic form, such as on a web site.      */
/*****************************************************************/

    import android.content.Intent;
    import android.support.v7.app.AppCompatActivity;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.Toast;

    import org.orekit.data.DataProvidersManager;
    import org.orekit.data.DirectoryCrawler;
    import org.orekit.errors.OrekitException;

    import java.io.BufferedInputStream;
    import java.io.File;
        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.util.Enumeration;
        import java.util.zip.ZipEntry;
        import java.util.zip.ZipFile;
        import java.io.BufferedOutputStream;
        import java.io.FileInputStream;
        import java.util.zip.ZipInputStream;
        //import org.apache.commons.io.FileSystemUtils;


public class ExtractZipContents {
/*
    public static void unZip(File archive, File destinationDir) {

        try {
            final int BUFFER_SIZE = 1024;
            BufferedOutputStream dest = null;
            FileInputStream fis = new FileInputStream(archive);
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(fis));
            ZipEntry entry;
            File destFile;
            while ((entry = zis.getNextEntry()) != null) {
                //destFile = FileSystemUtils.combineFileNames(destinationDir, entry.getName());
                if (entry.isDirectory()) {
                    destFile.mkdirs();
                    continue;
                } else {
                    int count;
                    byte data[] = new byte[BUFFER_SIZE];
                    destFile.getParentFile().mkdirs();
                    FileOutputStream fos = new FileOutputStream(destFile);
                    dest = new BufferedOutputStream(fos, BUFFER_SIZE);
                    while ((count = zis.read(data, 0, BUFFER_SIZE)) != -1) {
                        dest.write(data, 0, count);
                    }
                    dest.flush();
                    dest.close();
                    fos.close();
                }
            }
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */
}
