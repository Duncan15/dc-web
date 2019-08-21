package util;

import java.io.File;

public class FileOp {
    /**
     * 删除文件夹
     * @param dir
     */
    public static void delete(File dir) {
        if (!dir.exists()) return;
        if (dir.isDirectory()) {
            for (File subDir : dir.listFiles()) {
                delete(subDir);
            }
        }
        dir.delete();
    }

}
