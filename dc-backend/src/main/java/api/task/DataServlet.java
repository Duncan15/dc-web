package api.task;

import format.RespWrapper;
import util.DBUtil;
import util.RequestParser;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class webs
 */
@WebServlet(name = "DataServlet",urlPatterns = {"/api/datacrawling/data/*"})
public class DataServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	/*
    for api: /api/datacrawling/data/:id
    for api: /api/datacrawling/data/all

     */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setHeader("content-type", "text/html;charset=UTF-8");
		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
        String[] pathParam= RequestParser.parsePath(request.getRequestURI(),1);
        if(pathParam[0].equals("all")){
            String tbName = request.getParameter("tbName");
            String pageSize11 = request.getParameter("pageSize");
            String pageNum = request.getParameter("pageNum");
            int curPage = Integer.parseInt(pageNum);
            int pageSize = Integer.parseInt(pageSize11);
            int beginId = ((curPage- 1) * pageSize) + 1;
            List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
            String[][] result =null;
                try{
                    result = DBUtil.selectAllTable_data(tbName);
                }catch(Exception e){
                    response.getWriter().println(RespWrapper.build(RespWrapper.AnsMode.SYSERROR,"暂无数据"));
                    return;
                }
                int totalItemNum = result.length - 1;
                if(totalItemNum>=beginId){
                    String[][]tempResult=sortPage(result,pageSize,curPage);
                    dataList = createAns(tempResult);
                }
            response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
            return;
        } else {
            Map<String,Object> data=new HashMap<>();
            String id=pathParam[0];
            String[] para={"webName"};
            String[] pattern={"patternName"};
            String[] param={"webId","type"};
            String[] paramValue={id,"json"};
            List<Map<String,Object>> dataList=new ArrayList<Map<String,Object>>();
            String tableName =DBUtil.select("website",para, Integer.parseInt(id))[0][0];

            data.put("tableName",tableName);
            dataList.add(data);
            Map<String,Object> data1=new HashMap<>();
            data1.put("tableName",tableName+="_1");
            dataList.add(data1);
            String patternName[][]=DBUtil.select("pattern",pattern,param, paramValue);
            for(int i=0;patternName.length>0&&i<patternName[0].length;i++){
                Map<String,Object> data2=new HashMap<>();
                data2.put("tableName",patternName[i]);
                dataList.add(data2);
            }
            response.getWriter().println(RespWrapper.build(dataList,dataList.size()));
        }
	}

	
	
	
	
	public static String[][] sortPage(String[][] result, int pageSize, int curPageNum) {

		int beginId = ((curPageNum- 1) * pageSize) + 1;
		int totalItemNum = result.length - 1;
		int totalPage = totalItemNum / pageSize + 1;
		int columns = result[0].length;
		
		String[][] tempResult = new String[pageSize + 1][columns];
		System.out.println("tempResult.length"+tempResult.length);
		System.out.println("totalItemNum"+totalItemNum);

		System.out.println("beginId"+beginId);
		if (totalItemNum >= beginId) {
			int completePagNum = totalPage - 1;
			if (curPageNum <= completePagNum) {
				for (int i = 0; i < columns; i++) {
					tempResult[0][i] = result[0][i];
				}
				for (int j = beginId; j < (beginId + pageSize); j++) {
					for (int i = 0; i < columns; i++) {
						tempResult[j - beginId + 1][i] = result[j][i];
					}
				}
			}

			else {
				pageSize = totalItemNum % pageSize;		

				tempResult = new String[pageSize + 1][columns];
				for (int i = 0; i < columns; i++) {
					tempResult[0][i] = result[0][i];
				}
				for (int j = beginId; j < (beginId + pageSize); j++) {
					for (int i = 0; i < columns; i++) {
						tempResult[j - beginId + 1][i] = result[j][i];
					}
				}
			}
			
		}
		return tempResult;
	}

	public static List<Map<String,Object>> createAns(String[][] aResult) {
        List<Map<String,Object>> datalist=new ArrayList<Map<String,Object>>();

		for (int i = 0; i < aResult.length - 1; i++) {
            Map<String,Object> data=new HashMap<>();
			for (int j = 0; j < aResult[0].length; j++) {
                data.put(aResult[0][j],aResult[i+1][j]);
			}
            datalist.add(data);
		}
		return datalist;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub

		doGet(request, response);
	}

}
