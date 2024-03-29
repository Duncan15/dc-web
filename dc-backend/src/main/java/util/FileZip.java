package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.util.zip.Adler32;
import java.util.zip.CRC32;
import java.util.zip.CheckedOutputStream;

//使用org.apache.tools.zip这个就不会中文乱码
//import org.apache.tools.zip.ZipEntry;
//import org.apache.tools.zip.ZipOutputStream;

//使用java.util.zip原生ZipOutputStream与ZipEntry会中文乱码

import java.util.zip.ZipOutputStream;
import java.util.zip.ZipEntry;

/**
 * 作者:Code菜鸟
 * 博客:http://blog.csdn.net/qq969422014/article/category/2944339
 * **/
public class FileZip
{


    private static final int BUFFER_SIZE = 2 * 1024;

    public static void main(String args[]) throws Exception
    {

//		File lp1 = new File("D:\\picture");
//		OutputStream is = new FileOutputStream("D:\\ziptest\\Test7.zip");
//		ZipOutputStream lp = new ZipOutputStream(is);
//		Test.compress(lp1, lp, "", true);

//fileZip("D:\\picture","D:\\ziptest\\Test111.zip","file",true);
        deleteFile("D:/picture/128/index/128.zip");
    }






    /**
     * 递归压缩方法
     *
     * @param sourceFile       源文件
     * @param zos              zip输出流
     * @param name             压缩后的名称
     * @param KeepDirStructure 是否保留原来的目录结构, true:保留目录结构;
     *                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     * @throws Exception
     *
     */

    private static void compress(File sourceFile, ZipOutputStream zos, String name, boolean KeepDirStructure)
            throws Exception {

        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            // 向zip输出流中添加一个zip实体，构造器中name为zip实体的文件的名字
            zos.putNextEntry(new ZipEntry(name));
            // copy文件到zip输出流中
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                // 需要保留原来的文件结构时,需要对空文件夹进行处理
                if (KeepDirStructure) {
                    // 空文件夹的处理
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    // 没有文件，不需要文件的copy
                    zos.closeEntry();
                }
            } else {
                for (File file : listFiles) {
                    // 判断是否需要保留原来的文件结构
                    if (KeepDirStructure) {
                        // 注意：file.getName()前面需要带上父文件夹的名字加一斜杠,
                        // 不然最后压缩包中就不能保留原来的文件结构,即：所有文件都跑到压缩包根目录下了
                        compress(file, zos, name + "/" + file.getName(), KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }
                }
            }
        }
    }



    public static boolean fileZip(String sourceFilePath,String afterZipPath,String name,boolean KeepDirStructure) {
        try{
            File sourceFile = new File(sourceFilePath);
            OutputStream opt = new FileOutputStream(afterZipPath);
            ZipOutputStream zos = new ZipOutputStream(opt);
            compress(sourceFile,zos,name,KeepDirStructure);
            zos.close();
            return true;

        }catch (Exception e) {
            return false;

        }

    }

    /**
     * 删除单个文件
     * @param   sPath    被删除文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            System.out.println("将要删除");
            file.delete();
            flag = true;
        }
        return flag;
    }



}