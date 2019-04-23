package api.deliver;

import format.RespWrapper;
import util.DBUtil;
import util.FileZip;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


@WebServlet(name = "DownloadServlet",urlPatterns = {"/api/datacrawling/download"})
public class DownloadServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
// TODO Auto-generated method stub

        String webId = request.getParameter("id");
        String partFile = request.getParameter("partFile");
        if(partFile == null){
            partFile = "fulltext";
        }
        int id = Integer.valueOf(webId);
        String []params = {"workFile"};
        String workFile = DBUtil.select("website", params,id)[0][0]+"/"+webId+"/index/"+partFile;
        System.out.println("workfile是:   "+workFile);
        String afterZipPath = DBUtil.select("website", params,id)[0][0]+"/"+webId+"/index/"+webId+".zip";
        System.out.println("afterZipPath为：  "+afterZipPath);
        boolean ifZip =  FileZip.fileZip(workFile,afterZipPath,webId,true);
        if(ifZip) {
            System.out.println("压缩成功");
            String filename = webId + ".zip";


            //设置文件MIME类型
            response.setContentType(getServletContext().getMimeType(filename));
            //设置Content-Disposition
            response.setHeader("Content-Disposition", "attachment;filename=" + filename);
            //读取目标文件，通过response将目标文件写到客户端


            //读取文件
            InputStream in = new FileInputStream(afterZipPath);
            OutputStream out = response.getOutputStream();

            //写文件
            int b;
            while ((b = in.read()) != -1) {
                out.write(b);
            }

            in.close();
            out.close();

            try{
                if(FileZip.deleteFile(afterZipPath)){

//                    FileZip.deleteFile(afterZipPath);

                    System.out.println("删除成功");

                }else {
                    System.out.println("删除失败");
                };

            }catch (Exception e){
                System.out.println("删除程序错误");
            }

        }
        else {
            System.out.println("压缩失败 ");
            response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,"文件夹压缩失败"));
        }



    }




}
