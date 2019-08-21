package api.deliver;

import format.RespWrapper;
import util.DBUtil;
import util.FileZip;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


@WebServlet(name = "DownloadServlet",urlPatterns = {"/api/datacrawling/download"})
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub

        String webId = request.getParameter("id");
        int id = Integer.valueOf(webId);
        String []params = {"workFile"};
        String workDir = DBUtil.select("website", params,id)[0][0];
        Path parDir = Paths.get(workDir, webId + "", "index");
        Path zipPath = parDir.resolve(webId + ".zip");
        Path sourcePath = parDir.resolve("fulltext");
        String filename = webId + ".zip";

        File f = zipPath.toFile();
        if (!f.exists()) {
            System.out.println("开始压缩");
            FileZip.fileZip(sourcePath.toString(), zipPath.toString(), "lucene",true);
            System.out.println("压缩完成");
        }

        //设置文件MIME类型
        response.setContentType(getServletContext().getMimeType(filename));
        //设置Content-Disposition
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        //读取目标文件，通过response将目标文件写到客户端


        //读取文件
        InputStream in = new FileInputStream(zipPath.toString());
        OutputStream out = response.getOutputStream();

        //写文件
        int b;
        try {
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            in.close();
            out.close();
        } catch (IOException ex) {
            //
        }

    }

}
